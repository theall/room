package lan.server.thread;

import java.net.Socket;
import lan.utils.NetCommand;
import java.util.ArrayList;
import lan.utils.Room;

public class WorkThreadManager {
	private ArrayList<WorkThread> thread_pool;//创建一个类型为ArrayList<WorkThread>的成员变量，用于管理线程
	private Room room;//创建一个数据类型为room的成员变量(room为基础类)

	public WorkThreadManager(Room room) {
		this.room = room;//将传递过来的room类给到成员变量room里
		thread_pool = new ArrayList<WorkThread>();//创建一个对象，用于管理线程
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
