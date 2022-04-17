package com.movies.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.model.Rental;
import com.movies.model.RentalReturn;
import com.movies.model.error.ApiException;
import com.movies.model.error.SystemException;
import com.movies.schema.RentalsDAO;

import io.dropwizard.jackson.Jackson;

public class Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
	private ObjectMapper aMAPPER = null;
	private final RentalsDAO rentalsDAO;
	private static final AtomicInteger seq = new AtomicInteger();

	public Controller(RentalsDAO dao) {
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

	public String saveRental(String text) {
		String res = null;
		Rental rental = null;
		try {
			rental = aMAPPER.readValue(text, Rental.class);
		} catch (final IOException e) {
			LOGGER.error(e.toString() + " " + e.getMessage() + " " + e.getCause(), e);
		}
		if (rental == null) {
			throw new SystemException("rental is null");
		} else {
			final int id = rental.getId();

			rentalsDAO.addByID(id, rental);

			res = Integer.toString(id);
		}
		return res;
	}

	public int returnRental(final int pID, int pElapsedDays) {
		RentalReturn ret = null;
		try {

			final Rental rental = rentalsDAO.findByID(pID);

			ret = new RentalReturn(rental.getLeasedays(), rental.getCart(), pElapsedDays);

		} catch (final Exception e) {
			throw new ApiException("RENTAL_NOT_FOUND");
		}

		return ret.surCharge();
	}

	public Collection<Rental> getRentals() {
		final Collection<Rental> rentals = new ArrayList<>();
		for (final Rental rental : rentalsDAO.getAll()) {
			rentals.add(rental);
		}
		return rentals;
	}

}
