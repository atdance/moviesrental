/**
 *
 */
package com.movies.api.error;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {
	/*
	 * A com.fasterxml.jackson.databind.ObjectMapper that serializes any Java
	 * value as a String.
	 */
	protected static ObjectMapper serializer = null;

	static {
		serializer = new ObjectMapper();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
	 */
	@Override
	public Response toResponse(Exception ex) {
		Response res = null;
		try {
			res = Response.status(Status.INTERNAL_SERVER_ERROR).entity(serializer.writeValueAsString(ex.getMessage()))
					.type(MediaType.APPLICATION_JSON).build();
		} catch (final JsonProcessingException e) {
			res = Response.status(Status.INTERNAL_SERVER_ERROR).entity("JsonProcessingException")
					.type(MediaType.APPLICATION_JSON).build();
		} catch (final Exception e) {
			res = Response.status(Status.UNSUPPORTED_MEDIA_TYPE).type(MediaType.APPLICATION_JSON)
					.entity("JsonProcessingException").build();

		}
		return res;
	}
}