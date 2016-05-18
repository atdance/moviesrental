/**
 *
 */
package com.movies.model.error;

/**
 * @author void
 *
 */
public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 7091252269424243077L;

	private final String errorMsg;

	public ApiException(String code) {
		super(code);
		errorMsg = code;
	}

	@Override
	public String toString() {
		return "ApiException [errorMsg=" + errorMsg + "]";
	}

}