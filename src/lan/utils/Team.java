package lan.utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Team implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	enum Type {
		NULL, BLUE, RED
	}

	private Type type;
	private ArrayList<Player> players;
	public static int MAX_PLAYERS = 5;

	public Team(Type type) {
		this.type = type;

		players = new ArrayList<Player>();
		for(int i=0;i<MAX_PLAYERS;i++) {
			players.add(null);
		}
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getSize() {
		int n = 0;
		for (Player player : players) {
			if (player!=null && player.isValid())
				n++;
		}
		return n;
	}

	public ArrayList<Player> getPlayers() {
		ArrayList<Player> playerList = new ArrayList<Player>();
		for(Player player : players) {
			if(player != null)
				playerList.add(player);
		}
		return playerList;
	}

	public int available() {
		int n = 0;
		for (Player player : players) {
			if (player==null)
				n++;
		}
		return n;
	}

	public int add(Player player) {
		int index = -1;
		for (int i = 0; i < MAX_PLAYERS; i++) {
			Player p = players.get(i);
			if(p == null) {
				index = i;
				break;
			}
		}
		if(index != -1)
			players.set(index, player);
		return index;
	}

	public String toString() {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("Team:" + type.toString() + "\n");
		for (Player player : players) {
			if (player==null || !player.isValid()) {
				sBuffer.append("______________________");
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

	public boolean remove(Player player) throws IOException {
		int index = players.indexOf(player);
		if(index < 0)
			return false;
		player.getOutputStream().close();
		player.reset();
		return true;
	}
}
