package com.movies.api.test;

import org.junit.Test;

import com.movies.model.Movie;
import com.movies.model.error.ApiException;

public class MovieTest {

	@Test
	public void postValidMovie() throws Exception {

		new Movie("aa", Movie.TypeAndBonus.NEW, Movie.PriceType.BASIC);
	}

	@Test(expected = ApiException.class)
	public void postInvalidMovieTitle() {

		new Movie("a", Movie.TypeAndBonus.NEW, Movie.PriceType.BASIC);
	}

	@Test(expected = ApiException.class)
	public void postInvalidMovieBonus() throws Exception {

		new Movie("aaaa", null, Movie.PriceType.BASIC);
	}

}
