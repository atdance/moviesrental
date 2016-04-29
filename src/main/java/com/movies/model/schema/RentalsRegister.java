/**
 *
 */
package com.movies.model.schema;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.movies.model.Limits;
import com.movies.model.Rental;
import com.movies.model.error.ApiException;

/**
 * Contains information about the rentals. Thread consistency implemented with a
 * ReentrantReadWriteLock .
 *
 */
public class RentalsRegister {

	private static final Logger LOGGER = LoggerFactory.getLogger(RentalsRegister.class);
	private volatile static RentalsRegister localSession;

	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final Lock readLock = rwl.readLock();
	private final Lock writeLock = rwl.writeLock();

	private final List<Rental> rentals = new ArrayList<Rental>();

	private RentalsRegister() {
	}

	/**
	 * Factory method to get singleton the RentalsRegister
	 *
	 * @return RentalsRegister
	 * @throws IOException
	 */
	public static synchronized RentalsRegister getInstance() {
		RentalsRegister tempSession = localSession;

		if (tempSession == null) {
			synchronized (RentalsRegister.class) {
				tempSession = localSession;
				if (tempSession == null) {
					tempSession = new RentalsRegister();
					localSession = tempSession;
				}
			}
		}
		return tempSession;
	}

	/*
	 * public void add(Rental obj) { writeLock.lock(); try { rentals.add(obj);
	 *
	 * } catch (final Exception e) { LOGGER.error("Could not add Rental"); }
	 * finally { writeLock.unlock(); } }
	 */

	public void addByID(int pID, Rental pRental) {
		writeLock.lock();
		try {
			rentals.add(pID, pRental);
		} catch (final Exception e) {
			LOGGER.error("Could not add rental by ID " + pID + " " + e.toString());
		} finally {
			writeLock.unlock();
		}
	}

	public void remove(Rental rental) {
		writeLock.lock();
		try {
			final int id = rental.getId();
			rentals.set(id, null);
			// rentals.rentals.remove(obj);
		} catch (final Exception e) {
			LOGGER.info("Could not remove Rental");
		} finally {
			writeLock.unlock();
		}
	}

	public Rental findByID(int pID) {
		readLock.lock();

		Rental res = null;
		try {
			for (final Rental rental : rentals) {
				if (rental.getId() == pID) {
					res = rental;
					break;
				}
			}
		} catch (final Exception e) {
			res = null;
			LOGGER.error(e.toString());
		} finally {
			readLock.unlock();
		}
		if (res == null) {
			throw new ApiException(Limits.RENTAL_NOT_FOUND);
		}

		return res;
	}

	public List<Rental> getAll() {
		readLock.lock();

		List<Rental> res = null;
		try {
			res = rentals;
		} catch (final Exception e) {
			LOGGER.info("return EMPTY_LIST");
			res = Collections.emptyList();
		} finally {
			readLock.unlock();
		}
		return res;
	}
}