package ru.bechol.jwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bechol.jwt.models.User;
import ru.bechol.jwt.services.*;

import java.util.Optional;

/**
 * Interface UserRepository.
 *
 * @author Father_BO
 * @version 1.0
 * @email oleg071984@gmail.com
 * @see User
 * @see UserService
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	/**
	 * Method findByEmail.
	 * Search for a user by email.
	 *
	 * @param email user email.
	 * @return Optional of User.
	 */
	Optional<User> findByEmail(String email);

	/**
	 * Method findByCode.
	 * Search for a user by email confirmation code.
	 *
	 * @param code user email confirmation code.
	 * @return Optional of User.
	 */
	Optional<User> findByCode(String code);

}
