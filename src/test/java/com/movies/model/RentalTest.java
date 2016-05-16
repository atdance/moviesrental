package com.movies.model;

import java.util.Arrays;

import org.junit.Test;

import com.movies.model.Cart;
import com.movies.model.Movie;
import com.movies.model.Rental;
import com.movies.model.error.ApiException;

/**
 * tests the Rental class
 *
 */
public class RentalTest {

	private static final int VALID_LEASE_DAYS = 4;
	private static final int INVALID_LEASE_DAYS = 9000;
	private static final int INVALID_LEASE_DAYS_MIN = -1;

	@Test
	public void validRent() {

		final int ID = 0;
		final Cart cart = buildCart();
		new Rental(ID, VALID_LEASE_DAYS, cart);
	}

	@Test(expected = ApiException.class)
	public void invalidRent() {

		final int ID = 0;
		final Cart cart = buildCart();
		new Rental(ID, INVALID_LEASE_DAYS, cart);
	}

	@Test(expected = ApiException.class)
	public void invalidRentLeaseDaysMin() {

		final int ID = 0;
		final Cart cart = buildCart();
		new Rental(ID, INVALID_LEASE_DAYS_MIN, cart);
	}

	/**
	 * The Cart is valid but we put an invalid Movie in it.
	 */
	@Test(expected = ApiException.class)
	public void validRentButInvalidMovie() {

		final int ID = 0;

		final Movie movie = new Movie("aaa", null, Movie.PriceType.BASIC);

		final Cart cart = new Cart(Arrays.asList(movie));

		new Rental(ID, VALID_LEASE_DAYS, cart);
	}

	protected static Cart buildCart() {
		final Movie movie = new Movie("aaa", Movie.TypeAndBonus.NEW, Movie.PriceType.BASIC);

		final Cart cart = new Cart(Arrays.asList(movie));
		return cart;
	}

}