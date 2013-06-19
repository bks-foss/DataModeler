package com.isb.datamodeler.messages;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DataModelerMessages
{

private final static char[] DOUBLE_QUOTES = "''".toCharArray();  //$NON-NLS-1$
private final static char[] SINGLE_QUOTE = "'".toCharArray();  //$NON-NLS-1$

/**
 * Lookup the message with the given ID in this catalog and bind its
 * substitution locations with the given string values.
 */
public static String bind(String id, String[] bindings, ResourceBundle resBundle) 
{	
	if (id == null || resBundle==null)
		return "No message available"; //$NON-NLS-1$
	String message = null;
	try
	{
		message = resBundle.getString(id);
	}
	catch (MissingResourceException e)
	{
		// If we got an exception looking for the message, fail gracefully by just returning
		// the id we were looking for.  In most cases this is semi-informative so is not too bad.
		return "Missing message: " + id + " in: " + resBundle.toString(); //$NON-NLS-2$ //$NON-NLS-1$
	}
	// for compatibility with MessageFormat which eliminates double quotes in original message
	char[] messageWithNoDoubleQuotes =
	replace(message.toCharArray(), DOUBLE_QUOTES, SINGLE_QUOTE);
	message = new String(messageWithNoDoubleQuotes);
	
	if (bindings == null || bindings.length==0)
		return message;
	
	int length = message.length();
	int start = -1;
	int end = length;
	StringBuffer output = new StringBuffer(80);
	while (true)
	{
		if ((end = message.indexOf('{', start)) > -1)
		{
			output.append(message.substring(start + 1, end));
			if ((start = message.indexOf('}', end)) > -1)
			{
				int index = -1;
				try
				{
					index = Integer.parseInt(message.substring(end + 1, start));
					output.append(bindings[index]);
				}
				catch (NumberFormatException nfe)
				{
					output.append(message.substring(end + 1, start + 1));
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
					output.append("{missing " + Integer.toString(index) + "}"); //$NON-NLS-2$ //$NON-NLS-1$
				}
			}
			else
			{
				output.append(message.substring(end, length));
				break;
			}
		}
		else
		{
			output.append(message.substring(start + 1, length));
			break;
		}
	}
	return output.toString();
}

/**
 * Answers a new array of characters with substitutions. No side-effect is operated on the original
 * array, in case no substitution happened, then the result is the same as the
 * original one.
 * <br>
 * <br>
 * For example:
 * <ol>
 * <li><pre>
 *    array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
 *    toBeReplaced = { 'b' }
 *    replacementChar = { 'a', 'a' }
 *    result => { 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a' }
 * </pre>
 * </li>
 * <li><pre>
 *    array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
 *    toBeReplaced = { 'c' }
 *    replacementChar = { 'a' }
 *    result => { 'a' , 'b', 'b', 'a', 'b', 'a' }
 * </pre>
 * </li>
 * </ol>
 * 
 * @param array the given array
 * @param toBeReplaced characters to be replaced
 * @param replacementChars the replacement characters
 * @return a new array of characters with substitutions or the given array if none
 * @exception NullPointerException if the given array is null
 */
private static final char[] replace(
	char[] array,
	char[] toBeReplaced,
	char[] replacementChars)
{

	int max = array.length;
	int replacedLength = toBeReplaced.length;
	int replacementLength = replacementChars.length;

	int[] starts = new int[5];
	int occurrenceCount = 0;

	if (!equals(toBeReplaced, replacementChars))
	{
		next : for (int i = 0; i < max; i++)
		{
			int j = 0;
			while (j < replacedLength)
			{
				if (i + j == max)
					continue next;
				if (array[i + j] != toBeReplaced[j++])
					continue next;
			}
			if (occurrenceCount == starts.length)
			{
				System.arraycopy(
					starts,
					0,
					starts = new int[occurrenceCount * 2],
					0,
					occurrenceCount);
			}
			starts[occurrenceCount++] = i;
		}
	}
	if (occurrenceCount == 0)
		return array;
	char[] result =
		new char[max
			+ occurrenceCount * (replacementLength - replacedLength)];
	int inStart = 0, outStart = 0;
	for (int i = 0; i < occurrenceCount; i++)
	{
		int offset = starts[i] - inStart;
		System.arraycopy(array, inStart, result, outStart, offset);
		inStart += offset;
		outStart += offset;
		System.arraycopy(
			replacementChars,
			0,
			result,
			outStart,
			replacementLength);
		inStart += replacedLength;
		outStart += replacementLength;
	}
	System.arraycopy(array, inStart, result, outStart, max - inStart);
	return result;
}

/**
 * Answers true if the two arrays are identical character by character, otherwise false.
 * The equality is case sensitive.
 * <br>
 * <br>
 * For example:
 * <ol>
 * <li><pre>
 *    first = null
 *    second = null
 *    result => true
 * </pre>
 * </li>
 * <li><pre>
 *    first = { }
 *    second = null
 *    result => false
 * </pre>
 * </li>
 * <li><pre>
 *    first = { 'a' }
 *    second = { 'a' }
 *    result => true
 * </pre>
 * </li>
 * <li><pre>
 *    first = { 'a' }
 *    second = { 'A' }
 *    result => false
 * </pre>
 * </li>
 * </ol>
 * @param first the first array
 * @param second the second array
 * @return true if the two arrays are identical character by character, otherwise false
 */
private static final boolean equals(char[] first, char[] second)
{
	if (first == second)
		return true;
	if (first == null || second == null)
		return false;
	if (first.length != second.length)
		return false;

	for (int i = first.length; --i >= 0;)
		if (first[i] != second[i])
			return false;
	return true;
}
}
