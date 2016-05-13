/**
 *
 */
package com.movies.api.dropwizard;

import com.movies.api.error.ApiExceptionMapper;
import com.movies.api.error.GlobalExceptionMapper;
import com.movies.api.error.SystemExceptionMapper;
import com.movies.api.resource.RentalResource;
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
	public void run(MoviesCatalogConfig configuration, Environment environment) throws Exception {

		final RentalsDAO dao = RentalsDAO.getInstance();
		environment.jersey().register(new RentalResource(dao));

		environment.jersey().register(new GlobalExceptionMapper());
		environment.jersey().register(new SystemExceptionMapper());
		environment.jersey().register(new ApiExceptionMapper());

		environment.healthChecks().register("system", new AppHealthCheck());

	}

	public static void main(String[] args) throws Exception {
		new MoviesDropwizardApp().run(args);
	}
}