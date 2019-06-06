package multiex.jersey;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.Collections;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import multiex.core.LatLong;
import multiex.jaxrs.LatLongObjectMapperProvider;
import multiex.jaxrs.LatLongService;

public class LatLongServiceTest extends MultiExJerseyTest {

	private static final String LAT_LONG_SERVICE_PATH = LatLongService.LAT_LONG_SERVICE_PATH;

	private ObjectMapper objectMapper;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		objectMapper = new LatLongObjectMapperProvider().getContext(getClass());
	}

	@Test
	public void testPostGetPutGetDelete() throws Exception {
		// POST, i.e. add
		final LatLong latLong = new LatLong(63, 11);
		final Response postResponse = target(LAT_LONG_SERVICE_PATH)
				.request("application/json; charset=UTF-8")
				.post(Entity.entity(objectMapper.writeValueAsString(Collections.singleton(latLong)), MediaType.APPLICATION_JSON));
		Assert.assertEquals(200, postResponse.getStatus());
		final Integer postNum = objectMapper.readValue(postResponse.readEntity(String.class), Integer.class);
		Assert.assertEquals(0, postNum.intValue());
		// GET
		testGet(0, latLong);
		// PUT, i.e. set
		final LatLong altLatLong = new LatLong(63, 11);

		final Response putResponse = target(LAT_LONG_SERVICE_PATH).path("0")
				.request("application/json; charset=UTF-8")
				.put(Entity.entity(objectMapper.writeValueAsString(latLong), MediaType.APPLICATION_JSON));
		Assert.assertEquals(200, putResponse.getStatus());
		final Integer putNum = objectMapper.readValue(putResponse.readEntity(String.class), Integer.class);
		Assert.assertEquals(0, putNum.intValue());
		// GET
		testGet(0, altLatLong);
		// DELETE, i.e. remove
		final Response deleteResponse = target(LAT_LONG_SERVICE_PATH).path("0")
				.request("application/json; charset=UTF-8")
				.delete();
		testContent(deleteResponse.readEntity(String.class), altLatLong);
	}

	protected void doJsonOutput(final HttpURLConnection postCon, final Object content)
			throws IOException, JsonGenerationException, JsonMappingException {
		postCon.setDoOutput(true);
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		objectMapper.writeValue(out, content);
		out.close();
		final byte[] postBytes = out.toByteArray();
		postCon.setFixedLengthStreamingMode(postBytes.length);
		postCon.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		postCon.connect();
		try (OutputStream conOut = postCon.getOutputStream()) {
			conOut.write(postBytes);
		}
	}

	protected void testGet(final int num, final LatLong latLong)
			throws MalformedURLException, IOException, ProtocolException, JsonParseException, JsonMappingException {
		// GET
		final Response response = target(LAT_LONG_SERVICE_PATH).path(String.valueOf(num))
				.request("application/json; charset=UTF-8")
				.get();
		Assert.assertEquals(200, response.getStatus());
		testContent(response.readEntity(String.class), latLong);
	}

	protected void testContent(final String content, final LatLong latLong)
			throws IOException, JsonParseException, JsonMappingException {
		final LatLong getLatLong = objectMapper.readValue(content, LatLong.class);
		Assert.assertEquals(latLong, getLatLong);
	}
}
