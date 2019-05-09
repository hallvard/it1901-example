package multiex.ui;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class DraggableMarkerController {

	private NodeDraggedHandler nodeDraggedHandler;

	public void setNodeDraggedHandler(final NodeDraggedHandler nodeDraggedHandler) {
		this.nodeDraggedHandler = nodeDraggedHandler;
	}

	public DraggableMarkerController() {
	}

	public DraggableMarkerController(final NodeDraggedHandler nodeDraggedHandler) {
		setNodeDraggedHandler(nodeDraggedHandler);
	}

	private Node currentNode = null;
	private Point2D startPoint = null;
	private Point2D startTranslate = null;

	private final EventHandler<MouseEvent> mousePressedHandler = this::mousePressed;
	private final EventHandler<MouseEvent> mouseDraggedHandler = this::mouseDragged;
	private final EventHandler<MouseEvent> mouseReleasedHandler = this::mouseReleased;

	public void attach(final Node node) {
		node.setOnMousePressed(mousePressedHandler);
		node.setOnMouseDragged(mouseDraggedHandler);
		node.setOnMouseReleased(mouseReleasedHandler);
	}

	public void detach(final Node node) {
		node.setOnMousePressed(null);
		node.setOnMouseDragged(null);
		node.setOnMouseReleased(null);
	}

	private void mousePressed(final MouseEvent mouseEvent) {
		if (currentNode == null && mouseEvent.getSource() instanceof Node) {
			currentNode = (Node) mouseEvent.getSource();
			startPoint = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
			startTranslate = new Point2D(currentNode.getTranslateX(), currentNode.getTranslateY());
		}
	}

	private void mouseDragged(final MouseEvent mouseEvent) {
		if (currentNode != null && currentNode == mouseEvent.getSource()) {
			final double dx = mouseEvent.getSceneX() - startPoint.getX();
			final double dy = mouseEvent.getSceneY() - startPoint.getY();
			currentNode.setTranslateX(startTranslate.getX() + dx);
			currentNode.setTranslateY(startTranslate.getY() + dy);
		}
	}

	private void mouseReleased(final MouseEvent mouseEvent) {
		if (currentNode != null && currentNode == mouseEvent.getSource()) {
			final double dx = mouseEvent.getSceneX() - startPoint.getX();
			final double dy = mouseEvent.getSceneY() - startPoint.getY();
			currentNode.setTranslateX(startTranslate.getX());
			currentNode.setTranslateY(startTranslate.getY());
			final Node node = currentNode;
			currentNode = null;
			if (nodeDraggedHandler != null) {
				nodeDraggedHandler.nodeDragged(node, dx, dy);
			}
		}
	}

	public interface NodeDraggedHandler {
		public void nodeDragged(Node currentNode2, double dx, double dy);
	}
}
