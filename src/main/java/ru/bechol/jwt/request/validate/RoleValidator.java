package ru.bechol.jwt.request.validate;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.*;
import ru.bechol.jwt.request.RegisterRequest;
import ru.bechol.jwt.services.RoleService;

import javax.validation.*;
import java.util.Objects;

/**
 * Class RoleValidator.
 * Implementing the rules for validating the role field.
 *
 * @author Father_BO
 * @version 1.0
 * @email oleg071984@gmail.com
 * @see RegisterRequest
 * @see CheckRoleExistence
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleValidator implements ConstraintValidator<CheckRoleExistence, String> {

	RoleService roleService;

	@Autowired
	public RoleValidator(@Qualifier("roleServiceImpl") RoleService roleService) {
		this.roleService = roleService;
	}

	@Override
	public void initialize(CheckRoleExistence constraintAnnotation) {

	}

	/**
	 * Method isValid.
	 * Checking the existence of a role by the line containing the name of the role.
	 *
	 * @param roleName the role name obtained from the request body.
	 * @return true if the role is found by name in the base list of roles.
	 */
	@Override
	public boolean isValid(String roleName, ConstraintValidatorContext constraintValidatorContext) {
		return Objects.nonNull(roleService.findByName(roleName));
	}

}
