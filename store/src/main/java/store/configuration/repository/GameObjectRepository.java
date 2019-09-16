package store.configuration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import store.configuration.model.GameObject;

@Repository
public interface GameObjectRepository extends JpaRepository<GameObject, Integer> {

	@Query("SELECT o FROM GameObject o WHERE o.status = 'VERIFIED'")
	List<GameObject> findAllVerifiedObjects();
	
	@Query("SELECT o FROM GameObject o WHERE o.status = 'PENDING'")
	List<GameObject> findAllUnverifiedObjects();

}
