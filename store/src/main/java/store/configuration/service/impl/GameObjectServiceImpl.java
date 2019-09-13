package store.configuration.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import store.configuration.model.GameObject;
import store.configuration.repository.GameObjectRepository;
import store.configuration.service.GameObjectService;

@Service
@Transactional
public class GameObjectServiceImpl implements GameObjectService {
	@Autowired
	GameObjectRepository gameObjectRepository;

	@Override
	public List<GameObject> getGamesObject() {
		return gameObjectRepository.findAll();
	}

	@Override
	public void saveGameObject(GameObject gameObject) {
		gameObjectRepository.save(gameObject);

	}

	@Override
	public Optional<GameObject> getGameObject(int id) {
		return gameObjectRepository.findById(id);
	}

	@Override
	public void deleteGameObject(int id) {
		gameObjectRepository.deleteById(id);

	}

}
