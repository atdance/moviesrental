package com.movies.simulation;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ProcessingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.api.resource.RentalResource;
import com.movies.model.Cart;
import com.movies.model.Movie;
import com.movies.model.Rental;
import com.movies.schema.MoviesDAO;

import io.dropwizard.cli.Command;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

public class WorkFlow extends Command {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkFlow.class);

	public WorkFlow() {
		super("workflow", "execute rentals and returns");
	}

	@Override
	public void configure(Subparser subparser) {
	}

	@Override
	public void run(Bootstrap<?> bootstrap, Namespace namespace) throws Exception {
		LOGGER.info("Workflow begin");

		javax.ws.rs.client.Client client = null;

		try {
			client = ClientBuilder.newClient();
		} catch (final Exception e) {
			LOGGER.error(e.toString(), e);
		}

		if (null == client) {
			LOGGER.error("client null");
			return;
		}
		
		final int id = RentalResource.nextID();

		System.out.println("\n\n\n Submitting Rental request with ID " + id );

		Response response = null;

		try {
			final String aURL_POST = "http://localhost:8080/rentals/save";

			final int aLEASE_DAYS = 4;

			ObjectMapper MAPPER = null;
			MAPPER = Jackson.newObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			final List<Movie> basket = new ArrayList<>();
			/*
			 * basket of movies
			 */
			basket.add(MoviesDAO.getInstance().get(6));
			basket.add(MoviesDAO.getInstance().get(7));

			final Cart cart = new Cart(basket);
			
			System.out.println("\n " + basket );


			/*
			 * rental
			 */
			final Rental rentalSubmitted = new Rental(id, aLEASE_DAYS, cart);

			final String rentalString = MAPPER.writeValueAsString(rentalSubmitted);

			final Entity<String> entity = Entity.entity(rentalString, MediaType.APPLICATION_JSON);
			response = client.target(aURL_POST).request(MediaType.APPLICATION_JSON).post(entity);
			
			System.out.println("\n Response status "+ response.getStatus());
			System.out.println("Rental successfully accepted with ID " + response.readEntity(Integer.class) );
			System.out.println("" );
			
		}catch (final ProcessingException e) {
			System.out.println(" ! Did you start the server? [" + e.getMessage() +"]");
		} catch (final Exception e) {
			LOGGER.error(e.toString(), e);
		} finally {
			if (null != response) {
				response.close();
			}
		}

		/*
		 * continue with a Return
		 */
		System.out.println("\n\n\nReturning movies. Rental has ID " +id);
		System.out.println("" );
		
		
		Response response2 = null;

		try {
			final WebTarget target = client.target("http://localhost:8080/rentals/return?id=" + id + "&elapseddays=1");

			response2 = target.request().accept(MediaType.APPLICATION_JSON).get();
			
			System.out.println("Response status "+ response2.getStatus());
			System.out.println("Rental return successfully submitted.  Rental had ID " +	response2.readEntity(Integer.class) );
			System.out.println("" );

		}catch (final ProcessingException e) {
			System.out.println(" ! Did you start the server? [" + e.getMessage() +"]");
		}catch (final Exception e) {
			LOGGER.error(e.toString(), e);
		} finally {
			LOGGER.info("Workflow end");

			if (null != response2) {
				response2.close();
			}
		}
		

	}
}