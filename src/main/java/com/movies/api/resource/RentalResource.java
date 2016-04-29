/**
 *
 */
package com.movies.api.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.model.IdGenerator;
import com.movies.model.Movie;
import com.movies.model.Rental;
import com.movies.model.RentalReturn;
import com.movies.model.schema.MovieDatabase;
import com.movies.model.schema.RentalsRegister;
import com.movies.model.schema.Titles;

import io.dropwizard.jackson.Jackson;

public class RentalResource implements Resource {

	private final static Logger LOGGER = LoggerFactory.getLogger(RentalResource.class);
	private ObjectMapper MAPPER = null;

	public RentalResource() {
		MAPPER = Jackson.newObjectMapper();
		MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	static {
		final int LEASE_DAYS = 4;
		final int RENTAL_ID = IdGenerator.next();

		final List<Movie> basket = new ArrayList<Movie>();
		basket.add(MovieDatabase.getInstance().getByTitle(Titles.getInstance().get(5)));
		basket.add(MovieDatabase.getInstance().getByTitle(Titles.getInstance().get(3)));
		RentalsRegister.getInstance().addByID(RENTAL_ID, new Rental(RENTAL_ID, LEASE_DAYS, basket));
	}

	@Override
	public Response saveRental(String text) {
		Response res = null;
		Rental rental = null;
		int id = -1;
		try {
			rental = MAPPER.readValue(text, Rental.class);
		} catch (final IOException e) {
			LOGGER.error(e.toString());
		}
		if (rental == null) {
			res = Response.status(Status.INTERNAL_SERVER_ERROR).entity("rental is null")
					.type(MediaType.APPLICATION_JSON).build();
		} else {
			id = rental.getId();
			RentalsRegister.getInstance().addByID(id, rental);

			res = Response.ok().status(Status.OK).build();
		}
		return res;
	}

	@Override
	public Response returnRental(final int pID, int pElapsedDays) {
		Response res = null;

		final Rental rental = RentalsRegister.getInstance().findByID(pID);

		final RentalReturn ret = new RentalReturn(rental, pElapsedDays);
		RentalsRegister.getInstance().remove(rental);
		res = Response.ok(ret.surCharge()).build();
		return res;
	}

	@Override
	public Response getRentals() {
		LOGGER.info("getRentals");

		final Collection<Rental> rentals = new ArrayList<>();
		for (final Rental rental : RentalsRegister.getInstance().getAll()) {
			rentals.add(rental);
		}
		LOGGER.info("Returning [{}] Rentals", rentals.size());
		final Response res = Response.ok(rentals).build();
		return res;
	}

}