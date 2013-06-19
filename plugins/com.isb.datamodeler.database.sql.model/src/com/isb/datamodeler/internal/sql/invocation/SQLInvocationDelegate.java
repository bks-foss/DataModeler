package com.isb.datamodeler.internal.sql.invocation;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.BasicInternalEList;
import org.eclipse.emf.ecore.util.BasicInvocationDelegate;

import sqldatabase.SQLDatabase;
import sqldatabase.SQLPrimitiveType;
import sqldatabase.SqldatabasePackage;

import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.datatypes.EApproximateNumericDataType;
import com.isb.datamodeler.datatypes.EBigBinaryStringDataType;
import com.isb.datamodeler.datatypes.EBigCharacterStringDataType;
import com.isb.datamodeler.datatypes.EBinaryStringDataType;
import com.isb.datamodeler.datatypes.ECharacterStringDataType;
import com.isb.datamodeler.datatypes.EDatatypesFactory;
import com.isb.datamodeler.datatypes.EFixedPrecisionDataType;
import com.isb.datamodeler.datatypes.EPredefinedDataType;
import com.isb.datamodeler.datatypes.EPrimitiveDataType;
import com.isb.datamodeler.datatypes.Units;
import com.isb.datamodeler.model.triggers.DataModelerTriggersPlugin;
import com.isb.datamodeler.model.triggers.initializers.IDatamodelerInitializer;
import com.isb.datamodeler.tables.EPersistentTable;

public class SQLInvocationDelegate extends BasicInvocationDelegate{

	public SQLInvocationDelegate(EOperation operation) {
		super(operation);
	}
	
	@Override
	public Object dynamicInvoke(InternalEObject target, EList<?> arguments)
			throws InvocationTargetException {
		
	      switch (eOperation.getEContainingClass().getEAllOperations().indexOf(eOperation))
	      {	
	      	case SqldatabasePackage.SQL_DATABASE___CREATE_NEW_PREDEFINED_DATA_TYPE__EPRIMITIVEDATATYPE:
	            return createPredefinedDatatype((SQLDatabase)target, (EPrimitiveDataType)arguments.get(0));		            
	      	case SqldatabasePackage.SQL_DATABASE___LOAD_PRIMITIVE_DATATYPES:
	      		return loadPrimitiveDatatypes();
	      	case SqldatabasePackage.SQL_DATABASE___INITIALIZE_FOREIGN_KEY_NAME__EBASETABLE_EBASETABLE:
//	      		return initializeForeignKeyName((EBaseTable)arguments.get(0), (EBaseTable)arguments.get(1));
	      	case SqldatabasePackage.SQL_DATABASE___INITIALIZE_UNIQUE_CONSTRAINT_NAME__EBASETABLE_EUNIQUECONSTRAINT:
	      		return initializeUniqueConstraintName((EPersistentTable)arguments.get(0), (EUniqueConstraint)arguments.get(1));
	      	case SqldatabasePackage.SQL_DATABASE___VALIDATE_EDATA_TYPE__EDATATYPE_ESQLOBJECT_STRING_OBJECT:
//	      		return validateEDataType((EDataType)arguments.get(0), (ESQLObject)arguments.get(1) , (String)arguments.get(2), (Object)arguments.get(3));
	      }
		
		return super.dynamicInvoke(target, arguments);
	}

	
	private Object initializeUniqueConstraintName(EPersistentTable eBaseTable,
			EUniqueConstraint eUniqueConstraint) {
			
		IDatamodelerInitializer initializer = DataModelerTriggersPlugin.getInstance().getInitializer(eBaseTable); 
		if(initializer!=null)
			initializer.initialize(eBaseTable, eUniqueConstraint);
		
		return eUniqueConstraint.getName();
	}

