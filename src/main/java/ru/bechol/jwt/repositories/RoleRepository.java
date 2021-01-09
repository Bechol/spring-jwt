package ru.bechol.jwt.repositories;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bechol.jwt.models.Role;
import ru.bechol.jwt.services.RoleService;

import java.util.Optional;

/**
 * Interface RoleRepository.
 *
 * @author Father_BO
 * @version 1.0
 * @email oleg071984@gmail.com
 * @see Role
 * @see RoleService
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	/**
	 * Method findByName.
	 * Search for the user's role in the database by name.
	 *
	 * @param name role name.
	 * @return Optional<Role>.
	 */
	Optional<Role> findByName(@NonNull String name);
}
