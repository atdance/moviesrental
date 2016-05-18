/**
 *
 */
package com.movies.schema;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.movies.model.Rental;

/**
 * Contains information about the rentals. Thread consistency implemented with a
 * ReentrantReadWriteLock .
 *
 */
public class RentalsDAO {

	private static final Logger aLOGGER = LoggerFactory.getLogger(RentalsDAO.class);
	private static volatile RentalsDAO localSession;

	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final Lock readLock = rwl.readLock();
	private final Lock writeLock = rwl.writeLock();

	private List<Rental> rentals = null;

	private RentalsDAO() {
		rentals = new ArrayList<>();
	}

	/**
	 * Factory method to get singleton the RentalsRegister
	 *
	 * @return RentalsRegister
	 * @throws IOException
	 */
	public static synchronized RentalsDAO getInstance() {
		RentalsDAO tempSession = localSession;

		if (tempSession == null) {
			synchronized (RentalsDAO.class) {
				tempSession = localSession;
				if (tempSession == null) {
					tempSession = new RentalsDAO();
					localSession = tempSession;
				}
			}
		}
		return tempSession;
	}

	public void addByID(int pID, Rental pRental) {
		writeLock.lock();

		try {
			rentals.add(pID, pRental);
		} catch (final Exception e) {
			aLOGGER.error("Could not add rental by ID " + pID + " " + e.toString(), e);
		} finally {
			writeLock.unlock();
		}
	}

	public Rental findByID(int pID) {
		writeLock.lock();

		Rental res = null;

		try {
			for (final Rental rental : rentals) {
				if (rental.getId() == pID) {
					res = rental;
					rentals.set(rental.getId(), null);
					break;
				}
			} // end if else

		} catch (final Exception e) {
			res = null;
			aLOGGER.error(e.toString() + " " + e.getMessage(), e);
		} finally {
			writeLock.unlock();
		}

		return res;
	}

	public List<Rental> getAll() {
		readLock.lock();

		List<Rental> res = null;
		try {
			if (rentals != null) {
				res = new ArrayList<>(100);
				res.addAll(rentals);
			}
		} catch (final Exception e) {
			aLOGGER.warn("return EMPTY_LIST", e);
			res = Collections.emptyList();
		} finally {
			readLock.unlock();
		}
		return res;
	}

	public void clear() {
		writeLock.lock();

		try {
			rentals.clear();
		} catch (final Exception e) {
			aLOGGER.warn(e.toString(), e);
		} finally {
			writeLock.unlock();
		}
	}

}