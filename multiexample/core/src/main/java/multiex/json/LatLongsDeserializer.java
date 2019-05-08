package multiex.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import multiex.core.LatLong;
import multiex.core.LatLongs;

public class LatLongsDeserializer extends JsonDeserializer<LatLongs> {

	private final LatLongDeserializer latLongDeserializer = new LatLongDeserializer();

	@Override
	public LatLongs deserialize(final JsonParser jsonParser, final DeserializationContext deserContext) throws IOException, JsonProcessingException {
		final JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
		if (jsonNode instanceof ArrayNode) {
			final ArrayNode latLongsArray = (ArrayNode) jsonNode;
			final Collection<LatLong> latLongs = new ArrayList<>(latLongsArray.size());
			for (final JsonNode latLongNode : latLongsArray) {
				final LatLong latLong = latLongDeserializer.deserialize(latLongNode);
				latLongs.add(latLong);
			}
			return new LatLongs(latLongs);
		}
		return null;
	}
}
