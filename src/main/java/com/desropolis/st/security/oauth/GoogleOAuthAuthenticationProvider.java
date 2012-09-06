package com.desropolis.st.security.oauth;

import java.util.logging.Logger;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.Assert;

import com.desropolis.st.security.core.JobSiteAuthenticationToken;

public class GoogleOAuthAuthenticationProvider implements AuthenticationProvider, InitializingBean {

	private static final Logger logger = Logger
			.getLogger(GoogleOAuthAuthenticationProvider.class.getName());

    private AuthenticationUserDetailsService<JobSiteAuthenticationToken> userDetailsService;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.userDetailsService, "The userDetailsService must be set");
    }

    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {

        if (!supports(authentication.getClass())) {
            return null;
        }

        if (authentication instanceof JobSiteAuthenticationToken) {

        	JobSiteAuthenticationToken response = (JobSiteAuthenticationToken) authentication;
        	UserDetails userDetails = userDetailsService.loadUserDetails(response);
        	return createSuccessfulAuthentication(userDetails, response);

        }

        return null;
    
    }

    /**
     * Handles the creation of the final <tt>Authentication</tt> object which will be returned by the provider.
     * <p>
     * The default implementation just creates a new OpenIDAuthenticationToken from the original, but with the
     * UserDetails as the principal and including the authorities loaded by the UserDetailsService.
     *
     * @param userDetails the loaded UserDetails object
     * @param auth the token passed to the authenticate method, containing
     * @return the token which will represent the authenticated user.
     */
    protected Authentication createSuccessfulAuthentication(UserDetails userDetails, JobSiteAuthenticationToken auth) {
        return new JobSiteAuthenticationToken(userDetails, authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));
    }

    /**
     * Used to load the {@code UserDetails} for the authenticated OpenID user.
     */
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = new UserDetailsByNameServiceWrapper<JobSiteAuthenticationToken>(userDetailsService);
    }

    /**
     * Used to load the {@code UserDetails} for the authenticated OpenID user.
     */
    public void setAuthenticationUserDetailsService(AuthenticationUserDetailsService<JobSiteAuthenticationToken> userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.authentication.AuthenticationProvider#supports(java.lang.Class)
     */
    public boolean supports(Class<?> authentication) {
        return JobSiteAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public void setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapper) {
        this.authoritiesMapper = authoritiesMapper;
    }
}