	private EPredefinedDataType createPredefinedDatatype(SQLDatabase target,
			EPrimitiveDataType type)
	{
		EPredefinedDataType predefinedDataType = null;
		
		switch(type.getValue()){
			case SQLPrimitiveType.CHARACTER_VALUE: 
				predefinedDataType = EDatatypesFactory.eINSTANCE.createCharacterStringDataType();
				((ECharacterStringDataType)predefinedDataType).setLength(4);
				break;
			case SQLPrimitiveType.CHARACTER_LARGE_OBJECT_VALUE: //CLOB
				predefinedDataType = EDatatypesFactory.eINSTANCE.createBigCharacterStringDataType();
				((EBigCharacterStringDataType)predefinedDataType).setLength(1);
				((EBigCharacterStringDataType)predefinedDataType).setUnit(Units.MB);
				break;
			case SQLPrimitiveType.CHARACTER_VARYING_VALUE:
				predefinedDataType = EDatatypesFactory.eINSTANCE.createCharacterStringDataType();
				((ECharacterStringDataType)predefinedDataType).setLength(50);
				break;

			case SQLPrimitiveType.BINARY_VALUE:
				predefinedDataType = EDatatypesFactory.eINSTANCE.createBinaryStringDataType(); 
				((EBinaryStringDataType)predefinedDataType).setLength(1);
				break;
				
			case SQLPrimitiveType.BINARY_VARYING_VALUE:
				predefinedDataType = EDatatypesFactory.eINSTANCE.createBinaryStringDataType(); 
				((EBinaryStringDataType)predefinedDataType).setLength(50);
				break;
			case SQLPrimitiveType.BINARY_LARGE_OBJECT_VALUE: //BLOB
				predefinedDataType = EDatatypesFactory.eINSTANCE.createBigBinaryStringDataType(); 
				((EBigBinaryStringDataType)predefinedDataType).setLength(1);
				((EBigBinaryStringDataType)predefinedDataType).setUnit(Units.MB);
				break;
			
			case SQLPrimitiveType.DECIMAL_VALUE:
			case SQLPrimitiveType.NUMERIC_VALUE:
				predefinedDataType = EDatatypesFactory.eINSTANCE.createFixedPrecisionDataType(); 
				((EFixedPrecisionDataType)predefinedDataType).setPrecision(15);
				((EFixedPrecisionDataType)predefinedDataType).setScale(2);
				break;
			case SQLPrimitiveType.SMALLINT_VALUE:
			case SQLPrimitiveType.INTEGER_VALUE:
			case SQLPrimitiveType.BIGINT_VALUE:
				predefinedDataType = EDatatypesFactory.eINSTANCE.createIntegerDataType(); 
				break;
			case SQLPrimitiveType.FLOAT_VALUE:
			case SQLPrimitiveType.REAL_VALUE:
			case SQLPrimitiveType.DOUBLE_PRECISION_VALUE:
				predefinedDataType = EDatatypesFactory.eINSTANCE.createApproximateNumericDataType(); 
				((EApproximateNumericDataType)predefinedDataType).setPrecision(8);
				break;
			case SQLPrimitiveType.TIME_VALUE:
			case SQLPrimitiveType.TIME_WITH_TIMEZONE_VALUE:
			case SQLPrimitiveType.TIMESTAMP_VALUE:
			case SQLPrimitiveType.TIMESTAMP_WITH_TIMEZONE_VALUE:
				predefinedDataType = EDatatypesFactory.eINSTANCE.createTimeDataType(); break;
			case SQLPrimitiveType.INTERVAL_VALUE:
				predefinedDataType = EDatatypesFactory.eINSTANCE.createIntervalDataType(); break;
			case SQLPrimitiveType.BOOLEAN_VALUE:
				predefinedDataType = EDatatypesFactory.eINSTANCE.createBooleanDataType(); break;
			case SQLPrimitiveType.DATALINK_VALUE:
				predefinedDataType = EDatatypesFactory.eINSTANCE.createDataLinkDataType(); break;
			case SQLPrimitiveType.DATE_VALUE:
				predefinedDataType = EDatatypesFactory.eINSTANCE.createDateDataType(); break;
			case SQLPrimitiveType.XML_VALUE:
				predefinedDataType = EDatatypesFactory.eINSTANCE.createXMLDataType(); break;
			case SQLPrimitiveType.ROWID_VALUE:
				predefinedDataType = EDatatypesFactory.eINSTANCE.createRowIDDataType(); break;
		}
		
		if(predefinedDataType!=null)
		{
			predefinedDataType.setPrimitiveType(type);
			predefinedDataType.setName(type.getName());
		}
			
		return predefinedDataType;
	}
	
	private EList<EPrimitiveDataType> loadPrimitiveDatatypes()
	{
		
		EList<EPrimitiveDataType> datatypesList = new BasicInternalEList<EPrimitiveDataType>(EPrimitiveDataType.class);
		for(SQLPrimitiveType type: SQLPrimitiveType.VALUES)
		{
			EPrimitiveDataType primitiveDataType = EDatatypesFactory.eINSTANCE.createPrimitiveDataType();
			primitiveDataType.setValue(type.getValue());
			primitiveDataType.setName(type.getLiteral());
			primitiveDataType.setLabel(type.getLiteral());
			primitiveDataType.setId(type.getName());
			
			datatypesList.add(primitiveDataType);
		}
		return datatypesList;	
	}
	
//	getPrimitiveDataTypes().addAll(loadPrimitiveDatatypes());
//	for(EPrimitiveDataType primitiveType: primitiveDataTypes)
//	{
//		if(primitiveType.equals(SQLPrimitiveType.CHARACTER))
//			setDefaultPrimitiveDataType(primitiveType);
//	}
	
}
