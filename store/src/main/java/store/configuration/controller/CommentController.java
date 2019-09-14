package store.configuration.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import store.configuration.model.Comment;
import store.configuration.model.GameObject;
import store.configuration.service.CommentService;
import store.configuration.service.GameObjectService;

@Controller
@RequestMapping("/comment")
public class CommentController {
	@Autowired
	private CommentService commentService;
	@Autowired
	private GameObjectService gameObjectService;
	private Date date = new Date();

	@RequestMapping(value = "/add/{id}", method = RequestMethod.GET)
	public ModelAndView createCommentToGameObject(@PathVariable("id") Integer id) {
		ModelAndView modelAndView = new ModelAndView("");
		Optional<GameObject> findGameObject = gameObjectService.getGameObject(id);
		if (findGameObject.isPresent()) {
			Comment newComment = new Comment();
			modelAndView.addObject("newComment", newComment);
			modelAndView.addObject("objectId", id);
			modelAndView.setViewName("add-comment");
			return modelAndView;

		}

		modelAndView.setViewName("error");
		return modelAndView;
	}

	@RequestMapping(path = "/add/{id}", method = RequestMethod.POST)
	public String saveNewComment(@Valid @ModelAttribute("newComment") Comment comment,
			@ModelAttribute("objectId") GameObject objectId, BindingResult result) {
		if (result.hasErrors()) {
			return "error";
		}
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		comment.setApproved(false);
		comment.setCreatedAt(ts);
		comment.setGameObject(objectId);
		commentService.saveComment(comment);

		return "redirect:/games/list/";
	}

}
