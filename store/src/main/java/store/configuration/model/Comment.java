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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
	private String message;
	@Column(name = "created_at")
	private Timestamp createdAt;
	@Column(name = "approved")
	private Boolean approved;
	@ManyToOne
	@JoinColumn(name = "object_id")
	private GameObject gameObject;

}
