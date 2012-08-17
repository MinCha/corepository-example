package com.github.corepo.example;

import java.util.ArrayList;
import java.util.List;

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

	@Override
	public void writeback(List<Item> items) {
		List<Object[]> parameters = new ArrayList<Object[]>();

		for (Item each : items) {
			int visitation = each.getValueAsInt();
			parameters.add(new Object[] { visitation, each.getKey() });
		}

		LOG.info("Write-Back " + parameters);
		jdbcTemplate.batchUpdate(
				"UPDATE visitation SET visitationCount = ? WHERE name = ?",
				parameters);
	}
}
