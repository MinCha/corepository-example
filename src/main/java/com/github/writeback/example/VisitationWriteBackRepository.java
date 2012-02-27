package com.github.writeback.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.github.writeback.client.WriteBackClient;
import com.github.writeback.client.WriteBackItem;

@Repository
public class VisitationWriteBackRepository {
	private WriteBackClient client;
	
	@Autowired
	private VisitationWriteBackRepository(WriteBackClient client) {
		this.client = client;
	}
	
	public Visitation find(String name, String age) {
		WriteBackItem result = client.select(createWriteBackKey(name, age));
		return new Visitation(name, age, result.getValueAsLong());
	}

	public void update(Visitation visitation) {
		WriteBackItem item = new WriteBackItem(createWriteBackKey(visitation.getName(), visitation.getAge()), visitation.getCount());
		client.update(item);
	}
	
	private String createWriteBackKey(String name, String age) {
		return name + "_" + age;
	}
}
