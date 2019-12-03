package lan.client.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import lan.client.util.ClientInterface;
import lan.utils.Room;
import lan.utils.Team;
import lan.utils.NetCommand;
import lan.utils.Player;
import lan.utils.Position;
import lan.utils.NetCommand.Code;

public class WorkThread extends Thread { //工作线程
	private RoomHeadInfo roomHeadInfo; //房间列表信息
	private Room room;
	private ObjectOutputStream out; //对象输出流
	private Player player;//玩家
	private String name;//玩家名字
	private ObjectInputStream in;//输入
	private Socket socket;//槽插座
	private ClientInterface clientInterface;//客户端接口
	public WorkThread(RoomHeadInfo room, String name) { //工作线程
		roomHeadInfo = room;
		this.name = name;
	}

	public void setClientInterface(ClientInterface clientInterface) { //客户端接口参数
		this.clientInterface = clientInterface; //启动接口
	}

	@Override
	public void run() {
		try {
			// 建立客户端Socket连接，指定服务器的位置以及端口
			InetAddress address = InetAddress.getByName(roomHeadInfo.host);
			socket = new Socket(address, roomHeadInfo.port);

			out = new ObjectOutputStream(socket.getOutputStream()); //OBject是对象，对象输出流
			in = new ObjectInputStream(socket.getInputStream()); //对象输入流

			boolean  exit = false; //出口为假
			NetCommand in_cmd; //网络指挥官
			while (!exit) {
				try {
					in_cmd = (NetCommand) in.readObject(); //读对象
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
					if(clientInterface != null)
						clientInterface.onMessage(in_cmd.getSenderName(), (String) in_cmd.getData());
					System.out.println(String.format("%s: %s", in_cmd.getSenderName(), (String) in_cmd.getData()));
					break;
				case NEW_PLAYER:
					Player p = (Player) in_cmd.getData();
					if (player == null)
						player = p;
					room.add(p);
					System.out.println("New player join: " + p.getName());
					if(clientInterface != null) {
						clientInterface.roomRefreshed(room);
					}
					break;
				case LEAVE:
					room.remove(player.getId());
					System.out.println("Player leave: " + player.getName());
					if(clientInterface != null) {
						clientInterface.roomRefreshed(room);
					}
					break;

				case TEAM_CHANGE:
					Player sender = in_cmd.getSender();
					Position position = (Position)in_cmd.getData();
					room.movePlayer(sender.getId(), position.getType(), position.getIndex());
					System.out.println(String.format("Player [%s] change team to [%s] position [%d]", sender.getName(),
							position.getType().toString(), position.getIndex()));
					if(clientInterface != null) {
						clientInterface.roomRefreshed(room);
					}
					break;
				case SEED:
					long seed = (long)in_cmd.getData();
					if(clientInterface != null) {
						clientInterface.onSeed(seed);
					}
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

	public boolean sendSeed() {
		boolean ret = false;
		long seed = System.currentTimeMillis();//获取一个种子
		NetCommand command = new NetCommand(Code.SEED);
		command.setSender(player);
		command.setData(seed);
		try {
			out.writeObject(command);//写输出流
			ret = true;
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return ret;
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

	public void movePlayer(Team.Type type, int index) {
		if(room==null || player==null)
			return;

		try {
			room.movePlayer(player.getId(), type, index);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}