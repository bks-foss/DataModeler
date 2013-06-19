/**
 * 
 */
package com.isb.datamodeler.internal.ui.dialogs;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.isb.datamodeler.internal.ui.views.actions.DataModelerProblems;
import com.isb.datamodeler.model.core.validation.DataModelDiagnostic;

/**
 * @author Félix Velasco
 *
 */
public class ExportDialog {

	private static final String STYLE = "<style type='text/css'>\n"
			+ "body {\n"
			+ "  color: #4F3B21; \n"
			+ "  background-color: cae1ff;\n"
			+ "}\n"
			+ "h1\n"
			+ "{\n"
			+ "  text-align:center;\n"
			+ "}\n"
			+ "ul\n" + "{\n"
			+ "  background-color:#b0c4de;\n"
			+ "  padding:10px;\n"
			+ "  border:3px solid gray;\n"
			+ "  margin:10px;\n"
			+ "}\n"
			+ "li.error\n"
			+ "{\n"
			+ "  margin:10px;\n"
			+ "}\n"
			+ "</style>\n";
	private Shell _shell;
	private String _basename;
	private String _repositoryInfo;
	private String _reportDateInfo;
	private String _fileDateInfo;
	
	private Map<String, Collection<DataModelerProblems>> _inputs;

	/**
	 * @param errors 
	 * @param shell 
	 * 
	 */
	public ExportDialog(Shell shell, Map<String,
			Collection<DataModelerProblems>> inputs, String basename,
			String repositoryInfo) {
		_shell = shell;
		_inputs = inputs;
		_basename = basename;
		Date date = new Date();
		DateFormat fileDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		_fileDateInfo = fileDateFormat.format(date);
		DateFormat reportDateFormat = new SimpleDateFormat("yyyy.MM.dd '-' HH:mm");
		_reportDateInfo = reportDateFormat.format(date);
		_repositoryInfo = repositoryInfo;
	}

	public void show() {
		FileDialog dialog = new FileDialog(_shell, SWT.SAVE);
		dialog.setFilterNames(new String[] { "Html Files", "All Files (*.*)" });
		dialog.setFilterExtensions(new String[] { "*.htm,*.html", "*.*" });
		dialog.setFilterPath(null);
		dialog.setFileName(_basename + _fileDateInfo + ".html");
		dialog.setOverwrite(true);
		String filename = dialog.open();

		if (filename!=null)
			try
			{
				save(filename);
			}
			catch (IOException ioe)
			{
				MessageDialog.openError(_shell, "Error al guardar", ioe.getMessage());
			}
	}

	private void save(String filename) throws IOException {
		File f = new File(filename);
		boolean canWrite = f.createNewFile() || f.canWrite();

		if (!canWrite)
			throw new IOException("No tengo permiso para crear/escribir en " + filename);

		PrintWriter pw = new PrintWriter(f);

		printHeader(pw);
		printBody(pw);
		printFooter(pw);
		
		pw.close();
	}

	private void printHeader(PrintWriter pw) throws IOException
	{
		pw.println("<HTML>");
		pw.println("<HEAD>");
		pw.println("<TITLE>" + "Informe para " + _basename + "</TITLE>");
		pw.println(STYLE);
		pw.println("</HEAD>");
	}

	private void printBody(PrintWriter pw) throws IOException
	{
		pw.println("<BODY>");
		pw.println("<H1>" + " Informe para " + _basename + "</H1>");
		pw.println("<H2>" + "Fecha: " + _reportDateInfo + "</H2>");
		pw.println("<H2>" + "Repositorio: " + _repositoryInfo + "</H2>");
		
		for (String inputElement : _inputs.keySet())
		{
			pw.println("<UL>");
			pw.println(inputElement);
			for(DataModelerProblems message : _inputs.get(inputElement))
			{
				pw.println("<LI class='error'>");
				int intSeverity = message.getSeverity();
				
				String severity = "<font color="+"yellow"+">WARNING: </font>";
				if(intSeverity==DataModelDiagnostic.ERROR)
					severity = "<font color="+"red"+">ERROR: </font>";;
				pw.println(severity + message.getMessage());
				pw.println("</LI>");
			}
			pw.println("</UL>");
		}
		
		pw.println("</BODY>");
		
	}

	private void printFooter(PrintWriter pw) throws IOException
	{
	
	}

}
