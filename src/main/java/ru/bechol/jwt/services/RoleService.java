package ru.bechol.jwt.services;

import org.springframework.stereotype.Service;
import ru.bechol.jwt.models.Role;

@Service
public interface RoleService {

	Role findByName(String userRoleName);
}
