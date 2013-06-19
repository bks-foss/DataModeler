package com.isb.datamodeler.internal.ui.views.actions.validation;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gmf.runtime.diagram.ui.OffscreenEditPartFactory;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.ui.IWorkbenchPage;

import com.isb.datamodeler.diagram.part.ValidateAction;
import com.isb.datamodeler.diagram.providers.DatamodelerMarkerNavigationProvider;
import com.isb.datamodeler.diagram.providers.DatamodelerValidationProvider;

public class DMValidateAction extends ValidateAction {

	public DMValidateAction(IWorkbenchPage page) {
		super(page);
	}

	public static void runNonUIValidation(View view, boolean deleteMarkers) {
		DiagramEditPart diagramEditPart = OffscreenEditPartFactory
				.getInstance().createDiagramEditPart(view.getDiagram());
		runValidation(diagramEditPart, view , deleteMarkers);
	}

	public static void runValidation(DiagramEditPart diagramEditPart, View view , boolean deleteMarkers) {
		final DiagramEditPart fpart = diagramEditPart;
		final View fview = view;
		final boolean fdeleteMarkers = deleteMarkers;
		TransactionalEditingDomain txDomain = TransactionUtil
				.getEditingDomain(view);
		DatamodelerValidationProvider.runWithConstraints(txDomain,
				new Runnable() {

					public void run() {
						validate(fpart, fview , fdeleteMarkers);
					}
				});
	}

	private static void validate(DiagramEditPart diagramEditPart, View view, boolean deleteMarkers) {
		IFile target = view.eResource() != null ? WorkspaceSynchronizer
				.getFile(view.eResource()) : null;
		if (target != null && deleteMarkers) {
			DatamodelerMarkerNavigationProvider.deleteMarkers(target);
		}
		Diagnostic diagnostic = runEMFValidator(view);
		createMarkers(target, diagnostic, diagramEditPart);
		IBatchValidator validator = (IBatchValidator) ModelValidationService
				.getInstance().newValidator(EvaluationMode.BATCH);
		validator.setIncludeLiveConstraints(true);
		if (view.isSetElement() && view.getElement() != null) {
			IStatus status = validator.validate(view.getElement());
			createMarkers(target, status, diagramEditPart);
		}
	}
}
