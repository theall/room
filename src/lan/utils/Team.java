package lan.utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Team implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum Type {
		NULL, BLUE, RED
	}

	private Type type;
	private ArrayList<Player> players;
	public static int MAX_PLAYERS = 5;

	public Team(Type type) {
		this.type = type;

		players = new ArrayList<Player>();
		for (int i = 0; i < MAX_PLAYERS; i++) {
			players.add(null);
		}
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean setPlayer(int index, Player player) {
		if (index < 0 || index >= players.size())
			return false;
		players.set(index, player);
		return true;
	}

	public int getSize() {//统计玩家人数
		int n = 0;
		for (Player player : players) {
			if (player != null && player.isValid())
				n++;
		}
		return n;
	}

	public int getCapacity() {
		return players.size();
	}
	
	public Player getPlayer(int index) {
		if (index < 0 || index >= players.size())
			return null;
		return players.get(index);
	}

	public ArrayList<Player> getPlayers() {
		ArrayList<Player> playerList = new ArrayList<Player>();
		for (Player player : players) {
			if (player != null)
				playerList.add(player);
		}
		return playerList;
	}

	public int available() {
		int n = 0;
		for (Player player : players) {
			if (player == null)
				n++;
		}
		return n;
	}

	public int add(Player player) {
		int index = -1;
		for (int i = 0; i < MAX_PLAYERS; i++) {
			Player p = players.get(i);
			if (p == null) {
				index = i;
				break;
			}
		}
		if (index != -1)
			players.set(index, player);
		return index;
	}

	public String toString() { //这里是打字的吗
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("Team:" + type.toString() + "\n");
		for (Player player : players) {
			if (player == null || !player.isValid()) {
				sBuffer.append("----------------------");
			} else {
				sBuffer.append(player.getName());
			}
			sBuffer.append("\n");
		}
		return sBuffer.toString();
	}

	public boolean contains(Player player) {
		return players.contains(player);
	}

	public Player remove(int id) throws IOException {
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			if (player != null && player.getId() == id) {
				players.set(i, null);
				return player;
			}
		}
		return null;
	}
}
