package ru.bechol.jwt.response;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.bechol.jwt.response.dto.UserDto;

import java.util.*;

/**
 * Class Response.
 * Response serialization.
 *
 * @author Father BO.
 * @version 1.0
 * @email oleg071984@gmail.com
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Response {

	boolean result;
	String token;
	@JsonProperty(value = "user")
	UserDto userDto;
	ValidationErrors errors;
	String message;

	@Getter
	@Builder
	public static class ValidationErrors {

		Map<String, List<String>> errorMap;

		@JsonAnyGetter
		public Map<String, List<String>> getErrorMap() {
			return errorMap;
		}
	}
}
