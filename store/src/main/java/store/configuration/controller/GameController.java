package store.configuration.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import store.configuration.model.Game;
import store.configuration.service.GameService;

@Controller
@RequestMapping("/games")
public class GameController {
	@Autowired
	private GameService gameService;
	
	@RequestMapping("/list")
	public String listGames(Model model) {
		List<Game> list = gameService.getGames();
		model.addAttribute("list", list);
		return "game-list";
	}



}
