/**
 *
 */
package com.movies.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.movies.model.error.ApiException;
import com.movies.model.error.SystemException;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author void
 *
 */
@ToString
@EqualsAndHashCode
public class RentalReturn {

	private final Rental aRental;
	private final int aElapsedDays;

	private static final Logger LOGGER = LoggerFactory.getLogger(RentalReturn.class);
	private static final Integer ZERO = new Integer(0);

	/**
	 * @throws SystemException
	 *
	 */
	public RentalReturn(Rental pRental, int elapsedDays) {
		if (null == pRental) {
			throw new SystemException("Arg in constructor is null");
		}
		if (elapsedDays < 0) {
			throw new SystemException("Length of rental cannot be negative.");
		}
		if (elapsedDays > Limits.MAX_DELAY_LENGTH.getLimit()) {
			throw new ApiException(Limits.MAX_DELAY_LENGTH);
		}
		aRental = pRental;
		aElapsedDays = elapsedDays;
	}

	public Integer surCharge() {
		int charge = 0;
		if (aElapsedDays > 0 && aElapsedDays <= aRental.getLeasedays()) {
			LOGGER.info("Movies returned before the booked end of rental");
			return ZERO;
		}

		final int leaseDays = aRental.getLeasedays();
		final int delay = aElapsedDays - leaseDays;
		if (delay > 0) {

			final Cart cart = aRental.getCart();

			for (final Movie movie : cart.getMovies()) {
				final int price = movie.getPrice();
				charge += (price * delay);
			}
		}
		return new Integer(charge);
	}

}