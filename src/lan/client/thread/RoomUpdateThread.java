package lan.client.thread;

public class RoomUpdateThread extends Thread {
	private RoomHeadList roomHeadList;
	private int checkInterval;

	public RoomUpdateThread(RoomHeadList roomHeadList) {
		checkInterval = 500;
		this.roomHeadList = roomHeadList;
	}
	
	public int getCheckInterval() {
		return checkInterval;
	}

	public void setCheckInterval(int checkInterval) {
		this.checkInterval = checkInterval;
	}
	
	@Override
	public void run() {
		if(roomHeadList == null)
			return;
		
		while(true) {
			roomHeadList.checkTimeOut();
			try {
				sleep(checkInterval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
