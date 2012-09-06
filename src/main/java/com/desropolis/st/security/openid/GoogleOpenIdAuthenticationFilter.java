package com.desropolis.st.security.openid;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

public class GoogleOpenIdAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public static final String DEFAULT_GAPPS_DOMAIN_FIELD = "hd";
	
//	private GoogleAppsOpenId4JavaConsumer consumer;
    private String gAppsDomainFieldName = DEFAULT_GAPPS_DOMAIN_FIELD;
    private Map<String,String> realmMapping = Collections.emptyMap();
    private Set<String> returnToUrlParameters = Collections.emptySet();

    public GoogleOpenIdAuthenticationFilter() {
        super("/j_google_openid_security_check");
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();

//        if (consumer == null) {
//            try {
//                consumer = new GoogleAppsOpenId4JavaConsumer();
//            } catch (ConsumerException e) {
//                throw new IllegalArgumentException("Failed to initialize OpenID", e);
//            }
//        }

        //From Spring OpenID
//        if (returnToUrlParameters.isEmpty() &&
//                getRememberMeServices() instanceof AbstractRememberMeServices) {
//            returnToUrlParameters = new HashSet<String>();
//            returnToUrlParameters.add(((AbstractRememberMeServices)getRememberMeServices()).getParameter());
//        }
    }
	
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {

    	logger.debug("In attemptAuthentication()");
        
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		// logger.debug("GAE UserService returned user: " + user.getUserId());

		String homeDomain = obtainDomain(request);
		
		if (user == null) {

	    	logger.debug("User is null, home domain:" + homeDomain);

			try {
				String returnToUrl = buildReturnToUrl(request);
		    	logger.debug("returnToUrl: " + returnToUrl);
				String openIdUrl = userService.createLoginURL(returnToUrl,
						null, homeDomain, new HashSet<String>());
		    	logger.debug("openIdUrl: " + openIdUrl);
		    	logger.debug("redirecting...");
				response.sendRedirect(openIdUrl);
				return null;
			} catch (Exception e) {
				logger.debug("Failed to redirect for: " + homeDomain, e);
				throw new AuthenticationServiceException(
						"Unable to process domain '" + homeDomain + "'");
			}
		}

		if (logger.isDebugEnabled()) {
            logger.debug("Supplied OpenID identity is " + user.getEmail());
        }

		JobSiteAuthenticationToken token = new JobSiteAuthenticationToken(homeDomain, user.getEmail(), null);
        token.setDetails(authenticationDetailsSource.buildDetails(request));
        Authentication authentication = this.getAuthenticationManager().authenticate(token);

        return authentication;

    }
    
    protected String obtainDomain(HttpServletRequest req) {
        String homeDomain = req.getParameter(gAppsDomainFieldName);

        if (!StringUtils.hasText(homeDomain)) {
            logger.error("No Google Apps domain supplied in authentication request");
            return "";
        }

        return homeDomain.trim();
    }
    
    // Probably don't need  - use GAE createLoginUrl
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

    // The realm of THIS app
    protected String lookupRealm(String returnToUrl) {
        
    	String mapping = realmMapping.get(returnToUrl);

        if (mapping == null) {
            try {
                URL url = new URL(returnToUrl);
                int port = url.getPort();

                StringBuilder realmBuffer = new StringBuilder(returnToUrl.length())
                        .append(url.getProtocol())
                        .append("://")
                        .append(url.getHost());
                if (port > 0) {
                    realmBuffer.append(":").append(port);
                }
                realmBuffer.append("/");
                mapping = realmBuffer.toString();
            } catch (MalformedURLException e) {
                logger.warn("returnToUrl was not a valid URL: [" + returnToUrl + "]", e);
            }
        }

        return mapping;
    
    }
    
}
