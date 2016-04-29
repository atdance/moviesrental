/**
 *
 */
package com.movies.model.schema;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Index of all available movie titles.
 *
 */
public class Titles {

	private final String titlesArray[] = { "Rush", "Prisoners", "Thor: The Dark World", "This Is the End",
			"Insidious: Chapter 2", "World War Z", "X-Men: Days of Future Past", "Now You See Me", "Gravity", "Riddick",
			"The Family", "Star Trek Into Darkness", "After Earth", "The Great Gatsby", "Divergent",
			"We Are What We Are", "Iron Man 3", "The Amazing Spider-Man 2", "The Conjuring", "Escape Plan", "Elysium",
			"Cloudy with a Chance of Meatballs 2", "RoboCop", "Need for Speed", "Runner Runner" };

	/**
	 * Constraint of movie title being unique within the Movies Store is
	 * enforced with a Set.
	 */
	private Set<String> uniqueTitles = null;

	/**
	 * Used to retrieve titles by a number index
	 */
	ArrayList<String> list = null;

	private volatile static Titles localSession;

	/**
	 * Factory method to get singleton the Titles
	 *
	 * @return Titles
	 * @throws IOException
	 */
	public static synchronized Titles getInstance() {
		Titles tempSession = localSession;

		if (tempSession == null) {
			synchronized (Titles.class) {
				tempSession = localSession;
				if (tempSession == null) {
					tempSession = new Titles();
					localSession = tempSession;
				}
			}
		}
		return tempSession;
	}

	/**
	 *
	 */
	private Titles() {
		uniqueTitles = new HashSet<>();
		uniqueTitles.addAll(Arrays.asList(titlesArray));

		list = new ArrayList<String>();

		final List<Object> tmpList = Arrays.asList(uniqueTitles.toArray());
		for (final Object object : tmpList) {
			list.add((String) object);
		}

	}

	public String get(int pID) {
		return list.get(pID);
	}

	public List<String> getAll() {
		return new ArrayList<String>(list);
	}
}