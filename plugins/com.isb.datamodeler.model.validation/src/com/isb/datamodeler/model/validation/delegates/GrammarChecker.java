package com.isb.datamodeler.model.validation.delegates;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.isb.datamodeler.model.validation.Messages;


/**
 * 
 * Comprueba algunas reglas para la descripción.
 *   
 */
public class GrammarChecker
{
	private static final String GrammarChecker_NumbersBetweenLetters = Messages.bind("GrammarChecker.grammarChecker_numbersBetweenLetters"); //$NON-NLS-1$
	private static final String GrammarChecker_RepeatedNumbers = Messages.bind("GrammarChecker.grammarChecker_repeatedNumbers");//$NON-NLS-1$
	private static final String GrammarChecker_RepeatedLetters = Messages.bind("GrammarChecker.grammarChecker_repeatedLetters");//$NON-NLS-1$
	private static final String GrammarChecker_RepeatedWords = Messages.bind("GrammarChecker.grammarChecker_repeatedWords");//$NON-NLS-1$
	private static final String GrammarChecker_FewWords = Messages.bind("GrammarChecker.grammarChecker_fewWords");//$NON-NLS-1$

public static String checkGrammar(String text)
{
	if(text == null)
		return "";
	
	text += " ";
	String[] patterns = 
	{
		"[A-Za-z]+\\d{5}[A-Za-z]+",	// Más de 5 números entre letras
		"[\\d]{10}+",			 	// Números repetidos más de 10 veces
		"(\\w)\\1{6,}",				// Letras repetidas mas de 6 veces
		"(\\w+ \\s*){2}\\1"			// Palabras repetidas más de 2 veces
	};
	
	String[] errorMessages = 
	{
		GrammarChecker_NumbersBetweenLetters,
		GrammarChecker_RepeatedNumbers,
		GrammarChecker_RepeatedLetters,
		GrammarChecker_RepeatedWords,
		GrammarChecker_FewWords,
	};
	
	for (int i=0; i<patterns.length; i++)
	{
		Matcher matcher = Pattern.compile(patterns[i]).matcher(text);
		if(matcher.find())
			return errorMessages[i];
	}

	// La descripción debe tener más de 5 palabras
	Matcher matcher = Pattern.compile("\\w+").matcher(text);
	int numWords = 0;
	while(matcher.find()) 
		numWords++;
	
	if(numWords < 5)
		return GrammarChecker_FewWords;
	
	return "";
}

}
