package lan.server.thread;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import lan.utils.NetCommand;
import lan.utils.Player;
import lan.utils.Position;
import lan.utils.Room;
import lan.utils.NetCommand.Code;

public class WorkThread extends Thread {
	private Socket socket;
	private ObjectOutputStream outputStream;
	private Room room;
	private Player player;
	private ThreadControl threadControl;

	public WorkThread(Room room, Socket socket, ThreadControl threadControl) {
		this.room = room;
		this.socket = socket;
		this.threadControl = threadControl;
	}

	@Override
	public void run() {
		NetCommand command = new NetCommand(Code.HELLO);
		try {
			command.setData("Im your daddy!");
			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.writeObject(command);
			command = (NetCommand) inputStream.readObject();
			if (command.getCode() != Code.OK) {
				System.out.println("Fake, go away!");
				return;
			}
			player = new Player((String) command.getData());
			player.setId(room.createPlayerId());
			player.setOutputStream(outputStream);
			room.cmdAdd(player);
			System.out.println("New player: " + player.getName());

			boolean exit = false;
			while (!exit) {
				try {
					command = (NetCommand) inputStream.readObject();
				} catch (SocketException | EOFException se) {
					System.out.println("Player leave: " + player.getName());
					room.cmdRemove(player);
					inputStream.close();
					socket.close();
					break;
				}
				NetCommand out = new NetCommand(Code.NULL);
				int senderId = command.getSender();
				Player sender = room.findPlayerById(senderId);
				switch (command.getCode()) {
				case MSG: // forward to clients
					System.out.println(sender.getName() + ": " + (String) command.getData());
					room.groupSend(command);
					break;
				case TEAM_CHANGE:
					Position position = (Position)command.getData();
					room.movePlayer(senderId, position.getType(), position.getIndex());
					System.out.println(String.format("%s change team to [%s] position [%d]", sender.getName(),
					position.getType().toString(), position.getIndex()));
					room.groupSend(command);
					break;
				case KICK:
					int playerIdToKick = (int)command.getData();
					boolean check = isYou(playerIdToKick);
					if(check == false) {
						boolean detection = threadControl.remove(playerIdToKick);
						if(detection == true) {
							NetCommand kickCmd = new NetCommand(Code.KICK);
							kickCmd.setData(sender);
							room.groupSend(kickCmd);
							break;
						}
					} else {
						System.out.println("Data illegal");
						break;
					}
				case SELECT_ROLE:
					int playerId = command.getSender();
					int roleId = (Integer)command.getData();
					room.synchronizePlayerRoleId(playerId, roleId);
					System.out.println(String.format("%s select role %d", sender.getName(), roleId));
					room.groupSend(command);
					break;
				case CREATE_ROOM:
					break;
				default:
					room.groupSend(command);
					break;
				}
				if (!out.isNull()) {
					outputStream.writeObject(command);
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	public boolean isYou(int id) {
		return this.player.getId() == id;
	}

	public void sendObject(Object object) {
		if (outputStream != null) {
			try {
				outputStream.writeObject(object);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}