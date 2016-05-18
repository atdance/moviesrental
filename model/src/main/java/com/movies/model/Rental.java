/**
 *
 */
package com.movies.model;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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

		id = pID;
		leasedays = pDays;
		cart = pMovies;

		final List<Movie> moviesList = cart.getMovies();
		for (final Movie movie : moviesList) {
			price += movie.getPrice();
			bonus += movie.getBonus();
		}
		MyValidator.validate(this);
	}

	@JsonProperty("bonus")
	public int getBonus() {
		return bonus;
	}

	@JsonProperty("price")
	public int getPrice() {
		return price;
	}

	@Min(value = 1, message = "The rental period is too short")
	@Max(value = 9, message = "The rental period is too long")
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