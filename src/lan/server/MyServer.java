package lan.server;

import java.util.Scanner;

import lan.utils.CmdParser;
import lan.utils.Room;

public class MyServer { //·þÎñÆ÷

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server server = new Server();
		Room room = server.createRoom("debug");
		server.start();
		
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
	}
	
	public static void showMenu() {
		System.out.println("0.Show this menu");
		System.out.println("1.Display room infor");
		System.out.println("2.Kick player");
		System.out.println("9.Shutdown server");
	}
}
