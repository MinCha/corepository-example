package com.github.corepo.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.corepo.client.Item;
import com.github.corepo.client.OriginalRepository;

@Repository
public class VisitationCountIntOriginalRepository implements OriginalRepository {
	private static final Log LOG = LogFactory
			.getLog(VisitationCountIntOriginalRepository.class);
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public VisitationCountIntOriginalRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Item read(String name) {
		int count = jdbcTemplate.queryForInt(
				"SELECT visitationCount FROM visitation WHERE name = ?", name);

		return new Item(name, count);
	}

	public void writeback(Item item) {
		LOG.info("Write-Back : " + item);
		int visitation = item.getValueAsInt();
		jdbcTemplate.update(
				"UPDATE visitation SET visitationCount = ? WHERE name = ?",
				visitation, item.getKey());
	}
}
