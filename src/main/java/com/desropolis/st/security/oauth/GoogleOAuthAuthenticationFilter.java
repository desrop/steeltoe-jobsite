package com.desropolis.st.security.oauth;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.oauth.OAuthException;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.desropolis.st.security.core.JobSiteAuthenticationToken;

public class GoogleOAuthAuthenticationFilter extends GenericFilterBean {

	private static final Logger logger = Logger
			.getLogger(GoogleOAuthAuthenticationFilter.class.getName());
	private static final String DEFAULT_GAPPS_DOMAIN_FIELD = "hd";

	private String gAppsDomainFieldName = DEFAULT_GAPPS_DOMAIN_FIELD;
	private Set<String> returnToUrlParameters = Collections.emptySet();
	private ApplicationEventPublisher eventPublisher = null;
	private AuthenticationManager authenticationManager = null;
	private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
	private OAuthValidator oAuthValidator;

	public GoogleOAuthAuthenticationFilter() {
		// super("/v/j_google_oauth_registration");
	}

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(authenticationManager,
				"An AuthenticationManager must be set");

		// if (returnToUrlParameters.isEmpty()
		// && getRememberMeServices() instanceof AbstractRememberMeServices) {
		// returnToUrlParameters = new HashSet<String>();
		// returnToUrlParameters
		// .add(((AbstractRememberMeServices) getRememberMeServices())
		// .getParameter());
		// }

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Checking secure context token: "
					+ SecurityContextHolder.getContext().getAuthentication());
		}

		try {

			if (requiresAuthentication((HttpServletRequest) request)) {
				doAuthenticate((HttpServletRequest) request,
						(HttpServletResponse) response);
			}

			if (validatingOpenId((HttpServletRequest) request)) {
				response.getWriter().flush();
				response.getWriter().close();
			} else {
				chain.doFilter(request, response);
			}

		} catch (UsernameNotFoundException unf) {
			response.getWriter().flush();
			response.getWriter().close();
		}

	}

	private void doAuthenticate(HttpServletRequest request,
			HttpServletResponse response) {

		Authentication authResult;

		logger.fine("In doAuthenticate()");

		String openSocialViewerId = request
				.getParameter("opensocial_viewer_id");
		if (openSocialViewerId == null) {
			logger.warning("No Open Social Viewer Id");
			return;
		}

		String oauthConsumerKey = request.getParameter("oauth_consumer_key");
		if (oauthConsumerKey == null) {
			logger.warning("No OAuth Consumer Key");
			return;
		}

		String homeDomain = obtainDomain(request);
		if (homeDomain == null) {
			logger.warning("No Home Domain");
			return;
		}

		JobSiteAuthenticationToken token = null;
		try {

			logger.fine("openSocialViewerId = " + openSocialViewerId);
			logger.fine("oauthConsumerKey = " + oauthConsumerKey);
			logger.fine("homeDomain = " + homeDomain);

			try {
				logger.fine("validating OAuth Signature");
				oAuthValidator
						.validateOAuthSignature(request, oauthConsumerKey);
				logger.fine("OAuth Signature valid");
			} catch (OAuthException e) {
				throw new PreAuthenticatedCredentialsNotFoundException(
						"Invalid OAuth Signature: " + e.getMessage());
			}

			token = new JobSiteAuthenticationToken(homeDomain, null,
					openSocialViewerId);
			token.setDetails(authenticationDetailsSource.buildDetails(request));
			authResult = this.getAuthenticationManager().authenticate(token);
			successfulAuthentication(request, response, authResult);

		} catch (UsernameNotFoundException unf) {
			if (validatingOpenId(request)) {
				logger.fine("We're validating the OpenId - content type json");
				startRegistration(request, response, token);
				throw unf;
			} else {
				unsuccessfulAuthentication(request, response, unf);
				throw new PreAuthenticatedCredentialsNotFoundException(
						"No user found for this open_social_id");
			}
		} catch (AuthenticationException failed) {
			unsuccessfulAuthentication(request, response, failed);
			throw failed;
		}

	}

	private boolean requiresAuthentication(HttpServletRequest request) {
		Authentication currentUser = SecurityContextHolder.getContext()
				.getAuthentication();

		if (currentUser == null) {
			return true;
		}

		String openSocialViewerId = request
				.getParameter("opensocial_viewer_id");

		logger.fine("Found current user: " + currentUser.getName() + ": "
				+ openSocialViewerId);

		if (openSocialViewerId.equals((String) currentUser.getCredentials())) {
			return false;
		}

		logger.fine("Open Social Viewer Id has changed to "
				+ openSocialViewerId + " and will be reauthenticated");

		SecurityContextHolder.clearContext();

		HttpSession session = request.getSession(false);

		if (session != null) {
			logger.fine("Invalidating existing session");
			session.invalidate();
			request.getSession();
		}

		return true;

	}

	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, Authentication authResult) {

		logger.fine("Authentication success: " + authResult);
		SecurityContextHolder.getContext().setAuthentication(authResult);
		if (this.eventPublisher != null) {
			eventPublisher
					.publishEvent(new InteractiveAuthenticationSuccessEvent(
							authResult, this.getClass()));
		}

		if (validatingOpenId(request)) {
			logger.fine("We're validating the OpenId - content type json");
			try {
				response.setContentType("application/json");
				response.getWriter().print("{\"content\": {\"user_exists\" : \"true\"}}");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private boolean validatingOpenId(HttpServletRequest request) {

		String uri = request.getRequestURI();
		logger.fine("validatingOpenId: " + uri);

		int pathParamIndex = uri.indexOf(';');

        if (pathParamIndex > 0) {
            // strip everything after the first semi-colon
            uri = uri.substring(0, pathParamIndex);
        }

        if ("".equals(request.getContextPath())) {
            return uri.endsWith("/openid");
        }

        return uri.endsWith(request.getContextPath() + "/openid");

	}

	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed) {
		SecurityContextHolder.clearContext();

		logger.log(Level.FINE, "Cleared security context due to exception",
				failed);
		request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, failed);

	}

	protected String obtainDomain(HttpServletRequest req) {
		String homeDomain = req.getParameter(gAppsDomainFieldName);

		if (!StringUtils.hasText(homeDomain)) {
			logger.warning("No Google Apps domain supplied in authentication request");
			return "";
		}

		return homeDomain.trim();
	}

	private void startRegistration(HttpServletRequest request,
			HttpServletResponse response, JobSiteAuthenticationToken token) {

		SecurityContextHolder.clearContext();

		HttpSession sess = request.getSession(true);
		sess.setAttribute("ABCD", token);

		String host = request.getServerName();
		int port = request.getServerPort();
		
		try {
			URL url = new URL("http", host, port, "/jobsite/register");
			String popup = url.toString();
			popup += "?hd=" + token.getDomainName();
			popup += "&tokenId=" + "ABCD";
			response.setContentType("application/json");
			response.getWriter().print("{\"content\": {\"user_exists\" : \"false\", \"popup\": \"" + popup + "\"}}");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

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

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(
			AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public void setOAuthValidator(OAuthValidator oAuthValidator) {
		this.oAuthValidator = oAuthValidator;
	}

}
