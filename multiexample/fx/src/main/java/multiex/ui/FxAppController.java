package multiex.ui;

import java.io.File;

import fxmapcontrol.Location;
import fxmapcontrol.MapBase;
import fxmapcontrol.MapItemsControl;
import fxmapcontrol.MapNode;
import fxmapcontrol.MapProjection;
import fxutil.doc.FileMenuController;
import fxutil.doc.IDocumentListener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import multiex.core.LatLong;
import multiex.core.LatLongs;

public class FxAppController implements IDocumentListener<LatLongs, File> {

	private final LatLongsStorage latLongsStorage;

	public FxAppController() {
		latLongsStorage = new LatLongsStorage();
		latLongsStorage.addDocumentStorageListener(this);
	}

	public LatLongs getLatLongs() {
		return latLongsStorage.getDocument();
	}

	// to make it testable
	public void setLatLongs(final LatLongs latLongs) {
		latLongsStorage.setDocument(latLongs);
		updateLocationViewList(0);
	}

	@FXML
	private FileMenuController fileMenuController;

	@FXML
	private ListView<LatLong> locationListView;

	@FXML
	private MapBase mapView;

	private MapItemsControl<MapNode> markersParent;
	private MapMarker marker = null;
	private DraggableNodeController draggableMapController = null;
	private DraggableNodeController draggableMarkerController = null;

	@FXML
	private Slider zoomSlider;

	@FXML
	private void initialize() {
		fileMenuController.setDocumentStorage(latLongsStorage);
		// map stuff
		// mapView.getChildren().add(MapTileLayer.getOpenStreetMapLayer());
		zoomSlider.valueProperty().addListener((prop, oldValue, newValue) -> mapView.setZoomLevel(zoomSlider.getValue()));
		zoomSlider.setValue(8);
		markersParent = new MapItemsControl<MapNode>();
		mapView.getChildren().add(markersParent);
		draggableMapController = new DraggableNodeController(this::handleMapDragged);
		draggableMapController.setImmediate(true);
		draggableMapController.attach(mapView);
		draggableMarkerController = new DraggableNodeController(this::handleMarkerDragged);
		// the location list
		locationListView.getSelectionModel().selectedIndexProperty().addListener((prop, oldValue, newValue) -> updateMapMarker(true));
	}

	private void handleMapDragged(final Node node, final double dx, final double dy) {
		final MapProjection projection = mapView.getProjection();
		final Point2D point = projection.locationToViewportPoint(mapView.getCenter());
		final Location newCenter = projection.viewportPointToLocation(point.add(-dx, -dy));
		mapView.setCenter(newCenter);
	}

	private void handleMarkerDragged(final Node node, final double dx, final double dy) {
		final MapProjection projection = mapView.getProjection();
		final Point2D point = projection.locationToViewportPoint(marker.getLocation());
		final Location newLocation = projection.viewportPointToLocation(point.add(dx, dy));
		getLatLongs().setLatLong(locationListView.getSelectionModel().getSelectedIndex(), location2LatLong(newLocation));
		updateLocationViewListSelection(false);
	}

	private LatLong location2LatLong(final Location newLocation) {
		return new LatLong(newLocation.getLatitude(), newLocation.getLongitude());
	}

	private void updateMapMarker(final boolean centerOnMarker) {
		final int num = locationListView.getSelectionModel().getSelectedIndex();
		if (num < 0 || num >= getLatLongs().getLatLongCount()) {
			markersParent.getItems().clear();
			if (draggableMarkerController != null) {
				draggableMarkerController.detach(marker);
			}
			marker = null;
		} else {
			final LatLong latLong = getLatLongs().getLatLong(num);
			if (marker == null) {
				marker = new MapMarker(latLong);
				markersParent.getItems().add(marker);
				if (draggableMarkerController != null) {
					draggableMarkerController.attach(marker);
				}
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
		final int pos = getLatLongs().addLatLong(location2LatLong(center));
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
		locationListView.getItems().set(index, getLatLongs().getLatLong(index));
	}

	private void updateLocationViewList(int selectedIndex) {
		final LatLong[] latLongs = new LatLong[getLatLongs().getLatLongCount()];
		for (int i = 0; i < latLongs.length; i++) {
			latLongs[i] = getLatLongs().getLatLong(i);
		}
		final int oldSelectionIndex = locationListView.getSelectionModel().getSelectedIndex();
		locationListView.setItems(FXCollections.observableArrayList(latLongs));
		if (selectedIndex < 0 || selectedIndex >= latLongs.length) {
			selectedIndex = oldSelectionIndex;
		}
		if (selectedIndex >= 0 && selectedIndex < getLatLongs().getLatLongCount()) {
			locationListView.getSelectionModel().select(selectedIndex);
		}
	}

	// IDocumentListener

	@Override
	public void documentLocationChanged(final File documentLocation, final File oldDocumentLocation) {
	}

	@Override
	public void documentChanged(final LatLongs document, final LatLongs oldDocument) {
		updateLocationViewList(0);
	}
}
