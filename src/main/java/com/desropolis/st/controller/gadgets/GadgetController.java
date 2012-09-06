package com.desropolis.st.controller.gadgets;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@Controller
@RequestMapping("/v/**")
public class GadgetController {
    
	// private static Log log = LogFactory.getLog("HomeController");
	
    @RequestMapping("/openid")
    public String openid(Model model) {
    	
    	Authentication a = SecurityContextHolder.getContext().getAuthentication();
    	Object principal = a.getPrincipal();
    	Object credentials = a.getCredentials();
    	if(principal == null) {
    		if(credentials != null) {
    			// Create GUID, store token in Session under GUID
    			// create json response that tells that the user is not
    			// registered and compose the url with hd and tokenid=GUID
    		}
    	}
    	return "gadgets/view";
    }
    
    @RequestMapping(method=RequestMethod.GET)
    public String view(Model model) {
    	UserService svc = UserServiceFactory.getUserService();
    	User user = svc.getCurrentUser();
    	model.addAttribute("user", user);
    	return "gadgets/view";
    }

}
