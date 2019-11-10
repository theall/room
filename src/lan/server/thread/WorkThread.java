package lan.server.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import javax.smartcardio.CommandAPDU;

import org.omg.CORBA.COMM_FAILURE;

import lan.utils.NetCommand;
import lan.utils.Player;
import lan.utils.Room;
import lan.utils.NetCommand.Code;

public class WorkThread extends Thread {
	private Socket socket;
	private ObjectOutputStream outputStream;
	private Room room;
	private Player player;

	public WorkThread(Room room, Socket socket) {
		this.room = room;
		this.socket = socket;
	}

	@Override
	public void run() {
		NetCommand command = new NetCommand(Code.HELLO);
		command.setData("Im your daddy!");

		try {
			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.writeObject(command);
			command = (NetCommand) inputStream.readObject();
			if (command.getCode() != Code.OK) {
				System.out.println("Fake, go away!");
				return;
			}
			player = new Player((String) command.getData());
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
				default:
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