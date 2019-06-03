package multiex.ui;

import fxmapcontrol.Location;
import fxmapcontrol.MapBase;
import fxmapcontrol.MapItemsControl;
import fxmapcontrol.MapNode;
import fxmapcontrol.MapProjection;
import fxmapcontrol.MapTileLayer;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import multiex.core.LatLong;
import multiex.core.LatLongs;

public class FxAppController {

	private LatLongs latLongs;

	// to make it testable
	public void setLatLongs(final LatLongs latLongs) {
		this.latLongs = latLongs;
		updateLocationViewList(0);
	}

	@FXML
	private ListView<LatLong> locationListView;

	@FXML
	private MapBase mapView;

	private MapItemsControl<MapNode> markersParent;
	private MapMarker marker = null;
	private DraggableMarkerController draggableMarkerController;

	@FXML
	private Slider zoomSlider;

	@FXML
	private void initialize() {
		// map stuff
		mapView.getChildren().add(MapTileLayer.getOpenStreetMapLayer());
		zoomSlider.valueProperty().addListener((prop, oldValue, newValue) -> mapView.setZoomLevel(zoomSlider.getValue()));
		zoomSlider.setValue(8);
		markersParent = new MapItemsControl<MapNode>();
		mapView.getChildren().add(markersParent);
		draggableMarkerController = new DraggableMarkerController(this::handleMarkerDragged);
		// the location list
		locationListView.getSelectionModel().selectedIndexProperty().addListener((prop, oldValue, newValue) -> updateMapMarker(true));
	}

	private void handleMarkerDragged(final Node node, final double dx, final double dy) {
		final MapProjection projection = mapView.getProjection();
		final Point2D point = projection.locationToViewportPoint(marker.getLocation());
		final Point2D newPoint = point.add(dx, dy);
		final Location newLocation = projection.viewportPointToLocation(newPoint);
		latLongs.setLatLong(locationListView.getSelectionModel().getSelectedIndex(), location2LatLong(newLocation));
		updateLocationViewListSelection(false);
	}

	private LatLong location2LatLong(final Location newLocation) {
		return new LatLong(newLocation.getLatitude(), newLocation.getLongitude());
	}

	private void updateMapMarker(final boolean centerOnMarker) {
		final int num = locationListView.getSelectionModel().getSelectedIndex();
		if (num < 0 || num >= latLongs.getLatLongCount()) {
			markersParent.getItems().clear();
			draggableMarkerController.detach(marker);
			marker = null;
		} else {
			final LatLong latLong = latLongs.getLatLong(num);
			if (marker == null) {
				marker = new MapMarker(latLong);
				markersParent.getItems().add(marker);
				draggableMarkerController.attach(marker);
			} else {
				marker.setLocation(latLong);
			}
			if (centerOnMarker) {
				mapView.setCenter(marker.getLocation());
			}
		}
	}

	@FXML
	private void handleAddLocation() {
		final Location center = mapView.getCenter();
		final int pos = latLongs.addLatLong(location2LatLong(center));
		updateLocationViewList(pos);
	}

	private void updateLocationViewListSelection(final Boolean updateMapMarker) {
		final int selectedIndex = locationListView.getSelectionModel().getSelectedIndex();
		updateLocationViewListItem(selectedIndex);
		if (updateMapMarker != null) {
			updateMapMarker(updateMapMarker);
		}
	}

	private void updateLocationViewListItem(final int index) {
		locationListView.getItems().set(index, this.latLongs.getLatLong(index));
	}

	private void updateLocationViewList(int selectedIndex) {
		final LatLong[] latLongs = new LatLong[this.latLongs.getLatLongCount()];
		for (int i = 0; i < latLongs.length; i++) {
			latLongs[i] = this.latLongs.getLatLong(i);
		}
		final int oldSelectionIndex = locationListView.getSelectionModel().getSelectedIndex();
		locationListView.setItems(FXCollections.observableArrayList(latLongs));
		if (selectedIndex < 0 || selectedIndex >= latLongs.length) {
			selectedIndex = oldSelectionIndex;
		}
		if (selectedIndex >= 0 && selectedIndex < this.latLongs.getLatLongCount()) {
			locationListView.getSelectionModel().select(selectedIndex);
		}
	}

	@FXML
	private void handleLoad() {

	}
}
