package store.configuration.service;

import java.util.List;
import java.util.Optional;

import store.configuration.model.Comment;
import store.configuration.model.GameObject;

public interface CommentService {

	public void saveComment(Comment comment);

	public Optional<Comment> getCommentById(int id);

	public void deleteComment(int id);

	public List<Comment> findAllApprovedComments();

	List<Comment> findAllCommentsByObjectId(GameObject objectId);

}
