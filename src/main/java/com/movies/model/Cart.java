package com.movies.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.movies.model.error.ApiException;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode

public class Cart {
	private int bonus;
	private int price;
	private final List<Movie> movies;

	@JsonCreator
	public Cart(@JsonProperty("movies") List<Movie> pMovies) {

		if (pMovies == null || pMovies.size() < Limits.BASKET_EMPTY.getLimit()) {
			throw new ApiException(Limits.BASKET_EMPTY);

		}
		if (pMovies.size() > Limits.MAX_BASKET_SIZE.getLimit()) {
			throw new ApiException(Limits.MAX_BASKET_SIZE);
		}

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

	@JsonProperty("movies")
	public List<Movie> getMovies() {
		return movies;
	}

}
