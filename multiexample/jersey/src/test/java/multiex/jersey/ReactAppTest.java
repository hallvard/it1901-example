package multiex.jersey;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;

public class ReactAppTest extends MultiExJerseyTest {

	private static final String REACT_APP_PATH = ReactApp.REACT_APP_PATH;

	@Test
	public void testGet() throws Exception {
		final Response response = target(REACT_APP_PATH).path("index.html")
				.request("plain/html; charset=UTF-8")
				.get();
		Assert.assertEquals(200, response.getStatus());
		final String entity = response.readEntity(String.class);
		Assert.assertTrue(entity.contains("MultiEx"));
	}
}
