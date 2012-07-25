package com.github.corepo.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.github.corepo.client.CoRepositoryClient;
import com.github.corepo.client.Item;

@Repository
public class VisitationRepository {
	private CoRepositoryClient client;
	
	@Autowired
	private VisitationRepository(CoRepositoryClient client) {
		this.client = client;
	}
	
	public Visitation find(String name) {
		Item result = client.selectAsObject(name);
		return (Visitation) result.getValue();
	}

	public void update(Visitation visitation) {
		client.update(new Item(visitation.getName(), visitation));
	}
}
