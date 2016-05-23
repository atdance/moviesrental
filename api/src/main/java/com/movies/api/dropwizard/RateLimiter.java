package com.movies.api.dropwizard;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet Filter implementation It limits the rate of incoming requests at the
 * Application level . It applies to POST requests only.
 *
 * Requests more frequent than 2.5 seconds are not handled and are answered with
 * an error code.
 *
 */
@WebFilter(urlPatterns = { "/*" })
public class RateLimiter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(RateLimiter.class);

	private final long TIME_LIMIT_2_5_SECONDS = 2_500_000_000L;;
	private long now = 0L;
	private long before = System.nanoTime();
	private long elapsedMillis = 0L;

	/**
	 * disables the filter if false,used for unit tests.
	 */
	private static boolean enabled;

	/**
	 * Default constructor.
	 */
	public RateLimiter() {
	}

	private static Logger LOG = null;
	static {
		LOG = LoggerFactory.getLogger(RateLimiter.class);
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		final HttpServletResponse res = (HttpServletResponse) response;

		final HttpServletRequest req = (HttpServletRequest) request;

		if (req.getMethod().equalsIgnoreCase("GET")) {
			chain.doFilter(request, response);
			return;
		}

		now = System.nanoTime();

		elapsedMillis = TimeUnit.MILLISECONDS.toMillis(elapsed());

		before = now;

		if (enabled && (elapsedMillis < TIME_LIMIT_2_5_SECONDS)) {
			LOGGER.info("FILTERED out " + elapsedMillis + " vs " + TIME_LIMIT_2_5_SECONDS);
			res.sendError(429);
			res.addIntHeader("Retry/After", (int) (TimeUnit.NANOSECONDS.toSeconds(TIME_LIMIT_2_5_SECONDS)));
			return;
		} else {
			LOGGER.info(" " + elapsedMillis + " vs " + TIME_LIMIT_2_5_SECONDS);
		}

		chain.doFilter(request, response);
	}

	public long elapsed() {
		return System.nanoTime() - before;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		LOG.info("RATELIMITER init");

	}

	public static void enable() {
		enabled = true;
	}

	public static void disable() {
		enabled = false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

}