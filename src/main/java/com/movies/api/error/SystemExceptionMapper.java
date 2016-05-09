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
public class SystemExceptionMapper extends MoviesExceptionMapper {

	public SystemExceptionMapper() {
		aStatus = Status.INTERNAL_SERVER_ERROR;
		aLOGGER = LoggerFactory.getLogger(SystemExceptionMapper.class);
	}
}