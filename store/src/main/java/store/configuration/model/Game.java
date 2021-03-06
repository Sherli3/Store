package store.configuration.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
@Entity
@Table(name = "game")
public class Game {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "game_id")
	private int id;
	@Length(min = 3, message = "*Game ust have at least 3 characters")
	@NotEmpty(message = "*Please write game")
	@Column(name = "game_name")
	private String gameName;
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "game")
	private List<GameObject> object;

}
