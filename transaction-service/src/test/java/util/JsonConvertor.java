package util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonConvertor {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
			.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
			.registerModule(new JavaTimeModule());

	public static <T> T convertJsonToObject(String serializedObject, Class<T> objectClass) throws
			JsonProcessingException {
		return OBJECT_MAPPER.readValue(serializedObject, objectClass);
	}

	public static String convertObjectToJson(Object object) throws JsonProcessingException {
		return OBJECT_MAPPER.writeValueAsString(object);
	}

	public static <T> T convertJsonToObject(String object, TypeReference<T> typeReference) throws IOException {
		return OBJECT_MAPPER.readValue(object, typeReference);
	}
}
