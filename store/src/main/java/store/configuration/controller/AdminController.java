package store.configuration.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import store.configuration.model.Comment;
import store.configuration.model.GameObject;
import store.configuration.model.Status;
import store.configuration.service.CommentService;
import store.configuration.service.GameObjectService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private GameObjectService gameObjectService;
	@Autowired
	private CommentService commentService;

	@RequestMapping(value = "/edit/object/{id}", method = RequestMethod.GET)
	public String editGameObjectGet(@PathVariable("id") Integer id, Model model) {
		gameObjectService.getGameObject(id).ifPresent(idObj -> model.addAttribute("gameObject", idObj));
		model.addAttribute("statusType", Status.values());
		return "object-edit-admin";
	}

	@RequestMapping(path = "/edit/object/{id}", method = RequestMethod.POST)
	public String editGameObject(@Valid @ModelAttribute("gameObject") GameObject gameObj, BindingResult result) {
		if (result.hasErrors()) {
			return "error";
		}
		gameObjectService.saveGameObject(gameObj);

		return "redirect:/object/list/";
	}

	@RequestMapping(value = "/edit/comment/{id}", method = RequestMethod.GET)
	public String editGameGet(@PathVariable("id") Integer id, Model model) {
		commentService.getCommentById(id).ifPresent(comment -> model.addAttribute("comment", comment));
		return "comment-edit-admin";
	}

	@RequestMapping(path = "/edit/comment/{id}", method = RequestMethod.POST)
	public String editComment(@Valid @ModelAttribute("comment") Comment comment,
			@RequestParam(defaultValue = "false") Boolean approved, BindingResult result) {
		if (result.hasErrors()) {
			return "error";
		}
		comment.setApproved(approved);
		commentService.saveComment(comment);
		return "redirect:/object/list/";
	}

	@RequestMapping(path = "/comment/delete/{id}", method = RequestMethod.GET)
	public String deleteCommen(@PathVariable("id") Integer id) {
		Optional<Comment> comment = commentService.getCommentById(id);
		comment.ifPresent(c -> commentService.deleteComment(c.getId()));
		return "redirect:/object/list/";
	}
	
	@RequestMapping("/list/object/unverified")
	public String listGames(Model model) {
		List<GameObject> listObject = gameObjectService.findAllUnverifiedObjects();
		model.addAttribute("listObject", listObject);
		return "object-list";
	}

}
