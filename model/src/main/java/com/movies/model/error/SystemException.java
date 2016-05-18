/**
 *
 */
package com.movies.model.error;

/**
 * @author void
 *
 */
public class SystemException extends RuntimeException {

	private static final long serialVersionUID = 2007829136146104920L;

	/**
	 * For failures that has to do with the system and not with the user.
	 */
	public SystemException(String msg) {
		super(msg);
	}

}