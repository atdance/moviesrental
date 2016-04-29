package com.movies.api.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import junit.framework.JUnit4TestAdapter;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TestClient.class })

public class TestApp {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(TestApp.class);
	}

}