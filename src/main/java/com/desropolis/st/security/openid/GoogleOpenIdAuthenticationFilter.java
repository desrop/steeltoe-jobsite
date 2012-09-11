package com.desropolis.st.security.openid;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StringUtils;

import com.desropolis.st.security.core.JobSiteAuthenticationToken;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class GoogleOpenIdAuthenticationFilter extends
		AbstractAuthenticationProcessingFilter {

	public static final String DEFAULT_GAPPS_DOMAIN_FIELD = "hd";

	public static final Logger logger = Logger
			.getLogger(GoogleOpenIdAuthenticationFilter.class.getName());

	private String gAppsDomainFieldName = DEFAULT_GAPPS_DOMAIN_FIELD;
	private Set<String> returnToUrlParameters = Collections.emptySet();

	public GoogleOpenIdAuthenticationFilter() {
		super("/jobsite/j_google_openid_security_check");
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();

		// if (consumer == null) {
		// try {
		// consumer = new GoogleAppsOpenId4JavaConsumer();
		// } catch (ConsumerException e) {
		// throw new IllegalArgumentException("Failed to initialize OpenID", e);
		// }
		// }
		
		returnToUrlParameters = new HashSet<String>();
		returnToUrlParameters.add("hd");
		returnToUrlParameters.add("tokenId");

		// From Spring OpenID
		// if (returnToUrlParameters.isEmpty() &&
		// getRememberMeServices() instanceof AbstractRememberMeServices) {
		// returnToUrlParameters = new HashSet<String>();
		// returnToUrlParameters.add(((AbstractRememberMeServices)getRememberMeServices()).getParameter());
		// }
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException,
			IOException {

		logger.fine("In attemptAuthentication()");

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String homeDomain = obtainDomain(request);

		if (user == null) {

			try {
				String returnToUrl = buildReturnToUrl(request);
				logger.fine("returnToUrl: " + returnToUrl);
				String openIdUrl = userService.createLoginURL(returnToUrl,
						null, homeDomain, new HashSet<String>());
				logger.fine("openIdUrl: " + openIdUrl);
				logger.fine("redirecting...");
				response.sendRedirect(openIdUrl);
				return null;
			} catch (Exception e) {
				logger.log(Level.WARNING, "Failed to redirect for: "
						+ homeDomain, e);
				throw new AuthenticationServiceException(
						"Unable to process domain '" + homeDomain + "'");
			}
		}

		logger.fine("Supplied OpenID identity is " + user.getEmail());

		JobSiteAuthenticationToken token = null;
		String openSocialViewerId = null;
		String tokenId = request.getParameter("tokenId");
		if (tokenId != null) {
			logger.fine("Completing registration... ");
			JobSiteAuthenticationToken oAuthToken = (JobSiteAuthenticationToken) request
					.getSession().getAttribute(tokenId);
			if (oAuthToken != null) {
				openSocialViewerId = oAuthToken.getOpenSocialViewerId();
				logger.fine("OAuth Open Social User Id: " + openSocialViewerId);
				token = new JobSiteAuthenticationToken(homeDomain,
						user.getEmail(), openSocialViewerId);
			} else {
				logger.log(
						Level.WARNING,
						"Couldn't complete oAuth registration for "
								+ user.getEmail() + " - No oAuthToken found.");
				throw new AuthenticationServiceException(
						"Couldn't complete oAuth registration for "
								+ user.getEmail() + " - No oAuthToken found.");
			}
		} else {
			token = new JobSiteAuthenticationToken(homeDomain, user.getEmail(),
					null);
		}

		token.setDetails(authenticationDetailsSource.buildDetails(request));
		Authentication authentication = this.getAuthenticationManager()
				.authenticate(token);

		return authentication;

	}

	protected String obtainDomain(HttpServletRequest req) {
		String homeDomain = req.getParameter(gAppsDomainFieldName);

		if (!StringUtils.hasText(homeDomain)) {
			logger.warning("No Google Apps domain supplied in authentication request");
			return "";
		}

		return homeDomain.trim();
	}

	// Probably don't need - use GAE createLoginUrl
	protected String buildReturnToUrl(HttpServletRequest request) {

		StringBuffer sb = request.getRequestURL();

		Iterator<String> iterator = returnToUrlParameters.iterator();
		boolean isFirst = true;

		while (iterator.hasNext()) {
			String name = iterator.next();
			// Assume for simplicity that there is only one value
			String value = request.getParameter(name);

			if (value == null) {
				continue;
			}

			if (isFirst) {
				sb.append("?");
				isFirst = false;
			}
			sb.append(name).append("=").append(value);

			if (iterator.hasNext()) {
				sb.append("&");
			}
		}

		return sb.toString();

	}

}
