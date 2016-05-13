/**
 *
 */
package com.movies.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
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
	@Min(value = 0, message = "The rental period is too short")
	@Max(value = 1000, message = "The rental period is too long")
	private final int aElapsedDays;

	private static final Logger LOGGER = LoggerFactory.getLogger(RentalReturn.class);
	private static final Integer ZERO = new Integer(0);

	/**
	 * @throws SystemException
	 *
	 */
	@JsonCreator
	public RentalReturn(Rental pRental, int elapsedDays) {
		if (null == pRental) {
			throw new SystemException("Arg in constructor is null");
		}
		aRental = pRental;
		aElapsedDays = elapsedDays;
		MyValidator.validate(this);
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