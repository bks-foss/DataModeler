package com.isb.datamodeler.model.triggers;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.schema.EDataModelerNamedElement;
import com.isb.datamodeler.schema.ESQLObject;

/**
 * Trigger abstracto con el método genérico de inicialización
 * @author xIS05655
 *
 */
public abstract class AbstractInitializerTrigger extends AbstractTrigger {

	public AbstractInitializerTrigger(TransactionalEditingDomain domain) {
		super(domain);
	}
	
	public void initializeBasics(ESQLObject newElement, EList<? extends ESQLObject> existingChildren, String baseName) {
		
		//Si ya está inicializado no hacemos nada
		if(newElement.getId()!=null && !newElement.getId().equals(""))
			return;
		
		//Si no tiene nombre, lo inicializamos
		if(newElement.getName()==null || newElement.getName().equals(""))
		{
			String newDefaultName = getNewDefaultName(existingChildren, baseName);
			newElement.setName(newDefaultName);
		}else
		{
			//Si ya tiene nombre, se recalcula para evitar colisiones:
			String newName = getNewDefaultName(existingChildren, newElement.getName());
			if(!newElement.equals(newName))
				newElement.setName(newName);
				
		}
		
		//Inicializamos Id
		String newId = generateRandomID(16);
		newElement.setId(newId);
		
	}
	
	private String getNewDefaultName(EList<? extends ESQLObject> existingChildren, String baseName)
	{
		int i=0;
		
		String newName = baseName;
		
		while(hasChildWithSameName(existingChildren, newName))
			newName = baseName + ++i;
		
		return newName;
	}

	private boolean hasChildWithSameName(EList<? extends ESQLObject> existingChildren, String name)
	{
		for(EDataModelerNamedElement namedElement:existingChildren)
			if(namedElement.getName()!=null && namedElement.getName().equalsIgnoreCase(name))
				return true;
		
		return false;
	}
	
	/**
	 * @param idLength number of digits to return in the random string generated
	 * @return A random identifier of up to idLength digits
	 */
	public static String generateRandomID(int idLength)
	{
	    String finalString = ""; //$NON-NLS-1$
	    //int pasadas = 0;
	    while(finalString.length()<idLength)
	    {
//		    	Generamos el nº aleatorio y quitamos el "0." para que no acabe nunca en "."
	    	String auxString = String.valueOf(Math.random()).substring(2);
	    	 
	    	 finalString += auxString.substring(2);
	    }
	    
		return finalString.substring(0,idLength);
	}
}
