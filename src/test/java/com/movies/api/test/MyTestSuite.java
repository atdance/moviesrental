package com.movies.api.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.movies.model.CartTest;
import com.movies.model.MovieTest;
import com.movies.model.RentalReturnTest;
import com.movies.model.RentalTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TestClient.class, MovieTest.class, CartTest.class, RentalTest.class, RentalReturnTest.class })

public class MyTestSuite {

}
