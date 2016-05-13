package com.movies.simulation;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.api.resource.RentalResource;
import com.movies.model.Cart;
import com.movies.model.Movie;
import com.movies.model.Rental;
import com.movies.schema.MoviesDAO;

import io.dropwizard.cli.Command;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

public class WorkFlow extends Command {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkFlow.class);

	public WorkFlow() {
		super("workflow", "execute rentals and returns");
	}

	@Override
	public void configure(Subparser subparser) {
	}

	@Override
	public void run(Bootstrap<?> bootstrap, Namespace namespace) throws Exception {
		LOGGER.info("Workflow begin");
		Response response = null;
		javax.ws.rs.client.Client client = null;

		final int id = RentalResource.nextID();
		try {
			client = ClientBuilder.newClient();
		} catch (final Exception e) {
			LOGGER.error(e.toString(), e);
		}

		if (null == client) {
			LOGGER.error("client null");
			return;
		}

		try {
			final String aURL_POST = "http://localhost:8080/rentals/save";

			final int aLEASE_DAYS = 4;

			ObjectMapper MAPPER = null;
			MAPPER = Jackson.newObjectMapper();
			MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			final List<Movie> basket = new ArrayList<>();
			/*
			 * basket of movies
			 */
			basket.add(MoviesDAO.getInstance().get(6));
			basket.add(MoviesDAO.getInstance().get(7));

			final Cart cart = new Cart(basket);

			/*
			 * rental
			 */
			final Rental rentalSubmitted = new Rental(id, aLEASE_DAYS, cart);

			final String rentalString = MAPPER.writeValueAsString(rentalSubmitted);

			final Entity<String> entity = Entity.entity(rentalString, MediaType.APPLICATION_JSON);
			response = client.target(aURL_POST).request(MediaType.APPLICATION_JSON).post(entity);
		} catch (final Exception e) {
			LOGGER.error(e.toString(), e);
		} finally {
			if (null != response) {
				response.close();
			}
		}

		/*
		 * continue with a Return
		 */
		Response response2 = null;

		try {
			final WebTarget target = client.target("http://localhost:8080/rentals/return?id=" + id + "&elapseddays=1");

			response2 = target.request().accept(MediaType.APPLICATION_JSON).get();

		} catch (final Exception e) {
			LOGGER.error(e.toString(), e);
		} finally {
			LOGGER.info("Workflow end");

			if (null != response2) {
				response2.close();
			}
		}

	}
}