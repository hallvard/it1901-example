package multiex.jersey;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.glassfish.jersey.test.grizzly.GrizzlyTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;

public abstract class MultiExJerseyTest extends JerseyTest {

	protected boolean shouldLog() {
		return false;
	}

	@Override
	protected ResourceConfig configure() {
		final LatLongConfig config = new LatLongConfig();
		if (shouldLog()) {
			enable(TestProperties.LOG_TRAFFIC);
			enable(TestProperties.DUMP_ENTITY);
			config.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, "WARNING");
		}
		return config;
	}

	@Override
	protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
		return new GrizzlyTestContainerFactory();
	}
}
