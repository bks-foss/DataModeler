package com.isb.datamodeler.internal.ui.views.actions.refactor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.transaction.TransactionalCommandStack;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.actions.SelectionListenerAction;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.constraints.EConstraintsFactory;
import com.isb.datamodeler.constraints.EConstraintsPackage;
import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.ETableConstraint;
import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.diagram.actions.RestoreTablesCanonicalEditPolicy;
import com.isb.datamodeler.diagram.edit.parts.PersistentTableEditPart;
import com.isb.datamodeler.diagram.edit.parts.SchemaEditPart;
import com.isb.datamodeler.diagram.navigator.DatamodelerDomainNavigatorItem;
import com.isb.datamodeler.internal.ui.dialogs.PossibleRelationElement;
import com.isb.datamodeler.internal.ui.dialogs.PossibleRelationsDialog;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.EBaseTable;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.EPersistentTable;
import com.isb.datamodeler.tables.ETable;
import com.isb.datamodeler.tables.ETablesPackage;
import com.isb.datamodeler.ui.DataModelerUI;
import com.isb.datamodeler.ui.project.EProject;
import com.isb.datamodeler.ui.triggers.configurators.UITriggerConfigurator;

/**
 * Acción encargada de detectar posibles referencias desde las tablas de entrada a otras tablas del modelo. 
 * Ofrece la posibilidad de añadir estas referencias al modelo mediando un diálogo.
 * 
 * La acción estará disponible desde el menú contextual del nodo del proyecto del navegador Data Modeler.
 * 
 * @author xIS00714
 *
 */
public abstract class DetectPossibleRelationsAction extends SelectionListenerAction {

	// Proyecto seleccionado para el cual vamos a las calular las 
	// relaciones posibles de sus tablas
//	private IProject _project;
	EProject _eProject;
	private List<ETable> _eTables = null;
	private List<PersistentTableEditPart> _editParts = null;
	
	
	// Caché que guarda para cada esquema la relacion entre sus tabla y las posibles relaciones
	// de cada una de esas tablas.

	private Map<ESchema, Map<EPersistentTable, List<PossibleRelation>>> _schema2table2relations = null;
	// Control para calcular las permutaciones de elementos sin repetición
	static boolean[] control;
	
	public DetectPossibleRelationsAction(String message) {
		super(message);
	}
	
	// Objeto para representar un posible relación
	public class PossibleRelation
	{
		private EUniqueConstraint _uk;
		private List<EColumn> _columns;
		private EPersistentTable _parentTable;
		
