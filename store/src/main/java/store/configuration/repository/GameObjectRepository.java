package store.configuration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import store.configuration.model.GameObject;

@Repository
public interface GameObjectRepository extends JpaRepository<GameObject, Integer> {

}
