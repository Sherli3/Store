package store.configuration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import store.configuration.model.Game;
@Repository
public interface GameRepository extends JpaRepository<Game, Integer>{

}
