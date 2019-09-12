package store.configuration.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import store.configuration.model.Game;
import store.configuration.repository.GameRepository;
import store.configuration.service.GameService;

@Service
@Transactional
public class GameServiceImpl implements GameService {

	@Autowired
	GameRepository gameRepository;

	@Override
	public List<Game> getGames() {
		return gameRepository.findAll();
	}

	@Override
	public void saveGame(Game game) {
		gameRepository.save(game);

	}

	@Override
	public  Optional<Game> getGame(int id) {
		return gameRepository.findById(id);
	}

	@Override
	public void deleteGame(int id) {
		gameRepository.deleteById(id);
		
	}

}
