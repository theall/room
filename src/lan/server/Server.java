package lan.server;

import lan.server.thread.BroadcastThread;
import lan.server.thread.ListenThread;
import lan.server.thread.WorkThreadManager;
import lan.utils.Lobby;
import lan.utils.Room;
import lan.utils.RoomManager;
import lan.utils.ServerRunMode;
import lan.utils.Utils;

public class Server {
	private BroadcastThread broadcastThread;
	private ListenThread listenThread;
	private WorkThreadManager workThreadManager;
	private RoomManager roomManager;
	private Lobby lobby;
	private ServerRunMode serverRunMode;
	private String host;
	
	public Server() {
		lobby = new Lobby();
		serverRunMode = ServerRunMode.LAN;
		roomManager = new RoomManager();
		host = Utils.getLocalIpAddress();
		workThreadManager = new WorkThreadManager();
		listenThread = new ListenThread(workThreadManager);
		broadcastThread = new BroadcastThread();
	}

	public String getHost() {
		return host;
	}
	
	public Lobby getLobby() {
		return lobby;
	}
	
	public ServerRunMode getServerRunMode() {
		return serverRunMode;
	}

	public void setServerRunMode(ServerRunMode serverRunMode) {
		if(this.serverRunMode == serverRunMode)
			return;
		
		this.serverRunMode = serverRunMode;
	}
	
	public void broadcastRoom(Room room) {
		workThreadManager.setRoom(room);
		broadcastThread.setRoom(room);
		broadcastThread.start();
		System.out.println("Broadcast thread started");
	}
	
	public Room createRoom(String name) {
		Room _room = roomManager.createRoom(name);
		_room.setName(name);//·¿¼äÃû×Ö
		return _room;
	}
	
	public void start() {
		listenThread.start();
		System.out.println("Woking thread started");
	}

	public void shutdown() {
		if(broadcastThread != null)
			broadcastThread.interrupt();
		
		if(listenThread != null)
			listenThread.interrupt();
	}
}