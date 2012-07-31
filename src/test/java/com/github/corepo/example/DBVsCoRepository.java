package com.github.corepo.example;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.corepo.client.CoRepositoryClient;
import com.github.corepo.client.Item;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-measurement.xml" })
public class DBVsCoRepository {
	private final String id = "min";
	private final int clientCount = 1000;
	private final int callCount = 100;
	@Autowired
	@Qualifier("jdbcTemplate-measurement")
	private JdbcTemplate jdbcTemplate;
	@Qualifier("local")
	@Autowired
	private CoRepositoryClient localMemory;
	@Qualifier("tt")
	@Autowired
	private CoRepositoryClient tt;
	@Qualifier("redis")
	@Autowired
	private CoRepositoryClient redis;

	@Test
	public void db() throws InterruptedException, ExecutionException {
		long result = measure(new DBCallable());

		highlight("DB : " + result);
	}

	@Test
	public void localMemory() throws InterruptedException, ExecutionException {
		initializeKey(localMemory);

		long result = measure(new CoReposiotryCallable(localMemory));

		assertThat(localMemory.selectAsInt(id).getValueAsInt(), is(clientCount
				* callCount));
		highlight("localMemory : " + result);
	}

	@Test
	public void tt() throws InterruptedException, ExecutionException {
		initializeKey(tt);

		long result = measure(new CoReposiotryCallable(tt));

		assertThat(tt.selectAsInt(id).getValueAsInt(), is(clientCount
				* callCount));
		highlight("tt : " + result);
	}

	@Test
	public void redis() throws InterruptedException, ExecutionException {
		initializeKey(redis);

		long result = measure(new CoReposiotryCallable(redis));

		assertThat(redis.selectAsInt(id).getValueAsInt(), is(clientCount
				* callCount));
		highlight("redis : " + result);
	}

	@Before
	public void cleanTable() {
		jdbcTemplate.update("TRUNCATE visitation");
		jdbcTemplate.update("INSERT INTO visitation VALUES ('" + id + "',0)");
	}

	private class DBCallable implements Callable<Boolean> {
		@Override
		public Boolean call() throws Exception {
			for (int j = 0; j < callCount; j++) {
				try {
					jdbcTemplate
							.update("UPDATE visitation SET visitationCount = visitationCount + 1 WHERE name = ?",
									id);
					jdbcTemplate
							.queryForInt(
									"SELECT visitationCount FROM visitation WHERE name = ?",
									id);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return Boolean.TRUE;
		}
	}

	private class CoReposiotryCallable implements Callable<Boolean> {
		private CoRepositoryClient client;

		private CoReposiotryCallable(CoRepositoryClient client) {
			this.client = client;
		}

		@Override
		public Boolean call() throws Exception {
			for (int j = 0; j < callCount; j++) {
				try {
					client.increase(id);
					client.selectAsInt(id);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return Boolean.TRUE;
		}
	}

	private long measure(Callable<Boolean> test) throws InterruptedException,
			ExecutionException {
		long startTime = System.currentTimeMillis();
		ExecutorService executors = Executors.newFixedThreadPool(clientCount);
		List<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>();
		for (int i = 0; i < clientCount; i++) {
			tasks.add(test);
		}
		executors.invokeAll(tasks);

		return System.currentTimeMillis() - startTime;
	}

	private void initializeKey(final CoRepositoryClient client) {
		client.update(new Item(id, 0));
	}

	private void highlight(String message) {
		System.out.println("====================================");
		System.out.println(message);
		System.out.println("====================================");
	}
}
