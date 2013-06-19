/*
 * Created on 19-ago-2003 To change this generated comment go to Window>Preferences>Java>Code Generation>Code Template
 */
package com.isb.datamodeler.internal.ui.dialogs;

import org.eclipse.gef.dnd.SimpleObjectTransfer;
import org.eclipse.swt.dnd.Transfer;

/**
 * @author Delia Salmerón
 * <p>
 * Implementación base de <code>SimpleObjectTransfer</code>.
 */
public class ElementTransfer extends SimpleObjectTransfer
{
	private static final ElementTransfer _Instance = new ElementTransfer();
	
	private static final String TYPE_NAME = "Element transfer" //$NON-NLS-1$
		+ System.currentTimeMillis() + ":" + _Instance.hashCode(); //$NON-NLS-1$
	
	private static final int TYPEID = registerType(TYPE_NAME);

public ElementTransfer()
{
}

/**
 * Returns the singleton instance
 * @return the singleton
 */
public static ElementTransfer getInstance()
{
	return _Instance;
}

/**
 * Returns the <i>template</i> object.
 * @return the template
 */
public Object[] getElements()
{
	return (Object[])getObject();
}

/**
 * @see Transfer#getTypeIds()
 */
protected int[] getTypeIds()
{
	return new int[]{TYPEID};
}

/**
 * @see Transfer#getTypeNames()
 */
protected String[] getTypeNames()
{
	return new String[]{TYPE_NAME};
}

/**
 * Sets the <i>template</template> Object.
 * @param template the template
 */
public void setElement(Object[] elements)
{
	setObject(elements);
}
}
