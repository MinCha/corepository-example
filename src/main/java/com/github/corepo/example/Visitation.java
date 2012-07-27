package com.github.corepo.example;

import java.io.Serializable;

public class Visitation implements Serializable {
	private static final long serialVersionUID = -7402729445629121011L;
	
	private String name;
	private int visitationCount;

	public Visitation(String name, int visitationCount) {
		this.name = name;
		this.visitationCount = visitationCount;
	}

	public void visit() {
		visitationCount++;
	}

	public String getName() {
		return name;
	}

	public int getCount() {
		return visitationCount;
	}

	@Override
	public String toString() {
		return "Visitation [name=" + name + ", visitationCount="
				+ visitationCount + "]";
	}
}
