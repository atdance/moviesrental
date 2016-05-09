/**
 *
 */
package com.movies.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.movies.model.error.ApiException;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author void
 *
 */
@ToString
@EqualsAndHashCode
public class Rental {

	private int bonus;
	private int price;
	private final int leasedays;
	private final Cart cart;
	private final int id;

	@JsonCreator
	public Rental(@JsonProperty("id") int pID, @JsonProperty("leasedays") int pDays,
			@JsonProperty("cart") Cart pMovies) {
		if (pDays < Limits.MIN_RENTAL_DAYS.getLimit()) {

			throw new ApiException(Limits.MIN_RENTAL_DAYS);
		}
		if (pDays > Limits.MAX_RENTAL_DAYS.getLimit()) {
			throw new ApiException(Limits.MAX_RENTAL_DAYS);
		}

		id = pID;
		leasedays = pDays;
		cart = pMovies;
		final List<Movie> moviesList = cart.getMovies();
		for (final Movie movie : moviesList) {
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

	@JsonProperty("cart")
	public Cart getCart() {
		return cart;
	}

}