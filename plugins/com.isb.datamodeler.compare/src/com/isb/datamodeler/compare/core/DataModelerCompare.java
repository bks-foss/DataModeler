package com.isb.datamodeler.compare.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.ResourceNode;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.MatchOptions;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.ui.EMFCompareUIMessages;
import org.eclipse.emf.compare.ui.EMFCompareUIPlugin;
import org.eclipse.emf.compare.ui.TypedElementWrapper;
import org.eclipse.emf.compare.ui.team.AbstractTeamHandler;
import org.eclipse.emf.compare.util.EclipseModelUtils;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.isb.datamodeler.compare.merge.ConfigureMergeButtons;
import com.isb.datamodeler.compare.messages.Messages;
import com.isb.datamodeler.datatypes.EPrimitiveDataType;
import com.isb.datamodeler.datatypes.ESQLDataType;
import com.isb.datamodeler.datatypes.impl.EPredefinedDataTypeImpl;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.model.core.VirtualURIDataModeler;
import com.isb.datamodeler.schema.EDatabase;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.ETable;
import com.isb.datamodeler.ui.project.EProject;
import com.isb.datamodeler.ui.project.EProjectFactory;

/**
 * EMF Compare y DataModeler
 * 
 * <br> Esta clase es la encargada de realizar el macheo específico para DataModeler y lanzar el motor de 
 * diferencias (espífico para DataModeler; no compara por ejemplo el atributo id al igual que algunos otros 
 * ver <code>DataModelerDiffEngine</code> )  </br>
 * 
 *  En todos los casos hay que adaptar la entrada de comparación a emfCompare , 
 *  hay que cargar los modelos o bien crearse recurso virtual asociado a los EProject.
 * 
 * @author Alfonso
 *
 */
public class DataModelerCompare {
	
	/** Representa el proyecto del modelo cargado para comparar.*/
	private EObject _leftDataModelerEProject = null;
	private EObject _rightDataModelerEProject = null;
	
	/** ResourceSet. Sólo hace falta uno, pero en el ejemplo de emfCompare utiliza los dos*/
	private ResourceSet _res1 = null;
	private ResourceSet _res2 = null;
	
	/** Carga de los recursos de comparación*/
	private ITypedElement rightElement;
	private ITypedElement leftElement;
	private boolean loadingSucceeded;
	private boolean remote;
	private Resource leftResource;
	private Resource rightResource;
	private CompareConfiguration compareConfiguration; 
	
	/** */
	protected ComparisonSnapshot comparisonResult;
	private static final Set<TeamHandlerDescriptor> CACHED_HANDLERS = new LinkedHashSet<TeamHandlerDescriptor>();	
	public static final String DATA_MODELER_EXTENSION_FILE = ".dm"; //$NON-NLS-1$ 
	public static final String DATA_MODELER_EXTENSION = "dm"; //$NON-NLS-1$ 
	public static final String DATA_MODELER_VIRTUAL_EXTESNION = "dmvirtual"; //$NON-NLS-1$ 
		
	/** */
	public DataModelerCompare (CompareConfiguration configuration){
		compareConfiguration = configuration;		
	}
		
	/** 
	 * Constructor por defecto
	 */
	public DataModelerCompare (){		
	}
		
	/**
	 * 
	 * @param project
	 */
	public void setLeftDataModelerProject (EProject project) {		
		_leftDataModelerEProject = project;
	}
	
	/**
	 * @param project
	 */
	public void setRightDataModelerProject (EProject project) {
		_rightDataModelerEProject = project;
	}
	
	/**
	 * 
	 * @param project
	 */
	public void prepareLeftWizardCompare(boolean refresh, EProject project) {
		
		if (refresh) {
			_leftDataModelerEProject = project;
			return;
		}
		
		if (_res1 == null)
			_res1 = new ResourceSetImpl();
		
		try {
			IResource resource = EclipseModelUtils.findIResource(project.eResource());
			_leftDataModelerEProject = EclipseModelUtils.load(resource.getFullPath(), _res1);				
			
		} catch (IOException e) {			
			e.printStackTrace();
		}	
	}
	
	/**
	 * Crea un recurso virtual asociado al EProject para realizar la comparación
	 * para la importación de tablas.
	 * 
	 */
	public void prepareRightVirtualCompare (EProject rightModel) { 		
		
		if (_res2 == null)
			_res2 = new ResourceSetImpl();				
						
		_rightDataModelerEProject = createVirtualElement(rightModel ,"R", "modelovirtual", _res2); //$NON-NLS-1$ //$NON-NLS-2$	
		remote = true;
			
	}
	
