package lan.server.thread;

import java.net.Socket;
import lan.utils.NetCommand;
import java.util.ArrayList;
import lan.utils.Room;

public class WorkThreadManager {
	private ArrayList<WorkThread> thread_pool;//����һ������ΪArrayList<WorkThread>�ĳ�Ա���������ڹ����߳�
	private Room room;//����һ����������Ϊroom�ĳ�Ա����(roomΪ������)

	public WorkThreadManager(Room room) {
		this.room = room;//�����ݹ�����room�������Ա����room��
		thread_pool = new ArrayList<WorkThread>();//����һ���������ڹ����߳�
	}

	public void createNewWorkThread(Socket socket) {
		WorkThread thread = new WorkThread(room, socket);
		thread_pool.add(thread);
		thread.start();
	}

	public void batchSend(NetCommand command) {
		for (WorkThread thread : thread_pool) {
			thread.sendObject(command);
		}
	}

	public void shutdown() {
		for (WorkThread thread : thread_pool) {
			thread.interrupt();
		}
	}
}
