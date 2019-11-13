package lan.client.thread;

import java.util.ArrayList;

public class RoomHeadList {
	private long timeOut = 2000;
	private ArrayList<RoomHeadInfo> roomHeadInfors;

	public RoomHeadList() {
		roomHeadInfors = new ArrayList<RoomHeadInfo>();
	}

	public synchronized void add(RoomHeadInfo roomHeadInfo) {
		for (RoomHeadInfo rhInfor : roomHeadInfors) {
			if (rhInfor.equals(roomHeadInfo)) {
				rhInfor.set(roomHeadInfo);
				return;
			}
		}
		roomHeadInfors.add(roomHeadInfo);
	}

	public synchronized void checkTimeOut() {
		long current = System.currentTimeMillis();
		for (int i = roomHeadInfors.size() - 1; i >= 0; i--) {
			RoomHeadInfo rhInfor = roomHeadInfors.get(i);
			if (current - rhInfor.timestamp >= timeOut) {
				roomHeadInfors.remove(i);
			}
		}
	}

	public synchronized void print() {
		int index = 1;
		for (RoomHeadInfo roomHeadInfo : roomHeadInfors) {
			System.out.println(String.format("%d %s", index, roomHeadInfo.toString()));
			index++;
		}
	}

	public synchronized RoomHeadInfo getRoom(int index) {
		if (index < 0 || index >= roomHeadInfors.size())
			return null;
		return roomHeadInfors.get(index);
	}
	
	public long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}
}

