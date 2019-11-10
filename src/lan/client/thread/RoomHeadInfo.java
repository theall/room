package lan.client.thread;

public class RoomHeadInfo {
	public String name;
	public String host;
	public int port;
	public int current;
	public int capacity;
	public long timestamp;

	public boolean equals(RoomHeadInfo roomHeadInfo) {
		return host.equals(roomHeadInfo.host) && port == roomHeadInfo.port;
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

