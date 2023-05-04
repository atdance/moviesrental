package com.movies.simulation;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Encapsulates the result and a result of a Response */
public class Operation {

	private static final Logger LOGGER                = LoggerFactory.getLogger(Operation.class);

	private boolean             isOperationSucessfull = false;

	private int                 responseStatus;

	private int                 id;

	private String              message;

	public Operation(Response pResponse, String pMsg) {
		try {
			responseStatus        = pResponse.getStatus();

			id                    = pResponse.readEntity(Integer.class);

			message               = pMsg;

			isOperationSucessfull = true;

		} catch (final ProcessingException e) {
			System.out.println(" ! Did you start the server? [" + e.getMessage() + "]");
		} catch (final Exception e) {
			LOGGER.error(e.toString(), e);
		}
	}

	public Operation(boolean pIsOperationSucessfull) {
		responseStatus        = Response.Status.NOT_ACCEPTABLE.getStatusCode();

		id                    = -1;

		isOperationSucessfull = pIsOperationSucessfull;
	}

	boolean isOperationSucessfull() {
		return isOperationSucessfull;
	}

	public String getResult() {
		return "Response status " + responseStatus + System.lineSeparator() + message + id + System.lineSeparator();
	}
}