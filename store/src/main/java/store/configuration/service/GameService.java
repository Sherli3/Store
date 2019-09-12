package store.configuration.service;

import java.util.List;
import java.util.Optional;

import store.configuration.model.Game;

public interface GameService {

	public List<Game> getGames();

	public void saveGame(Game game);

	public Optional<Game> getGame(int id);

	public void deleteGame(int id);

}
