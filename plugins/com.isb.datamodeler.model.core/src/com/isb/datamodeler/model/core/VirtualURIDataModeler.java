package com.isb.datamodeler.model.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;

/**
 * Creación de URI's virtuales para DataModeler, es una copia
 * del VirtualURI que existe en VegaModeler (No podemos tener
 * dependencias.)
 */
public class VirtualURIDataModeler extends URIHandlerImpl{
		
		private Map<URI, ByteArrayOutputStream> _instances = Collections.synchronizedMap(new WeakHashMap<URI, ByteArrayOutputStream>());
		
		public static final String VIRTUAL_URI_SCHEMA = "dmvirtual";

		/* (non-Javadoc)
		 * @see org.eclipse.emf.ecore.resource.impl.URIHandlerImpl#canHandle(org.eclipse.emf.common.util.URI)
		 */
		@Override
		public boolean canHandle(URI uri) {
			return VIRTUAL_URI_SCHEMA.equals(uri.scheme());
		}

		/**
		 * Default constructor
		 */
		public VirtualURIDataModeler() {
			super();
			_instances = Collections.synchronizedMap(new HashMap<URI, ByteArrayOutputStream>());
		}

		/* (non-Javadoc)
		 * @see org.eclipse.emf.ecore.resource.impl.URIHandlerImpl#createOutputStream(org.eclipse.emf.common.util.URI, java.util.Map)
		 */
		@Override
		public OutputStream createOutputStream(URI uri, Map<?, ?> options) throws IOException
		{
			return getBaos(uri);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.emf.ecore.resource.impl.URIHandlerImpl#createInputStream(org.eclipse.emf.common.util.URI, java.util.Map)
		 */
		@Override
		public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException
		{
			return new ByteArrayInputStream(getBaos(uri).toByteArray());
		}

		/* (non-Javadoc)
		 * @see org.eclipse.emf.ecore.resource.impl.URIHandlerImpl#delete(org.eclipse.emf.common.util.URI, java.util.Map)
		 */
		@Override
		public void delete(URI uri, Map<?, ?> options) throws IOException
		{
			_instances.remove(uri);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.emf.ecore.resource.impl.URIHandlerImpl#exists(org.eclipse.emf.common.util.URI, java.util.Map)
		 */
		@Override
		public boolean exists(URI uri, Map<?, ?> options)
		{
			return _instances.containsKey(uri);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.emf.ecore.resource.impl.URIHandlerImpl#getAttributes(org.eclipse.emf.common.util.URI, java.util.Map)
		 */
		@Override
		public Map<String, ?> getAttributes(URI uri, Map<?, ?> options)
		{
			return Collections.emptyMap();
		}

		/* (non-Javadoc)
		 * @see org.eclipse.emf.ecore.resource.impl.URIHandlerImpl#setAttributes(org.eclipse.emf.common.util.URI, java.util.Map, java.util.Map)
		 */
		@Override
		public void setAttributes(URI uri, Map<String, ?> attributes, Map<?, ?> options) throws IOException
		{
			return;
		}

		private ByteArrayOutputStream getBaos(URI uri) {
			ByteArrayOutputStream baos = _instances.get(uri);
			if (baos == null)
			{
				_instances.put(uri, new ByteArrayOutputStream());
				baos = _instances.get(uri);
			}
			else
			{
				baos.reset();
			}
			return baos;
		}

}
