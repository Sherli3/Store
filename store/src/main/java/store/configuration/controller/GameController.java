package store.configuration.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import store.configuration.model.Comment;
import store.configuration.model.Game;
import store.configuration.model.GameObject;
import store.configuration.service.CommentService;
import store.configuration.service.GameObjectService;
import store.configuration.service.GameService;

@Controller
@RequestMapping("/games")
public class GameController {
	@Autowired
	private GameService gameService;
	@Autowired
	private GameObjectService gameObjectService;
	@Autowired
	private CommentService commentService;

	@RequestMapping("/list")
	public String listGames(Model model) {
		List<Game> list = gameService.getGames();
		model.addAttribute("list", list);
		return "game-list-user";
	}

	@RequestMapping("/list/all")
	public String listGamesVer(Model model) {
		List<GameObject> listObject = gameObjectService.findAllVerifiedObjects();
		model.addAttribute("listObject", listObject);
		return "object-list";
	}

	@RequestMapping(value = "/object/more/comment/{id}", method = RequestMethod.GET)
	public String getPostWithId(@PathVariable int id, Model model) {
		Optional<GameObject> gameObject = gameObjectService.getGameObject(id);
		List<Comment> list = commentService.findAllCommentsByObjectId(gameObject.get());
		model.addAttribute("gameObject", gameObject);
		model.addAttribute("listComment", list);
		return "object-comment";
	}
	/*
	 * @RequestMapping(path="/list", method=RequestMethod.GET) public List<Game>
	 * getAllGame(){ return gameService.getGames(); }
	 */

}
