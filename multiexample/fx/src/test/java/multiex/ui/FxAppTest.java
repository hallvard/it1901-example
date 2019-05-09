package multiex.ui;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

	@Override
	public void start(final Stage stage) throws Exception {
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("FxApp.fxml"));
		final Parent root = loader.load();
		this.controller = loader.getController();
		final Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@Test
	public void testRootPane() {
		Assert.assertNotNull(lookup("#locationListView").query());
		Assert.assertNotNull(lookup("#mapView").query());
	}

	@Test
	public void testController() {
		Assert.assertTrue(this.controller instanceof FxAppController);
	}
}
