package lan.server;

import java.util.Scanner;

import lan.server.thread.BroadcastThread;
import lan.server.thread.ListenThread;
import lan.server.thread.WorkThreadManager;
import lan.utils.CmdParser;
import lan.utils.Room;

class Server {
	private BroadcastThread broadcastThread;
	private ListenThread listenThread;
	private WorkThreadManager workThreadManager;
	private Room room;

	public Server() {

	}

	public void start() {
		room = new Room();
		room.setName("theall");//·¿¼äÃû×Ö

		workThreadManager = new WorkThreadManager(room);
		
		listenThread = new ListenThread(workThreadManager);
		listenThread.start();
		System.out.println("Woking thread started");
		broadcastThread = new BroadcastThread(room);
		broadcastThread.start();
		System.out.println("Broadcast thread started");
		
		Scanner scanner = new Scanner(System.in);
		boolean exit = false;
		while (!exit) {
			System.out.print(">");
			String string = scanner.nextLine();
			CmdParser.Cmd cmd = null;
			try {
				cmd = CmdParser.parse(string);
			} catch (Exception e) {
				System.out.println("Invalid command, reinput!");
				continue;
			}
			
			int n = cmd.index;
			switch (n) {
			case 0:
				showMenu();
				break;
			case 1:
				System.out.println(room.toString());
				break;
			case 2: // kick player
				break;
			case 9:
				exit = true;
				break;
			default:
				System.out.println("error number");
				break;
			}
		}
		scanner.close();
		broadcastThread.interrupt();
		listenThread.interrupt();
	}

	public void createRoom(Room room) {
		
	}
	
	public void showMenu() {
		System.out.println("0.Show this menu");
		System.out.println("1.Display room infor");
		System.out.println("2.Kick player");
		System.out.println("9.Shutdown server");
	}
}