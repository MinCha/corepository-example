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
public class VisiationCountOriginalRepository implements OriginalRepository {
	private static final Log LOG = LogFactory
			.getLog(VisiationCountOriginalRepository.class);
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
	}

	@Override
	public void writeback(List<Item> items) {
		List<Object[]> parameters = new ArrayList<Object[]>();

		for (Item each : items) {
			Visitation visitation = (Visitation) each.getValue();
			parameters.add(new Object[] { visitation.getCount(),
					visitation.getName() });
		}

		LOG.info("Write-Back " + parameters.size());
		jdbcTemplate.batchUpdate(
				"UPDATE visitation SET visitationCount = ? WHERE name = ?",
				parameters);
	}
}
