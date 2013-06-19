package com.isb.datamodeler.constraints.properties;

import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;

import com.isb.datamodeler.constraints.EConstraintsPackage;
import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EPrimaryKey;
import com.isb.datamodeler.constraints.EReferenceConstraint;
import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.provider.DataModelerItemPropertyDescriptor;
import com.isb.datamodeler.provider.IItemPropertyDescriptorProvider;
import com.isb.datamodeler.tables.EBaseTable;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.ETable;

public class ReferenceConstraintItemPropertyDescriptorProvider extends
		AdapterImpl implements IItemPropertyDescriptorProvider {
	
	@Override
	public ItemPropertyDescriptor createItemPropertyDescriptor(
			AdapterFactory adapterFactory, ResourceLocator resourceLocator,
			String displayName, String description, EStructuralFeature feature,
			boolean isSettable, boolean multiLine, boolean sortChoices,
			Object staticImage, String category, String[] filterFlags) {

		return new ReferenceConstraintItemPropertyDescriptor(adapterFactory, 
				  resourceLocator, 
				  displayName, 
				  description, 
				  feature, 
				  isSettable, 
				  multiLine, 
				  sortChoices, 
				  staticImage, 
				  category, 
				  filterFlags);
	}

	
	class ReferenceConstraintItemPropertyDescriptor extends DataModelerItemPropertyDescriptor {
  
		public ReferenceConstraintItemPropertyDescriptor
		     (AdapterFactory adapterFactory,
		      ResourceLocator resourceLocator,
		      String displayName,
		      String description,
		      EStructuralFeature feature, 
		      boolean isSettable,
		      boolean multiLine,
		      boolean sortChoices,
		      Object staticImage,
		      String category,
		      String [] filterFlags)
		{
			super(adapterFactory, 
				  resourceLocator, 
				  displayName, 
				  description, 
				  feature, 
				  isSettable, 
				  multiLine, 
				  sortChoices, 
				  staticImage, 
				  category, 
				  filterFlags);
		}
		
		@Override
		public Collection<?> getChoiceOfValues(Object object) {
			
			if(feature==EConstraintsPackage.Literals.REFERENCE_CONSTRAINT__MEMBERS)
			{
				Collection<Object> choiceOfValues = new UniqueEList<Object>();
				
				//Filtramos las columnas, para ofrecer unicamente las definidas dentro de la tabla
				EBaseTable table = ((EReferenceConstraint)object).getBaseTable();
				
				if(object instanceof EPrimaryKey)
				{
					for(EColumn col:table.getColumns())
					{
						boolean include = true;
						// Si la columna no está incluida en ninguna constraint aparece en la lista
						if(col.getReferencingConstraints().size()>0)
						{
							// No se permite añadir a la lista de una PK una columna que forma parte de una
							// FK que referencia a esa tabla.
							for(EReferenceConstraint constraint: col.getReferencingConstraints())
							{
								if(constraint instanceof EForeignKey)
								{
									if(((EForeignKey)constraint).getParentTable() == table)
										include = false;
								}
							}
						}
						if(include)
							choiceOfValues.add(col);
					}
				}
				else if(object instanceof EUniqueConstraint)
				{
					for(EColumn col:table.getColumns())
					{
						boolean include = true;
						// Si la columna no está incluida en ninguna constraint aparece en la lista
						if(col.getReferencingConstraints().size()>0)
						{
							// No se permite añadir a la lista de una UK una columna que forma parte de una
							// FK que referencia a esa misma UK
							for(EReferenceConstraint constraint: col.getReferencingConstraints())
							{
								if(constraint instanceof EForeignKey)
								{
									if(((EForeignKey)constraint).getUniqueConstraint() == (EUniqueConstraint)object)
										include = false;
								}
							}
						}
						if(include)
							choiceOfValues.add(col);
					}					
				}
				else if(object instanceof EForeignKey)
				{
					for(EColumn col:table.getColumns())
					{
						boolean include = true;
						// Si la columna no está incluida en ninguna constraint aparece en la lista
						if(col.getReferencingConstraints().size()>0)
						{
							// Si una FK referencia a la misma tabla:
							if(((EForeignKey)object).getBaseTable()==((EForeignKey)object).getParentTable())
							{
								for(EReferenceConstraint constraint: col.getReferencingConstraints())
								{
									// No se pueden meter como miembros columnas que son parte de la PK
									if(constraint instanceof EPrimaryKey)
									{
										include = false;
									}
									// Si la FK referencia a una UK, no se pueden meter como miembros columnas que
									// son parte de esa misma UK.
									else if(constraint instanceof EUniqueConstraint)
									{
										if(((EUniqueConstraint)constraint)== ((EForeignKey)object).getUniqueConstraint())
											include = false;
									}
								}
							}
						}
						if(include)
							choiceOfValues.add(col);
					}					
				}
				return choiceOfValues;
			}
			
			//Filtramos las Constraints, para ofrecer unicamente las definidas dentro de la tabla destino
			if(feature==EConstraintsPackage.Literals.FOREIGN_KEY__UNIQUE_CONSTRAINT)
			{
				Collection<Object> choiceOfValues = new UniqueEList<Object>();
				
				EBaseTable referencedTable = ((EForeignKey)object).getParentTable();
				
				// Si una FK referencia a la misma tabla:
				if(((EForeignKey)object).getBaseTable()==referencedTable)
				{
					// Como clave referenciada no se puede seleccionar una clave que contenga
					// columnas que forman parte de la FK
					for(EUniqueConstraint uniqueConstraint :referencedTable.getUniqueConstraints())
					{
						boolean include = true;
						
						
						EList<EColumn> columnasMiembroFK = ((EForeignKey)object).getMembers();
						
						for(EColumn columnaMiembroUK :((EUniqueConstraint)uniqueConstraint).getMembers())
						{
							if(columnasMiembroFK.contains(columnaMiembroUK))
								include = false;
						}
						
						if(include)
							choiceOfValues.add((EUniqueConstraint)uniqueConstraint);
					}
				}else
					choiceOfValues.addAll(referencedTable.getUniqueConstraints());

				
				return choiceOfValues;
			}
			
			return super.getChoiceOfValues(object); 
		}
		
		@Override
		public boolean canSetProperty(Object object) {
			
			boolean defaultValue = super.canSetProperty(object);
			
			switch (feature.getEContainingClass().getEAllStructuralFeatures().indexOf(feature))
			{
				case EConstraintsPackage.FOREIGN_KEY__PARENT_CARDINALITY:
					return !((EForeignKey)object).isIdentifying();
				case EConstraintsPackage.FOREIGN_KEY__IDENTIFYING:
					// No permitimos cambiar una FK a identifing si la tabla origen y destino son la misma
					ETable baseTable = ((EForeignKey)object).getBaseTable();
					ETable parentTable = ((EForeignKey)object).getParentTable();
					if(!((EForeignKey)object).isIdentifying() && baseTable.equals(parentTable))
							return false;	
			}
			
			return defaultValue;
		}

	}
	
}
