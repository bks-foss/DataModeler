package com.isb.datamodeler.diagram.edit.policy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DiagramDragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.notation.Node;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.ETableConstraint;
import com.isb.datamodeler.datatypes.EPredefinedDataType;
import com.isb.datamodeler.datatypes.EPrimitiveDataType;
import com.isb.datamodeler.diagram.edit.commands.ExternalSchemaCreateCommand;
import com.isb.datamodeler.diagram.edit.commands.ExternalTableAddCommand;
import com.isb.datamodeler.diagram.edit.parts.SchemaEditPart;
import com.isb.datamodeler.diagram.providers.DatamodelerElementTypes;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.schema.ESchemaFactory;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.EPersistentTable;
import com.isb.datamodeler.tables.ETable;
import com.isb.datamodeler.ui.project.EProject;

public class PersistentTableDragDropEditPolicy extends DiagramDragDropEditPolicy{
	
	ESchema _sourceSchema;
	
	ESchema _targetSchema;
	
	EProject _targetProject;
	
	ArrayList<ETable> _tablesNotInDiagram;
	boolean _isSchemaInTarget = true;
	
	protected Command getCreateElementAndViewCommand(DropObjectsRequest request) {

		CompositeCommand cc = new CompositeCommand("Drag&Drop Tables");
		
		//Chequeamos las tablas seleccionadas para ver si permitimos arrastrarlas
		if(!isEnabled(request))
			return UnexecutableCommand.INSTANCE;
		

		// Comprobamos que la subaplicación del esquema origen y destino coinciden.
		// Si no coinciden puede que haya que crear un esquema externo y hacer una copia de las tablas 
		// (la tabla se crea en un esquema externo, pero se pinta en el diagrama del esquema destino) 
		if(!_sourceSchema.getCapability().equalsIgnoreCase(_targetSchema.getCapability()))
		{
			// Obtenemos el esquema destino de las copias de las tablas
			_targetSchema = getTargetSchema(cc);
		}
		else if(!_sourceSchema.getName().equalsIgnoreCase(_targetSchema.getName()))
		{
			_targetSchema = getTargetSchema(cc);
		}
		
		// Descriptores para los shortcuts
		ArrayList<CreateViewRequest.ViewDescriptor> viewDescriptors = new ArrayList<CreateViewRequest.ViewDescriptor>();
		
		// Para todas las tablas que se arrastran y no existen en el diagrama
		for(ETable table :_tablesNotInDiagram)
		{
			boolean foundTable = false;
			// Comprobamos si la tabla no existe en el proyecto destino
			for(ESchema schema: _targetProject.getSchemas())
			{
				for(ETable targetTable:schema.getTables()){
					if(targetTable.getName().equalsIgnoreCase(table.getName()) && 
							targetTable.getSchema().getCapability().equals(table.getSchema().getCapability())&& 
							targetTable.getSchema().getName().equals(table.getSchema().getName()))
						foundTable = true;
				}
			}
			// Si la tabla ya existe hay que distinguir:
			// 1.- si se arrastra desde su propio proyecto hay que pintarla en el diagrama
			// 2.- si se arrastra desde otro proyecto no se permite la acción.
			if(foundTable)
			{
				if(_targetProject.equals((EProject)_sourceSchema.eContainer()))
						viewDescriptors.add(new CreateViewRequest.ViewDescriptor(
								new EObjectAdapter(table),Node.class, null,((SchemaEditPart)this.getHost()).getDiagramPreferencesHint()));
			}
			else // Si la tabla no existe en el modelo destino hay que hacer una copia
			{	
				
				ETable newTable = copyTable(table);

				newTable.setId(DataModelerUtils.generateRandomID(16));
				// Si el esquema ya esta en el proyecto destino
				// creamos una transaccion para añadir la tabla
				if(_isSchemaInTarget)
					cc.compose(getAddExternalTableCommand(newTable));
				else // Al crear el esquema ya estamos abriendo una transaccion
					_targetSchema.getTables().add(newTable);
				
				viewDescriptors.add(new CreateViewRequest.ViewDescriptor(
						new EObjectAdapter(newTable),Node.class, null,((SchemaEditPart)this.getHost()).getDiagramPreferencesHint()));
			}
		}
		if(viewDescriptors.size()>0)
			cc.compose(new CommandProxy(createViewsAndArrangeCommand(request,viewDescriptors)));

		return new ICommandProxy(cc);
	}

