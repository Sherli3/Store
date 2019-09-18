package store.configuration.service;

import java.util.List;
import java.util.Optional;

import store.configuration.model.GameObject;

public interface GameObjectService {
	public List<GameObject> getGamesObject();

	public void saveGameObject(GameObject gameObject);

	public Optional<GameObject> getGameObject(int id);

	public void deleteGameObject(int id);

	public List<GameObject> findAllVerifiedObjects();

	public List<GameObject> findAllUnverifiedObjects();

}
