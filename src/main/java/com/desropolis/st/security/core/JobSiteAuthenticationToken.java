package com.desropolis.st.security.core;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JobSiteAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 1276740574732453484L;

	private final String email;
	private final String openSocialViewerId;
	private final String domainName;
	private final Object principal;

	public JobSiteAuthenticationToken(String domainName, String email,
			String openSocialViewerId) {
		super(new ArrayList<GrantedAuthority>(0));
		this.domainName = domainName;
		this.email = email;
		this.openSocialViewerId = openSocialViewerId;
		this.principal = email;
		setAuthenticated(false);
	}

	public JobSiteAuthenticationToken(UserDetails principal,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.domainName = null;
		this.email = null;
		this.openSocialViewerId = null;
		this.principal = principal;
		setAuthenticated(true);
	}

	public Object getCredentials() {
		return getOpenSocialViewerId();
	}

	public Object getPrincipal() {
		return principal;
	}

	public String getEmail() {
		return email;
	}

	public String getOpenSocialViewerId() {
		return openSocialViewerId;
	}

	public String getDomainName() {
		return domainName;
	}

	@Override
	public String toString() {
		return "[" + super.toString() + "]";
	}

}
