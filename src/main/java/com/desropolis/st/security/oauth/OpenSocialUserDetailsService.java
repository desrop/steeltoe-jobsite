package com.desropolis.st.security.oauth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.desropolis.st.model.admin.DomainUser;
import com.desropolis.st.model.admin.DomainUserRepository;
import com.desropolis.st.security.core.JobSiteAuthenticationToken;

public class OpenSocialUserDetailsService implements
		AuthenticationUserDetailsService<JobSiteAuthenticationToken>,
		InitializingBean {

	public DomainUserRepository repo;

	public OpenSocialUserDetailsService(DomainUserRepository repo) {
		this.repo = repo;
	}

	@Override
	public UserDetails loadUserDetails(JobSiteAuthenticationToken token)
			throws UsernameNotFoundException {

		UserDetails userDetails = null;

		String viewerId = token.getOpenSocialViewerId();
		String domainName = token.getDomainName();
		DomainUser domainUser = repo.findByOpenSocialViewerId(domainName,
				viewerId);

		if (domainUser == null)
			throw new UsernameNotFoundException(
					"Did not find DomainUser with openSocialViewerId: "
							+ viewerId);

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (String role : domainUser.getRoles())
			authorities.add(new SimpleGrantedAuthority(role));
		userDetails = new User(domainUser.getEmail(),
				domainUser.getOpenSocialViewerId(), authorities);

		return userDetails;

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (repo == null) {
			throw new IllegalArgumentException(
					"Failed to initialize OpenSocialUserDetailsService.");
		}
	}

}
