package com.movies.api.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.movies.model.Cart;
import com.movies.model.Movie;
import com.movies.model.error.ApiException;

public class CartTest {

	@Test
	public void postValidCart() {
		final Movie movie = new Movie("aaa", Movie.TypeAndBonus.NEW, Movie.PriceType.BASIC);

		new Cart(Arrays.asList(movie));
	}

	@Test(expected = ApiException.class)
	public void postInvalidCart() {
		final List<Movie> empty = new ArrayList<>();

		new Cart(empty);
	}

}