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
	@Column(name = "title")
	private String title;
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
