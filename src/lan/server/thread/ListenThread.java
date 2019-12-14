package lan.server.thread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import lan.utils.Utils;

public class ListenThread extends Thread {
	private WorkThreadManager manager;

	public ListenThread(WorkThreadManager manager) {
		this.manager = manager;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			boolean exit = false;
			// ����һ��������Socket��ServerSocket��ָ���˿ڲ���ʼ����
			ServerSocket serverSocket = new ServerSocket();
			serverSocket.setReuseAddress(true);
			serverSocket.bind(new InetSocketAddress(Utils.WORK_PORT));
			System.out.println("Listening...");
			while(!exit) {
				// ʹ��accept()�����ȴ��ͻ��˴���ͨ��
				Socket socket = serverSocket.accept();
				manager.createNewWorkThread(socket);
			}
			// �ر���Դ
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
				
		}
	}
}
