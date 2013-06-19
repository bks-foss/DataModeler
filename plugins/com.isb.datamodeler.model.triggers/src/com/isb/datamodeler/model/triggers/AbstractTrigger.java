package com.isb.datamodeler.model.triggers;

import org.eclipse.emf.edit.command.AbstractOverrideableCommand;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

/**
 * Trigger abstracto con un RecordingCommand interno que se encarga de grabar las modificaciones hechas por el comando
 * y del undo y el redo
 * Todos los triggers que precisen de undo/redo deberian extender esta clase
 * @author xIS05655
 *
 */
public abstract class AbstractTrigger extends AbstractOverrideableCommand {

	private RecordingCommand internalCommand;
	
	public AbstractTrigger(TransactionalEditingDomain domain) {
		super(domain);
		
		internalCommand = new RecordingCommand(domain) {
			@Override
			protected void doExecute() {
				try
				{
					executeTrigger();	
				}catch(Exception exc)
				{
					exc.printStackTrace();
				}
				
			}
		};
	}
	
	@Override
	public final void doExecute() {
		internalCommand.execute();
	}

	public abstract void executeTrigger();

	@Override
	public final void doRedo() {
		internalCommand.redo();		
	}

	@Override
	public final void doUndo() {
		internalCommand.undo();
	}

	@Override
	/**
	 * Sobreescribir si se quiere evitar la ejecución en determinados casos
	 */
	protected boolean prepare() {
		return true;
	}
	
	

}
