/**
 *
 */
package com.movies.api.error;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.slf4j.LoggerFactory;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ApiExceptionMapper extends MoviesExceptionMapper {

	public ApiExceptionMapper() {
		aStatus = Status.BAD_REQUEST;
		aLOGGER = LoggerFactory.getLogger(ApiExceptionMapper.class);
	}

	@Override
	public String toString() {
		return "ApiExceptionMapper [aStatus=" + aStatus + "]";
	}

}