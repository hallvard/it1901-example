package multiex.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import multiex.core.LatLongs;

public class FxApp extends Application {

	@Override
	public void start(final Stage stage) throws Exception {
		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FxApp.fxml"));
		final Parent root = fxmlLoader.load();
		final FxAppController controller = fxmlLoader.getController();
		controller.setLatLongs(new LatLongs(63.1, 11.2, 63.2, 11.0));
		final Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(final String[] args) {
		launch(args);
	}
}
