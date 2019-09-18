package store.configuration.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = { "title", "text", "status", "creatdAt", "updateAt" })
@Entity
@Table(name = "game_object")
public class GameObject {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "object_id")
	private int id;
	@Length(min = 7, message = "*Your password must have at least 7 characters")
	@NotEmpty(message = "*Please write title")
	@Column(name = "title")
	private String title;
	@Length(min = 7, message = "*Your password must have at least 7 characters")
	@NotEmpty(message = "*Please write text for game object")
	@Column(name = "text_object", columnDefinition = "TEXT")
	private String text;
	@Enumerated(EnumType.STRING)
	@Column(name = "status_object")
	private Status status;
	@Column(name = "created_at", updatable = false)
	private Timestamp creatdAt;
	@Column(name = "updated_at")
	private Timestamp updateAt;
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "gameObject")
	private List<Comment> comments;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	@ManyToOne
	@JoinColumn(name = "game_id")
	private Game game;

}
