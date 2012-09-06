package com.desropolis.st.security.oauth;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.SimpleOAuthValidator;
import net.oauth.signature.RSA_SHA1;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class JobSiteOAuthValidator implements OAuthValidator, InitializingBean {

	private static final String GOOGLE_CERT_FILENAME = "pub.1210278512.2713152949996518384.cer";
	private static String GOOGLE_CERT;

	private static final Logger logger = Logger
			.getLogger(JobSiteOAuthValidator.class.getName());

	private String certificatePath;

	public JobSiteOAuthValidator(String certificatePath) {
		this.certificatePath = certificatePath;
	}

	@Override
	public void validateOAuthSignature(HttpServletRequest request,
			String oauthConsumerKey) throws OAuthException {

		HttpServletRequestWrapper rw = new HttpServletRequestWrapper(request);
		OAuthConsumer consumer = new OAuthConsumer(null, oauthConsumerKey,
				null, null);
		consumer.setProperty(RSA_SHA1.X509_CERTIFICATE, GOOGLE_CERT);
		OAuthMessage message = new OAuthMessage(request.getMethod(), rw
				.getRequestURL().toString(), readRequestParameters(rw));
		OAuthAccessor accessor = new OAuthAccessor(consumer);
		SimpleOAuthValidator validator = new SimpleOAuthValidator();
		try {
			validator.validateMessage(message, accessor);
		} catch (IOException e) {
			logger.warning(e.toString());
		} catch (URISyntaxException e) {
			logger.warning(e.toString());
		}

	}

	@SuppressWarnings("unchecked")
	protected List<OAuth.Parameter> readRequestParameters(
			HttpServletRequest request) {

		List<OAuth.Parameter> parameters = new ArrayList<OAuth.Parameter>();

		for (Object e : request.getParameterMap().entrySet()) {
			Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) e;

			for (String value : entry.getValue()) {
				parameters.add(new OAuth.Parameter(entry.getKey(), value));
			}
		}

		return parameters;

	}

	private String fetchCertificate() {

		String cert = "";

		try {
			File file = new File(certificatePath + "/" + GOOGLE_CERT_FILENAME);
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			byte[] bytes = new byte[(int) file.length()];
			bis.read(bytes);
			bis.close();
			cert = new String(bytes);
		} catch (FileNotFoundException e) {
			logger.warning("missing Google certificate file");
		} catch (IOException e) {
			logger.warning(e.toString());
		}

		return cert;

	}

	@Override
	public void afterPropertiesSet() {
		Assert.hasLength(this.certificatePath,
				"The certificatePath must be set");
		if (GOOGLE_CERT == null) {
			GOOGLE_CERT = fetchCertificate();
		}
	}

	public void setCertificatePath(String certificatePath) {
		this.certificatePath = certificatePath;
	}

}
