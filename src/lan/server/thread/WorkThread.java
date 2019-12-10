package lan.server.thread;

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
	private boolean exit;
	private ThreadControl threadControl;

	public WorkThread(Room room, Socket socket, ThreadControl ThreadControl) {
		this.room = room;
		this.socket = socket;
	}

	@Override
	public void run() {
		NetCommand command = new NetCommand(Code.HELLO);


		try {command.setData("Im your daddy!");
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
				} catch (SocketException se) {
					System.out.println("Player leave: " + player.getName());
					room.cmdRemove(player);
					inputStream.close();
					socket.close();
					break;
				}
				NetCommand out = new NetCommand(Code.NULL);
				switch (command.getCode()) {
				case MSG: // forward to clients
					System.out.println(command.getSenderName() + ": " + (String) command.getData());
					room.groupSend(command);
					break;
				case TEAM_CHANGE:
					Player sender = command.getSender();
					Position position = (Position)command.getData();
					room.movePlayer(sender.getId(), position.getType(), position.getIndex());
					System.out.println(String.format("Player [%s] change team to [%s] position [%d]", sender.getName(),
							position.getType().toString(), position.getIndex()));
					room.groupSend(command);
					break;
				case KICK:
					Player player = (Player)command.getData();
					boolean check = isPlayerThread(player);
					if(check == true) {
						boolean detection = threadControl.remove(player);
						if(detection == true) {
							NetCommand kickCmd = new NetCommand(Code.KICK);
							kickCmd.setData(player);
							room.groupSend(kick);
							break;
						}
					} else {
						System.out.println("Data illegal");
						break;
					}
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
		exit = true;
		try {
			socket.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	public boolean isPlayerThread(Player player) {
		return this.player.getId() == player.getId();
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