	/**
	 * Comparación por defecto
	 * @param leftProject
	 * @param rightProject
	 * @return
	 */
	public ComparisonResourceSnapshot compare (EProject leftProject, EProject rightProject)
	{
		_leftDataModelerEProject = leftProject;
		_rightDataModelerEProject = rightProject;		
		return compare(false, true);
	}
	
	/**
	 * Comparación por defecto
	 * @param leftProject
	 * @param rightProject
	 * @return
	 */
	public ComparisonResourceSnapshot compare (EProject leftProject, EProject rightProject, boolean notShowifNotDifference)
	{
		_leftDataModelerEProject = leftProject;
		_rightDataModelerEProject = rightProject;		
		return compare(false, notShowifNotDifference);
	}
	
	
	/**
	 * Acción de comparación. Llama al motor de macheo y de diferencias y 
	 * posteriormente pasa el modelo de diferencias al editor por defecto 
	 * para que muestre las diferencias del modelo.
	 */	
	public ComparisonResourceSnapshot compare(boolean refresh, boolean notShowifNotDifference) {	
			
		ComparisonResourceSnapshot snapshot = null;
		
		if (!refresh && (leftResource != null && leftResource.getContents() != null))
			_leftDataModelerEProject = leftResource.getContents().get(0);
		
		if (rightResource != null && rightResource.getContents() != null)
			_rightDataModelerEProject = rightResource.getContents().get(0);
		
		assert _leftDataModelerEProject == null;
		assert _rightDataModelerEProject == null;
		
		try {		
			
			checkResourcesLoaded();

			// Creates the match then the diff model for those two models -->CASO 1 con el genérico
			//System.out.println("Matching models.\n"); //$NON-NLS-1$
			Map<String,Object> matchOption = new HashMap<String, Object> ();
			
			matchOption.put(MatchOptions.OPTION_IGNORE_ID, true);
			matchOption.put(MatchOptions.OPTION_IGNORE_XMI_ID, true);		
			
			// Scope para el macheo para sólo comparar el modelo no los diagramas que se encuentran en el recurso.
			matchOption.put(MatchOptions.OPTION_MATCH_SCOPE_PROVIDER, new DataModelerScopeProvider());
			
			ExtendModelUtils.prepareFK((EProject) _leftDataModelerEProject);
			ExtendModelUtils.prepareFK((EProject) _rightDataModelerEProject);
			
			final MatchModel match = MatchService.doMatch(_leftDataModelerEProject, _rightDataModelerEProject, matchOption);
			
			ConfigureMergeButtons.getInstance().initialize((EProject)_leftDataModelerEProject, (EProject)_rightDataModelerEProject);
									
			//Creates the difference with the match model
			//System.out.println("Differencing models.\n"); //$NON-NLS-1$
			final DiffModel diff = DiffService.doDiff(match, false);				

			// Prints the results	[DEBUG]	TODO BORRAR ESTO AL FINAL O COMENTARLO
			System.out.println("MatchModel :\n"); //$NON-NLS-1$
			//System.out.println(ModelUtils.serialize(match));
			System.out.println("DiffModel :\n"); //$NON-NLS-1$
			//System.out.println(ModelUtils.serialize(diff)); 
			
			// En el caso que no haya diferencias cerramos. Mostrando un mensaje indicando que los modelos son identicos
			// TODO: [Pasar al messages.properties.] Este diálogo es herencia de los comparadores Vega para que el resultado
			// al no contener diferencias no es un editor vacío.
			if (notShowifNotDifference && diff.getDifferences().size() == 0) {
				MessageDialog.openInformation(null, Messages.bind("datamodeler.compare.docompare.title"), Messages.bind("datamodeler.compare.docompare.text"));
				return null;
			}
			
			// Creación de la foto de diferencias
			snapshot = DiffFactory.eINSTANCE.createComparisonResourceSnapshot();
			snapshot.setDate(Calendar.getInstance().getTime());
			snapshot.setMatch(match);
			snapshot.setDiff(diff);
			comparisonResult = snapshot;
											
		} catch (final InterruptedException e) {
			e.printStackTrace();			
//		} catch (IOException e) {		
//			e.printStackTrace();
		}
		
		return snapshot;
	}
	
