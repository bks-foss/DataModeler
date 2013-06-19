package com.isb.datamodeler.ui.validation.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;

import com.isb.datamodeler.constraints.EConstraint;
import com.isb.datamodeler.constraints.ETableConstraint;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.model.core.validation.AbstractDataModelerElementValidator;
import com.isb.datamodeler.model.core.validation.DataModelDiagnostic;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.EBaseTable;
import com.isb.datamodeler.tables.ETable;
import com.isb.datamodeler.ui.model.validation.Messages;
/**
 * Restricciones controladas por DataModeler.Validacion de modelo
 * Validación Obligatoria.
 * Un elemento no puede aparecer repetido en un diagrama de modelado.
 * 
 * @author Delia Salmerón
 *
 */
public class DiagramDuplicateElementInvValidator extends
		AbstractDataModelerElementValidator {

	@Override
	public DataModelDiagnostic validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, EOperation invariant, String expression) 
	{
		
		ESchema schema = (ESchema)eObject;
		
		TransactionalEditingDomain editingDomain = DataModelerUtils.getDataModelerEditingDomain();

		return  findDuplicateElement(schema, editingDomain);
		
	}
	private DataModelDiagnostic findDuplicateElement(ESchema schema , TransactionalEditingDomain editingDomain)
	{
		DataModelDiagnostic dataModelDiagnostic = new DataModelDiagnostic();
		
		try
		{
			Resource resource = schema.eResource();
			
			// Iteramos sobre los elementos del proyecto
			EList<EObject> resourceContents = resource.getContents();
			
			for(EObject content : resourceContents)
			{
				if(content instanceof Diagram)
				{
					Diagram diagram = (Diagram)content;
					
					List<String> idTables = new ArrayList<String>();
					List<String> constraints = new ArrayList<String>();
					
					if(diagram.getElement() instanceof ESchema)
					{
						// Validamos los diagramas que pertenezcan al esquema
						ESchema schemaDiagram = (ESchema)diagram.getElement();
						if(schemaDiagram==schema)
						{
							for(EObject diagramContent : diagram.eContents())
							{
								if(diagramContent instanceof Node)
								{
									Node node = (Node)diagramContent;
									if(node.getElement() instanceof ETable)
									{
										String schemaName = ((ETable)node.getElement()).getSchema().getName();
										String capability = ((ETable)node.getElement()).getSchema().getCapability();
										String tableName = ((ETable)node.getElement()).getName();
										String idTable = schemaName+capability+tableName;
										if(idTables.contains(idTable))
										{
											// ERROR El elemento '{0}' está repetido en el diagrama '{1}'
											dataModelDiagnostic.add(new DataModelDiagnostic(
												DataModelDiagnostic.ERROR,
												Messages.bind("DiagramDuplicateElementInvValidator.validation",new String[]{((ETable)node.getElement()).getName(),diagram.getName()}),//$NON-NLS-1$
												getId(), 
												new Object[]{(ETable)node.getElement()},
												getCategory(),
												getCode()));
										}
										else idTables.add(idTable);
										// Comprobamos las restricciones
										List<String> constraintsNames = new ArrayList<String>();
										if(node.getElement() instanceof EBaseTable)
										{
											EBaseTable baseTable = (EBaseTable)node.getElement();
											for(ETableConstraint constraint : baseTable.getConstraints())
											{
												if(constraintsNames.contains(constraint.getName()))
													// ERROR El elemento '{0}' está repetido en el diagrama '{1}'
													dataModelDiagnostic.add(new DataModelDiagnostic(
														DataModelDiagnostic.ERROR,
														Messages.bind("DiagramDuplicateElementInvValidator.validation",new String[]{constraint.getName(),diagram.getName()}),//$NON-NLS-1$
														getId(), 
														new Object[]{constraint},
														getCategory(),
														getCode()));
												else constraintsNames.add(constraint.getName());
											}
										}
									}
								}
								else if(diagramContent instanceof Edge)
								{
									Edge edge = (Edge)diagramContent;
									if(edge.getElement() instanceof EConstraint)
									{
										if(constraints.contains(((EConstraint)edge.getElement()).getName()))
										{
											// ERROR El elemento '{0}' está repetido en el diagrama '{1}'
											dataModelDiagnostic.add(new DataModelDiagnostic(DataModelDiagnostic.ERROR,
													Messages.bind("DiagramDuplicateElementInvValidator.validation",new String[]{((EConstraint)edge.getElement()).getName(),diagram.getName()}),//$NON-NLS-1$
													getId(), new Object[]{schema},getCategory(),getCode()));
										}
										else constraints.add(((EConstraint)edge.getElement()).getName());
									}
								}
								
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
