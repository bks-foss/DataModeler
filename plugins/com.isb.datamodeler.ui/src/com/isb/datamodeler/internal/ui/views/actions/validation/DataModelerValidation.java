package com.isb.datamodeler.internal.ui.views.actions.validation;

import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;

import com.isb.datamodeler.diagram.part.DatamodelerDiagramEditorPlugin;
import com.isb.datamodeler.model.core.validation.DataModelDiagnostic;
import com.isb.datamodeler.schema.ESQLObject;

public class DataModelerValidation {
	
	public static final String MARKER_TYPE = 
		DatamodelerDiagramEditorPlugin.ID + ".diagnostic"; //$NON-NLS-1$
	
	public static void createDMMarker(DataModelDiagnostic dataModelDiagnostic , IResource resource)
	{
		// Creamos a partir del diagnostic el marcador de error del elemento y se los asociamos al fichero .vm
		List data = dataModelDiagnostic.getData();
		if (data != null && !data.isEmpty()
				&& data.get(0) instanceof ESQLObject) {
			ESQLObject element = (ESQLObject) data.get(0);
		
			addDMMarker(resource, element.getId(),
					".dm", dataModelDiagnostic.getMessage(), dataModelDiagnostic.getSeverity() , dataModelDiagnostic.getValidatorId()); 
		}
		
	}
	public static IMarker addDMMarker(IResource resource, String elementId,
			String location, String message, int statusSeverity , String validatorId) {
		IMarker marker = null;
		try {
			marker = resource.createMarker(MARKER_TYPE);
			marker.setAttribute(DataModelDiagnostic.ATTR_MARKER_VALIDATOR_ID, validatorId);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.LOCATION, location);
			marker.setAttribute(
							org.eclipse.gmf.runtime.common.ui.resources.IMarker.ELEMENT_ID,
							elementId);
			int markerSeverity = IMarker.SEVERITY_INFO;
			if (statusSeverity == IStatus.WARNING) {
				markerSeverity = IMarker.SEVERITY_WARNING;
			} 
			else if (statusSeverity == IStatus.ERROR
					|| statusSeverity == IStatus.CANCEL) {
				markerSeverity = IMarker.SEVERITY_ERROR;
			}
			else if(statusSeverity == IMarker.SEVERITY_WARNING)
			{
				markerSeverity = IMarker.SEVERITY_WARNING;
			}
			else if(statusSeverity == IMarker.SEVERITY_ERROR)
			{
				markerSeverity = IMarker.SEVERITY_ERROR;
			}
			marker.setAttribute(IMarker.SEVERITY, markerSeverity);
		} catch (CoreException e) {
			DatamodelerDiagramEditorPlugin.getInstance().logError(
					"Failed to create validation marker", e); //$NON-NLS-1$
		}
		return marker;
	}
	public static void validate(ESQLObject sqlObject)
	{
		// Borramos los marcadores asociados al elemento
		IResource resource = WorkspaceSynchronizer
			.getFile(sqlObject.eResource());
		if (resource == null || !resource.exists()) {
			return;
		}
		IMarker[] markers = null;
		try {
			markers = resource.findMarkers(MARKER_TYPE, true,
					IResource.DEPTH_INFINITE);

			if(markers != null)
			{
				for (int i = 0; i < markers.length; i++) {
					IMarker marker = markers[i];
					String attribute = marker.getAttribute(
									org.eclipse.gmf.runtime.common.ui.resources.IMarker.ELEMENT_ID,
									""); //$NON-NLS-1$
					if (attribute.equals(sqlObject.getId())) {
						marker.delete();
					}
				}
			}
		} catch (CoreException e) {
			DatamodelerDiagramEditorPlugin.getInstance().logError(
					"Validation markers refresh failure", e); //$NON-NLS-1$
		}
		// Validamos el elemento
		Diagnostic diagnostic = Diagnostician.INSTANCE.validate(sqlObject);
		for(Diagnostic childDiagnostic : diagnostic.getChildren())
		{
			if(!(childDiagnostic instanceof DataModelDiagnostic))
				continue;
			
			DataModelDiagnostic vegaDiagnostic = (DataModelDiagnostic)childDiagnostic;
			createDMMarker(vegaDiagnostic, resource);
		}
		
	}
}
