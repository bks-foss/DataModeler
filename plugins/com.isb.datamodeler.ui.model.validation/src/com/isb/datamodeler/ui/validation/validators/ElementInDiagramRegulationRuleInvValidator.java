package com.isb.datamodeler.ui.validation.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;

import com.isb.datamodeler.constraints.EConstraint;
import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.model.core.validation.AbstractDataModelerElementValidator;
import com.isb.datamodeler.model.core.validation.DataModelDiagnostic;
import com.isb.datamodeler.schema.ESQLObject;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.EBaseTable;
import com.isb.datamodeler.tables.ETable;
import com.isb.datamodeler.ui.model.validation.Messages;
/**
 * Reglas de normativa. Validacion de modelo. Validación opcional.
 * Todos los objetos de un esquema de la aplicación:
 * SI existe algún diagrama en el esquema, deben aparecer en 
 * algún diagrama de dicho esquema
 * SI NO , deben aparecer en algún diagrama de la aplicación.
 * 
 * @author Delia Salmerón
 *
 */
public class ElementInDiagramRegulationRuleInvValidator extends
		AbstractDataModelerElementValidator {
	
	private List<Diagram> schemaDiagrams = new ArrayList<Diagram>();
	private List<Diagram> applicationDiagrams = new ArrayList<Diagram>();

	@Override
	public DataModelDiagnostic validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, EOperation invariant, String expression) {
		
		ESchema schema = (ESchema)eObject;
		
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
					
					if(diagram.getElement() instanceof ESchema)
					{
						ESchema schemaDiagram = (ESchema)diagram.getElement();
						if(schemaDiagram==schema)
							schemaDiagrams.add(diagram);
						else applicationDiagrams.add(diagram);
					}
							
				}
				
			}
			if(schemaDiagrams.size()>0)
			{
				// Comprobamos que los elementos del esquema estén en alguno de los diagramas pertenecientes al esquema
				for(ETable table:schema.getTables())
				{
					boolean existTable = false;
					for(Diagram diagram:schemaDiagrams)
					{
						if(existElementInDiagram(table, diagram))
						{
							existTable = true;
							break;
						}
					}
					if(!existTable)
					{
						// ERROR :"La tabla <nombreTabla> no está incluida en ningún diagrama de su esquema <nombreEsquema>"
						dataModelDiagnostic.add(new DataModelDiagnostic(
								DataModelDiagnostic.ERROR,
								Messages.bind("ElementInDiagramRegulationRuleInvValidator.validation1",//$NON-NLS-1$
										new String[]{table.getName(),schema.getName()}),
								getId(), 
								new Object[]{table},
								getCategory(),
								getCode()));
					}
					for(EConstraint constraint : ((EBaseTable)table).getConstraints())
					{
						boolean existConstraints = false;
						for(Diagram diagram:schemaDiagrams)
						{
							if(existElementInDiagram(constraint, diagram))
							{
								existConstraints = true;
								break;
							}
						}
						if(!existConstraints && constraint instanceof EForeignKey)
						{
							EForeignKey fk = (EForeignKey)constraint;
							String msg = fk.isIdentifying()? "Identificativa":"No Identificativa";
							// ERROR: "La tabla <nombreTablaHija> tiene una relación de tipo <tipo de relación> con <nombreTablaPadre> que
							// no esta incluida en ningún diagrama de su esquema"
							dataModelDiagnostic.add(new DataModelDiagnostic(
									DataModelDiagnostic.ERROR,
									Messages.bind("ElementInDiagramRegulationRuleInvValidator.validation2",//$NON-NLS-1$ 
											new String[]{table.getName(),msg, fk.getParentTable().getName(),schema.getName()}),
									getId(), 
									new Object[]{table},
									getCategory(),
									getCode()));
							
						}
					}
				}
				
			}
			else // Comprobamos que los elementos del esquema esten en alguno de los diagramas pertenecientes a la aplicacion
			{
				// Comprobamos que los elementos del esquema estén en alguno de los diagramas pertenecientes al esquema
				for(ETable table:schema.getTables())
				{
					boolean existTable = false;
					for(Diagram diagram:applicationDiagrams)
					{
						if(existElementInDiagram(table, diagram))
						{
							existTable = true;
							break;
						}
					}
					if(!existTable)
					{
						//ERROR :"La tabla <nombreTabla> no está incluida en ningún diagrama"
						dataModelDiagnostic.add(new DataModelDiagnostic(
								DataModelDiagnostic.ERROR,
								"La tabla '" +table.getName()+"' no está incluida en ningún diagrama", 
								getId(), 
								new Object[]{table},
								getCategory(),
								getCode()));
					}
					for(EConstraint constraint : ((EBaseTable)table).getConstraints())
					{
						boolean existConstraints = false;
						for(Diagram diagram:applicationDiagrams)
						{
							if(existElementInDiagram(constraint, diagram))
							{
								existConstraints = true;
								break;
							}
						}
						if(!existConstraints && constraint instanceof EForeignKey)
						{
							EForeignKey fk = (EForeignKey)constraint;
							String msg = fk.isIdentifying()? "Indetificativa":"No Identificativa";
							// ERROR: "La tabla <nombreTablaHija> tiene una relación de tipo <tipo de relación> con <nombreTablaPadre> que
							// no esta incluida en ningún diagrama "
							dataModelDiagnostic.add(new DataModelDiagnostic(
									DataModelDiagnostic.ERROR,
									Messages.bind("ElementInDiagramRegulationRuleInvValidator.validation3"
											,new String[]{table.getName(),msg,fk.getParentTable().getName()}),
									getId(), 
									new Object[]{table},
									getCategory(),
									getCode()));
							
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
	private boolean existElementInDiagram(ESQLObject sqlObject , Diagram diagram)
	{
		for(EObject diagramContent : diagram.eContents())
		{
			if(diagramContent instanceof Node)
			{
				Node node = (Node)diagramContent;
				if(node.getElement() instanceof ETable)
				{
					if(sqlObject==node.getElement())
						return true;
				}
			}
			else if(diagramContent instanceof Edge)
			{
				Edge edge = (Edge)diagramContent;
				if(edge.getElement() instanceof EConstraint)
				{
					if(sqlObject==edge.getElement())
						return true;
				}
			}
			
		}
		return false;
	}

}