		public PossibleRelation(EPersistentTable parentTable , EUniqueConstraint uk , List<EColumn> columns)
		{
			_parentTable = parentTable;
			_uk = uk;
			_columns = columns;
			
		}
		public EUniqueConstraint getUK()
		{
			return _uk;
		}
		public EPersistentTable getParentTable()
		{
			return _parentTable;
		}
		public List<EColumn> getColumns()
		{
			return _columns;
		}
		
		
	}
	@Override
	public void run() 
	{
		control = new boolean[100];
		_schema2table2relations = new Hashtable<ESchema, Map<EPersistentTable,List<PossibleRelation>>>();
//		EProject eProject = UtilsDataModelerUI.findEProject(_project);
		if(_eProject == null)
			return;
		// PUNTO 1: Detectamos, para cada tabla de entrada, grupos de columnas que podrían ser referencias a claves de otras tablas 
		// (tanto de la aplicación como externas). 
		List<ETable> possibleTables = getPossibleTables();// ArrayList<ETable>();

		// Obtenemos las tablas del proyecto
//		for(ESchema eSchema : eProject.getSchemas())
//		{
//			possibleTables.addAll(eSchema.getTables());
//		}
		// Obtenemos las tablas pertenecientes a la aplicación , para las cuales queremos detectar posibles relaciones
		List<ETable> applicationTables = getApplicationTables();//new ArrayList<ETable>();
//		for(ESchema schema : eProject.getSchemas())
//		{
//			// No detectamos posibles relaciondes desde tablas externas
//			if(schema.getCapability().equals(eProject.getCapability()))
//				applicationTables.addAll(schema.getTables());
//		}
		for(ETable sourceTable  : applicationTables)
		{
//			System.err.println(sourceTable.getName());
			for(ETable targetTable : possibleTables)
			{
				// No detectamos relaciones sobre si misma
				if(sourceTable.equals(targetTable))
					continue;
				
				// Obtenemos las claves de la tabla referenciada posible
				if(targetTable instanceof EPersistentTable)
				{
					EPersistentTable targetPersistentTable = (EPersistentTable)targetTable;
					for(ETableConstraint constraint : targetPersistentTable.getConstraints())
					{
						if(constraint instanceof EUniqueConstraint)
						{
							EUniqueConstraint uk = (EUniqueConstraint)constraint;
							EList<EColumn> columnsUK = uk.getMembers();
							// Obtenemos columnas candidatas a formar una relacion de la tabla origen. Aplicamos dos condiciones
							// 1. El nombre de la columna debe coincidir o empezar igual que el de la columna referenciada
							// 2. El tipo (y parametros) de la columna debe ser igual al de la columna referenciada
							List<List<EColumn>> setColumnsPossible = getPossibleColumnsList(columnsUK, sourceTable.getColumns());

							// Comprobamos la siguiente y ultima condicción:
							// 3. Al menos una columna no debe pertenecer a una FK
							// Para ello obtenemos las columnas de las las FK de la tabla a la cual pertenecen las columnas
							boolean belongs = true;
							List<EColumn> columnsFK = new ArrayList<EColumn>();
							if(sourceTable instanceof EPersistentTable)
							{
								EPersistentTable sourcePersistentTable = (EPersistentTable)sourceTable;
								
								for(ETableConstraint contraintSource : sourcePersistentTable.getConstraints())
								{
									if(contraintSource instanceof EForeignKey)
									{
										columnsFK.addAll(((EForeignKey)contraintSource).getMembers());
										
									}
								}
								for(List<EColumn> columnsPossible : setColumnsPossible)
								{
									belongs = true;
									for(EColumn columnPossible : columnsPossible)
									{
										if(!columnsFK.contains(columnPossible))
										{
											belongs = false;
											break;
										}
									}
									// Si al menos una columna no pertenece a una FK y ademas no genera ciclos
									if(!belongs && !haveCycles(sourcePersistentTable, targetPersistentTable , new ArrayList<String>()))
									{
										// Podemos crear una FK con el grupo de columnas posibles columnsPossible
										PossibleRelation pr = new PossibleRelation(targetPersistentTable, uk, columnsPossible);
										Map<EPersistentTable, List<PossibleRelation>> table2relations = _schema2table2relations.get(sourcePersistentTable.getSchema());
										if(table2relations == null)
										{
											List<PossibleRelation> possibleReltions = new ArrayList<DetectPossibleRelationsAction.PossibleRelation>();
											possibleReltions.add(pr);
											table2relations = new Hashtable<EPersistentTable, List<PossibleRelation>>();
											table2relations.put(sourcePersistentTable, possibleReltions);
											_schema2table2relations.put(sourcePersistentTable.getSchema(), table2relations);
										}
										else
										{
											List<PossibleRelation> possibleReltions = table2relations.get(sourcePersistentTable);
											if(possibleReltions == null)
											{
												possibleReltions = new ArrayList<DetectPossibleRelationsAction.PossibleRelation>();
												table2relations.put(sourcePersistentTable, possibleReltions);
											}
											possibleReltions.add(pr);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if(_schema2table2relations.isEmpty())
		{
			MessageDialog.openInformation(DataModelerUI.getActiveWorkbenchShell(), 
					Messages.bind("DetectPossibleRelationsAction.notPossibleRelations.title"), 
					Messages.bind("DetectPossibleRelationsAction.notPossibleRelations.message"));
			return;
		}
		// PUNTO 2 :  En este momento ya tenemos detectadas para cada tabla de la aplicacion seleccionada,
		// las posibles relaciones.
		// El siguiente punto es mostrarle al usuario las relaciones detectadas para cada tabla hija 
		// cumpliendo los siguientes requisitos:
		// A. Aparecen ordenadas por: 
		//	  1.Nombre de tabla hija. 
		//	  2.Tamaño (de mayor a menor número de columnas que componen la clave). 
		//	  3.Nombre de tabla padre. 
		// B. Para cada posible relación de una tabla hija, se indican las columnas de la tabla sugeridas para la relación, 
		//    la tabla padre y las columnas que forman la clave única sugerida. 
		// C. Habrá un check que permitirá al usuario selecionar la posible relación o no. 
		//		Los checks estarán inicialmente desmarcados. 
		final PossibleRelationsDialog dialog = new PossibleRelationsDialog(_eProject , DataModelerUI.getActiveWorkbenchShell() , _schema2table2relations);
		dialog.create();
		dialog.getShell().setText(Messages.bind("DetectPossibleRelationsAction.dialog.text"));  //$NON-NLS-1$
		dialog.getShell().setSize(900, 750);
		int result = dialog.open();
	
		// PUNTO 3 : Cuando el usuario acepte, se incluirán en el modelo las relaciones seleccionadas, 
		// con el nombre por defecto y asociando las columnas de la clave referenciada con 
		// las columnas para las que se detectó la correspondencia
		
		if(result == Window.OK)
		{
			Runnable runnable = new Runnable() 
			{
				@Override
				public void run() {
					TransactionalEditingDomain editingDomain = DataModelerUtils.getDataModelerEditingDomain();
					
					List<PossibleRelationElement> tElements = dialog.getTransformElements();
					
					for(PossibleRelationElement pre : tElements)
					{
						if(pre.isCreate())
						{
							EPersistentTable childTable = pre.getChildTable();
						
							if (editingDomain != null)
							{
								CompoundCommand cmd = new CompoundCommand();
													
								EForeignKey fk = EConstraintsFactory.eINSTANCE.createForeignKey();
								// Calculamos si la fk deber ser identificativa
								List<EColumn> columns = pre.getSuggestedColumns();
								boolean identifying = true;
								for(EColumn c:columns)
								{
									if(!c.isPrimaryKey())
									{
										identifying = false;
										break;
									}
								}
								
								fk.setDefaultIdentifying(identifying);
								SetCommand parentTableCommand = new SetCommand(editingDomain, fk, EConstraintsPackage.eINSTANCE.getForeignKey_ParentTable(), pre.getParentTable());
								AddCommand addCommand = new AddCommand(editingDomain, childTable, ETablesPackage.eINSTANCE.getBaseTable_Constraints(), fk);
								Command setUkCmd = SetCommand.create(editingDomain, fk, EConstraintsPackage.eINSTANCE.getForeignKey_UniqueConstraint(), pre.getUk());
								AddCommand members = new AddCommand(editingDomain, fk, EConstraintsPackage.eINSTANCE.getReferenceConstraint_Members(), pre.getSuggestedColumns());
																
								cmd = new CompoundCommand();
								cmd.append(parentTableCommand);
								cmd.append(addCommand);
								cmd.append(members);
								cmd.append(setUkCmd);
								
		
								Map<Object, Object> transOptions = new HashMap<Object, Object>();
								transOptions.put(UITriggerConfigurator.NOT_EXECUTE_UI_CONFIGURATOR, Boolean.TRUE);
								try 
								{
									((TransactionalCommandStack)editingDomain.getCommandStack()).execute(cmd , transOptions);
								
								}
								catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								restoreEdges();
								
								
							}
						}
					}
					
				}
			};
			runnable.run();
	
		}

		
	}

	@Override
	public boolean updateSelection(IStructuredSelection selection) 
	{
		// Deshabilitamos la accion si:
		// - Alguna de las tablas seleccionadas es externa
		// - Las tablas seleccionadas son de distinto proyecto
				
		_eTables = new ArrayList<ETable>();
		_editParts = new ArrayList<PersistentTableEditPart>();
		if(selection instanceof IStructuredSelection)
		{
			for(Iterator<?> iter = ((IStructuredSelection)selection).iterator(); iter.hasNext();)
			{
				Object element = iter.next();
				
				if(element instanceof DatamodelerDomainNavigatorItem)
				{
					DatamodelerDomainNavigatorItem item = (DatamodelerDomainNavigatorItem)element;
					if(item.getEObject() instanceof ETable)
					{
						_eTables.add((ETable)item.getEObject());
					}

				}
				else if(element instanceof PersistentTableEditPart)
				{
					PersistentTableEditPart ep = (PersistentTableEditPart)element;
					_editParts.add(ep);
					if(((Node)ep.getModel()).getElement() instanceof ETable)
					{
						_eTables.add((ETable)((Node)ep.getModel()).getElement());
					}
				}
			}
		}
		Set<String> projectsNames = new HashSet<String>();
		
		for(ETable eTable:_eTables)
		{
			if(eTable.getSchema().isExternal())
				return false;
			
			projectsNames.add(eTable.getIProject().getName());
		}
		if(projectsNames.size()>1)
			return false;

		if(_eTables!=null && !_eTables.isEmpty())
			_eProject = (EProject)_eTables.get(0).getRootContainer();
		
		return !_eTables.isEmpty();

}
/**
 * Obtiene una lista que va a contener las listas de columnas que pueden formar una relación.
 * 
 * @param columnsUK columnas que forman la UK
 * @param columnsTable columnas de la tabla hija
 * @return devuelve lista que va a contener las listas de columnas que pueden formar una relación.
 */
private List<List<EColumn>> getPossibleColumnsList (List<EColumn> columnsUK , List<EColumn> columnsTable)
{
	// Lista que va a contener las listas de columnas que pueden formar una relación.
	List<List<EColumn>> columnPossibleList = new ArrayList<List<EColumn>>();

	List<String> columnsName = new ArrayList<String>();
	List<String> columnsNameUK = new ArrayList<String>();
	for(EColumn cUK : columnsUK)
	{
		for(EColumn c : columnsTable)
		{
			if(c.getName().equals(cUK.getName()) || c.getName().startsWith(cUK.getName()))
			{
				if(!columnsName.contains(c.getName()))
					columnsName.add(c.getName());
			}
		}
		columnsNameUK.add(cUK.getName());
	}

	// A partir de los nombres de las columnas de la tabla hija obtenemos permutaciones sin repeticion de 
	// n elementos (siendo n el tamano de las columnas de la UK)
	List<List<String>> result = generateCombinations(columnsName, columnsNameUK);
//	generatePermutationNotSust(columnsName, "", columnsUK.size() , strings); //$NON-NLS-1$
	// A partir de las combinaciones de nombres de columnas generadas, obtenemos las EColumn
	for(List<String> l : result)
	{
		List<EColumn> columnsPossible = new ArrayList<EColumn>();
		for(String nameColumn : l)
		{
			for(EColumn c : columnsTable)
			{
				if(nameColumn.equals(c.getName()))
				{
					columnsPossible.add(c) ;
					break;
				}
			}
		}
		// Filtamos las columnas posible en funcion del nombre y del tipo
		if(columnsPossible.size() == columnsUK.size())
		{
			// Comprobamos que no haya columnas repetidas
			List<String> namesList = new ArrayList<String>();
			Set<String> namesSet = new HashSet<String>();
			for(EColumn eColumn : columnsPossible)
			{
				namesList.add(eColumn.getName());
				namesSet.add(eColumn.getName());
			}
			if(namesList.size()!=namesSet.size())
				continue;
			boolean areEquals = true;
			for(int j=0 ; j<columnsUK.size() ; j++ )
			{
				// El nombre de la columna debe coincidir o empezar igual que el de la columna referenciada
				if(!columnsPossible.get(j).getName().equals(columnsUK.get(j).getName()) &&
						!columnsPossible.get(j).getName().startsWith(columnsUK.get(j).getName()))
				{
					areEquals = false;
					continue;
				}
				// Es una posible candidata. Comprobemos la siguiente condicción:
				// El tipo (y parametros) de la columna debe ser igual al de la columna referenciada
				else if(columnsPossible.get(j).getDataType().compareTo(columnsUK.get(j).getDataType())!=0)
				{
					areEquals = false;
					continue;
				}
				
			}
			if(areEquals)
				columnPossibleList.add(columnsPossible);
		}
		
	}
	return columnPossibleList;

	
}
/**
 * A partir de una lista de String obtiene permutaciones de n elementos. 
 * El algoritmo consiste en comenzar con la cadena vacía, agregar un string(nombre de la columna) 
 * y llamamos recursivamente el mismo método para generar las cadenas de tamaño N-1.
 * 
 * EJEMPLO: Para una lista de columnas {col , col1 , col11} con n=2 generaria lo siguiente
 * 
 * col,col1,
 * col,col11,
 * col1,col,
 * col1,col11,
 * col11,col,
 * col11,col1
 * 
 * @param elementos lista de elementos a permutar.
 * @param actual almacena la secuencia de string que va generando
 * @param n número de elementos que va a formar cada permutación
 * @param strings lista que contiene cada una de las permutaciones
 */
//private static void generatePermutationNotSust(List<String> elementos, 
//		String actual, int n , 
//		List<String> strings ) {
//	
//	// Caso base
//    if(n==0) 
//    {
//        // Que hacemos Hacer con la secuencia generada?
//    	// La añadimos a la lista de strings
//    	strings.add(actual);
//    	
//    }
//    else {
//        for(int i=0; i<elementos.size(); i++ ) 
//        {
//            if(control[i]==true) continue;
//            control[i]=true;
//            generatePermutationNotSust(elementos,  actual+elementos.get(i)+",",n-1 , strings); //$NON-NLS-1$
//            control[i]=false;
//        }
//    }
//}
public List<List<String>> generateCombinations(List<String> columnsNames , List<String> columnsUK)
{
	List<List<String>> list = new ArrayList<List<String>>();
	for(String colUK:columnsUK)
	{
		// Busco los nombres de las columnas posibles para la colUK
		List<String> possibleNameColumns = getPossibleNamesColumns(columnsNames, colUK);
		if(list.isEmpty())
		{
			for(String s: possibleNameColumns)
			{
				List<String> l = new ArrayList<String>();
				l.add(s);
				list.add(l);
			}
		}
		else
		{
			if(possibleNameColumns.size()==1)
			{
				// A cada una de las listas le añado el elemento
				for(List<String> l:list)
				{
					l.add(possibleNameColumns.get(0));
				}
			}
			else
			{
				// Creo tantas listas como posilidades hay y añado una posibilidad a cada lista
				List<List<String>> newList = new ArrayList<List<String>>();
				for(int i=0 ; i< possibleNameColumns.size();i++)
				{
					copyList(list , newList,possibleNameColumns.get(i));
						
				}
				list = newList;
			}
		}
	}

	return list;
}
private void copyList(List<List<String>> source , List<List<String>> target , String string)
{
	for(List<String> l:source)
	{
		ArrayList<String> listTarget = new ArrayList<String>();
		for(String s:l)
		{
			listTarget.add(s);
		}
		listTarget.add(string);
		target.add(listTarget);
	}
	
}
private List<String> getPossibleNamesColumns(List<String> columnsNames , String colUKName)
{
	List<String> result = new ArrayList<String>();
	for(String c : columnsNames)
	{
		if(c.equals(colUKName) || c.startsWith(colUKName))
			result.add(c);
	}
	return result;
}
// La tabla de entrada no debe ser referenciada por la posible tabla padre (ni directa ni indirectamente a través de otra tabla). 
// Es decir, la tabla de entrada candidata a ser tabla hija no debe ser tabla padre (o abuela, o bisabuela, etc.) 
// de la posible tabla padre. Por lo tanto, no se propondrán relaciones que vayan a generar ciclos.
private boolean haveCycles( EBaseTable sourceTable , EBaseTable targetTable , List<String> visitedTables)
{
	boolean haveCycles = false;
	String tables = sourceTable.getName()+"."+targetTable.getName();
	if(visitedTables.contains(tables))
		return haveCycles;
	else visitedTables.add(tables);
	
	if(targetTable.getForeignKeys().isEmpty())
		return haveCycles;
	
	for(EForeignKey fk:targetTable.getForeignKeys())
	{
		if(fk.getParentTable().equals(sourceTable))
		{
			haveCycles = true;
			break;
		}
		else 
			if(fk.getParentTable()!=sourceTable)
				return haveCycles = haveCycles(sourceTable, fk.getParentTable(),visitedTables);
	}
	
	return haveCycles;
}
private void restoreEdges()
{
	for(PersistentTableEditPart ep : _editParts)
	{
		SchemaEditPart _schemaEditPart = (SchemaEditPart)ep.getParent();
	
		
		final Diagram diagram = _schemaEditPart.getDiagramView();
		
		RestoreTablesCanonicalEditPolicy cep = new RestoreTablesCanonicalEditPolicy(diagram , ep);
	
		
		//Al instalar las policies se refrescan automaticamente
		_schemaEditPart.installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, cep);
	
		_schemaEditPart.removeEditPolicy(EditPolicyRoles.CANONICAL_ROLE);
		
		//Hacemos que los cambios que se han realizado persintan
		
		TransactionalEditingDomain ted = TransactionUtil.getEditingDomain(diagram);
		
		AbstractTransactionalCommand atc = new AbstractTransactionalCommand(ted, "", Collections.singletonList(
													WorkspaceSynchronizer.getFile(diagram.eResource())))
		{
			@Override
			protected CommandResult doExecuteWithResult(
					IProgressMonitor monitor, IAdaptable info)
					throws ExecutionException
			{
				diagram.persistChildren();
				diagram.persistEdges();
	
				return null;
			}
		};
		
		try {
			atc.execute(new NullProgressMonitor(), null);
		} catch (ExecutionException e) {
			
		}
	}
}
public abstract List<ETable> getPossibleTables(); 
public abstract List<ETable> getApplicationTables();
protected EProject getEProject()
{
	return _eProject;
}
protected List<ETable> getSelectedTables()
{
	return _eTables;
}
}
