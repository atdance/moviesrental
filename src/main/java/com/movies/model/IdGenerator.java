package com.movies.model;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
	static AtomicInteger seq = new AtomicInteger();

	private IdGenerator() {
	}

	public static int next() {
		return seq.incrementAndGet() - 1;
	}

}
