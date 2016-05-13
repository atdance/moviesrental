package com.movies.api.test;

import java.util.ArrayList;
import java.util.List;

import com.movies.model.Cart;
import com.movies.model.Movie;
import com.movies.schema.MoviesDAO;

public class TestCommon {
	static final int LEASE_DAYS = 4;

	private static List<Movie> basket = null;

	static {
		basket = new ArrayList<>();
		basket.add(MoviesDAO.getInstance().get(0));
		basket.add(MoviesDAO.getInstance().get(9));
	}

	TestCommon() {
	}

	protected static Cart buildCart() {
		return new Cart(basket);
	}

}
