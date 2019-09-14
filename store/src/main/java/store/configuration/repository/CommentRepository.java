package store.configuration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import store.configuration.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
