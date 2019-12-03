package lan.client.thread;

import java.util.ArrayList;

public class RoomHeadList {//房间标题列表
	private long timeOut = 2000;//超时
	private ArrayList<RoomHeadInfo> roomHeadInfors;//创建一个成员变量数据类型为ArrayList<RoomHeadInfo（房间标题信息）>

	public RoomHeadList() {//构造函数，用于初始化函数
		roomHeadInfors = new ArrayList<RoomHeadInfo>();//创建房间标题信息对象
	}

	public synchronized void add(RoomHeadInfo roomHeadInfo) {
		for (RoomHeadInfo  	rhInfor : roomHeadInfors) { //高级for迭代器这里是在。。。
			if (rhInfor.equals(roomHeadInfo)) {
				rhInfor.set(roomHeadInfo);
				return;
			}
		}
		roomHeadInfors.add(roomHeadInfo);
	}

	public synchronized void checkTimeOut() { //检查时间输出
		long current = System.currentTimeMillis();
		for (int i = roomHeadInfors.size() - 1; i >= 0; i--) {
			RoomHeadInfo rhInfor = roomHeadInfors.get(i);
			if (current - rhInfor.timestamp >= timeOut) {
				roomHeadInfors.remove(i);
			}
		}
	}

	public synchronized void print() {//打印
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

	public synchronized ArrayList<RoomHeadInfo> getRoomHeadInfo() {
		return roomHeadInfors;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}
}

