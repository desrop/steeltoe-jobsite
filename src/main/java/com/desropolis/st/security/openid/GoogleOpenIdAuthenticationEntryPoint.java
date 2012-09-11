package com.desropolis.st.security.openid;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.util.StringUtils;

public class GoogleOpenIdAuthenticationEntryPoint extends
		LoginUrlAuthenticationEntryPoint {

	protected final Log logger = LogFactory.getLog(getClass());

	private String googleOpenIdAuthenticationUrl;

	public GoogleOpenIdAuthenticationEntryPoint(String loginFormUrl,
			GoogleOpenIdAuthenticationFilter filter) {
		super(loginFormUrl);
		init(filter);
	}

	private void init(GoogleOpenIdAuthenticationFilter filter) {

		googleOpenIdAuthenticationUrl = filter.getFilterProcessesUrl();
		logger.debug("Found login filter URL: " + googleOpenIdAuthenticationUrl);

	}

	@Override
	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {

		logger.debug("in commence()");

		String homeDomain = request.getParameter("hd");
		String tokenId = request.getParameter("tokenId");
		if (StringUtils.hasText(homeDomain)) {
			logger.debug("Found homeDomain: " + homeDomain
					+ ". Redirecting to security check.");
			String url = googleOpenIdAuthenticationUrl + "?hd=" + homeDomain;
			if (tokenId != null) {
				logger.debug("Found tokenId, we're completing registration.");
				url += "&tokenId=" + tokenId;
			}
			response.sendRedirect(url);
			return;
		}

		super.commence(request, response, authException);

	}

}
