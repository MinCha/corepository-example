package com.github.writeback.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.writeback.client.OriginalRepository;
import com.github.writeback.client.WriteBackItem;

@Repository
public class VisiationCountOriginalRepository implements OriginalRepository {
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public VisiationCountOriginalRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public WriteBackItem read(String key) {
		String name = key.split("_")[0];
		String age = key.split("_")[1];

		long count = jdbcTemplate.queryForLong(
				"SELECT visitationCount FROM visitation WHERE name = ? AND age = ?",
				name, age);

		return new WriteBackItem(key, count);

	}

	public void writeBack(WriteBackItem item) {
		String name = item.getKey().split("_")[0];
		String age = item.getKey().split("_")[1];
		long count = (Long) item.getValue();
		
		jdbcTemplate.update("UPDATE visitation SET visitationCount = ? WHERE name = ? AND age = ?", count, name, age);
	}
}
