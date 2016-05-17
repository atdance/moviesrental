/**
 *
 */
package com.movies.api.resource;

import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.controller.Controller;
import com.movies.model.error.ApiException;

import io.dropwizard.jackson.Jackson;

public class RentalResource implements Resource {

	private static final Logger LOGGER = LoggerFactory.getLogger(RentalResource.class);
	private ObjectMapper aMAPPER = null;
	private final Controller controller;
	private static final AtomicInteger seq = new AtomicInteger();

	public RentalResource(Controller pController) {
		controller = pController;

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

		String rentalID = null;

		rentalID = controller.saveRental(text);

		if (rentalID == null) {
			res = Response.status(Status.INTERNAL_SERVER_ERROR).entity("rental is null")
					.type(MediaType.APPLICATION_JSON).build();
		} else {
			res = Response.ok(rentalID).build();
		}
		return res;
	}

	@Override
	public Response returnRental(final int pID, int pElapsedDays) {
		int surcharge = 0;

		try {
			surcharge = controller.returnRental(pID, pElapsedDays);

		} catch (final Exception e) {
			LOGGER.error(e.toString(), e);
			throw new ApiException("RENTAL_NOT_FOUND");
		}

		return Response.ok(surcharge).build();
	}

	@Override
	public Response getRentals() {

		return Response.ok(controller.getRentals()).build();
	}

}