package com.movies.api.test;

import java.util.Arrays;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.movies.model.Cart;
import com.movies.model.Movie;
import com.movies.model.Rental;
import com.movies.model.error.ApiException;

/**
 * tests the Rental class
 *
 */
public class RentalTest {

	private final Logger aLOGGER = LoggerFactory.getLogger(RentalTest.class);
	private static final int VALID_LEASE_DAYS = 4;
	private static final int INVALID_LEASE_DAYS = 9000;
	private static final int INVALID_LEASE_DAYS_MIN = -1;

	public void postValidRent() throws Exception {

		final int ID = 0;
		final Cart cart = buildCart();
		new Rental(ID, VALID_LEASE_DAYS, cart);
	}

	@Test(expected = ApiException.class)
	public void postInvalidRent() throws Exception {

		final int ID = 0;
		final Cart cart = buildCart();
		new Rental(ID, INVALID_LEASE_DAYS, cart);
	}

	@Test(expected = ApiException.class)
	public void postInvalidRentMin() throws Exception {

		final int ID = 0;
		final Cart cart = buildCart();
		new Rental(ID, INVALID_LEASE_DAYS_MIN, cart);
	}

	protected static Cart buildCart() {
		final Movie movie = new Movie("aaa", Movie.TypeAndBonus.NEW, Movie.PriceType.BASIC);

		final Cart cart = new Cart(Arrays.asList(movie));
		return cart;
	}

}