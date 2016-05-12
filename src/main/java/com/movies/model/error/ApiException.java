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

	private final String errorMsg;

	public ApiException(Limits code) {
		super(code.getMsg());
		errorMsg = code.getMsg();
	}

	@Override
	public String toString() {
		return "ApiException [errorMsg=" + errorMsg + "]";
	}

}