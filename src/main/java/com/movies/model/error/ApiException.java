/**
 *
 */
package com.movies.model.error;

import com.movies.model.Limits;

/**
 * @author void
 *
 */
public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 7091252269424243077L;

	private final int limit;
	private final String errorMsg;

	public ApiException(Limits code) {
		super(code.getMsg());
		errorMsg = code.getMsg();
		limit = code.getLimit();
	}

	@Override
	public String toString() {
		return "ApiException [errorCode=" + limit + ", errorMsg=" + errorMsg + "]";
	}

}