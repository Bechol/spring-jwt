package ru.bechol.jwt.response.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.bechol.jwt.models.Role;
import ru.bechol.jwt.response.serialize.RoleListSerializer;

import java.util.Set;

/**
 * Class UserDto.
 * User data serialization.
 *
 * @author Father_BO.
 * @version 1.0
 * @email oleg071984@gmail.com.
 */
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDto {

	String username;
	@JsonSerialize(using = RoleListSerializer.class)
	Set<Role> roles;
}
