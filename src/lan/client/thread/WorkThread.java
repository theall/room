package lan.client.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import lan.utils.Room;
import lan.utils.Team;
import lan.utils.NetCommand;
import lan.utils.Player;
import lan.utils.Position;
import lan.utils.NetCommand.Code;

public class WorkThread extends Thread {
	private RoomHeadInfo roomHeadInfo;
	private Room room;
	private ObjectOutputStream out;
	private Player player;
	private String name;
	private ObjectInputStream in;
	private Socket socket;

	public WorkThread(RoomHeadInfo room, String name) {
		roomHeadInfo = room;
		this.name = name;
	}

	@Override
	public void run() {
		try {
			// 建立客户端Socket连接，指定服务器的位置以及端口
			InetAddress address = InetAddress.getByName(roomHeadInfo.host);
			socket = new Socket(address, roomHeadInfo.port);

			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

			boolean exit = false;
			NetCommand in_cmd;
			while (!exit) {
				try {
					in_cmd = (NetCommand) in.readObject();
				} catch (SocketException se) {
					System.out.println("Server shutdown, exit!");
					break;
				} catch (IOException e) {
					System.out.println("Io error, exit!");
					break;
				}
				NetCommand out_cmd = new NetCommand();
				switch (in_cmd.getCode()) {
				case HELLO:
					out_cmd.setCode(Code.OK);
					out_cmd.setData(name);
					break;
				case ROOM_INFO:
					room = (Room) in_cmd.getData();
					System.out.println(room.toString());
					break;
				case MSG:
					System.out.println(String.format("%s: %s", in_cmd.getSenderName(), (String) in_cmd.getData()));
					break;
				case NEW_PLAYER:
					Player p = (Player) in_cmd.getData();
					if (player == null)
						player = p;
					room.add(p);
					System.out.println("New player join: " + p.getName());
					break;
				case LEAVE:
					room.remove(player.getId());
					System.out.println("Player leave: " + player.getName());
					break;
				case TEAM_CHANGE:
					Player sender = in_cmd.getSender();
					Position position = (Position)in_cmd.getData();
					room.movePlayer(sender.getId(), position.getType(), position.getIndex());
					System.out.println(String.format("Player [%s] change team to [%s] position [%d]", sender.getName(),
							position.getType().toString(), position.getIndex()));
					break;
				default:
					break;
				}
				if (!out_cmd.isNull()) {
					out.writeObject(out_cmd);
				}
			}
			// 关闭资源
			in.close();
			out.close();
			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendMessage(String msg) throws IOException {
		if (out == null)
			return;

		NetCommand command = new NetCommand(Code.MSG);
		command.setSender(player);
		command.setData(msg);
		out.writeObject(command);
	}

	public void changeTeam(Team.Type type, int index) throws IOException {
		if (out == null)
			return;

		NetCommand command = new NetCommand(Code.TEAM_CHANGE);
		command.setSender(player);
		
		Position position = new Position();
		position.setType(type);
		position.setIndex(index);
		command.setData(position);
		out.writeObject(command);
	}

	public Room getRoom() {
		return room;
	}

	public void shutdown() throws IOException {
		interrupt();

		if (in != null) {
			in.close();
			in = null;
		}
		if (out != null) {
			out.close();
			out = null;
		}
		if (socket != null) {
			socket.close();
			socket = null;
		}
	}
}