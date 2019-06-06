package fxutil.doc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;

public class FileMenuController {

	private IDocumentStorage<File> documentStorage;

	public void setDocumentStorage(final IDocumentStorage<File> documentStorage) {
		this.documentStorage = documentStorage;
		if (importMenu != null) {
			importMenu.setDisable(documentStorage.getDocumentImporters().isEmpty());
		}
	}

	@FXML
	public void handleNewAction() {
		documentStorage.newDocument();
	}

	private final List<File> recentFiles = new ArrayList<File>();

	@FXML
	private Menu recentMenu;

	protected void updateRecentMenu(final File file) {
		recentFiles.remove(file);
		recentFiles.add(0, file);
		recentMenu.getItems().clear();
		for (final File recentFile : recentFiles) {
			final MenuItem menuItem = new MenuItem();
			menuItem.setText(recentFile.toString());
			menuItem.setOnAction(event -> handleOpenAction(event));
			recentMenu.getItems().add(menuItem);
		}
	}

	private FileChooser fileChooser;

	FileChooser getFileChooser() {
		if (fileChooser == null) {
			fileChooser = new FileChooser();
		}
		return fileChooser;
	}

	@FXML
	public void handleOpenAction(final ActionEvent event) {
		File selection = null;
		if (event.getSource() instanceof MenuItem) {
			final File file = new File(((MenuItem) event.getSource()).getText());
			if (file.exists()) {
				selection = file;
			}
		}
		if (selection == null) {
			final FileChooser fileChooser = getFileChooser();
			selection = fileChooser.showOpenDialog(null);
		}
		if (selection != null) {
			handleOpenAction(selection);
		}
	}

	private void showExceptionDialog(final String message) {
		final Alert alert = new Alert(AlertType.ERROR, message, ButtonType.CLOSE);
		alert.showAndWait();
	}

	private void showExceptionDialog(final String message, final Exception e) {
		showExceptionDialog(message + ": " + e.getLocalizedMessage());
	}

	void handleOpenAction(final File selection) {
		try {
			documentStorage.openDocument(selection);
			updateRecentMenu(selection);
		} catch (final IOException e) {
			showExceptionDialog("Oops, problem when opening " + selection, e);
		}
	}

	private void showSaveExceptionDialog(final File location, final Exception e) {
		showExceptionDialog("Oops, problem saving to " + location, e);
	}

	@FXML
	public void handleSaveAction() {
		if (documentStorage.getDocumentLocation() == null) {
			handleSaveAsAction();
		} else {
			try {
				documentStorage.saveDocument();
			} catch (final IOException e) {
				showSaveExceptionDialog(documentStorage.getDocumentLocation(), e);
			}
		}
	}

	@FXML
	public void handleSaveAsAction() {
		final FileChooser fileChooser = getFileChooser();
		final File selection = fileChooser.showSaveDialog(null);
		handleSaveAsAction(selection);
	}

	void handleSaveAsAction(final File selection) {
		final File oldStorage = documentStorage.getDocumentLocation();
		try {
			documentStorage.setDocumentLocation(selection);
			documentStorage.saveDocument();
		} catch (final IOException e) {
			showSaveExceptionDialog(documentStorage.getDocumentLocation(), e);
			documentStorage.setDocumentLocation(oldStorage);
		}
	}

	@FXML
	public void handleSaveCopyAsAction() {
		final FileChooser fileChooser = getFileChooser();
		final File selection = fileChooser.showSaveDialog(null);
		handleSaveCopyAsAction(selection);
	}

	void handleSaveCopyAsAction(final File selection) {
		final File oldStorage = documentStorage.getDocumentLocation();
		try {
			documentStorage.setDocumentLocation(selection);
			documentStorage.saveDocument();
		} catch (final IOException e) {
			showSaveExceptionDialog(selection, e);
		} finally {
			documentStorage.setDocumentLocation(oldStorage);
		}
	}

	@FXML
	private Menu importMenu;

	@FXML
	public void handleFileImportAction() {
		final FileChooser fileChooser = getFileChooser();
		final File selection = fileChooser.showOpenDialog(null);
		//		String path = selection.getPath();
		//		int pos = path.lastIndexOf('.');
		//		String ext = (pos > 0 ? path.substring(pos + 1) : null);
		handleFileImportAction(selection);
	}

	void handleFileImportAction(final File selection) {
		for (final IDocumentImporter importer : documentStorage.getDocumentImporters()) {
			try (InputStream input = new FileInputStream(selection)) {
				importer.importDocument(input);
				break;
			} catch (final Exception e) {
			}
		}
	}

	private TextInputDialog inputDialog;

	@FXML
	public void handleURLImportAction() {
		if (inputDialog == null) {
			inputDialog = new TextInputDialog();
		}
		inputDialog.setTitle("Import from URL");
		inputDialog.setHeaderText("Enter URL to import from");
		inputDialog.setContentText("Enter URL: ");
		// https://developer.garmin.com/downloads/connect-api/sample_file.gpx
		URL url = null;
		while (url == null) {
			final Optional<String> result = inputDialog.showAndWait();
			if (! result.isPresent()) {
				return;
			}
			try {
				url = new URL(result.get());
				if (handleURLImportAction(url)) {
					return;
				}
				url = null;
				inputDialog.setHeaderText("Problems reading it...");
				inputDialog.setContentText("Enter another URL: ");
			} catch (final MalformedURLException e1) {
				inputDialog.setContentText("Enter a valid URL: ");
			}
		}
	}

	boolean handleURLImportAction(final URL url) {
		for (final IDocumentImporter importer : documentStorage.getDocumentImporters()) {
			try (InputStream input = url.openStream()) {
				importer.importDocument(input);
				return true;
			} catch (final Exception e) {
				System.err.println(e.getMessage());
			}
		}
		return false;
	}
}
