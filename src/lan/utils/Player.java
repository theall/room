package lan.utils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import lan.utils.Team.Type;

public class Player implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private Team.Type type;
	transient private ObjectOutputStream outputStream;
	private boolean ready;

	public Player(String name) {
		this.name = name;
		type = Type.NULL;
	}

	public void reset() {
		name = "";
		type = Type.NULL;
		outputStream = null;
	}

	public boolean isValid() {
		return !name.isEmpty() && type!=Type.NULL;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Team.Type getType() {
		return type;
	}

	public void setType(Team.Type type) {
		this.type = type;
	}

	public ObjectOutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(ObjectOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public void copyFrom(Player player) {
		name = player.getName();
		type = player.getType();
		outputStream = player.getOutputStream();
	}

	public boolean equals(Player player) {
		return name==player.getName() && type==player.getType() && outputStream==player.getOutputStream();
	}
	
	public void sendCommand(NetCommand command) throws IOException {
		if (outputStream == null)
			return;

		outputStream.writeObject(command);
	}
	
	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}
}
