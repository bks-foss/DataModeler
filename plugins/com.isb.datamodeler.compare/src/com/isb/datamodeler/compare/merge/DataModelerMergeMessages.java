package com.isb.datamodeler.compare.merge;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;

import com.isb.datamodeler.compare.messages.Messages;
import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.tables.EPersistentTable;

public class DataModelerMergeMessages {

	public Map <EForeignKey,EPersistentTable> notMergedElements = null;
	private static DataModelerMergeMessages _instance = null;
	
	private DataModelerMergeMessages() {
		notMergedElements = new HashMap<EForeignKey,EPersistentTable> ();	
	}
	
	public static DataModelerMergeMessages getInstance() {
		if (_instance == null)		
			_instance = new DataModelerMergeMessages();
		
		return _instance;
	}
	
	public void dispose() {
		if (notMergedElements != null)
			notMergedElements = null;
		_instance = null;
	}
	
	public void addElement (EForeignKey key ,EPersistentTable table) {
		if (notMergedElements != null && !notMergedElements.containsKey(key))					
			notMergedElements.put(key, table);	
	}
	
	public void showMessages () {
		if (notMergedElements != null && notMergedElements.size() > 0 ) {
			
			Iterator it = notMergedElements.entrySet().iterator();
			String message = Messages.bind("datamodeler.merge.FK.problemsToMerge.text"); //$NON-NLS-1$
			while (it.hasNext()) {
				message += "\n\n"; //$NON-NLS-1$ 
				Map.Entry e = (Map.Entry)it.next();
				System.out.println(e.getKey() + " " + e.getValue());
				message += Messages.bind("datamodeler.merge.FK.problemsToMerge.text1",  //$NON-NLS-1$
						new String[] {((EForeignKey)e.getKey()).getName(),
						((EPersistentTable) e.getValue()).getName(),
						((EPersistentTable) e.getValue()).getSchema().getName()});
				}
				
				MessageDialog.openWarning(null, Messages.bind("datamodeler.merge.FK.problemsToMerge.title"), //$NON-NLS-1$ 
					message); 	
			}								
	}
}
