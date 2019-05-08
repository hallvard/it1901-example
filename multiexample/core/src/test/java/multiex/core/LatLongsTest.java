package multiex.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import multiex.core.LatLongs;

public class LatLongsTest {

	private LatLongs latLongs;

	@Before
	public void setup() {
		latLongs = new LatLongs();
	}

	@Test
	public void testEmptyConstructor() {
		Assert.assertEquals(0, latLongs.getLatLongCount());
	}
}
