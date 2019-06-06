package fxutil.doc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public abstract class SimpleJsonFileStorageImpl<T> extends AbstractDocumentStorageImpl<T, File> implements IDocumentStorage<File> {

	private T document;

	private final ObjectMapper objectMapper;

	private final Class<T> documentClass;

	public SimpleJsonFileStorageImpl(final Class<T> documentClass) {
		this.documentClass = documentClass;
		final SimpleModule module = new SimpleModule();
		configureJacksonModule(module);
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(module);
	}

	protected abstract void configureJacksonModule(final SimpleModule module);

	@Override
	public T loadDocument(final InputStream inputStream) throws Exception {
		return objectMapper.readValue(inputStream, documentClass);
	}

	@Override
	public void saveDocument(final T document, final File documentLocation) throws Exception {
		objectMapper.writeValue(new FileOutputStream(documentLocation, false), document);
	}

	@Override
	public Collection<IDocumentImporter> getDocumentImporters() {
		return Collections.emptyList();
	}

	@Override
	public T getDocument() {
		return document;
	}

	@Override
	public void setDocument(final T document) {
		final T oldDocument = this.document;
		this.document = document;
		fireDocumentChanged(oldDocument);
	}

	@Override
	protected T createDocument() {
		try {
			return documentClass.getDeclaredConstructor().newInstance();
		} catch (final Exception e) {
			throw new RuntimeException("Exception when instantiating " + documentClass, e);
		}
	}
}
