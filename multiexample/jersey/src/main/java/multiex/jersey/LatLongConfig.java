package multiex.jersey;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import multiex.core.LatLongs;

public class LatLongConfig extends ResourceConfig {

	final LatLongs latLongs;

	public LatLongConfig() {
		this(new LatLongs());
	}

	public LatLongConfig(final LatLongs latLongs) {
		this.latLongs = latLongs;
		register(LatLongService.class);
		register(ReactApp.class);
		register(LatLongObjectMapperProvider.class);
		register(JacksonFeature.class);

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(latLongs);
			}
		});
	}
}
