package com.desropolis.st.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@Controller
@RequestMapping("/jobsite/home")
public class HomeController {
    
	// private static Log log = LogFactory.getLog("HomeController");
	
    @RequestMapping(method=RequestMethod.GET)
    public String view(Model model) {
    	UserService svc = UserServiceFactory.getUserService();
    	User user = svc.getCurrentUser();
    	model.addAttribute("user", user);
    	return "home/view";
    }

}
