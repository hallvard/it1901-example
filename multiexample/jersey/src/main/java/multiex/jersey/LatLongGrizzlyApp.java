package multiex.jersey;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class LatLongGrizzlyApp {

	private static final URI BASE_URI = URI.create("http://localhost:8080/");

	public static void main(final String[] args) {
		try {
			final ResourceConfig resourceConfig = new LatLongConfig();
			final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, resourceConfig);
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				@Override
				public void run() {
					server.shutdownNow();
				}
			}));
			Thread.currentThread().join();
		} catch (final InterruptedException ex) {
			Logger.getLogger(LatLongGrizzlyApp.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
