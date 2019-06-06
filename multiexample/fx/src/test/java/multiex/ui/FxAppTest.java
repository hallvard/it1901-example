package multiex.ui;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import fxmapcontrol.Location;
import fxmapcontrol.MapBase;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import multiex.core.LatLong;
import multiex.core.LatLongs;

public class FxAppTest extends ApplicationTest {

	@BeforeClass
	public static void headless() {
		if (Boolean.valueOf(System.getProperty("gitlab-ci", "false"))) {
			System.setProperty("prism.verbose", "true"); // optional
			System.setProperty("java.awt.headless", "true");
			System.setProperty("testfx.robot", "glass");
			System.setProperty("testfx.headless", "true");
			System.setProperty("glass.platform", "Monocle");
			System.setProperty("monocle.platform", "Headless");
			System.setProperty("prism.order", "sw");
			System.setProperty("prism.text", "t2k");
			System.setProperty("testfx.setup.timeout", "2500");
		}
	}

	private FxAppController controller;
	private LatLongs latLongs;

	@Override
	public void start(final Stage stage) throws Exception {
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("FxApp.fxml"));
		final Parent root = loader.load();
		this.controller = loader.getController();
		setUpLatLongs();
		final Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	private List<LatLong> latLongList;

	private void setUpLatLongs() {
		// test data
		latLongList = new ArrayList<>(List.of(new LatLong(63.1, 11.2), new LatLong(63.2, 11.0)));
		// "mocked" (faked) LatLongs object with very specific and limited behavior
		latLongs = mock(LatLongs.class);
		// get nth LatLong object
		when(latLongs.getLatLong(anyInt())).then(invocation -> latLongList.get(invocation.getArgument(0)));
		// get the number of LatLong objects
		when(latLongs.getLatLongCount()).then(invocation -> latLongList.size());
		// iterator for LatLong objects
		when(latLongs.iterator()).then(invocation -> latLongList.iterator());
		controller.setLatLongs(latLongs);
	}

	@Test
	public void testController() {
		Assert.assertTrue(this.controller instanceof FxAppController);
	}

	@Test
	public void testLocationListView() {
		final ListView<?> locationListView = lookup("#locationListView").query();
		// list contains equals elements in same order
		Assert.assertEquals(latLongList, locationListView.getItems());
		// first list element is auto-selected
		Assert.assertEquals(0, locationListView.getSelectionModel().getSelectedIndex());
	}

	@Test
	public void testMapView() {
		final MapBase mapView = lookup("#mapView").query();
		// center of map view is approx. the first LatLong object
		final Location center = mapView.getCenter();
		final double epsilon = 0.000001; // round-off error
		Assert.assertEquals(latLongList.get(0).latitude, center.getLatitude(), epsilon);
		Assert.assertEquals(latLongList.get(0).longitude, center.getLongitude(), epsilon);
	}

	@Test
	public void testAddLocation() {
		// needs map center
		final Location center = ((MapBase) lookup("#mapView").query()).getCenter();
		// add behavior for add
		final LatLong latLong = new LatLong(center.getLatitude(), center.getLongitude());
		when(latLongs.addLatLong(latLong)).thenReturn(2); // add center

		// make test less sensitive to exact button text
		final Button addLocButton = lookup(node -> node instanceof Button && ((Button) node).getText().toLowerCase().startsWith("add loc")).query();
		clickOn(addLocButton);
	}
}
