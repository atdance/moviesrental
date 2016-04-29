/**
 *
 */
package com.movies.api.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.movies.model.IdGenerator;
import com.movies.model.Limits;
import com.movies.model.Movie;
import com.movies.model.Rental;
import com.movies.model.RentalReturn;
import com.movies.model.error.ApiException;
import com.movies.model.error.SystemException;
import com.movies.model.schema.MovieDatabase;
import com.movies.model.schema.RentalsRegister;
import com.movies.model.schema.Titles;

/**
 * Tests classes in the package model . Not tested through Http calls
 *
 */
public class TestRental {

	private static final int LEASE_DAYS = 4;
	private static final int RENTAL_ID = IdGenerator.next();

	private final List<Movie> basket = new ArrayList<Movie>();
	private Rental rentalSubmitted = null;
	private Rental rentalRetrived = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void prepare() throws Exception {
		// basket of movies
		basket.add(MovieDatabase.getInstance().getByTitle(Titles.getInstance().get(0)));
		basket.add(MovieDatabase.getInstance().getByTitle(Titles.getInstance().get(3)));
		// rent
		rentalSubmitted = new Rental(RENTAL_ID, LEASE_DAYS, basket);

		RentalsRegister.getInstance().addByID(RENTAL_ID, rentalSubmitted);
		assertEquals(LEASE_DAYS, rentalSubmitted.getLeasedays());

		rentalRetrived = RentalsRegister.getInstance().findByID(RENTAL_ID);
		assertEquals(rentalSubmitted, rentalRetrived);
		assertEquals(LEASE_DAYS, rentalRetrived.getLeasedays());
	}

	@Test
	public void normalReturn() {
		final int aElapsedDays = 1;
		final RentalReturn ret = new RentalReturn(rentalRetrived, aElapsedDays);
		assertFalse(ret == null);
	}

	@Test
	public void testAnticipatedReturn() {
		assertEquals(rentalSubmitted, rentalRetrived);
		assertEquals(LEASE_DAYS, rentalRetrived.getLeasedays());

		final Integer expected = new Integer(0);

		final int aElapsedDays = 1;
		final RentalReturn ret = new RentalReturn(rentalRetrived, aElapsedDays);
		final Integer surcharge = ret.surCharge();
		assertEquals(expected, surcharge);
	}

	@Test
	public void testLateReturn() {
		final int aElapsedDays = 10;
		final Integer expected = surCharge(aElapsedDays);

		final RentalReturn ret = new RentalReturn(rentalRetrived, aElapsedDays);

		final Integer surcharge = ret.surCharge();
		assertEquals(expected, surcharge);
	}

	private Integer surCharge(int aElapsedDays) {
		int charge = 0;

		final int leaseDays = LEASE_DAYS;

		final int delay = aElapsedDays - leaseDays;
		if (delay > 0) {
			for (final Movie movie : basket) {
				final int price = movie.getPrice();
				charge += price * delay;
			}
		}
		return new Integer(charge);
	}

	@Test(expected = ApiException.class)
	public void testApiException() {
		final ApiException expected = new ApiException(Limits.MAX_DELAY_LENGTH);

		final int tooManyDays = 1002;

		@SuppressWarnings("unused")
		final RentalReturn ret = new RentalReturn(rentalSubmitted, tooManyDays);
	}

	@Test(expected = SystemException.class)
	public void testSystemException() {
		final SystemException expected = new SystemException("Length of rental cannot be negative.");

		final int aElapsedDays = -1;
		@SuppressWarnings("unused")
		final RentalReturn ret = new RentalReturn(rentalSubmitted, aElapsedDays);
	}

	@Test(expected = ApiException.class)
	public void testNullRental() {
		final ApiException expected = new ApiException(Limits.RENTAL_NOT_FOUND);

		final Rental notExistingRental = RentalsRegister.getInstance().findByID(435534);

		final int aElapsedDays = -1;
		@SuppressWarnings("unused")
		final RentalReturn ret = new RentalReturn(notExistingRental, aElapsedDays);
	}

	@Test
	public void testMovie() {
		Exception actual = null;
		@SuppressWarnings("unused")
		Movie movie = null;
		try {
			movie = new Movie("AA", Movie.TypeAndBonus.NEW, Movie.PriceType.BASIC);

		} catch (final Exception e) {
			actual = e;
		}

		assertEquals(null, actual);
	}

}