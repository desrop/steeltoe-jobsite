package com.desropolis.st.controller.appadmin;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.desropolis.st.model.admin.Domain;
import com.desropolis.st.model.admin.DomainRepository;
import com.google.gdata.client.appsforyourdomain.UserService;
import com.google.gdata.client.authn.oauth.GoogleOAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer;
import com.google.gdata.client.authn.oauth.OAuthParameters.OAuthType;
import com.google.gdata.client.authn.oauth.OAuthSigner;
import com.google.gdata.data.Link;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.provisioning.UserFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

@Controller
@RequestMapping("/appadmin/domains")
public class DomainController {
    
	private static Log log = LogFactory.getLog("DomainController");
	
	@Autowired
	DomainRepository repo;
	
//    @ModelAttribute @PathVariable 
    public Domain getDomain(Long id){
    	if (id > 0) {
        	return repo.find(id);
    	}
    	return new Domain();
    }
	
    @RequestMapping(method=RequestMethod.GET)
    public void list(Model model) {
    	model.addAttribute("domains", repo.listAll());
    }
    
    @RequestMapping("/add")
    public ModelAndView getForm() {
    	Map<String, Object> model = new HashMap<String, Object>();
    	model.put("domain", new Domain());
    	return new ModelAndView("appadmin/domain-form", model);
    }

    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String save(@ModelAttribute Domain domain, BindingResult result) {
        if (result.hasErrors()) {
        	log.error(result);
            return "appadmin/domain-form";
        }
        repo.save(domain);
        return "redirect:/appadmin/domains";
    }

    @RequestMapping(value="/{domainId}", method=RequestMethod.GET)
    public ModelAndView domain(@PathVariable Long domainId) {
    	Map<String, Object> model = new HashMap<String, Object>();
    	Domain domain = repo.find(domainId);
    	model.put("domain", domain);
    	UserFeed uf = null;
		try {
			uf = retrieveAllUsers();
		} catch (AppsForYourDomainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	model.put("userFeed", uf.getEntries());
    	return new ModelAndView("appadmin/domain-view", model);
    }
    
    
	private static final String APPS_FEEDS_URL_BASE = "https://apps-apis.google.com/a/feeds/";

	protected static final String SERVICE_VERSION = "2.0";

	protected UserService userService;
	protected String domainUrlBase;
	
	private void initUserService(String domain, String adminEmail, String adminPassword) throws AuthenticationException, OAuthException {

		GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
	    oauthParameters.setOAuthConsumerKey("770689874455.apps.googleusercontent.com"); //336188015936.apps.googleusercontent.com
	    oauthParameters.setOAuthConsumerSecret("e3kFW49Cr4WI5qQkCK9OJHu_"); //EuItZUAklboKAG0clgfWvDFE
	    oauthParameters.setScope("https://apps-apis.google.com/a/feeds/user/#readonly");
	    oauthParameters.setOAuthType(OAuthType.TWO_LEGGED_OAUTH);
	    log.info("New code.");
	    OAuthSigner signer = new OAuthHmacSha1Signer();
		this.domainUrlBase = APPS_FEEDS_URL_BASE + domain + "/";
//		if (userService == null) {
			userService = new UserService(
					"gdata-sample-AppsForYourDomain-UserService");
			userService.setOAuthCredentials(oauthParameters, signer); //  UserCredentials(adminEmail, adminPassword)
//		}
	}

	public UserFeed retrieveAllUsers() throws AppsForYourDomainException,
			ServiceException, IOException, OAuthException {

		log.info("Retrieving all users.");

		initUserService("desropolis.com", "dean@desropolis.com",
				"Noxon810");

		URL retrieveUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/");
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
