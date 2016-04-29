/**
 *
 */
package com.movies.api.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.model.IdGenerator;
import com.movies.model.Movie;
import com.movies.model.Rental;
import com.movies.model.schema.MovieDatabase;
import com.movies.model.schema.Titles;

import io.dropwizard.jackson.Jackson;

/**
 * test some operations towards the test API
 *
 */
public class TestClient {
	static String URL = "http://localhost:8080/rentals//gettest?tradeid=1";
	static String BAD_URL = "http://localhost:8080/rentals//axxaxaag";
	static String URL_BASE = "http://localhost:8080/rentals";
	static String URL_POST = "http://localhost:8080/rentals/save";
	static String URL_MOVIE = "http://localhost:8080/rentals/movie";
	static String URL_STATUS = "http://localhost:8080/rentals/status";

	private javax.ws.rs.client.Client client = null;

	private final Logger LOGGER = LoggerFactory.getLogger(TestClient.class);
	private static final int LEASE_DAYS = 4;

	ObjectMapper MAPPER = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		client = ClientBuilder.newClient();
		MAPPER = Jackson.newObjectMapper();
		MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	}

	@After
	public void tearDown() throws Exception {
		if (client != null) {
			client.close();
		}
	}

	@Test
	public void testRent() throws Exception {

		Response response = null;
		final int ID = IdGenerator.next();

		try {

			final List<Movie> basket = new ArrayList<>();
			/*
			 * basket of movies
			 */
			basket.add(MovieDatabase.getInstance().getByTitle(Titles.getInstance().get(6)));
			basket.add(MovieDatabase.getInstance().getByTitle(Titles.getInstance().get(7)));

			/*
			 * rental
			 */
			final Rental rentalSubmitted = new Rental(ID, LEASE_DAYS, basket);

			final String rentalString = MAPPER.writeValueAsString(rentalSubmitted);

			final Entity<String> entity = Entity.entity(rentalString, MediaType.APPLICATION_JSON);
			response = client.target(URL_POST).request(MediaType.APPLICATION_JSON).post(entity);
		} catch (final Exception e) {
			LOGGER.error(e.toString());
		} finally {
			if (null != response) {
				response.close();
			}
		}
		assertEquals(200, response.getStatus());

		/*
		 * continue with a Return
		 */
		Response response2 = null;
		final Integer MY_ZERO = new Integer(0);
		Integer msg2 = null;
		Exception exc2 = null;

		try {
			final WebTarget target = client.target("http://localhost:8080/rentals/return?id=" + ID + "&elapseddays=1");

			response2 = target.request().accept(MediaType.APPLICATION_JSON).get();

			assertEquals(200, response2.getStatus());

			try {
				msg2 = response2.readEntity(Integer.class);
			} catch (final Exception e) {
				LOGGER.info(e.getCause() + "" + e.getMessage());
				exc2 = e;
			}

		} catch (final Exception e) {
			LOGGER.error(e.toString());
		} finally {
			if (null != response2) {
				response2.close();
			}
		}

		assertTrue(exc2 == null);
		assertNotNull(msg2);
		assertTrue(msg2.compareTo(MY_ZERO) > -1);
	}

	@Test
	public void testReturnRentalNotExisting() {
		Response response = null;
		try {
			final WebTarget target = client.target("http://localhost:8080/rentals/return?id=999&elapseddays=2");
			response = target.request().accept(MediaType.APPLICATION_JSON).get();

		} catch (final Exception e) {
			LOGGER.error(e.toString());
		} finally {
			if (null != response) {
				response.close();
			}
		}
		assertTrue(response.getStatus() == Status.BAD_REQUEST.getStatusCode());
	}

	@Test
	public void testListRentals() {
		Response response = null;
		try {
			final WebTarget target = client.target(URL_BASE).path("/listrentals");

			final Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);

			final int OPS = 1;
			int successfulOps = 0;

			for (int i = 0; i < OPS; i++) {
				response = invocationBuilder.get();
				if (response.getStatus() == Response.Status.OK.getStatusCode()) {
					successfulOps++;
				}
			}
			Assert.assertEquals(OPS, successfulOps);
		} catch (final Exception e) {
			LOGGER.error(e.toString());
		} finally {
			if (null != response) {
				response.close();
			}
		}
	}

}