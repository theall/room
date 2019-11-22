package lan.client;

import java.util.Scanner;

import lan.client.thread.FindHostThread;
import lan.client.thread.RoomHeadInfo;
import lan.client.thread.WorkThread;
import lan.client.util.RoomUpdate;
import lan.utils.CmdParser;
import lan.utils.Team.Type;

public class Client {

	private String name = "test";//定义一个字符串标题
	private FindHostThread findThread; //定义寻找线程
	private WorkThread workThread; //定义工作线程

	public Client() {
		// TODO Auto-generated constructor stub

	}

	public void join(RoomHeadInfo roomHeadInfo) { //房间标题信息 jion连接
		if (workThread != null) //如果工作线程不为空
			workThread.interrupt();//终止工作线程
		workThread = new WorkThread(roomHeadInfo, name);	//在房间列表信息中标题
		workThread.start(); //启动该工作线程
	}

	public void setRoomUpdate(RoomUpdate roomUpdate) {
		if(findThread != null)
			findThread.setRoomUpdate(roomUpdate);
	}

	public void start() {
		findThread = new FindHostThread();	//在寻找房间线程中  建立find线程
		findThread.start(); //启动寻找

		try {
			Scanner scanner = new Scanner(System.in);//在键盘上输入
			boolean exit = false; //出口为假
			while (!exit) {
				System.out.print(name + ">");
				String string = scanner.nextLine();
				CmdParser.Cmd cmd = null;
				try {
					cmd = CmdParser.parse(string);
				} catch (Exception e) {
					System.out.println("Invalid command, reinput!");//命令无效重新输入
					continue;
				}
				int n = cmd.index;
				if (workThread == null) {
					switch (n) {
					case 0:
						showMainMenu();
						break;
					case 1:// set name
						if (cmd.data == null) {
							System.out.println("Invalid command, reinput!");//命令无效重新输入
						continue;
					}
					name = cmd.data;
					break;
					case 2:// scan rooms
						findThread.getRooms().print();
						break;
					case 3:// join room
						int roomNo = 1;
						if (cmd.data != null) {
							roomNo = Integer.parseInt(cmd.data);
						}
						join(findThread.getRooms().getRoom(roomNo - 1));
						System.out.println("Your choose " + roomNo); //你的的选择
						break;
					case 9:// exit
						exit = true;
						break;
					default:
						System.out.println("error number");
						break;
					}
				} else {
					switch (n) {
					case 0:
						showRoomMenu();
						break;
					case 1:// change team
						if (cmd.data == null) {
							System.out.println("Need team name!"); //需要团队名称
							continue;
						}
						name = cmd.data;
						break;
					case 2:// send message
						if (cmd.data == null) {
							System.out.println("Need message!"); //需要留言
							continue;
						}
						workThread.sendMessage(cmd.data); //这里是接受消息
						break;
					case 3:// show room infor
						System.out.println(workThread.getRoom().toString());
						break;
					case 4:
						int index = Integer.parseInt(cmd.data);
						if(index>=0 && index<workThread.getRoom().getCapacity()) {
							Type type = index<5?Type.BLUE:Type.RED;
							if(index > 5)
								index -= 5;
							workThread.changeTeam(type, index); //type是队伍，INdex索引
						}
						break;
					case 9:// exit room
						workThread.shutdown();
						workThread = null;
						break;
					default:
						System.out.println("error number");//错误号
						break;
					}
				}
			}
			scanner.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void showMainMenu() {
		System.out.println("###############");
		System.out.println("0.Show this menu");
		System.out.println("1.Set name");
		System.out.println("2.Scan rooms");
		System.out.println("3.Join room");
		System.out.println("9.exit");
		System.out.println("###############");
	}

	public void showRoomMenu() {
		System.out.println("###############");
		System.out.println("0.Show menu");
		System.out.println("1.Change team");
		System.out.println("2.Send message");
		System.out.println("3.Show room information");
		System.out.println("4.Change team");
		System.out.println("9.exit room");
		System.out.println("###############");
	}
}