	private ETable copyTable(ETable table)
	{
		ETable newTable = EcoreUtil.copy(table);//(ETable)copier.copy(table);
		
		// Para evitar copiar las referencias a los tipos de datos
		// se los asiganmos a mano, una vez copiada la tabla
		for(EColumn cSource:table.getColumns())
		{
			for(EColumn cNew:newTable.getColumns())
			{
				if(cNew.getName().equals(cSource.getName()))
				{
					for(EPrimitiveDataType pt : _targetProject.getDatabase().getPrimitiveDataTypes())
					{
						if(pt.getName().equals(cSource.getPrimitiveType().getName()))
						{
							cNew.setPrimitiveType(pt);
							if(cNew.getContainedType() instanceof EPredefinedDataType)
							{
								((EPredefinedDataType)cNew.getContainedType()).setPrimitiveType(pt);
							}
							break;
						}
					}
				}
			}
		}

		// Borramos las FK de la tabla externa.
		Set<ETableConstraint> delete = new HashSet<ETableConstraint>();
		for(ETableConstraint contraint:((EPersistentTable)newTable).getConstraints())
		{
			if(contraint instanceof EForeignKey)
			{
				delete.add(contraint);
			}
				
		}
		for(ETableConstraint c:delete)
			((EPersistentTable)newTable).getConstraints().remove(c);
		
		return newTable;
	}
	private Boolean isEnabled(DropObjectsRequest request)
	{
		ArrayList<ETable> selectedTables = new ArrayList<ETable>();

		for (Iterator<?> it = request.getObjects().iterator(); it.hasNext();) {
			Object nextTable = it.next();
			if(nextTable instanceof ETable)
				selectedTables.add((ETable)nextTable);
		}
		
		// Todas las tablas seleccionadas deben pertenecer al mismo esquema
		if(!checkIsSameSchema(selectedTables))
			return false;
		
		// Las tablas no deben existir en el diagrama destino
		_tablesNotInDiagram = removeTablesExistingInDiagram(selectedTables,(SchemaEditPart)getHost());
		if(_tablesNotInDiagram.size()<1)
			return false;
		
		_targetSchema = (ESchema)this.getHostObject();
		_targetProject = (EProject)_targetSchema.eContainer();
		
		// Las bases de datos origen y destino deben coincidir
		if(!_targetProject.getDatabase().getName().equalsIgnoreCase(_sourceSchema.getDatabase().getName()))
			return false;
		
		return true;
	}
	
	// comprueba que todas las tablas seleccionadas pertenecen al mismo esquema
	private boolean checkIsSameSchema(List<ETable> tables)
	{
		if(!tables.isEmpty())
		{
			_sourceSchema = tables.get(0).getSchema();
			for (ETable table:tables)
			{
				if(!_sourceSchema.equals(table.getSchema()))
					return false;
			}
		}
		return true;
	}
	
	// Elimina de la lista las tablas que ya existen en el diagrama destino
	private ArrayList<ETable> removeTablesExistingInDiagram(List<ETable> tables, SchemaEditPart editPart)
	{
		ArrayList<ETable> tablesNotInDiagram = new ArrayList<ETable>();

		if(editPart.getPrimaryEditParts().size()<1)
			tablesNotInDiagram.addAll(tables);
		else
			for (ETable table : tables) 
			{
				boolean inDiagram = false;
				
				for(Object childEditPart: editPart.getPrimaryEditParts())
				{
					if(childEditPart instanceof GraphicalEditPart)
					{
						EObject semanticElement = ((GraphicalEditPart)childEditPart).resolveSemanticElement();
						if(semanticElement instanceof ETable)
						{
							if(compareTables((ETable)semanticElement,table))
								inDiagram = true;
						}
					}
				}
				if(!inDiagram)
					tablesNotInDiagram.add(table);
			}
		
		return tablesNotInDiagram;
	}
	
	private Boolean compareTables(ETable table1, ETable table2)
	{
		if(table1.getSchema().getName().equals(table2.getSchema().getName()) && table1.getName().equals(table2.getName()) &&
				table1.getSchema().getCapability().equals(table2.getSchema().getCapability()))
			return true;
		return false;
			
	}

	private ESchema getTargetSchema(CompositeCommand cc)
	{
		boolean foundSchemaInTarget = false;
		for(ESchema schema :_targetProject.getSchemas())
		{
			if(schema.getCapability().equalsIgnoreCase(_sourceSchema.getCapability()) &&
					schema.getName().equalsIgnoreCase(_sourceSchema.getName()))
			{
				// Ya existe un esquema con el mismo nombre y subaplicación
				_targetSchema = schema;
				_isSchemaInTarget = true;
				foundSchemaInTarget = true;
			}
		}
		if(!foundSchemaInTarget)
		{	
			_isSchemaInTarget = false;
			_targetSchema = ESchemaFactory.eINSTANCE.createSchema();
			// Comando para crear el esquema en el proyecto
			cc.compose(getCreateExternalSchemaCommand());
		}
		return _targetSchema;
	}
	
	private EditElementCommand getCreateExternalSchemaCommand()
	{
		CreateElementRequest createElementRequest = new CreateElementRequest(_targetProject,DatamodelerElementTypes.Schema_1000);
		createElementRequest.setLabel("External Schema Creation");
		
		_targetSchema.setId(DataModelerUtils.generateRandomID(16));
		_targetSchema.setCapability(_sourceSchema.getCapability());
		_targetSchema.setDescription(_sourceSchema.getDescription());
		_targetSchema.setName(_sourceSchema.getName());
		
		return new ExternalSchemaCreateCommand(_targetProject,_targetSchema, _sourceSchema, createElementRequest);
	}
	private EditElementCommand getAddExternalTableCommand(ETable newTable)
	{
		CreateElementRequest createElementRequest = new CreateElementRequest(_targetSchema,DatamodelerElementTypes.PersistentTable_2009);
		createElementRequest.setLabel("External Table Adition");
		
		return new ExternalTableAddCommand(_targetSchema, newTable, createElementRequest);
	}
	
	public boolean understandsRequest(Request request) {
		
		return RequestConstants.REQ_DROP_OBJECTS.equals(request.getType());

	}
	
	public Command getCommand(Request request) {

		if(understandsRequest(request))
		{
			DropObjectsRequest dndRequest = (DropObjectsRequest) request;
			dndRequest.setRequiredDetail(getRequiredDragDetail(dndRequest));
			return getCreateElementAndViewCommand(dndRequest);
		}
		return null;
	}
}
