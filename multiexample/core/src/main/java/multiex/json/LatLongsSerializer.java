package multiex.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import multiex.core.LatLong;
import multiex.core.LatLongs;

public class LatLongsSerializer extends JsonSerializer<LatLongs> {

	@Override
	public void serialize(final LatLongs latLongs, final JsonGenerator jsonGen, final SerializerProvider provider) throws IOException {
		jsonGen.writeStartArray(latLongs.getLatLongCount());
		for (final LatLong latLong : latLongs) {
			jsonGen.writeObject(latLong);
		}
		jsonGen.writeEndArray();
	}
}
