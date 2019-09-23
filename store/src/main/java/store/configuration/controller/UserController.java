package store.configuration.controller;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import store.configuration.model.Comment;
import store.configuration.model.User;
import store.configuration.model.UserVerificationToken;
import store.configuration.registration.OnRegistrationCompleteEvent;
import store.configuration.service.CommentService;
import store.configuration.service.UserVerificationTokenService;
import store.configuration.service.impl.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	@Autowired
	private UserVerificationTokenService tokenService;
	@Autowired
	private UserDetailsService manager;
	private Date date = new Date();

	@RequestMapping(value = "/logout", method = { RequestMethod.GET, RequestMethod.POST })
	public String logout(ModelMap model) {
		return "login";
	}

	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public ModelAndView registration() {
		ModelAndView modelAndView = new ModelAndView("");
		modelAndView.addObject("user", new User());
		modelAndView.setViewName("registration");
		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public String registerUserAccount(@Valid User user, HttpServletRequest request, BindingResult result) {
		if (result.hasErrors()) {
			return "error";
		}
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		user.setCreatedAt(ts);
		userService.saveUser(user);
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), getAppUrl(request)));
		return "redirect:/user/login";
	}

	@RequestMapping(value = "/registration/confirmation", method = RequestMethod.GET)
	public String confirmRegistration(HttpServletRequest request, @RequestParam("token") String token) {
		Optional<UserVerificationToken> tokenItem = ((UserVerificationTokenService) tokenService).findByToken(token);
		switch (((UserVerificationTokenService) tokenService).validateVerificationToken(token)) {
		case EXPIRED:
			return "redirect:/user/login";
		case INVALID:
			return "redirect:/user/login";
		case VERIFIED:
			UserDetails userDetails = manager.loadUserByUsername(tokenItem.get().getUser().getEmail());
			Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
					userDetails.getPassword(), userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);
			return "redirect:/cabinet";
		default:
			return "redirect:/user/login";
		}
	}

	private String getAppUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

	@RequestMapping(value = "/comment/edit/{id}", method = RequestMethod.GET)
	public String editGameGet(@PathVariable("id") Integer id, Model model) {
		commentService.getCommentById(id).ifPresent(comment -> model.addAttribute("comment", comment));
		return "comment-edit";
	}

	@RequestMapping(path = "/comment/edit/{id}", method = RequestMethod.POST)
	public String editComment(@Valid @ModelAttribute("comment") Comment comment, BindingResult result,
			Principal principal) {
		if (result.hasErrors()) {
			return "error";
		}
		User user = userService.findByEmail(principal.getName());
		comment.setAuthor(user);
		commentService.saveComment(comment);
		return "redirect:/object/list/";
	}

}
