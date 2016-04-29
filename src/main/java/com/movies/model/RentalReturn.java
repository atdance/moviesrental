/**
 *
 */
package com.movies.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.movies.model.error.ApiException;
import com.movies.model.error.SystemException;

/**
 * @author void
 *
 */
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
		if (elapsedDays > Limits.MAX_DELAY_LENGTH.getCode()) {
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
			for (final Movie movie : aRental.getMovies()) {
				final int price = movie.getPrice();
				charge += (price * delay);
			}
		}
		return new Integer(charge);
	}

	@Override
	public String toString() {
		return "RentalReturn [aRental=" + aRental + ", aElapsedDays=" + aElapsedDays + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + aElapsedDays;
		result = prime * result + ((aRental == null) ? 0 : aRental.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final RentalReturn other = (RentalReturn) obj;
		if (aElapsedDays != other.aElapsedDays) {
			return false;
		}
		if (aRental == null) {
			if (other.aRental != null) {
				return false;
			}
		} else if (!aRental.equals(other.aRental)) {
			return false;
		}
		return true;
	}

}