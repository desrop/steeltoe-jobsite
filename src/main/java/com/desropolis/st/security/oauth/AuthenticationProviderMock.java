package com.desropolis.st.security.oauth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.desropolis.st.model.admin.DomainUser;
import com.desropolis.st.security.core.JobSiteAuthenticationToken;

public class AuthenticationProviderMock implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		JobSiteAuthenticationToken token = (JobSiteAuthenticationToken) authentication;
		if ("SUCCEED".equals(token.getCredentials())) {

			List<String> roles = new ArrayList<String>();
			roles.add("ROLE_USER");
			DomainUser joyce = new DomainUser();
			joyce.setEmail("joyce@desropolis.com");
			joyce.setDomain("desropolis.com");
			joyce.setOpenSocialViewerId(token.getOpenSocialViewerId());
			joyce.setRoles(roles);

			UserDetails userDetails = null;

			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			for (String role : joyce.getRoles())
				authorities.add(new SimpleGrantedAuthority(role));
			userDetails = new User(joyce.getEmail(),
					joyce.getOpenSocialViewerId(), authorities);

			JobSiteAuthenticationToken result = new JobSiteAuthenticationToken(
					userDetails, userDetails.getAuthorities());
			return result;

		} else {
			throw new UsernameNotFoundException("Token said fail.");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication
				.isAssignableFrom(JobSiteAuthenticationToken.class);
	}

}
