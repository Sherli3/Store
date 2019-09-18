package store.configuration.controller;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import store.configuration.model.Game;
import store.configuration.model.GameObject;
import store.configuration.model.Status;
import store.configuration.model.User;
import store.configuration.service.GameObjectService;
import store.configuration.service.GameService;
import store.configuration.service.impl.UserService;

@Controller
@RequestMapping("/trader")
public class GameObjectController {

	@Autowired
	private GameObjectService gameObjectService;
	@Autowired
	private GameService gameService;
	@Autowired
	private UserService userService;
	private Date date = new Date();

	@RequestMapping(value = "/add/{id}", method = RequestMethod.GET)
	public ModelAndView registrationGame(@PathVariable("id") Integer id) {
		ModelAndView modelAndView = new ModelAndView("");
		Optional<Game> findGame = gameService.getGame(id);
		if (findGame.isPresent()) {
			GameObject gameObject = new GameObject();
			gameObject.setGame(findGame.get());
			modelAndView.addObject("gameObject", gameObject);
			modelAndView.setViewName("add-object");
			return modelAndView;
		}

		modelAndView.setViewName("error");
		return modelAndView;
	}

	@RequestMapping(path = "/addObject", method = RequestMethod.POST)
	public String createNewGame(@Valid GameObject gameObject, BindingResult result, ModelMap model,
			Principal principal) {
		if (result.hasErrors()) {
			return "error";
		}
		User user = userService.findByEmail(principal.getName());
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		gameObject.setStatus(Status.PENDING);
		gameObject.setCreatdAt(ts);
		gameObject.setUser(user);
		gameObjectService.saveGameObject(gameObject);
		return "redirect:/games/list/";
	}

	@RequestMapping("/list")
	public String listGames(Model model) {
		List<GameObject> listObject = gameObjectService.getGamesObject();
		model.addAttribute("listObject", listObject);
		return "object-list";
	}

	@RequestMapping("/list/all")
	public String listGamesVer(Model model) {
		List<GameObject> listObject = gameObjectService.findAllVerifiedObjects();
		model.addAttribute("listObject", listObject);
		return "object-list";
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editGameGet(@PathVariable("id") Integer id, Model model) {
		gameObjectService.getGameObject(id).ifPresent(idObj -> model.addAttribute("gameObject", idObj));
		return "object-edit";
	}

	@RequestMapping(path = "/edit/{id}", method = RequestMethod.POST)
	public String editGame(@Valid @ModelAttribute("gameObject") GameObject gameObj, BindingResult result,
			Principal principal) {
		if (result.hasErrors()) {
			return "error";
		}
		User user = userService.findByEmail(principal.getName());
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		gameObj.setUpdateAt(ts);
		gameObj.setUser(user);
		gameObjectService.saveGameObject(gameObj);

		return "redirect:/trader/list/";
	}

	@RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
	public String deleteGame(@PathVariable("id") Integer id) {
		Optional<GameObject> gameObj = gameObjectService.getGameObject(id);
		gameObj.ifPresent(o -> gameObjectService.deleteGameObject(o.getId()));
		return "redirect:/trader/list/";
	}

}
