package lan.utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Room implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private int capacity;
	private Map map;
	private Team blue;
	private Team red;
	private String host;
	private int port;
	private long timeStamp;

	public Room() {
		blue = new Team(Team.Type.BLUE);
		red = new Team(Team.Type.RED);
		capacity = Team.MAX_PLAYERS * 2;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public Room(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getAvailable() {
		return blue.available() + red.available();
	}

	public int getUsedSize() {
		return blue.getSize() + red.getSize();
	}
	
	public Team getBlue() {
		return blue;
	}
	
	public Team getRed() {
		return red;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		// name|player_count|capacity
		buffer.append(String.format("%s|%d|%d\n", name, getAvailable(), capacity));
		buffer.append(blue.toString());
		buffer.append(red.toString());
		return buffer.toString();
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public ArrayList<Player> getPlayers() {
		ArrayList<Player> players = new ArrayList<Player>();
		players.addAll(blue.getPlayers());
		players.addAll(red.getPlayers());
		return players;
	}
	
	public Team getBiasTeam() {
		return blue.available() > red.available() ? blue : red;
	}

	public synchronized boolean remove(Player player) throws IOException {
		boolean ret = false;
		ret = blue.remove(player) || red.remove(player);;
		return ret;
	}
	
	public synchronized void add(Player player) throws IOException {
		if(player.getType() == Team.Type.BLUE) {
			getBlue().add(player);
		} else {
			getRed().add(player);
		}
	}
	
	public static Room parse(String s) {
		String[] array = s.split("\\|");
		if (array.length != 5)
			return null;
		Room room = new Room();
		room.setHost(array[0]);
		room.setPort(Integer.parseInt(array[1]));
		room.setName(array[2]);
		room.setCapacity(0);
		return room;
	}
	
	private void cmdNewPlayer(Player player) throws IOException {
		NetCommand command = new NetCommand(NetCommand.Code.NEW_PLAYER);
		command.setData(player);
		groupSend(command);
	}

	private void cmdRemovePlayer(Player player) throws IOException {
		NetCommand command = new NetCommand(NetCommand.Code.LEAVE);
		command.setData(player);
		groupSend(command);
	}
	
	public void groupSend(NetCommand command) throws IOException {
		ArrayList<Player> players = getPlayers();
		for (Player player : players) {
			player.sendCommand(command);
		}
	}
	
	public void cmdAdd(Player player) throws IOException {
		// syn room infor to this player
		NetCommand command = new NetCommand(NetCommand.Code.ROOM_INFO);
		command.setData((Room)this);
		player.sendCommand(command);

		Team team = getBiasTeam();
		player.setType(team.getType());
		team.add(player);
		
		// send new player to other players
		cmdNewPlayer(player);
	}
	
	public boolean cmdRemove(Player player) throws IOException {
		boolean ret = remove(player);
		cmdRemovePlayer(player);
		return ret;
	}
}
