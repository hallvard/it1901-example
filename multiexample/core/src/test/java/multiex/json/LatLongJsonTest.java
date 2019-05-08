package multiex.json;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import multiex.core.LatLong;
import multiex.json.LatLongDeserializer;
import multiex.json.LatLongSerializer;

public class LatLongJsonTest extends AbstractJsonTest {

	@Before
	@Override
	public void setup() {
		super.setup();
	}

	@Override
	protected ObjectMapper createObjectMapper() {
		return createObjectMapper(LatLong.class, new LatLongSerializer(), new LatLongDeserializer());
	}

	@Test
	public void testLatLongSerialization() throws Exception {
		assertWriteValue("{\"latitude\":63.1,\"longitude\":12.3}", new LatLong(63.1, 12.3));
	}

	@Test
	public void testLatLongDeserialization() throws Exception {
		Assert.assertEquals(new LatLong(63.1, 12.3), readValue("[ 63.1, 12.3 ]", LatLong.class));
	}
}
