package com.vulnerablecode.JavaTI;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class URLRedirection {

	
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLogin(
			@RequestParam(value = "target", required = false) String target,
			@RequestParam(value = "username", required = false) String username,
			Model model,
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		// Check if user is already logged in
		if (httpRequest.getSession().getAttribute("username") != null) {
			logger.info("User is already logged in - redirecting...");
			if (target != null && !target.isEmpty() && !target.equals("null")) {
				return "redirect:" + target;
			} else {
				// default to user's feed
				return Utils.redirect("feed");
			}
		}
		User user = UserFactory.createFromRequest(httpRequest);
		if (user != null) {
			Utils.setSessionUserName(httpRequest, httpResponse, user.getUserName());
			logger.info("User is remembered - redirecting...");
			if (target != null && !target.isEmpty() && !target.equals("null")) {
				return "redirect:" + target;
			} else {
				// default to user's feed
				return Utils.redirect("feed");
			}
		} else {
			logger.info("User is not remembered");
		}
		if (username == null) {
			username = "";
		}
		if (target == null) {
			target = "";
		}
		logger.info("Entering showLogin with username " + username + " and target " + target);
		model.addAttribute("username", username);
		model.addAttribute("target", target);
		return "login";
	}
}
