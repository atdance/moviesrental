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

	private final int errorCode;
	private final String errorMsg;

	public ApiException(Limits code) {
		errorMsg = code.getMsg();
		errorCode = code.getCode();
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	@Override
	public String toString() {
		return "ApiException [errorCode=" + errorCode + ", errorMsg=" + errorMsg + "]";
	}

}