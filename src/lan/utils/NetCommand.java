package lan.utils;

import java.io.Serializable;

public class NetCommand implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum Code {
		NULL, HELLO, OK, NEW_PLAYER, LEAVE, ROOM_INFO, MAP_CHANGE, TEAM_CHANGE, MSG, SEND_MSG, SEED
	}

	private Code code;
	private Player sender;
	private Object data;

	// 这里设置时间
	public NetCommand() {
		code = Code.NULL;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public NetCommand(Code code) {
		this.code = code;
	}

	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public boolean isNull() {
		return code == Code.NULL;
	}

	public Player getSender() {
		return sender;
	}

	public void setSender(Player sender) {
		this.sender = sender;
	}

	public String getSenderName() {
		return sender != null ? sender.getName() : "";
	}
}
