package ru.bechol.jwt.response.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import org.springframework.boot.jackson.JsonComponent;
import ru.bechol.jwt.models.Role;

import java.io.IOException;
import java.util.Set;

/**
 * Class RoleListSerializer.
 * Serializing a list of user roles to an array.
 *
 * @author Father_BO.
 * @version 1.0
 * @email oleg071984@gmail.com.
 * @see ru.bechol.jwt.response.dto.UserDto
 * @see ru.bechol.jwt.response.Response
 */
@JsonComponent
public class RoleListSerializer extends JsonSerializer<Set<Role>> {

	@Override
	public void serialize(Set<Role> roles, JsonGenerator jsonGenerator,
						  SerializerProvider serializerProvider) throws IOException {
		jsonGenerator.writeStartArray();
		for (Role role : roles) {
			jsonGenerator.writeString(role.getName().replace("ROLE_", ""));
		}
		jsonGenerator.writeEndArray();
	}
}
