package com.github.corepo.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.corepo.client.Item;
import com.github.corepo.client.OriginalRepository;

@Repository
public class VisiationCountOriginalRepository implements OriginalRepository {
	private static final Log LOG = LogFactory.getLog(VisiationCountOriginalRepository.class);
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public VisiationCountOriginalRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Item read(String name) {
		int count = jdbcTemplate.queryForInt(
				"SELECT visitationCount FROM visitation WHERE name = ?", name);

		return new Item(name, new Visitation(name, count));

	}

	public void writeback(Item item) {
		LOG.info("Write-Back : " + item);
		Visitation visitation = (Visitation) item.getValue();
		jdbcTemplate.update(
				"UPDATE visitation SET visitationCount = ? WHERE name = ?",
				visitation.getCount(), item.getKey());
	}
}