	private void checkResourcesLoaded() {				
		boolean leftNotLoaded = true;
		boolean rightNotLoaded = true;
		
		EList<Resource> resources = DataModelerUtils.getDataModelerEditingDomain().getResourceSet().getResources();
		for (Resource resource : resources) {
			if (resource.getURI().toString().contains(((EProject)_leftDataModelerEProject).getName()))
				leftNotLoaded = false;
			else if (!remote && rightResource != null && !rightResource.getURI().toString().contains(DATA_MODELER_VIRTUAL_EXTESNION) 
					&& resource.getURI().toString().contains(((EProject)_rightDataModelerEProject).getName()))
				rightNotLoaded = false;
		}
		
		if (leftNotLoaded)
		{
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IFile modelFile = root.getFile(root.getProject(((EProject)_leftDataModelerEProject).getName()).
					getFullPath().append(((EProject)_leftDataModelerEProject).getName()+ DATA_MODELER_EXTENSION_FILE)); //$NON-NLS-1$

			DataModelerUtils.getDataModelerEditingDomain().getResourceSet().createResource(
					URI.createPlatformResourceURI(modelFile.getFullPath().toString(), true));			
		}
		
		if (rightNotLoaded && !remote && rightResource != null && rightResource.getURI().toString().contains("platform"))
		{
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IFile modelFile = root.getFile(root.getProject(((EProject)_rightDataModelerEProject).getName())
					.getFullPath().append(((EProject)_rightDataModelerEProject).getName() + DATA_MODELER_EXTENSION_FILE)); //$NON-NLS-1$

			DataModelerUtils.getDataModelerEditingDomain().getResourceSet().createResource(
					URI.createPlatformResourceURI(modelFile.getFullPath().toString(), true));			
		}
		
	}

	/**
	 * Crea un modelo virtual del DataModeler para comparar. Completamente necesario
	 * si queremos realizar la comparación del diagrama y que aparezca dentro de los esquemas
	 * asociados.
	 * 
	 * @param originalSchemas
	 * @param virtualModelName
	 * @param leftOrRight
	 * @param res
	 * @return
	 */
	private EObject createVirtualElement (EProject originalProject ,String virtualModelName, String leftOrRight, ResourceSet res)
	{
		URI modelGraph = URI.createHierarchicalURI(VirtualURIDataModeler.VIRTUAL_URI_SCHEMA,
				null,
				null,
				new String[] { virtualModelName + leftOrRight + DATA_MODELER_EXTENSION_FILE},
				null,
				null);
			
		res.getURIConverter().getURIHandlers().add(0, new VirtualURIDataModeler());		
		Resource resource = res.createResource(modelGraph);	   
		EProject project = EProjectFactory.eINSTANCE.createProject();
		
		// Agregamos una copia de los tipos de datos al proyecto original.
		EDatabase originalDB = originalProject.getDatabase();
		EDatabase copyDB = EcoreUtil.copy(originalDB);
		project.setDatabase(copyDB);
	    
		// Añadimos el proyecto al recurso
		resource.getContents().add(project);	
		
		// Agregamos una copia de los esquemas originales.		
		List<ESchema> copySchema =  (List<ESchema>) EcoreUtil.copyAll(originalProject.getSchemas());
		for (ESchema schema : copySchema) {
			schema.setDatabase(copyDB);
			EList<ETable> tables = schema.getTables();
			for (ETable table : tables) {
				EList<EColumn>columns = table.getColumns();
				for (EColumn column : columns) {
					
					EPrimitiveDataType columnDataType = findCopyPrimitiveType(column.getPrimitiveType(), copyDB);
					column.setPrimitiveType(columnDataType);
					EPrimitiveDataType containedType = findCopyContainedType( column.getContainedType(), copyDB);
					EPredefinedDataTypeImpl predefinedType = (EPredefinedDataTypeImpl) column.getContainedType();
					predefinedType.setPrimitiveType(containedType);
				}				
			}			
		}
		
		project.getSchemas().addAll(copySchema);	
	    	   
	    return resource.getContents().get(0);	
	}
	
	/**
	 * 
	 * @param original
	 * @param db
	 * @return
	 */
	private EPrimitiveDataType findCopyPrimitiveType (EPrimitiveDataType original, EDatabase db)
	{
		EList<EPrimitiveDataType> types = db.getPrimitiveDataTypes();
		for (EPrimitiveDataType ePrimitiveDataType : types) {
			if (ePrimitiveDataType.getName().equals(original.getName()))
				return ePrimitiveDataType;
		}
		return null;
	}
	
