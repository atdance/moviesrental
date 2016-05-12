/**
 *
 */
package com.movies.api.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.model.Limits;
import com.movies.model.Rental;
import com.movies.model.RentalReturn;
import com.movies.model.error.ApiException;
import com.movies.model.schema.RentalsDAO;

import io.dropwizard.jackson.Jackson;

public class RentalResource implements Resource {

	private static final Logger LOGGER = LoggerFactory.getLogger(RentalResource.class);
	private ObjectMapper aMAPPER = null;
	private final RentalsDAO rentalsDAO;
	private static final AtomicInteger seq = new AtomicInteger();

	public RentalResource(RentalsDAO dao) {
		rentalsDAO = dao;

		aMAPPER = Jackson.newObjectMapper();
		aMAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static int nextID() {
		return seq.incrementAndGet() - 1;
	}

	public static void clearIdGenerator() {
		seq.set(0);
	}

	@Override
	public Response saveRental(String text) {
		Response res = null;
		Rental rental = null;
		try {
			rental = aMAPPER.readValue(text, Rental.class);
		} catch (final IOException e) {
			LOGGER.error(e.toString() + " " + e.getMessage() + " " + e.getCause(), e);
		}
		if (rental == null) {
			res = Response.status(Status.INTERNAL_SERVER_ERROR).entity("rental is null")
					.type(MediaType.APPLICATION_JSON).build();
		} else {
			final int id = rental.getId();

			rentalsDAO.addByID(id, rental);

			res = Response.ok(Integer.toString(id)).build();
		}
		return res;
	}

	@Override
	public Response returnRental(final int pID, int pElapsedDays) {
		RentalReturn ret = null;
		try {

			final Rental rental = rentalsDAO.findByID(pID);

			ret = new RentalReturn(rental, pElapsedDays);

		} catch (final Exception e) {
			LOGGER.error(e.toString(), e);
			throw new ApiException(Limits.RENTAL_NOT_FOUND);
		}

		return Response.ok(ret.surCharge()).build();
	}

	@Override
	public Response getRentals() {
		final Collection<Rental> rentals = new ArrayList<>();
		for (final Rental rental : rentalsDAO.getAll()) {
			rentals.add(rental);
		}
		return Response.ok(rentals).build();
	}

}