package multiex.json;

import java.io.IOException;

import org.junit.Assert;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public abstract class AbstractJsonTest {

	private ObjectMapper objectMapper;

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public void setup() {
		objectMapper = createObjectMapper();
	}

	protected abstract ObjectMapper createObjectMapper();

	protected <T> SimpleModule createSimpleModule(final Class<T> clazz, final JsonSerializer<T> serializer, final JsonDeserializer<T> deserializer) {
		return new SimpleModule()
				.addSerializer(clazz, serializer)
				.addDeserializer(clazz, deserializer);
	}

	protected <T> ObjectMapper createObjectMapper(final Class<T> clazz, final JsonSerializer<T> serializer, final JsonDeserializer<T> deserializer) {
		return new ObjectMapper().registerModule(createSimpleModule(clazz, serializer, deserializer));
	}

	protected void assertEqualsIgnoreWhitespace(final String expected, final String actual) throws Exception {
		Assert.assertEquals(expected, actual.replaceAll("\\s+",  ""));
	}

	protected void assertWriteValue(final String expected, final Object value) throws Exception {
		assertEqualsIgnoreWhitespace(expected, getObjectMapper().writeValueAsString(value));
	}

	protected <T> T readValue(final String s, final Class<T> clazz) throws IOException, JsonParseException, JsonMappingException {
		return getObjectMapper().readValue(s, clazz);
	}
}
