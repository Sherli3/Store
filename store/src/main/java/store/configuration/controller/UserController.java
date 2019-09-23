package store.configuration.controller;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
import store.configuration.service.PasswordResetTokenService;
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
	@Autowired
	private MessageSource messages;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private Environment env;
	@Autowired
	private PasswordResetTokenService passwordResetToken;
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
	public String editComment(@Valid @ModelAttribute("comment") Comment comment, BindingResult result, Principal principal) {
		if (result.hasErrors()) {
			return "error";
		}
		User user = userService.findByEmail(principal.getName());
		comment.setAuthor(user);
		commentService.saveComment(comment);
		return "redirect:/object/list/";
	}

	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
	public ModelAndView displayResetPassword(ModelAndView modelAndView, String email) {
		modelAndView.addObject("email", email);
		modelAndView.setViewName("forgotPassword");
		return modelAndView;
	}

	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public String resetPassword(final HttpServletRequest request, final Model model,
			@RequestParam("email") String userEmail) {
		final User userFind = userService.findByEmail(userEmail);
		if (userFind == null) {
			model.addAttribute("message", "This email does not exist!");
			return "error";
		}
		final String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(userFind, token);
		final SimpleMailMessage email = constructResetTokenEmail(getAppUrl(request), request.getLocale(), token,
				userFind);
		mailSender.send(email);

		model.addAttribute("message", "Request to reset password received. Check your inbox for the reset link.");
		return "successForgotPassword";
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	public String changePassword(final HttpServletRequest request, final Model model, @RequestParam("id") final long id,
			@RequestParam("token") final String token) {
		final String result = passwordResetToken.validatePasswordResetToken(id, token);
		if (result != null) {
			model.addAttribute("message", "result null");
			return "redirect:/user/login";
		}
		return "redirect:/:/user/resetPassword";

	}

	@RequestMapping(value = "/savePassword", method = RequestMethod.POST)
	public String savePassword(final HttpServletRequest request, final Model model,
			@RequestParam("password") final String password) {
		final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		userService.changeUserPassword(user, password);
		model.addAttribute("message", "Passwrod save");
		return "redirect:/user/login";
	}

	private SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, User user) {
		String url = contextPath + "/user/changePassword?token=" + token;
		String message = messages.getMessage("message.resetPassword", null, locale);
		return constructEmail("Reset Password", message + " \r\n" + url, user);
	}

	private SimpleMailMessage constructEmail(String subject, String body, User user) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject(subject);
		email.setText(body);
		email.setTo(user.getEmail());
		email.setFrom(env.getProperty("support.email"));
		return email;
	}

}
