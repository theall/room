package lan.server.thread;

import java.net.Socket;
import lan.utils.NetCommand;

import java.util.ArrayList;
import lan.utils.Room;

public class WorkThreadManager implements ThreadControl {
	private ArrayList<WorkThread> thread_pool;
	private Room room;

	public WorkThreadManager() {
		thread_pool = new ArrayList<WorkThread>();
	}
	
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
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
	public boolean remove(int playerId) {
		// TODO 自动生成的方法存根
		for( int i = 0 ; i < thread_pool.size() ; i++) {
			WorkThread workThread = thread_pool.get(i);
			if(workThread.getPlayerId() == playerId) {
				workThread.close();
				return true;
			}
		}
		return false;
	}
}
