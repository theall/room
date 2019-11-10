package lan.utils;

import java.io.Serializable;

import lan.utils.Team.Type;

public class Position implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Type type;
	private int index;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
