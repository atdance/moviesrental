/**
 *
 */
package com.movies.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.api.dropwizard.MoviesDropwizardApp;
import com.movies.api.dropwizard.RateLimiter;
import com.movies.api.resource.RentalResource;
import com.movies.model.Cart;
import com.movies.model.Rental;
import com.movies.model.error.ApiException;
import com.movies.schema.RentalsDAO;

import io.dropwizard.jackson.Jackson;

/**
 * test some operations towards the test API. This test starts a DropWizard
 * server instance and closes it. This server does not execute if Dropwizard is
 * already started
 *
 */
public class TestClient extends TestCommon {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestClient.class);
	private static final String HOST = "http://localhost:8080";
	private static final String URL_BASE = HOST + "/rentals";
	private static final String URL_POST = HOST + "/rentals/save";

	private static javax.ws.rs.client.Client client = null;

	private final Logger aLOGGER = LoggerFactory.getLogger(TestClient.class);
	private static final int VALID_LEASE_DAYS = 4;
	private static final int INVALID_LEASE_DAYS = 9000;

	private Rental rentalSubmitted = null;

	static ObjectMapper aMAPPER = null;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		client = ClientBuilder.newClient();
		aMAPPER = Jackson.newObjectMapper();
		aMAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		RateLimiter.disable();

		final String[] args = { "server" };
		try {
			new MoviesDropwizardApp().run(args);
		} catch (final java.net.BindException e) {
			LOGGER.error("Test cannot excute as server is already in use");
		}

	}

	@AfterClass
	public static void tearDown() {
		if (client != null) {
			client.close();
		}
		RentalsDAO.getInstance().clear();
		RentalResource.clearIdGenerator();
		TestClient.destroyJetty();
	}

	@Test
	public void testRent() throws Exception {

		// load some data first
		final int ID = RentalResource.nextID();
		postRental(ID, VALID_LEASE_DAYS);

		/*
		 * continue with a Return
		 */
		Response response2 = null;
		Integer msg2 = null;
		Exception exc2 = null;

		try {
			final WebTarget target = client.target(HOST + "/rentals/return?id=" + ID + "&elapseddays=1");

			response2 = target.request().accept(MediaType.APPLICATION_JSON).get();

			assertEquals(200, response2.getStatus());

			try {
				msg2 = response2.readEntity(Integer.class);

			} catch (final Exception e) {
				aLOGGER.info(e.getCause() + "" + e.getMessage(), e);
				exc2 = e;
			}

		} catch (final Exception e) {
			aLOGGER.error(e.toString() + " " + e.getMessage() + " " + e.getCause(), e);

		} finally {
			if (null != response2) {
				response2.close();
			}
		}

		assertTrue(exc2 == null);
		assertNotNull(msg2);
		final Integer MY_ZERO = new Integer(0);
		assertTrue(msg2.compareTo(MY_ZERO) > -1);
	}

	@Test(expected = ApiException.class)
	public void postInvalidRent() throws Exception {

		final int ID = RentalResource.nextID();
		final Cart cart = buildCart();
		new Rental(ID, INVALID_LEASE_DAYS, cart);
	}

	@Test
	public void testReturnRentalNotExisting() {
		Response response = null;
		try {
			final WebTarget target = client.target(HOST + "/rentals/return?id=999&elapseddays=2");
			response = target.request().accept(MediaType.APPLICATION_JSON).get();

		} catch (final Exception e) {
			aLOGGER.error(e.toString() + " " + e.getMessage() + " " + e.getCause(), e);

		} finally {
			if (null != response) {
				response.close();
			}
		}
		assertNotNull(response);

		System.out.println("...." + response.getStatus());
		assertTrue(response.getStatus() != Response.Status.OK.getStatusCode());
	}

	@Test
	public void testListRentals() {

		// load some data first
		final int ID = RentalResource.nextID();
		postRental(ID, VALID_LEASE_DAYS);

		Response response = null;

		try {
			final WebTarget target = client.target(URL_BASE).path("/listrentals");

			final Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);

			final int iterations = 1;
			int successfulIterations = 0;

			for (int i = 0; i < iterations; i++) {
				response = invocationBuilder.get();
				if (response.getStatus() == Response.Status.OK.getStatusCode()) {
					successfulIterations++;
				}
			}
			Assert.assertEquals(iterations, successfulIterations);

		} catch (final Exception e) {
			aLOGGER.error(e.toString() + " " + e.getMessage() + " " + e.getCause(), e);

		} finally {
			if (null != response) {
				response.close();
			}
		}
	}

	private Response postRental(int pID, int pDays) {
		Response response = null;

		RateLimiter.disable();

		try {
			final Cart cart = buildCart();
			rentalSubmitted = new Rental(pID, pDays, cart);

			final String rentalString = aMAPPER.writeValueAsString(rentalSubmitted);

			final Entity<String> entity = Entity.entity(rentalString, MediaType.APPLICATION_JSON);

			response = client.target(URL_POST).request(MediaType.APPLICATION_JSON).post(entity);

			assertEquals(200, response.getStatus());
		} catch (final Exception e) {
			aLOGGER.error(e.toString() + " " + e.getMessage() + " " + e.getCause(), e);

		} finally {
			if (null != response) {
				response.close();
			}
		}
		return response;

	}

	private static void destroyJetty() {
		Server jettyServer = null;
		try {
			final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
			context.setContextPath("/");

			jettyServer = new Server(8080);
			jettyServer.setHandler(context);

		} catch (final Exception e) {
			e.printStackTrace();

		} finally {
			try {
				if (jettyServer != null) {
					LOGGER.info("destroy");
					jettyServer.destroy();
				}
			} catch (final Exception e) {
				LOGGER.error("failed to destroy Jetty server");
			}
		}
	}
}