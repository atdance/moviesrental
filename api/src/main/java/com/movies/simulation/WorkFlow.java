package com.movies.simulation;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
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

	private static final Logger LOGGER          = LoggerFactory.getLogger(WorkFlow.class);

	private static final String aURL_BASE       = "http://localhost:8080/rentals/";

	private Client              client          = null;

	private static Operation    failedOperation = new Operation(false);

	public WorkFlow() {
		super("workflow", "execute rentals and returns");
		try {
			client = ClientBuilder.newClient();
		} catch (final Exception e) {
			LOGGER.error(e.toString(), e);
		}
	}

	@Override
	public void configure(Subparser subparser) {
	}

	@Override
	public void run(Bootstrap<?> bootstrap, Namespace namespace) throws Exception {
		if (null == client) {
			LOGGER.error("client null");
			return;
		}

		LOGGER.info("Workflow begin");

		final Rental rental = createRental();

		if (submitRent(rental).isOperationSucessfull()) {
			returnRentedMovies(rental.getId());
		}
	}

	private Operation submitRent(Rental pRental) {
		System.out.println("Submitting Rental request. Rental ID is " + pRental.getId());

		String rentalString = null;

		boolean failed = false;

		try {
			ObjectMapper MAPPER = Jackson.newObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);

			rentalString = MAPPER.writeValueAsString(pRental);

		} catch (JsonProcessingException e) {
			failed = true;

			LOGGER.error(e.toString(), e);
		}

		if (failed) {
			return failedOperation;
		}

		final Entity<String> entity = Entity.entity(rentalString, MediaType.APPLICATION_JSON);

		try (Response response = client.target(aURL_BASE + "save").request(MediaType.APPLICATION_JSON).post(entity)) {

			return new Operation(response, "Rental successfully accepted with ID ");
		}
	}

	private Operation returnRentedMovies(int pId) {
		/* continue with a Return */
		System.out.println("\n\n\nReturning movies. Rental ID is " + pId);
		System.out.println("");

		final WebTarget target = client.target(aURL_BASE + "return?id=" + pId + "&elapseddays=1");

		try (Response response = target.request().accept(MediaType.APPLICATION_JSON).get()) {
			return new Operation(response, "Rental return successfully submitted. Rental had ID ");
		}
	}

	private Rental createRental() {
		int id = RentalResource.nextID();

		final List<Movie> basket = new ArrayList<>();
		/* basket of movies */
		basket.add(MoviesDAO.getInstance().get(6));
		basket.add(MoviesDAO.getInstance().get(7));

		final Cart cart = new Cart(basket);

		System.out.println("\n " + basket);

		/* rental */
		final int aLEASE_DAYS = 4;

		return new Rental(id, aLEASE_DAYS, cart);
	}
}