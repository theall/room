package lan.server.thread;

import java.net.Socket;
import lan.utils.NetCommand;
import lan.utils.Player;

import java.util.ArrayList;
import lan.utils.Room;

public class WorkThreadManager implements ThreadControl {
	private ArrayList<WorkThread> thread_pool;
	private Room room;

	public WorkThreadManager(Room room) {
		this.room = room;
		thread_pool = new ArrayList<WorkThread>();
	}

	public void createNewWorkThread(Socket socket) {
		WorkThread thread = new WorkThread(room, socket, this);
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

	@Override
	public boolean remove(Player player) {
		// TODO 自动生成的方法存根
		for( int i = 0 ; i < thread_pool.size() ; i++) {
			if((thread_pool.get(i)).getId() == player.getId()) {
				(thread_pool.get(i)).close();
				return true;
			}
		}
		return false;
	}
}
