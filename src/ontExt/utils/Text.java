/**
 * 
 */
package ontExt.utils;

/**
 * Методы для обработки текста
 * 
 * @author Lomov PA.
 *
 */
public abstract class Text
{
    /**
     * Дает большее слово в строке.
     * 
     * @param str
     * @param delimiter
     *            разделитель слов в строке.
     * @return
     */
    public static String getBiggestWord(String str, String delimiter)
    {
	String[] words = str.split(delimiter);

	if (words != null && words.length > 0)
	{
	    String biggestWord = words[0];
	    for (String word : words)
		biggestWord = biggestWord.length() >= word.length() ? biggestWord : word;
	    return biggestWord;
	} else
	    return null;

    }

}
