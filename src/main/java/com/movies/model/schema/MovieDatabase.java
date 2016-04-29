/**
 *
 */
package com.movies.model.schema;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.movies.model.Movie;

public class MovieDatabase {
	private volatile static MovieDatabase localSession;

	private List<String> titles = new ArrayList<>();
	private java.util.concurrent.ThreadLocalRandom random = null;
	private final List<Movie> movies = new ArrayList<>();
	private final Map<String, Movie> index = new HashMap<>();

	/**
	 * Factory method to get singleton the MovieDatabase
	 *
	 * @return MovieDatabase
	 * @throws IOException
	 */
	public static synchronized MovieDatabase getInstance() {
		MovieDatabase tempSession = localSession;

		if (tempSession == null) {
			synchronized (MovieDatabase.class) {
				tempSession = localSession;
				if (tempSession == null) {
					tempSession = new MovieDatabase();
					localSession = tempSession;
				}
			}
		}
		return tempSession;
	}

	/**
	 * Creates Movies and add them to database. All attributes of a movie apart
	 * the title are randomly chosen.
	 */
	private MovieDatabase() {
		random = ThreadLocalRandom.current();
		titles = Titles.getInstance().getAll();

		for (final String title : titles) {
			final Movie.TypeAndBonus ftype = randomValue(Movie.TypeAndBonus.values());

			final Movie.PriceType price = randomValue(Movie.PriceType.values());

			final Movie movie = new Movie(title, ftype, price);
			movies.add(movie);
			index.put(title, movie);
		}
	}

	private <T> T randomValue(T[] values) {
		return values[random.nextInt(values.length)];
	}

	public Movie getByTitle(String title) {
		return index.get(title);

	}

	@Override
	public String toString() {
		final int maxLen = 10;
		return "MovieDatabase [movies=" + (movies != null ? movies.subList(0, Math.min(movies.size(), maxLen)) : null)
				+ "]";
	}
}