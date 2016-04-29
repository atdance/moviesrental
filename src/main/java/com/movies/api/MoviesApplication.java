/**
 *
 */
package com.movies.api;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.ws.rs.core.Application;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import com.movies.api.error.ApiExceptionMapper;
import com.movies.api.error.GlobalExceptionMapper;
import com.movies.api.error.SystemExceptionMapper;

public class MoviesApplication extends Application {

	public static void main(String[] args) throws Exception {
		final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");

		final Server jettyServer = new Server(8080);
		jettyServer.setHandler(context);

		context.addServlet(new ServletHolder(new ServletContainer(new JerseyConfig())), "/*");

		try {
			jettyServer.start();
			jettyServer.join();
		} finally {
			jettyServer.destroy();
		}
	}

	public static void configureCors(ServletContextHandler environment) {

		final FilterHolder holder = new FilterHolder(new CrossOriginFilter());
		holder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
		holder.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET, POST, DELETE, PUT, PATCH, OPTIONS");
		holder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_HEADERS_HEADER,
				"Content-Type, api_key, Authorization");

		holder.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "");
		holder.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM,
				"Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
		holder.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
		environment.addFilter(holder, "/*", EnumSet.of(DispatcherType.REQUEST));
	}
}

class JerseyConfig extends ResourceConfig {
	public JerseyConfig() {
		packages(true, "com.bondsbiz");
		register(SystemExceptionMapper.class);
		register(ApiExceptionMapper.class);
		register(GlobalExceptionMapper.class);
	}
}