package com.movies.model;

import java.util.Arrays;

import org.junit.Test;

import com.movies.model.Cart;
import com.movies.model.Movie;
import com.movies.model.Rental;
import com.movies.model.RentalReturn;
import com.movies.model.error.ApiException;

public class RentalReturnTest {
	private static final int VALID_LEASE_DAYS = 4;
	private static final int INVALID_LEASE_DAYS = 9000;
	private static final int INVALID_LEASE_DAYS_MIN = -1;

	@Test
	public void validRenturn() {

		final int ID = 0;
		final Cart cart = buildCart();
		final Rental rental = new Rental(ID, VALID_LEASE_DAYS, cart);

		new RentalReturn(rental, 20);
	}

	@Test(expected = ApiException.class)
	public void invalidReturn() {
		final int ID = 0;
		final Cart cart = buildCart();
		new Rental(ID, INVALID_LEASE_DAYS, cart);
	}

	/**
	 * The Return valid but we put an invalid Rental in it.
	 */
	@Test(expected = ApiException.class)
	public void invalidRentLeaseDaysMin() {

		final int ID = 0;
		final Cart cart = buildCart();
		final Rental rental = new Rental(ID, INVALID_LEASE_DAYS_MIN, cart);
		new RentalReturn(rental, 20);
	}

	/**
	 * The Return is valid but we put an invalid Movie in the Rental.
	 */
	@Test(expected = ApiException.class)
	public void validRentButInvalidMovie() {

		final int ID = 0;

		final Movie movie = new Movie("aaa", null, Movie.PriceType.BASIC);

		final Cart cart = new Cart(Arrays.asList(movie));

		final Rental rental = new Rental(ID, VALID_LEASE_DAYS, cart);

		new RentalReturn(rental, 20);
	}

	protected static Cart buildCart() {
		final Movie movie = new Movie("aaa", Movie.TypeAndBonus.NEW, Movie.PriceType.BASIC);

		final Cart cart = new Cart(Arrays.asList(movie));
		return cart;
	}

}