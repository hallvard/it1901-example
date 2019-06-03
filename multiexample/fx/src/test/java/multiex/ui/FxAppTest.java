package multiex.ui;

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
import javafx.scene.Node;
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
		latLongList = List.of(new LatLong(63.1, 11.2), new LatLong(63.2, 11.0));
		// "mocked" (faked) LatLongs object with very specific and limited behavior
		latLongs = mock(LatLongs.class);
		when(latLongs.getLatLong(0)).thenReturn(latLongList.get(0)); // get first LatLong object
		when(latLongs.getLatLong(1)).thenReturn(latLongList.get(1)); // get second LatLong object
		when(latLongs.getLatLongCount()).thenReturn(latLongList.size()); // get the number of LatLong objects
		when(latLongs.iterator()).thenReturn(latLongList.iterator()); // iterator for LatLong objects
		controller.setLatLongs(latLongs);
	}

	@Test
	public void testController() {
		Assert.assertTrue(this.controller instanceof FxAppController);
	}

	@Test
	public void testlLocationListView() {
		final Node locationListNode = lookup("#locationListView").query();
		Assert.assertTrue(locationListNode instanceof ListView<?>);
		final ListView<?> locationListView = (ListView<?>) locationListNode;
		// list contains equals elements in same order
		Assert.assertEquals(latLongList, locationListView.getItems());
		// first list element is auto-selected
		Assert.assertEquals(0, locationListView.getSelectionModel().getSelectedIndex());
	}

	@Test
	public void testlMapView() {
		final Node mapNode = lookup("#mapView").query();
		Assert.assertTrue(mapNode instanceof MapBase);
		final MapBase mapView = (MapBase) mapNode;
		// center of map view is approx. the first LatLong object
		final Location center = mapView.getCenter();
		final double epsilon = 0.000001; // round-off error
		Assert.assertEquals(latLongList.get(0).latitude, center.getLatitude(), epsilon);
		Assert.assertEquals(latLongList.get(0).longitude, center.getLongitude(), epsilon);
	}

	private boolean addLocationButtonTest(final Node node) {
		return node instanceof Button && ((Button) node).getText().toLowerCase().startsWith("add loc");
	}

	@Test
	public void testlAddLocation() {
		final Node addLocNode = lookup(this::addLocationButtonTest).query();
		Assert.assertTrue(addLocNode instanceof Button);
		final Button addLocButton = (Button) addLocNode;
		// needs map center
		final Node mapNode = lookup("#mapView").query();
		Assert.assertTrue(mapNode instanceof MapBase);
		final MapBase mapView = (MapBase) mapNode;
		final Location center = mapView.getCenter();
		// add behavior for add
		final LatLong latLong = new LatLong(center.getLatitude(), center.getLongitude());
		when(latLongs.addLatLong(latLong)).thenReturn(2); // add center
		when(latLongs.getLatLong(2)).thenReturn(latLong); // get second LatLong object
		when(latLongs.getLatLongCount()).thenReturn(3); // get the number of LatLong objects
		final List<LatLong> newLatLongList = new ArrayList<>(latLongList);
		newLatLongList.add(latLong);
		when(latLongs.iterator()).thenReturn(newLatLongList.iterator()); // iterator for LatLong objects
		clickOn(addLocButton);
	}
}
