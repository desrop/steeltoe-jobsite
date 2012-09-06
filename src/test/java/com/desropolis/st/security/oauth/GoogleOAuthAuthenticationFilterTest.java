package com.desropolis.st.security.oauth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.AuthenticationException;

public class GoogleOAuthAuthenticationFilterTest {

	private GoogleOAuthAuthenticationFilter filter;
	private OAuthValidatorMock mockValidator;
	private AuthenticationProviderMock mockProvider;
	
	@Before
	public void setUp() {

		filter = new GoogleOAuthAuthenticationFilter();
		mockValidator = new OAuthValidatorMock();
		filter.setOAuthValidator(mockValidator);
		mockProvider = new AuthenticationProviderMock();
		List<AuthenticationProvider> aps = new ArrayList<AuthenticationProvider>();
		aps.add(mockProvider);
		AuthenticationManager am = new ProviderManager(aps);
		filter.setAuthenticationManager(am);
		// "WEB-INF/certificates/pub.1210278512.2713152949996518384.cer";
	}
	
	@Test
	public void testSuccess() throws AuthenticationException, IOException, ServletException {
		
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.setParameter("opensocial_viewer_id", "SUCCEED");
		req.setParameter("oauth_consumer_key", "?");
		req.setParameter("hd", "desropolis.com");
		
		MockHttpServletResponse res = new MockHttpServletResponse();
		MockFilterChain chain = new MockFilterChain();
		
		filter.doFilter(req, res, chain);
		// assertNotNull(a);
		
	}
	
}
