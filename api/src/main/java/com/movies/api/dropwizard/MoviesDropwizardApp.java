/**
 *
 */
package com.movies.api.dropwizard;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import com.movies.api.error.ApiExceptionMapper;
import com.movies.api.error.GlobalExceptionMapper;
import com.movies.api.error.SystemExceptionMapper;
import com.movies.api.resource.RentalResource;
import com.movies.controller.Controller;
import com.movies.schema.RentalsDAO;
import com.movies.simulation.WorkFlow;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MoviesDropwizardApp extends Application<MoviesCatalogConfig> {
	@Override
	public void initialize(Bootstrap<MoviesCatalogConfig> bootstrap) {

		bootstrap.addCommand(new WorkFlow());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see io.dropwizard.Application#run(io.dropwizard.Configuration,
	 * io.dropwizard.setup.Environment)
	 */
	@Override
	public void run(MoviesCatalogConfig configuration, final Environment environment) throws Exception {

		final RentalsDAO dao = RentalsDAO.getInstance();
		final Controller controller = new Controller(dao);
		environment.jersey().register(new RentalResource(controller));

		environment.jersey().register(new GlobalExceptionMapper());
		environment.jersey().register(new SystemExceptionMapper());
		environment.jersey().register(new ApiExceptionMapper());

		environment.healthChecks().register("system", new AppHealthCheck());

		final RateLimiter limiter = new RateLimiter();
		RateLimiter.enable();

		environment.servlets().addFilter("Custom-Filter-Name", limiter)
				.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

	}

	public static void main(String[] args) throws Exception {
		new MoviesDropwizardApp().run(args);
	}
}