package com.desropolis.st.security.oauth;

import javax.servlet.http.HttpServletRequest;

import net.oauth.OAuthException;

public class OAuthValidatorMock implements OAuthValidator {

	HttpServletRequest request;
	String oauthConsumerKey;
	Boolean fail = false;
	
	@Override
	public void validateOAuthSignature(HttpServletRequest request,
			String oauthConsumerKey) throws OAuthException {

		this.request = request;
		this.oauthConsumerKey = oauthConsumerKey;
		
		if (fail) {
			throw new OAuthException("Fail was set.");
		}
		
		
	}

}
