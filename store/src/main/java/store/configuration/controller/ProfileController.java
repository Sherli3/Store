package store.configuration.controller;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import store.configuration.model.Comment;
import store.configuration.model.GameObject;
import store.configuration.model.User;
import store.configuration.service.CommentService;
import store.configuration.service.GameObjectService;
import store.configuration.service.PasswordResetTokenService;
import store.configuration.service.impl.UserService;

@Controller
public class ProfileController {
	@Autowired
	private UserService userService;
	@Autowired
	private GameObjectService gameObjectService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private MessageSource messages;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private Environment env;
	@Autowired
	private PasswordResetTokenService passwordResetToken;

	@Secured({ "ADMIN", "USER", "TRADER" })
	@RequestMapping(value = "/cabinet", method = RequestMethod.GET)
	public String cabinetList(Model model) {
		List<GameObject> listObject = gameObjectService.findAllVerifiedObjects();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByEmail(auth.getName());
		List<Comment> listComment = commentService.findAllCommentFormUser(user);
		model.addAttribute("listObject", listObject);

		model.addAttribute("userName",
				"Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
		model.addAttribute("listComment", listComment);

		return "cabinet";
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

	private String getAppUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	public String changePassword(final HttpServletRequest request, final Model model, @RequestParam("id") long id,
			@RequestParam("token") final String token) {
		final String result = passwordResetToken.validatePasswordResetToken(id, token);
		if (result != null) {
			model.addAttribute("message", "result null");
			return "redirect:/user/login";
		}
		return "redirect:/savePassword";

	}

	@RequestMapping(value = "/savePassword", method = RequestMethod.POST)
	public String savePassword(final HttpServletRequest request, final Model model,
			@RequestParam("password") final String password) {
		final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		userService.changeUserPassword(user, password);
		model.addAttribute("message", "Passwrod save");
		return "redirect:/user/login";
	}

	@RequestMapping(value = "/savePassword", method = RequestMethod.GET)
	public ModelAndView getPassword(ModelAndView modelAndView, String password) {
		modelAndView.addObject("password", password);
		modelAndView.setViewName("resetPassword");
		return modelAndView;
	}

	private SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, User user) {
		String url = contextPath + "/changePassword?id=" + user.getId() + "&token=" + token;
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
