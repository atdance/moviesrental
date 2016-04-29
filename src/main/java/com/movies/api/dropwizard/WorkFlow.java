package com.movies.api.dropwizard;

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
import com.movies.model.IdGenerator;
import com.movies.model.Movie;
import com.movies.model.Rental;
import com.movies.model.schema.MovieDatabase;
import com.movies.model.schema.Titles;

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
		int ID = 0;

		try {
			client = ClientBuilder.newClient();
		} catch (final Exception e) {
			LOGGER.error(e.toString());
		}

		if (null == client) {
			LOGGER.error("cleint null");
			return;
		}

		try {
			final String URL_POST = "http://localhost:8080/rentals/save";
			ID = IdGenerator.next();

			final int LEASE_DAYS = 4;

			ObjectMapper MAPPER = null;
			MAPPER = Jackson.newObjectMapper();
			MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

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

		/*
		 * continue with a Return
		 */
		Response response2 = null;

		try {
			final WebTarget target = client.target("http://localhost:8080/rentals/return?id=" + ID + "&elapseddays=1");

			response2 = target.request().accept(MediaType.APPLICATION_JSON).get();

			@SuppressWarnings("unused")
			final Integer msg2 = response2.readEntity(Integer.class);

		} catch (final Exception e) {
			LOGGER.error(e.toString());
		} finally {
			LOGGER.info("Workflow end");

			if (null != response2) {
				response2.close();
			}
		}

	}
}