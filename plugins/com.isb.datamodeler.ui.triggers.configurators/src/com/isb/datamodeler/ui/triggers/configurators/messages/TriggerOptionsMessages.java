package com.isb.datamodeler.ui.triggers.configurators.messages;

import org.eclipse.osgi.util.NLS;

public class TriggerOptionsMessages extends NLS {
	
	private static final String BUNDLE_NAME = "com.isb.datamodeler.ui.triggers.configurators.messages.TriggerOptionsMessages";//$NON-NLS-1$
	
	public static String ReferenceConstraintTriggersConfigurator_Remove_Unique_Constraint_Members;
	public static String ReferenceConstraintTriggersConfigurator_Remove_Unique_Constraint_Members_msg;
	public static String ReferenceConstraintTriggersConfigurator_Toogle_Label;

	public static String ReferenceConstraintTriggersConfigurator_Add_Unique_Constraint_Members;
	public static String ReferenceConstraintTriggersConfigurator_Matching_Columns_msg;
	public static String ReferenceConstraintTriggersConfigurator_Use_Existing_Child_Columns_Option;
	public static String ReferenceConstraintTriggersConfigurator_Replace_Existing_Columns_Option;
	public static String ReferenceConstraintTriggersConfigurator_Create_New_Columns_Option;

	public static String ColumnTriggersConfigurator_RenameParentColumn;
	public static String ColumnTriggersConfigurator_RenameChildColumn;
	
	public static String TableConstraintInitializerTriggerConfigurator_Select_UK;
	public static String TableConstraintInitializerTriggerConfigurator_New_FK;
	
	public static String ReferenceConstraintTriggersConfigurator_Invalid_Option_Message;
	public static String ReferenceConstraintTriggersConfigurator_Invalid_Option_Message_Title;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, TriggerOptionsMessages.class);
	}
}
