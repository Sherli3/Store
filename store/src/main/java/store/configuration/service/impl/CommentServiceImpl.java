package store.configuration.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import store.configuration.model.Comment;
import store.configuration.service.CommentService;
import store.configuration.repository.CommentRepository;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
	@Autowired
	CommentRepository commentRepository;

	@Override
	public void saveComment(Comment comment) {
		commentRepository.saveAndFlush(comment);
	}

	@Override
	public Optional<Comment> getCommentById(int id) {
		return commentRepository.findById(id);
	}

	@Override
	public void deleteComment(int id) {
		commentRepository.deleteById(id);

	}

	@Override
	public List<Comment> findAllApprovedComments() {
		return commentRepository.findAllApprovedComments();
	}

}
