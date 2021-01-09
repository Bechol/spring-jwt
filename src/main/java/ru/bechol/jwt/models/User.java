package ru.bechol.jwt.models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.bechol.jwt.repositories.UserRepository;
import ru.bechol.jwt.response.serialize.RoleListSerializer;
import ru.bechol.jwt.services.UserService;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Class User.
 * Domain model representing the user
 *
 * @author Father_BO
 * @version 1.0
 * @email oleg071984@gmail.com
 * @see UserRepository
 * @see UserService
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	long id;
	@Column(nullable = false)
	String email;
	@JsonIgnore
	@Column(nullable = false)
	String password;
	@CreationTimestamp
	@Column(name = "reg_time", nullable = false, columnDefinition = "timestamp")
	LocalDateTime regTime;
	String code;
	@Column(name = "is_enabled")
	boolean enabled;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "users_roles",
			joinColumns = {@JoinColumn(name = "user_id")},
			inverseJoinColumns = {@JoinColumn(name = "role_id")}
	)
	@JsonManagedReference
	@JsonSerialize(using = RoleListSerializer.class)
	Set<Role> roles;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
}
