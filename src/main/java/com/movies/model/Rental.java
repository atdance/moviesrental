/**
 *
 */
package com.movies.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.movies.model.error.ApiException;

/**
 * @author void
 *
 */
public class Rental {
	private int bonus;
	private int price;
	private final int leasedays;
	private final List<Movie> movies;
	private final int id;

	@JsonCreator
	public Rental(@JsonProperty("id") int pID, @JsonProperty("leasedays") int pDays,
			@JsonProperty("movies") List<Movie> pMovies) {
		if (pDays < Limits.MIN_RENTAL_DAYS.getCode()) {

			throw new ApiException(Limits.MIN_RENTAL_DAYS);
		}
		if (pDays > Limits.MAX_RENTAL_DAYS.getCode()) {
			throw new ApiException(Limits.MAX_RENTAL_DAYS);
		}

		if (pMovies == null || pMovies.size() < Limits.BASKET_EMPTY.getCode()) {
			throw new ApiException(Limits.BASKET_EMPTY);

		}
		if (pMovies.size() > Limits.MAX_BASKET_SIZE.getCode()) {
			throw new ApiException(Limits.MAX_BASKET_SIZE);
		}

		id = pID;
		leasedays = pDays;
		movies = pMovies;
		for (final Movie movie : movies) {
			price += movie.getPrice();
			bonus += movie.getBonus();
		}
	}

	@JsonProperty("bonus")
	public int getBonus() {
		return bonus;
	}

	@JsonProperty("price")
	public int getPrice() {
		return price;
	}

	@JsonProperty("leasedays")
	public int getLeasedays() {
		return leasedays;
	}

	@JsonProperty("id")
	public int getId() {
		return id;
	}

	@JsonProperty("movies")
	public List<Movie> getMovies() {
		return new ArrayList<Movie>(movies);
	}

	@Override
	public String toString() {
		return "Rental [bonus=" + bonus + ", price=" + price + ", leasedays=" + leasedays + ", movies=" + movies
				+ ", id=" + id + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bonus;
		result = prime * result + id;
		result = prime * result + leasedays;
		result = prime * result + ((movies == null) ? 0 : movies.hashCode());
		result = prime * result + price;
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
		final Rental other = (Rental) obj;
		if (bonus != other.bonus) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (leasedays != other.leasedays) {
			return false;
		}
		if (movies == null) {
			if (other.movies != null) {
				return false;
			}
		} else if (!movies.equals(other.movies)) {
			return false;
		}
		if (price != other.price) {
			return false;
		}
		return true;
	}

}