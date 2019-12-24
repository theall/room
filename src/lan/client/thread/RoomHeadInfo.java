package lan.client.thread;

public class RoomHeadInfo { //房间标题信息
	public String name;//房间名字
	public String host;//标题
	public int port;//端口
	public int current;//
	public int capacity;
	public long timestamp;//时间戳

	public boolean equals(RoomHeadInfo roomHeadInfo) {
		return name.equals(roomHeadInfo.name) && port==roomHeadInfo.port && host.equals(roomHeadInfo.host);
	}

	public String toString() {
		return String.format("%s(%d|%d)", name, current, capacity);
	}

	public void set(RoomHeadInfo roomHeadInfo) {
		name = roomHeadInfo.name;
		host = roomHeadInfo.host;
		port = roomHeadInfo.port;
		current = roomHeadInfo.current;
		capacity = roomHeadInfo.capacity;
		timestamp = System.currentTimeMillis();
	}
}

