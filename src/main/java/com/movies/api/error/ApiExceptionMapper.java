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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.model.error.ApiException;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ApiExceptionMapper implements ExceptionMapper<ApiException> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionMapper.class);
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
	public Response toResponse(ApiException ex) {
		Response res = null;
		try {
			res = Response.status(Status.BAD_REQUEST).entity(serializer.writeValueAsString(ex.toString()))
					.type(MediaType.APPLICATION_JSON).build();
		} catch (final JsonProcessingException e) {
			res = Response.status(Status.BAD_REQUEST).entity("JsonProcessingException").type(MediaType.APPLICATION_JSON)
					.build();
		} catch (final Exception e) {
			LOGGER.error(e.toString());
			res = Response.status(Status.UNSUPPORTED_MEDIA_TYPE).entity("JsonProcessingException").build();

		}
		return res;
	}
}