	/**
	 * 
	 * @param esqlDataType
	 * @param db
	 * @return
	 */
	private EPrimitiveDataType findCopyContainedType (ESQLDataType esqlDataType, EDatabase db)
	{
		EList<EPrimitiveDataType> types = db.getPrimitiveDataTypes();
		for (EPrimitiveDataType ePrimitiveDataType : types) {
			if (ePrimitiveDataType.getName().equals(esqlDataType.getName()))
				return ePrimitiveDataType;
		}
		return null;
	}
	
	/**
	 * Carga de recursos para la acción de datamodeler: Comparación via CVS.
	 * @param leftCompareResource
	 * @param rightStorage
	 * @param rightName
	 * @return
	 */
	public boolean loadCVSResources (IResource leftCompareResource, InputStream rightStorage, String rightName) {
		try {
			
			 leftResource =  EclipseModelUtils.load(leftCompareResource.getFullPath(), new ResourceSetImpl()).eResource();						 
			 rightResource = ModelUtils.load(rightStorage, rightName, new ResourceSetImpl()).eResource();
			 remote = true;
		} catch (IOException e) {		
			e.printStackTrace();
		}
		
		
		return true;
	}
	
	/**
	 * Realiza la carga del modelo a partir de lo IProject que nos llega de la acción.
	 * Este método es para la comparación en local desde la vista de DataModeler.
	 */	
	public void loadResources(IProject leftProject, IProject rightProject) {
		
		try {
										
			_res1 = new ResourceSetImpl();
			_res2 = new ResourceSetImpl();
			
			String leftResourceName = "";
			String rightResourceName = "";
			
			try {
				IResource[] leftResources = leftProject.members();
				for (IResource leftResource : leftResources) {
					if (leftResource.getFileExtension().equals(DATA_MODELER_EXTENSION)) {
						leftResourceName = leftResource.getName();
						break;
					}					
				}
				
				IResource[] rightResources = rightProject.members();
				for (IResource rightResource : rightResources) {
					if (rightResource.getFileExtension().equals(DATA_MODELER_EXTENSION)) {
						rightResourceName = rightResource.getName();
						break;
					}					
				}
			} catch (CoreException e) {				
				e.printStackTrace();
			}
						
			IPath path1 = leftProject.getFullPath().append("/" + leftResourceName);
			IPath path2 = rightProject.getFullPath().append("/" + rightResourceName);						
					
			final EList<EObject> leftDataModelerRoot = ExtendModelUtils.load(new File(path1.toOSString()), _res1);
			final EList<EObject> rightDataModelerRoot = ExtendModelUtils.load(new File(path2.toOSString()),_res2);						
			
			if (leftDataModelerRoot == null || rightDataModelerRoot ==null)
				return;
			
			_leftDataModelerEProject = (EProject)leftDataModelerRoot.get(0);
			_rightDataModelerEProject = (EProject)rightDataModelerRoot.get(0);
		
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
	}
	
	/**
	 * LoadResources a través de un ICompareInput.
	 * @param input
	 * @return
	 */
	public boolean loadResources(ICompareInput input) {
		if (leftElement != input.getLeft()
				|| rightElement != input.getRight()) {
			clear();
			leftElement = input.getLeft();
			rightElement = input.getRight();			

			try {
				// Comparación en local
				loadingSucceeded = handleLocalResources(leftElement, rightElement);
				// Para diferentes tipos de comparación en remoto tenemos AbstractsTeamHandlers.
				if (!loadingSucceeded) {
					final Iterator<TeamHandlerDescriptor> handlerDescriptorIterator = CACHED_HANDLERS
							.iterator();
					while (handlerDescriptorIterator.hasNext()) {
						final AbstractTeamHandler handler = handlerDescriptorIterator.next()
								.getHandlerInstance();
						loadingSucceeded |= handler.loadResources(input);
						if (loadingSucceeded) {
							//comparisonHandler = handler;
							break;
						}
					}
				}
				// Modo genérico... (por norma general tenemos que pasar por aqui).
				if (!loadingSucceeded) {
					loadingSucceeded |= handleGenericResources(leftElement, rightElement);
				}
				/*
				 * The generic handler should work for any EMF resources. If we're here, no exception has been
				 * thrown and loading ended accurately
				 */
				loadingSucceeded = true;
			} catch (final IOException e) {
				final String dialogMessage;
				
				dialogMessage = EMFCompareUIMessages
						.getString("ModelComparator.ResourceLoadingFailureGanymede"); //$NON-NLS-1$
				
				final Dialog dialog = new CompareErrorDialog(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell(), "Comparison failed", dialogMessage, //$NON-NLS-1$
						new Status(IStatus.ERROR, EMFCompareUIPlugin.PLUGIN_ID, e.getMessage(), e));
				final int buttonPressed = dialog.open();
				if (buttonPressed == CompareErrorDialog.COMPARE_AS_TEXT_ID) {
					final Set<Object> set = new HashSet<Object>();
					final CompareEditorInput editorInput = (CompareEditorInput)compareConfiguration
							.getContainer();
					// value of the 3.3 and 3.4 "ICompareAsText.PROP_TEXT_INPUT"
					compareConfiguration.setProperty("org.eclipse.compare.TextInputs", set); //$NON-NLS-1$
					set.add(editorInput);
					set.add(editorInput.getCompareResult());
					CompareUI.openCompareEditorOnPage(editorInput, null);
				}
			} catch (final CoreException e) {
				EMFComparePlugin.log(e.getStatus());
			}
		} else {
			// input was the same as the last
		}
		return loadingSucceeded;
	}
	
	/**
	 * Carga recursos locales.
	 * @param left
	 * @param right
	 * @return
	 * @throws IOException
	 */
	private boolean handleLocalResources(ITypedElement left, ITypedElement right)
	throws IOException {
	if (left instanceof ResourceNode && right instanceof ResourceNode) {
		leftResource = EclipseModelUtils.load(((ResourceNode)left).getResource().getFullPath(),
				new ResourceSetImpl()).eResource();
		rightResource = EclipseModelUtils.load(((ResourceNode)right).getResource().getFullPath(),
				new ResourceSetImpl()).eResource();	
		return true;
	} else if (left instanceof TypedElementWrapper && right instanceof TypedElementWrapper)
	{
		Object leftObject = ((TypedElementWrapper)left).getObject();
		Object rightObject = ((TypedElementWrapper)right).getObject();
		if (leftObject != null && rightObject != null && 
				leftObject instanceof EProject && rightObject instanceof EProject)
		{
			leftResource = ((EProject) leftObject).eResource();
			rightResource = ((EProject) rightObject).eResource();
		}
		
	}
	return false;
}
	
	/**
	 * This generic handler should be able to load resources passed by any team plug-in. Using this handler
	 * instead of the subversive specific handler in plugin org.eclipse.emf.compare.team.subversive will
	 * result in unsaveable merge operations for example. Users of specific team plugins should write their
	 * own handler to avoid going through this method in order for their files to be saveable (thus
	 * mergeable).
	 * 
	 * @param left
	 *            Handler of the left compared model.
	 * @param right
	 *            Handler of the right compared model.
	 * @param ancestor
	 *            Handler of these two models' common ancestor.
	 * @return <code>true</code> If all resources have been loaded by this handler, <code>false</code>
	 *         otherwise.
	 * @throws IOException
	 *             Thrown if the right resource cannot be loaded.
	 * @throws CoreException
	 *             Thrown if exceptions occur when loading the remote resources (left and ancestor).
	 */
	public boolean handleGenericResources(ITypedElement left, ITypedElement right)
			throws IOException, CoreException {
		if (left instanceof ResourceNode && right instanceof IStreamContentAccessor) {
			if (((ResourceNode)left).getResource().isAccessible()) {
				leftResource = EclipseModelUtils.load(((ResourceNode)left).getResource().getFullPath(),
						new ResourceSetImpl()).eResource();
			} else {
				leftResource = ModelUtils.createResource(URI.createPlatformResourceURI(((ResourceNode)left)
						.getResource().getFullPath().toOSString(), true));
				// resource has been deleted. We set it as "remote" to disable merge facilities				
			}
			try {
				rightResource = ModelUtils.load(((IStreamContentAccessor)right).getContents(),
						right.getName(), new ResourceSetImpl()).eResource();
			} catch (final IOException e) {
				// We couldn't load the remote resource. Considers it has been added to the repository
				rightResource = ModelUtils.createResource(URI.createURI(right.getName()));
				// Set the left as remote to disable merge facilities
				//leftIsRemote = true;
			}
			//rightIsRemote = true;			
			return true;
		}
		

		/*
		 * We should never be here. There always is a local resource when comparing with CVS. this code will
		 * be executed if we couldn't manage to handle this local resource as such. Though the resource will
		 * be loaded thanks to this generic handler, note that it will not be saveable.
		 */
		boolean result = false;
		if (left instanceof IStreamContentAccessor && right instanceof IStreamContentAccessor) {
			leftResource = ModelUtils.load(((IStreamContentAccessor)left).getContents(), left.getName(),
					new ResourceSetImpl()).eResource();
			rightResource = ModelUtils.load(((IStreamContentAccessor)right).getContents(), right.getName(),
					new ResourceSetImpl()).eResource();			
			result = true;
		}
		return result;
	}


	/**
	 * Clears all loaded resources from the resource set.
	 */
	private void clear() {
		clearResourceSet(leftResource, rightResource);
		leftResource = null;
		rightResource = null;		
		comparisonResult = null;		
	}
	
	/**
	 * This will empty the resourceSet of the given <tt>resource</tt>.
	 * 
	 * @param resource
	 *            Resource that is to be cleared.
	 */
	private void clearResourceSet(Resource... resource) {
		for (int i = 0; i < resource.length; i++) {
			if (resource[i] == null) {
				continue;
			}
			final ResourceSet resourceSet = resource[i].getResourceSet();
			final Iterator<Resource> resourcesIterator = resourceSet.getResources().iterator();
			while (resourcesIterator.hasNext()) {
				resourcesIterator.next().unload();
			}
			resourceSet.getResources().clear();
		}
	}
	
	static final class CompareErrorDialog extends ErrorDialog {
		/** ID of the "Compare As Text" button. Not intended to be either subclassed or accessed externally. */
		static final int COMPARE_AS_TEXT_ID = IDialogConstants.CLIENT_ID + 1;

		/**
		 * Delegates to the super constructor.
		 * 
		 * @param parentShell
		 *            Parent Shell of this viewer.
		 * @param dialogTitle
		 *            Title of this error dialog.
		 * @param dialogMessage
		 *            Message to display to the user.
		 * @param status
		 *            The error to show to the user.
		 */
		public CompareErrorDialog(Shell parentShell, String dialogTitle, String dialogMessage, IStatus status) {
			super(parentShell, dialogTitle, dialogMessage, status, IStatus.OK | IStatus.INFO
					| IStatus.WARNING | IStatus.ERROR);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.dialogs.ErrorDialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
		 */
		@Override
		protected void createButtonsForButtonBar(Composite parent) {
			/*
			 * Using the CANCEL_ID for an "OK" button in order to inherit the cancel behavior but be coherent
			 * with the error message.
			 */
			createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.OK_LABEL, true);			
			createDetailsButton(parent);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.dialogs.ErrorDialog#buttonPressed(int)
		 */
		@Override
		protected void buttonPressed(int id) {
			if (id == COMPARE_AS_TEXT_ID) {
				setReturnCode(COMPARE_AS_TEXT_ID);
				close();
			} else {
				super.buttonPressed(id);
			}
		}
	}
	
	private static final class TeamHandlerDescriptor {
		/**
		 * Name of the extension point attribute corresponding to the handler's class.
		 */
		private static final String ATTRIBUTE_HANDLER_CLASS = "class"; //$NON-NLS-1$

		/**
		 * Keeps a reference to the configuration element that describes this handler.
		 */
		private final IConfigurationElement element;

		/** This descriptor's wrapped {@link AbstractTeamHandler handler}. */
		private AbstractTeamHandler handler;

		/**
		 * Constructs a new descriptor from an IConfigurationElement.
		 * 
		 * @param configuration
		 *            Configuration of the team handler.
		 */
		public TeamHandlerDescriptor(IConfigurationElement configuration) {
			element = configuration;
		}

		/**
		 * Returns an instance of the described handler.
		 * 
		 * @return Instance of the handler.
		 */
		public AbstractTeamHandler getHandlerInstance() {
			if (handler == null) {
				try {
					handler = (AbstractTeamHandler)element.createExecutableExtension(ATTRIBUTE_HANDLER_CLASS);
				} catch (final CoreException e) {
					EMFComparePlugin.log(e, true);
				}
			}
			return handler;
		}

		/**
		 * Returns the class of the wrapped AbstractTeamHandler. This is only used internally to set the
		 * RevisionComparisonHandler last.
		 * 
		 * @return The class of the wrapped AbstractTeamHandler.
		 */
		String getHandlerClass() {
			return element.getAttribute(ATTRIBUTE_HANDLER_CLASS);
		}
	}
	
	
}
