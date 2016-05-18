package com.movies.model;

import java.util.List;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
		movies = pMovies;

		for (final Movie movie : movies) {
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

	@Size(min = 1, max = 9, message = "The number of movies in cart '${validatedValue}' must be between {min} and {max}")
	@JsonProperty("movies")
	public List<Movie> getMovies() {
		return movies;
	}

}