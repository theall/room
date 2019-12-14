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
			// 建立一个服务器Socket（ServerSocket）指定端口并开始监听
			ServerSocket serverSocket = new ServerSocket();
			serverSocket.setReuseAddress(true);
			serverSocket.bind(new InetSocketAddress(Utils.WORK_PORT));
			System.out.println("Listening...");
			while(!exit) {
				// 使用accept()方法等待客户端触发通信
				Socket socket = serverSocket.accept();
				manager.createNewWorkThread(socket);
			}
			// 关闭资源
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
				
		}
	}
}
