package store.configuration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import store.configuration.model.Comment;
import store.configuration.model.GameObject;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

	@Query("SELECT c FROM Comment c WHERE c.approved = 1")
	List<Comment> findAllApprovedComments();

	@Query("SELECT c FROM Comment c WHERE c.approved = 0")
	List<Comment> findAllUnapprovedComments();

	@Query("SELECT c FROM Comment c WHERE c.gameObject = ?1 and c.approved = 1")
	List<Comment> findAllCommentsByObjectId(GameObject objectId);

}
