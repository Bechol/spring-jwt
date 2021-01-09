package ru.bechol.jwt.models;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import ru.bechol.jwt.repositories.RoleRepository;
import ru.bechol.jwt.services.RoleService;

import javax.persistence.*;
import java.util.Set;

/**
 * Class Role.
 * Domain model representing the role of user.
 *
 * @author Father_BO
 * @version 1.0
 * @email oleg071984@gmail.com
 * @see RoleRepository
 * @see RoleService
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "roles")
@NoArgsConstructor
public class Role implements GrantedAuthority {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	long id;
	@Column(name = "role_name", nullable = false)
	String name;
	@ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
	@JsonBackReference
	Set<User> users;

	@Override
	public String getAuthority() {
		return this.name;
	}
}
