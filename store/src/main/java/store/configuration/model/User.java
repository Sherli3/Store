package store.configuration.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = { "id", "firstName", "lastName", "password", "createdAt", "userRoles", "token",
		"resetToken","gameObject" })
@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Long id;
	@NotEmpty(message = "*Please provide your name")
	@Column(name = "first_name")
	private String firstName;
	@NotEmpty(message = "*Please provide your lastname")
	@Column(name = "last_name")
	private String lastName;
	@Email(message = "*Please provide a valid Email")
	@NotEmpty(message = "*Please provide an email")
	@Column(name = "email")
	private String email;
	@Length(min = 7, message = "*Your password must have at least 7 characters")
	@NotEmpty(message = "*Please provide your password")
	@Column(name = "password")
	private String password;
	@Column(name = "created_at")
	private Timestamp createdAt;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<Role> userRoles;
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
	private List<GameObject> gameObject;
	@Column(name = "enabled", nullable = false)
	private Boolean enabled;
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private UserVerificationToken token;
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private PasswordResetToken resetToken;

}
