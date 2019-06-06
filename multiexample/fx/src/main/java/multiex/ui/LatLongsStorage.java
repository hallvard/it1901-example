package multiex.ui;

import java.io.File;

import com.fasterxml.jackson.databind.module.SimpleModule;

import fxutil.doc.IDocumentStorage;
import fxutil.doc.SimpleJsonFileStorageImpl;
import multiex.core.LatLong;
import multiex.core.LatLongs;
import multiex.json.LatLongDeserializer;
import multiex.json.LatLongSerializer;
import multiex.json.LatLongsDeserializer;
import multiex.json.LatLongsSerializer;

public class LatLongsStorage extends SimpleJsonFileStorageImpl<LatLongs> implements IDocumentStorage<File> {

	public LatLongsStorage() {
		super(LatLongs.class);
	}

	@Override
	protected void configureJacksonModule(final SimpleModule module) {
		module.addSerializer(LatLong.class, new LatLongSerializer());
		module.addSerializer(LatLongs.class, new LatLongsSerializer());
		module.addDeserializer(LatLong.class, new LatLongDeserializer());
		module.addDeserializer(LatLongs.class, new LatLongsDeserializer());
	}
}
