package store.configuration.controller;

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
import store.configuration.service.GameService;

@Controller
@RequestMapping("/admin/games")
public class GameAdminController {
	@Autowired
	private GameService gameService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView registrationGame() {
		ModelAndView modelAndView = new ModelAndView("");
		Game game = new Game();
		modelAndView.addObject("game", game);
		modelAndView.setViewName("add-game");
		return modelAndView;
	}

	@RequestMapping(path = "/add", method = RequestMethod.POST)
	public String createNewGame(@Valid @ModelAttribute("game") Game game, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "error";
		}
		gameService.saveGame(game);
		return "redirect:/games/list/";
	}

	@RequestMapping("/list")
	public String listGames(Model model) {
		List<Game> list = gameService.getGames();
		model.addAttribute("list", list);
		return "game-list";
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editGameGet(@PathVariable("id") Integer id, Model model) {
		gameService.getGame(id).ifPresent(idObj -> model.addAttribute("game", idObj));
		return "game-edit";
	}

	@RequestMapping(path = "/edit/{id}", method = RequestMethod.POST)
	public String editGame(@Valid @ModelAttribute("game") Game game, BindingResult result) {
		if (result.hasErrors()) {
			return "error";
		}
		gameService.saveGame(game);
		return "redirect:/games/list/";
	}
	
	@RequestMapping(path = "/delete/{id}", method = RequestMethod.GET)
	public String deleteGame(@PathVariable("id") Integer id) {
		Optional<Game> game = gameService.getGame(id);
		game.ifPresent(o -> gameService.deleteGame(o.getId()));
		return "redirect:/games/list/";
	}

}
