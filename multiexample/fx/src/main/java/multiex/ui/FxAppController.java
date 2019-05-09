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

	private final LatLongs latLongs;

	public FxAppController() {
		latLongs = new LatLongs(63.1, 11.2, 63.2, 11.0);
	}

	@FXML
	private ListView<LatLong> locationListView;

	@FXML
	private MapBase mapView;

	private MapItemsControl<MapNode> markersParent;
	private MapMarker marker = null;
	private final DraggableMarkerController draggableMarkerController = new DraggableMarkerController() {
		@Override
		protected void handleDragged(final Node node, final double dx, final double dy) {
			final MapProjection projection = mapView.getProjection();
			final Point2D point = projection.locationToViewportPoint(marker.getLocation());
			final Point2D newPoint = point.add(dx, dy);
			final Location newLocation = projection.viewportPointToLocation(newPoint);
			latLongs.setLatLong(locationListView.getSelectionModel().getSelectedIndex(), new LatLong(newLocation.getLatitude(), newLocation.getLongitude()));
			updateMapMarker(false);
		}
	};

	@FXML
	private Slider zoomSlider;

	@FXML
	public void initialize() {
		mapView.getChildren().add(MapTileLayer.getOpenStreetMapLayer());
		zoomSlider.valueProperty().addListener((prop, oldValue, newValue) -> updateZoomLevel());
		zoomSlider.setValue(8);
		markersParent = new MapItemsControl<MapNode>();
		mapView.getChildren().add(markersParent);

		locationListView.getSelectionModel().selectedIndexProperty().addListener((prop, oldValue, newValue) -> updateMapMarker(true));
		updateLocationViewList(0);
		if (latLongs.getLatLongCount() > 0) {
			locationListView.getSelectionModel().select(0);
		}
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

	private void updateZoomLevel() {
		mapView.setZoomLevel(zoomSlider.getValue());
	}

	private void updateLocationViewList(final int selectedIndex) {
		final LatLong[] latLongs = new LatLong[this.latLongs.getLatLongCount()];
		for (int i = 0; i < latLongs.length; i++) {
			latLongs[i] = this.latLongs.getLatLong(i);
		}
		locationListView.setItems(FXCollections.observableArrayList(latLongs));
		if (selectedIndex >= 0 && selectedIndex < latLongs.length) {
			locationListView.getSelectionModel().select(selectedIndex);
		}
	}
}
