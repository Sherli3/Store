package store.configuration.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import store.configuration.model.Comment;
import store.configuration.model.GameObject;
import store.configuration.model.User;
import store.configuration.service.CommentService;
import store.configuration.service.GameObjectService;
import store.configuration.service.impl.UserService;

@Controller
public class ProfileController {
	@Autowired
	private UserService userService;
	@Autowired
	private GameObjectService gameObjectService;
	@Autowired
	private CommentService commentService;

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

}
