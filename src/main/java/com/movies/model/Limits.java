/**
 *
 */
package com.movies.model;

/**
 * Example: throw new ApiException(Limits.ERROR); catch (ApiException e) {
 * Debug(e.getErrorMsg()); ...
 */
public enum Limits {
	MAX_RENTAL_DAYS(1000, "too long rental period, max 1000 days allowed"), MIN_RENTAL_DAYS(1,
			"rental days less than 1"),

	MAX_BASKET_SIZE(9, "Too many movies in basket, max " + 9 + " allowed"), BASKET_EMPTY(1, "no movie in basket"),

	EMPTY_TITLE(1, "Movie title cannot be empty"), MAX_DELAY_LENGTH(1000,
			"Very long delay: not acceptable"), RENTAL_NOT_FOUND(0, "rental not found on requested ID");

	private final int limit;
	private final String msg;

	Limits(int pLimit, String pMsg) {
		limit = pLimit;
		msg = pMsg;
	}

	public int getLimit() {
		return limit;
	}

	public String getMsg() {
		return msg;
	}
}