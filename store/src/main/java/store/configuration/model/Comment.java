package store.configuration.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = { "message", "createdAt", "approved" })
@Entity
@Table(name = "comment")
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "comment_id")
	private int id;
	@Column(name = "message", columnDefinition = "TEXT")
	@NotEmpty(message = "*Please write something")
	@Length(min = 4, message = "*Your comment must have at least 4 characters")
	private String message;
	@Column(name = "created_at")
	private Timestamp createdAt;
	@Column(name = "approved")
	private Boolean approved;
	@ManyToOne
	@JoinColumn(name = "object_id")
	private GameObject gameObject;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User author;

}
