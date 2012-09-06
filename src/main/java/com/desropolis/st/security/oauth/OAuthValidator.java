package com.desropolis.st.security.oauth;

import javax.servlet.http.HttpServletRequest;

import net.oauth.OAuthException;

public interface OAuthValidator {
	void validateOAuthSignature(HttpServletRequest request,
			String oauthConsumerKey) throws OAuthException;
}
