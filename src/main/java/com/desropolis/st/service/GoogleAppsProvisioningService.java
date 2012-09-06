package com.desropolis.st.service;

import java.io.IOException;
import java.net.URL;

import com.desropolis.st.core.JobSiteException;
import com.google.gdata.client.appsforyourdomain.UserService;
import com.google.gdata.client.authn.oauth.GoogleOAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer;
import com.google.gdata.client.authn.oauth.OAuthParameters.OAuthType;
import com.google.gdata.client.authn.oauth.OAuthSigner;
import com.google.gdata.data.Link;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.provisioning.UserFeed;
import com.google.gdata.util.ServiceException;

public class GoogleAppsProvisioningService {

	private static final String APPS_FEEDS_URL_BASE = "https://apps-apis.google.com/a/feeds/";
	private static final String APPLICATION_NAME = "Steeltoe-JobSite-1.0-beta";
	private static final String USER_SCOPE = "https://apps-apis.google.com/a/feeds/user/#readonly";
	private static final String CONSUMER_KEY = "770689874455.apps.googleusercontent.com";
	private static final String CONSUMER_SECRET = "e3kFW49Cr4WI5qQkCK9OJHu_";
	protected static final String SERVICE_VERSION = "2.0";

	protected UserService userService;
	protected String domainUrlBase;

	public GoogleAppsProvisioningService() {

		GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
		oauthParameters.setOAuthConsumerKey(CONSUMER_KEY);
		oauthParameters.setOAuthConsumerSecret(CONSUMER_SECRET);
		oauthParameters.setScope(USER_SCOPE);
		oauthParameters.setOAuthType(OAuthType.TWO_LEGGED_OAUTH);
		OAuthSigner signer = new OAuthHmacSha1Signer();
		userService = new UserService(APPLICATION_NAME);
		try {
			userService.setOAuthCredentials(oauthParameters, signer);
		} catch (OAuthException e) {
			throw new JobSiteException("Unable to set up service: "
					+ e.getMessage(), e);
		}

	}

	public UserFeed retrieveAllUsers(String domain)
			throws AppsForYourDomainException, ServiceException, IOException,
			OAuthException {

		String urlBase = APPS_FEEDS_URL_BASE + domain + "/";
		URL retrieveUrl = new URL(urlBase + "user/" + SERVICE_VERSION + "/");
		UserFeed allUsers = new UserFeed();
		UserFeed currentPage;
		Link nextLink;

		do {
			currentPage = userService.getFeed(retrieveUrl, UserFeed.class);
			allUsers.getEntries().addAll(currentPage.getEntries());
			nextLink = currentPage.getLink(Link.Rel.NEXT, Link.Type.ATOM);
			if (nextLink != null) {
				retrieveUrl = new URL(nextLink.getHref());
			}
		} while (nextLink != null);

		return allUsers;

	}

}
