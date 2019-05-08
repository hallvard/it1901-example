package multiex.jersey;

import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path(ReactApp.REACT_APP_PATH)
public class ReactApp {

	static final String REACT_APP_PATH = "reactApp";

	@GET
	@Path("{path:.*}")
	public Response getLatLongs(@PathParam("path") final String path) {
		final String resourcePath = "/static/" + path;
		final InputStream input = getClass().getResourceAsStream(resourcePath);
		//		System.out.println("Trying to serve " + path + " as " + resourcePath + "..." + (input != null ? "ok" : "404)"));
		return (input != null ? Response.ok(input).build() : Response.status(404).build());
	}
}
