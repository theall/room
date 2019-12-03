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
		room = new Room();//����������Ķ���
		room.setName("theall");//����room�е�setName�����������ִ��ݵ�room����

		workThreadManager = new WorkThreadManager(room);//���������̹߳���WorkThreadManager���Ķ��󣬲�ͬʱ��room���������WorkThreadManager��
		
		listenThread = new ListenThread(workThreadManager);//�����������̵߳Ķ���listenThread������ͬʱ��workThreadManager����listenThread��
		listenThread.start();//�����߳�
		System.out.println("Woking thread started");//��������߳�������
		broadcastThread = new BroadcastThread(room);//�����㲥�̣߳�BroadcastThread�����󣬲�ͬʱ��room�������ࣩ����BroadcastThread��
		broadcastThread.start();//�����߳�
		System.out.println("Broadcast thread started");//����㲥�߳�������
		
		Scanner scanner = new Scanner(System.in);//����һ��������
		boolean exit = false;//����һ���������͵ı�������ֵΪfalse(��)
		while (!exit) {//����һ��ѭ������exitΪ��ʱ��ֹͣѭ��
			System.out.print(">");
			String string = scanner.nextLine();//��ȡһ������
			CmdParser.Cmd cmd = null;//����CmdParser.Cmd���͵ı���
			try {
				cmd = CmdParser.parse(string);//��string����CmdParser��
			} catch (Exception e) {
				System.out.println("Invalid command, reinput!");//�������Ϊ�٣��������
				continue;//���������ת����whileѭ���ϣ���������
			}
			
			int n = cmd.index;//�������Ÿ�����n
			switch (n) {//�ж����ĸ�����
			case 0:
				showMenu();//��ʾ���Ǽ�������
				break;
			case 1:
				System.out.println(room.toString());
				break;
			case 2: // kick player������
				break;
			case 9:
				exit = true;
				break;
			default:
				System.out.println("error number");//��û�ж�Ӧ������ʱ��������������
				break;//���˳�
			}
		}
		scanner.close();//�ر�������
		broadcastThread.interrupt();//�رչ㲥�߳�
		listenThread.interrupt();//�رռ����߳�
	}

	public void createRoom(Room room) {//��֪����ɶ��
		
	}
	
	public void showMenu() {//��һ�������ʵ��
		System.out.println("0.Show this menu");
		System.out.println("1.Display room infor");
		System.out.println("2.Kick player");
		System.out.println("9.Shutdown server");
	}
}