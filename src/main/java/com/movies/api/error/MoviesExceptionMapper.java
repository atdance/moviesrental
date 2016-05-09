package com.movies.api.error;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class MoviesExceptionMapper implements ExceptionMapper<Throwable> {

	static Logger aLOGGER = LoggerFactory.getLogger(MoviesExceptionMapper.class);
	/*
	 * A com.fasterxml.jackson.databind.ObjectMapper that serializes any Java
	 * value as a String.
	 */
	protected static ObjectMapper serializer = null;

	static {
		serializer = new ObjectMapper();
	}

	/**
	 * must be overrided by subclasses
	 */
	protected Status aStatus = null;

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
	 */
	@Override
	public Response toResponse(Throwable ex) {
		Response res = null;
		try {
			res = buildResponse(aStatus, serializer.writeValueAsString(ex.getMessage()));

		} catch (final JsonProcessingException e) {
			log(e);
			res = buildResponse(aStatus, "JsonProcessingException");
		} catch (final Exception e) {
			log(e);
			res = buildResponse(Status.UNSUPPORTED_MEDIA_TYPE, "JsonProcessingException");
		}
		return res;
	}

	Response buildResponse(Status pStatus, String pEntity) {
		return Response.status(pStatus).entity(pEntity).type(MediaType.APPLICATION_JSON).build();

	}

	void log(Throwable e) {
		aLOGGER.error(e.toString() + " " + e.getMessage() + " " + e.getCause(), e);

	}

	@Override
	public String toString() {
		return "MoviesExceptionMapper [aStatus=" + aStatus + "]";
	}

}