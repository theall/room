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

	public Room() {
		blue = new Team(Team.Type.BLUE);
		red = new Team(Team.Type.RED);
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

	public String getName() {//将已有的数据抛出去
		return name;
	}

	public void setName(String name) {
		this.name = name;//将传递过来的参数给到成员变量
	}

	public int getCapacity() {//判断还有多少房间容量
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

	public int getUsedSize() {//统计玩家人数
		return blue.getSize() + red.getSize();
	}

	public Team getBlue() {
		return blue;
	}

	public Team getRed() {
		return red;
	}

	public String toString() {//底2个命令的实现，显示房间情况
		StringBuffer buffer = new StringBuffer();//创建一个字符串修改
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

	public ArrayList<Player> getPlayers() {//获取玩家
		ArrayList<Player> players = new ArrayList<Player>();//创建对象
		players.addAll(blue.getPlayers());//
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

	public synchronized void add(Player player) throws IOException {
		if (player.getType() == Team.Type.BLUE) {
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

	public void groupSend(NetCommand command) throws IOException {//组发送
		ArrayList<Player> players = getPlayers();
		for (Player player : players) {
			player.sendCommand(command);
		}
	}

	public void cmdAdd(Player player) throws IOException {
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

	public boolean cmdRemove(Player player) throws IOException {
		Player ret = remove(player.getId());
		cmdRemovePlayer(player);
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
	
	public synchronized int createPlayerId() {
		int id = 0;
		while(true) {
			if(findPlayerById(id) == null)
				break;
			id++;
		}
		return id;
	}
}
