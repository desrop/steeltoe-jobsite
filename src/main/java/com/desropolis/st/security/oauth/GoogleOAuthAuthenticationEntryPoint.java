package com.desropolis.st.security.oauth;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.util.StringUtils;

public class GoogleOAuthAuthenticationEntryPoint implements
		AuthenticationEntryPoint {

	private static final Logger logger = Logger
			.getLogger(GoogleOAuthAuthenticationEntryPoint.class.getName());

	private String googleOAuthAuthenticationUrl;

	public GoogleOAuthAuthenticationEntryPoint(String loginFormUrl,
			GoogleOAuthAuthenticationFilter filter) {
		// super(loginFormUrl);
		init(filter);
	}

	private void init(GoogleOAuthAuthenticationFilter filter) {

//		googleOAuthAuthenticationUrl = filter.getFilterProcessesUrl();
//		logger.finer("Found login filter URL: " + googleOAuthAuthenticationUrl);

	}

	@Override
	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {

		logger.finer("in commence()");

		response.sendRedirect(googleOAuthAuthenticationUrl);
		
//		String homeDomain = request.getParameter("hd");
//		if (StringUtils.hasText(homeDomain)) {
//			logger.debug("Found homeDomain: " + homeDomain
//					+ ". Redirecting to security check.");
//			response.sendRedirect(googleOAuthAuthenticationUrl + "?hd="
//					+ homeDomain);
//			return;
//		}

	}

	// @Override
	// protected String determineUrlToUseForThisRequest(
	// HttpServletRequest request, HttpServletResponse response,
	// AuthenticationException exception) {
	//
	// logger.debug("in determineUrlToUseForThisRequest");
	//
	// String homeDomain = request.getParameter("hd");
	// if (StringUtils.hasText(homeDomain)) {
	// logger.debug("Found homeDomain: " + homeDomain
	// + ". Redirecting to security check.");
	// return googleOpenIdAuthenticationUrl + "?hd=" + homeDomain;
	// }
	//
	// logger.debug("Did not find homeDomain. Redirecting to login page.");
	// return getLoginFormUrl();
	//
	// }

}
