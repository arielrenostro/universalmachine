package com.ariel.universalmachine.model;

import java.io.Serializable;

public class MutableBoolean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private boolean value = false;
	
	public MutableBoolean(boolean value) {
		super();
		this.value = value;
	}

	public boolean getValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}
	
}
