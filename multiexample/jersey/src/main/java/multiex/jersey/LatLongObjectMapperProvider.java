package multiex.jersey;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import multiex.core.LatLong;
import multiex.core.LatLongs;
import multiex.json.LatLongDeserializer;
import multiex.json.LatLongSerializer;
import multiex.json.LatLongsDeserializer;
import multiex.json.LatLongsSerializer;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LatLongObjectMapperProvider implements ContextResolver<ObjectMapper> {

	private final ObjectMapper objectMapper;

	public LatLongObjectMapperProvider() {
		final SimpleModule module = new SimpleModule()
				.addSerializer(LatLong.class, new LatLongSerializer())
				.addSerializer(LatLongs.class, new LatLongsSerializer())
				.addDeserializer(LatLong.class, new LatLongDeserializer())
				.addDeserializer(LatLongs.class, new LatLongsDeserializer())
				;
		objectMapper = new ObjectMapper()
				.registerModule(module);
	}

	@Override
	public ObjectMapper getContext(final Class<?> type) {
		return objectMapper;
	}
}
