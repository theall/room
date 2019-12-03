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

	public Server(){

	}

	public void start() {
		room = new Room();//创建基础类的对象
		room.setName("theall");//调用room中的setName方法，把名字传递到room类里

		workThreadManager = new WorkThreadManager(room);//创建工作线程管理（WorkThreadManager）的对象，并同时将room基础类给到WorkThreadManager里
		
		listenThread = new ListenThread(workThreadManager);//创建出监听线程的对象（listenThread），并同时把workThreadManager给到listenThread中
		listenThread.start();//启动线程
		System.out.println("Woking thread started");//输出唤醒线程已启动
		broadcastThread = new BroadcastThread(room);//创建广播线程（BroadcastThread）对象，并同时将room（基础类）给到BroadcastThread里
		broadcastThread.start();//启动线程
		System.out.println("Broadcast thread started");//输出广播线程已启动
		
		Scanner scanner = new Scanner(System.in);//创建一个输入流
		boolean exit = false;//创建一个布尔类型的变量并赋值为false(假)
		while (!exit) {//创建一个循环，当exit为真时在停止循环
			System.out.print(">");
			String string = scanner.nextLine();//获取一行数据
			CmdParser.Cmd cmd = null;//创建CmdParser.Cmd类型的变量
			try {
				cmd = CmdParser.parse(string);//将string传入CmdParser中
			} catch (Exception e) {
				System.out.println("Invalid command, reinput!");//如果数据为假，则错误处理
				continue;//将程序继续转交到while循环上，舍弃本次
			}
			
			int n = cmd.index;//把命令编号给变量n
			switch (n) {//判断是哪个命令
			case 0:
				showMenu();//显示有那几个命令
				break;
			case 1:
				System.out.println(room.toString());
				break;
			case 2: // kick player，踢人
				break;
			case 9:
				exit = true;
				break;
			default:
				System.out.println("error number");//当没有对应的命令时，输出错误命令号
				break;//并退出
			}
		}
		scanner.close();//关闭输入流
		broadcastThread.interrupt();//关闭广播线程
		listenThread.interrupt();//关闭监听线程
	}

	public void createRoom(Room room) {//不知道干啥的
		
	}
	
	public void showMenu() {//第一个命令的实现
		System.out.println("0.Show this menu");
		System.out.println("1.Display room infor");
		System.out.println("2.Kick player");
		System.out.println("9.Shutdown server");
	}
}