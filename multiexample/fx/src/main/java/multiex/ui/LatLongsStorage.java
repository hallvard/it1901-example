package multiex.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import fxutil.doc.AbstractDocumentStorageImpl;
import fxutil.doc.IDocumentImporter;
import fxutil.doc.IDocumentStorage;
import multiex.core.LatLong;
import multiex.core.LatLongs;
import multiex.json.LatLongDeserializer;
import multiex.json.LatLongSerializer;
import multiex.json.LatLongsDeserializer;
import multiex.json.LatLongsSerializer;

public class LatLongsStorage extends AbstractDocumentStorageImpl<LatLongs, File> implements IDocumentStorage<File> {

	private LatLongs latLongs;

	private final ObjectMapper objectMapper;

	public LatLongsStorage() {
		final SimpleModule module = new SimpleModule();
		module.addSerializer(LatLong.class, new LatLongSerializer());
		module.addSerializer(LatLongs.class, new LatLongsSerializer());
		module.addDeserializer(LatLong.class, new LatLongDeserializer());
		module.addDeserializer(LatLongs.class, new LatLongsDeserializer());
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(module);
	}

	@Override
	public LatLongs loadDocument(final InputStream inputStream) throws Exception {
		return objectMapper.readValue(inputStream, LatLongs.class);
	}

	@Override
	public void saveDocument(final LatLongs document, final File documentLocation) throws Exception {
		objectMapper.writeValue(new FileOutputStream(documentLocation, false), document);
	}

	@Override
	public Collection<IDocumentImporter> getDocumentImporters() {
		return Collections.emptyList();
	}

	@Override
	protected LatLongs getDocument() {
		return latLongs;
	}

	@Override
	protected void setDocument(final LatLongs document) {
		this.latLongs = document;
	}

	@Override
	protected LatLongs createDocument() {
		return new LatLongs();
	}
}
