package com.isb.datamodeler.ui.validation.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.ETableConstraint;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.model.core.validation.AbstractDataModelerElementValidator;
import com.isb.datamodeler.model.core.validation.DataModelDiagnostic;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.EBaseTable;
import com.isb.datamodeler.tables.ETable;
import com.isb.datamodeler.ui.UtilsDataModelerUI;
import com.isb.datamodeler.ui.model.validation.Messages;
import com.isb.datamodeler.ui.project.EProject;
/**
 * Reglas de Normativa. Validacion de modelo. Validacion Opcional
 * Debería haber una relación de referencia hacia todas las tablas
 * externas al modelo.
 * 
 * @author Delia Salmerón
 *
 */
public class ExternalTablesRegulationRuleInvValidator extends
		AbstractDataModelerElementValidator {

	@Override
	public DataModelDiagnostic validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, EOperation invariant, String expression) 
	{
		if(!(eObject instanceof EProject))
			return null;
		
		EProject eProject = (EProject)eObject;
		
//		String modelerProjectName = DataModelerUtils.getDataModelerProject(schema);
//		if(modelerProjectName == null)
//			return DataModelDiagnostic.OK_INSTANCE;
//		
		IProject modelerProject = eProject.getIProject();
//		EProject eProject = UtilsDataModelerUI.findEProject(modelerProject);
		
		// Obtenemos todas las relaciones de la aplicación, para 
		// posteriormente si hay alguna tabla externa , comprobar 
		// que haya alguna relación que la referencia
		List<ETableConstraint> constraints = new ArrayList<ETableConstraint>();
		
		for(ESchema schema : eProject.getSchemas())
		{
			if(!schema.getCapability().equals(eProject.getCapability()))
				continue;
			for(ETable appTable:schema.getTables())
			{
				if(appTable instanceof EBaseTable)
				{
					EBaseTable baseTable = (EBaseTable)appTable;
					for(ETableConstraint c:baseTable.getConstraints())
					{
						if(c instanceof EForeignKey)
							constraints.add(c);
					}
				}
			}
		}

		
		// Obtenemos las tablas externas
		// Miramos si existe alguna relación con la tabla externa
		
		if(modelerProject == null)
			return DataModelDiagnostic.OK_INSTANCE;
		
		IFile file = UtilsDataModelerUI.findDataModelerFile(modelerProject);
				
		if(file==null)
			return DataModelDiagnostic.OK_INSTANCE;
		
		TransactionalEditingDomain editingDomain = DataModelerUtils.getDataModelerEditingDomain();
		
		URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
		
		DataModelDiagnostic dataModelDiagnostic = new DataModelDiagnostic();
		
		List<Diagram> diagrams = new ArrayList<Diagram>();
		
		try
		{
			Resource resource = editingDomain.getResourceSet().getResource(uri, true);
			
			
			EList<EObject> resourceContents = resource.getContents();
			
			// Obtenemos todos los diagramas de la aplicación
			for(EObject content : resourceContents)
			{
				if(content instanceof Diagram)
				{
					Diagram diagram = (Diagram)content;
					
					if(diagram.getElement() instanceof ESchema)
						diagrams.add(diagram);
							
				}
				
			}
			boolean existIndiagram = false;
			// Comprobamos que haya alguna tabla externa, para ello comprobamos que 
			// la aplicación sea distinta.
			List<ETable> externalTables = new ArrayList<ETable>();
			for(Diagram  diagram:diagrams)
			{
				for(EObject diagramContent : diagram.eContents())
				{
					if(diagramContent instanceof Node)
					{
						Node node = (Node)diagramContent;
						if(node.getElement() instanceof ETable)
						{
							ETable table = (ETable)node.getElement();
							String tableCapName = table.getSchema().getCapability();
							String diagramCapName = ((ESchema)diagram.getElement()).getCapability();
							if(tableCapName.equals(diagramCapName))
								continue;
							existIndiagram = true;
							// Estamos ante una tabla externa. 
							// Buscamos en las relaciones de la aplicacion si se esta referenciando la tabla.
							if(constraints.size()==0 && !externalTables.contains(table))
							{
								dataModelDiagnostic.add(new DataModelDiagnostic(
										DataModelDiagnostic.WARNING,
										Messages.bind("ExternalTablesRegulationRuleInvValidator",//$NON-NLS-1$ 
												table.getName()),
										getId(), 
										new Object[]{table},
										getCategory(),
										getCode()));
								externalTables.add(table);
							}
							else 
							{
								boolean referencedTable = true;
								for(ETableConstraint constraint:constraints)
								{
									if(constraint instanceof EForeignKey)
									{
										EForeignKey fk = (EForeignKey)constraint;
										if(!(fk.getParentTable().getSchema().getCapability().equals(table.getSchema().getCapability())))
										{
											referencedTable = false;
										}
										else 
										{
											referencedTable = true;
											break;
										}
									}
								}
								if(!referencedTable)
								{
									dataModelDiagnostic.add(new DataModelDiagnostic(
											DataModelDiagnostic.WARNING,
											Messages.bind("ExternalTablesRegulationRuleInvValidator",//$NON-NLS-1$ 
													table.getName()),
											getId(), 
											new Object[]{table},
											getCategory(),
											getCode()));
								}
							}
						}
					}
				}
				
			}
			// Hacemos la comprobacion a nivel de modelo.
			
			if(!existIndiagram)
			{
				for(ESchema schema : eProject.getSchemas())
				{
					for(ETable appTable:schema.getTables())
					{
						if(appTable instanceof EBaseTable)
						{
							if(!appTable.getSchema().getCapability().equals(eProject.getCapability()))
							{
								boolean found = false;
								for(ETableConstraint constraint:constraints)
								{
									if(constraint instanceof EForeignKey)
									{
										EForeignKey fk = (EForeignKey)constraint;
										if(fk.getBaseTable().getName().equals(appTable.getName()))
										{
											found = true;
										}
									}
								}
								if(!found)
									dataModelDiagnostic.add(new DataModelDiagnostic(
												DataModelDiagnostic.WARNING,
												Messages.bind("ExternalTablesRegulationRuleInvValidator",//$NON-NLS-1$ 
														appTable.getName()),
												getId(), 
												new Object[]{appTable},
												getCategory(),
												getCode()));
							}
						}
					}
				}
			}

			
		}
		finally
		{
			// Nos aseguramos de borrar el editingDomain
//			editingDomain.dispose();
		}
		
		return dataModelDiagnostic;
	}

}
