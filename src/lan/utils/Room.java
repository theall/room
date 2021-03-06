package lan.utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import lan.utils.Team.Type;

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
	private int owner;


	public Room(String name) {
		blue = new Team(Team.Type.BLUE);
		red = new Team(Team.Type.RED);
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCapacity() {
		return blue.getCapacity() + red.getCapacity();
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
		buffer.append(String.format("%s|%d|%d\n", name, getUsedSize(), capacity));
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

	public synchronized Player remove(int id) throws IOException {
		Player player = null;
		player = blue.remove(id);
		if(player == null)
			player = red.remove(id);
		return player;
	}
	
	public synchronized void synchronizePlayerRoleId(int playerId, int roleId) {
		if(playerId < 0)
			return;
		
		Player p = findPlayerById(playerId);
		if(p != null)
			p.setRoleId(roleId);
	}
	
	public synchronized void add(Player player) throws IOException {
		if (player.getType() == Team.Type.BLUE) {
			getBlue().add(player);
		} else {
			getRed().add(player);
		}
	}
	
	public synchronized void setPlayerState(int playerId, boolean isReady) {		
		Player p = findPlayerById(playerId);
		if(p != null)
			p.setReady(isReady);
	}

	public static Room parse(String s) {
		String[] array = s.split("\\|");
		if (array.length != 5)
			return null;
		
		Room room = new Room("");
		room.setHost(array[0]);
		room.setPort(Integer.parseInt(array[1]));
		room.setName(array[2]);
		return room;
	}

	private void cmdNewPlayer(Player player) throws IOException {
		NetCommand command = new NetCommand(NetCommand.Code.NEW_PLAYER);
		command.setData(player);
		groupSend(command);
	}

	private void cmdRemovePlayer(int playerId) throws IOException {
		NetCommand command = new NetCommand(NetCommand.Code.LEAVE);
		command.setData(playerId);
		groupSend(command);
	}

	public void cmdSendSeed(long seed) throws IOException {
		NetCommand seedForward = new NetCommand(NetCommand.Code.SEED);
		seedForward.setData(seed);
		groupSend(seedForward);
	}

	public void groupSend(NetCommand command) throws IOException {
		ArrayList<Player> players = getPlayers();
		for (Player player : players) {
			player.sendCommand(command);
		}
	}

	public void cmdAdd(Player player) throws IOException {
		int playerCount = getPlayers().size();
		if(playerCount == 0)
			owner = player.getId();
		
		// syn room infor to this player
		NetCommand command = new NetCommand(NetCommand.Code.ROOM_INFO);
		command.setData((Room) this);
		player.sendCommand(command);

		Team team = getBiasTeam();
		player.setType(team.getType());
		team.add(player);

		// send new player to other players
		cmdNewPlayer(player);
	}

	public boolean cmdRemove(int playerId) throws IOException {
		Player ret = remove(playerId);
		cmdRemovePlayer(playerId);
		return ret != null;
	}

	public boolean movePlayer(int id, Team.Type newTeam, int index) throws IOException {
		Team team = newTeam == Type.BLUE ? this.blue : red;
		if(index<0 || index>=team.getCapacity())
			return false;
		Player stubPlayer = team.getPlayer(index);
		if (stubPlayer != null && stubPlayer.isValid()) {
			return false;
		}
		Player player = remove(id);
		boolean ret = team.setPlayer(index, player);
		if (ret)
			player.setType(newTeam);
		return ret;
	}
	
	public Player findPlayerById(int id) {
		for(Player player : getPlayers()) {
			if(player.getId() == id)
				return player;
		}
		return null;
	}
	
	public Player findPlayerByIndex(Team.Type teamType, int index) {
		Player player = null;
		Team team = teamType == Type.BLUE ? this.blue : red;
		if(index>=0 && index<team.getCapacity())
			player = team.getPlayer(index);
		return player;
	}
	
	public synchronized int createPlayerId() {
		int id = 0;
		while(true) {
			if(findPlayerById(id) == null)
				break;
			id++;
		}
		return id;
	}

	public Player getPlayer(Team.Type teamType, int index) {
		Team team;
		if(teamType == Team.Type.BLUE) {
			team = blue;
		} else {
			team = red;
		}
		return team.getPlayer(index);
	}
	
	public int getOwnerId() {
		return owner;
	}

	public Player getOwner() {
		return findPlayerById(owner);
	}
	
	public void setOwner(int owner) {
		this.owner = owner;
	}
	
	public boolean isAllReady() {
		ArrayList<Player> players = getPlayers();
		if(players.size() <= 1) {
			// Only owner in room
			return false;
		}
		for(Player player : players) {
			if(player.getId() == owner)
				continue;
			if(!player.isReady())
				return false;
		}
		return true;
	}
}
