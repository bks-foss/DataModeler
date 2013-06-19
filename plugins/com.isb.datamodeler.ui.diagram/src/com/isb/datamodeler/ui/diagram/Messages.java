package com.isb.datamodeler.ui.diagram;

import java.util.Locale;
import java.util.ResourceBundle;

import com.isb.datamodeler.messages.DataModelerMessages;

public class Messages
{

private static String bundleName = "com.isb.datamodeler.ui.diagram.messages";//$NON-NLS-1$
private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(bundleName, Locale.getDefault());

/**
* Lookup the message with the given ID in this catalog 
*/
public static String bind(String id) 
{
	return bind(id, new String[]{});
}

/**
* Lookup the message with the given ID in this catalog and bind its
* substitution locations with the given string.
*/
public static String bind(String id, String binding) 
{
	return bind(id, new String[]{binding});
}

/**
* Lookup the message with the given ID in this catalog and bind its
* substitution locations with the given strings.
*/
public static String bind(String id, String[] bindings) 
{
	return DataModelerMessages.bind(id, bindings, RESOURCE_BUNDLE);
}
}
