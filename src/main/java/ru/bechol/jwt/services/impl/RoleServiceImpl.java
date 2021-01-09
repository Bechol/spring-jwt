package ru.bechol.jwt.services.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bechol.jwt.models.Role;
import ru.bechol.jwt.repositories.RoleRepository;
import ru.bechol.jwt.services.RoleService;

/**
 * Class RoleServiceImpl.
 * Implementation of RoleService interface.
 *
 * @author Father_BO
 * @version 1.0
 * @email oleg071984@gmail.com
 * @see Role
 * @see RoleRepository
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class RoleServiceImpl implements RoleService {

	RoleRepository roleRepository;

	@Autowired
	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	/**
	 * Метод findByName.
	 * Поиск роли пользователя по наименованию.
	 *
	 * @param userRoleName - наименование роли пользователя.
	 * @return - null - если роль пользователя не найдена.
	 */
	@Override
	public Role findByName(String userRoleName) {
		return roleRepository.findByName(userRoleName).orElse(null);
	}
}
