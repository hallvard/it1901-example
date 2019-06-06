package multiex.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import fxutil.doc.AbstractDocumentStorageImpl;
import fxutil.doc.IDocumentImporter;
import fxutil.doc.IDocumentLoader;
import fxutil.doc.IDocumentPersistence;
import fxutil.doc.IDocumentStorage;
import multiex.core.LatLongs;

public class LatLongsApp {

	private LatLongs latLongs = null;

	private final IDocumentPersistence<LatLongs, File> documentPersistence = new IDocumentPersistence<LatLongs, File>() {

		@Override
		public LatLongs loadDocument(final InputStream inputStream) throws Exception {
			// TODO
			return null;
		}

		@Override
		public void saveDocument(final LatLongs document, final File documentLocation) throws Exception {
			try (OutputStream output = new FileOutputStream(documentLocation)) {
				// TODO
			}
		}
	};

	private final AbstractDocumentStorageImpl<LatLongs, File> documentStorage = new AbstractDocumentStorageImpl<LatLongs, File>() {

		@Override
		protected LatLongs getDocument() {
			return latLongs;
		}

		@Override
		protected void setDocument(final LatLongs document) {
			final LatLongs oldDocument = getDocument();
			LatLongsApp.this.latLongs = document;
			fireDocumentChanged(oldDocument);
		}

		@Override
		protected LatLongs createDocument() {
			return new LatLongs();
		}

		@Override
		protected InputStream toInputStream(final File storage) throws IOException {
			return new FileInputStream(storage);
		}

		@Override
		public LatLongs loadDocument(final InputStream inputStream) throws Exception {
			return documentPersistence.loadDocument(inputStream);
		}

		@Override
		public void saveDocument(final LatLongs document, final File documentLocation) throws Exception {
			documentPersistence.saveDocument(document, documentLocation);
		}

		@Override
		public Collection<IDocumentImporter> getDocumentImporters() {
			return documentLoaders.stream().map(loader -> new IDocumentImporter() {
				@Override
				public void importDocument(final InputStream inputStream) throws IOException {
					try {
						setDocumentAndLocation(loader.loadDocument(inputStream), null);
					} catch (final Exception e) {
						throw new IOException(e);
					}
				}
			}).collect(Collectors.toList());
		}
	};

	public IDocumentStorage<File> getDocumentStorage() {
		return documentStorage;
	}

	private final Collection<IDocumentLoader<LatLongs>> documentLoaders = Arrays.asList();

	public Iterable<IDocumentLoader<LatLongs>> getDocumentLoaders() {
		return documentLoaders;
	}
}
