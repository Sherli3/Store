package store.configuration.controller;

import java.sql.Timestamp;
import java.util.Date;
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
			newComment.setGameObject(findGameObject.get());
			modelAndView.addObject("newComment", newComment);
			modelAndView.setViewName("add-comment");
			return modelAndView;

		}

		modelAndView.setViewName("error");
		return modelAndView;
	}

	@RequestMapping(path = "/addComment", method = RequestMethod.POST)
	public String saveNewComment(@Valid Comment newComment, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "error";
		}
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		newComment.setCreatedAt(ts);
		newComment.setApproved(false);
		commentService.saveComment(newComment);

		return "redirect:/games/list/";
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editGameGet(@PathVariable("id") Integer id, Model model) {
		commentService.getCommentById(id).ifPresent(comment -> model.addAttribute("comment", comment));
		return "comment-edit";
	}

	@RequestMapping(path = "/edit/{id}", method = RequestMethod.POST)
	public String editComment(@Valid @ModelAttribute("comment") Comment comment, BindingResult result) {
		if (result.hasErrors()) {
			return "error";
		}
		commentService.saveComment(comment);
		return "redirect:/object/list/";
	}

	@RequestMapping(path = "/delete/{id}", method = RequestMethod.GET)
	public String deleteCommen(@PathVariable("id") Integer id) {
		Optional<Comment> comment = commentService.getCommentById(id);
		comment.ifPresent(c -> commentService.deleteComment(c.getId()));
		return "redirect:/object/list/";
	}
	@RequestMapping("/list")
	public String listGames(Model model) {
		List<Comment> listComment = commentService.findAllApprovedComments();
		model.addAttribute("listComment", listComment);
		return "comment-list";
	}

}
