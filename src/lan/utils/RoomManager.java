package lan.utils;

import java.util.ArrayList;

public class RoomManager {
	private ArrayList<Room> rooms;
	
	public RoomManager() {
		rooms = new ArrayList<Room>();
	}
	
	public Room createRoom(String name) {
		Room room = new Room(name);
		rooms.add(room);
		return room;
	}
}
