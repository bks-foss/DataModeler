package com.isb.datamodeler.internal.ui.dialogs;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.util.Assert;

import com.isb.datamodeler.ui.DataModelerUI;

/**
 * Un IStatus asignable. 
 * Puede ser un error, warning, info o ok. 
 * Para estados de error, info y warning states, un mensaje describe el problema.
 */
public class StatusInfo implements IStatus
{

	private String fStatusMessage;
	private int fSeverity;

	/**
	 * Crea un "status" asignado a OK (sin mensaje)
	 */
	public StatusInfo()
	{
		this(OK, null);
	}

	/**
	 * Crea un status.
	 * @param severity La severidad de status: ERROR, WARNING, INFO y OK.
	 * @param message mensaje del status. Sólo para ERROR, WARNING y INFO.
	 */
	public StatusInfo(int severity, String message)
	{
		fStatusMessage = message;
		fSeverity = severity;
	}

	public boolean isOK()
	{
		return fSeverity == IStatus.OK;
	}

	public boolean isWarning()
	{
		return fSeverity == IStatus.WARNING;
	}

	public boolean isInfo()
	{
		return fSeverity == IStatus.INFO;
	}

	public boolean isError()
	{
		return fSeverity == IStatus.ERROR;
	}

	public String getMessage()
	{
		return fStatusMessage;
	}

	public void setError(String errorMessage)
	{
		Assert.isNotNull(errorMessage);
		fStatusMessage = errorMessage;
		fSeverity = IStatus.ERROR;
	}

	public void setWarning(String warningMessage)
	{
		Assert.isNotNull(warningMessage);
		fStatusMessage = warningMessage;
		fSeverity = IStatus.WARNING;
	}

	public void setInfo(String infoMessage)
	{
		Assert.isNotNull(infoMessage);
		fStatusMessage = infoMessage;
		fSeverity = IStatus.INFO;
	}

	public void setOK()
	{
		fStatusMessage = null;
		fSeverity = IStatus.OK;
	}

	public boolean matches(int severityMask)
	{
		return (fSeverity & severityMask) != 0;
	}

	/**
	 * Devuelve siempre <code>false</code>.
	 * @see IStatus#isMultiStatus()
	 */
	public boolean isMultiStatus()
	{
		return false;
	}

	/*
	 * @see IStatus#getSeverity()
	 */
	public int getSeverity()
	{
		return fSeverity;
	}

	/*
	 * @see IStatus#getPlugin()
	 */
	public String getPlugin()
	{
		return DataModelerUI.PLUGIN_ID;
	}

	/**
	 * Devuelve siempre <code>null</code>.
	 * @see IStatus#getException()
	 */
	public Throwable getException()
	{
		return null;
	}

	/**
	 * @see IStatus#getCode()
	 */
	public int getCode()
	{
		return fSeverity;
	}

	/**
	 * Devuelve siempre <code>null</code>.
	 * @see IStatus#getChildren()
	 */
	public IStatus[] getChildren()
	{
		return new IStatus[0];
	}

}