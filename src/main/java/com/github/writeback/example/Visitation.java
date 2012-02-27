package com.github.writeback.example;

public class Visitation {
	private String name;
	private String age;
	private long visitationCount;

	public Visitation(String name, String age, long visitationCount) {
		this.name = name;
		this.age = age;
		this.visitationCount = visitationCount;
	}

	public void visit() {
		visitationCount++;
	}

	public String getName() {
		return name;
	}

	public String getAge() {
		return age;
	}

	public long getCount() {
		return visitationCount;
	}
}
