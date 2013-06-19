package com.isb.datamodeler.compare.core;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ui.team.AbstractResolvingURIConverter;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.team.core.history.IFileRevision;

/**
 * 
 * @author Alfonso
 *
 */
public class DataModelerRevisionedURIConverter extends AbstractResolvingURIConverter {

	private final IFileRevision baseRevision;
	
	public DataModelerRevisionedURIConverter (IFileRevision element) {
		baseRevision = element;
	}
	
	@Override
	public URI resolve(URI uri) throws CoreException {
		URI deresolvedURI = uri;
		// We'll have to change the EMF URI to find the IFile it points to
		final IStorage storage = baseRevision.getStorage(null);
		if (uri.segmentCount() > 0 && uri.isRelative()) {
			// Current revision, yet the proxy could point to a file that has changed since.
			if (storage instanceof IFile) {
				final IFile file = (IFile)storage;
				deresolvedURI = uri.resolve(URI.createURI(file.getLocationURI().toString()));
			} else {
				IResource stateFile = (IResource)storage.getAdapter(IResource.class);
				if (stateFile == null) {
					stateFile = (IResource)Platform.getAdapterManager().getAdapter(storage,
							IResource.class);
				}
				if (stateFile == null) {
					stateFile = EcorePlugin.getWorkspaceRoot().findMember(storage.getFullPath());
				}
				if (stateFile != null) {
					deresolvedURI = uri.resolve(URI.createURI(stateFile.getLocationURI().toString()));
				}
			}
		}
		deresolvedURI = URI.createPlatformResourceURI(deresolvedURI.deresolve(
				URI.createURI(EcorePlugin.getWorkspaceRoot().getLocationURI().toString() + '/'))
				.toString());
		return deresolvedURI;
	}

}
