package store.configuration.controller;

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
import store.configuration.service.GameObjectService;
import store.configuration.service.GameService;

@Controller
@RequestMapping("/object")
public class GameObjectController {

	@Autowired
	private GameObjectService gameObjectService;
	@Autowired
	private GameService gameService;

	@RequestMapping(value = "/add/{id}", method = RequestMethod.GET)
	public ModelAndView registrationGame(@PathVariable("id") Integer id) {
		ModelAndView modelAndView = new ModelAndView("");
		Optional<Game> findGame = gameService.getGame(id);
		if (findGame.isPresent()) {
			GameObject game = new GameObject();
			modelAndView.addObject("gameObject", game);
			modelAndView.addObject("gameId", id);
			modelAndView.setViewName("add-object");
			return modelAndView;
		}

		return modelAndView;
	}

	@RequestMapping(path = "/add/{id}", method = RequestMethod.POST)
	public String createNewGame(@Valid @ModelAttribute("gameObject") GameObject gameObject,
			@ModelAttribute("gameId") Game gameId, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "error";
		}
		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		gameObject.setStatus(Status.PENDING);
		gameObject.setCreatdAt(ts);
		gameObject.setGame(gameId);
		gameObjectService.saveGameObject(gameObject);
		return "redirect:/games/list/";
	}

	

}
