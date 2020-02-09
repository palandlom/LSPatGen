/**
 * 
 */
package ontExt.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Содержит статические методы обработки текста.
 * @author Lomov PA.
 *
 */

public  class TextUtils
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TextUtils.class);

	/**
	 * Величина расширения найденного фрагмента текста влево и вправо.
	 */
	final static public int EXTENSION_FRAGMENT_SIZE = 50;

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
    

	/**
	 * Возвращает буфер с прочитанным файлом
	 * 
	 * @param filePathStr
	 *            путь к файлу для чтения.
	 * @return
	 */
	static public BufferedReader getTextBufferedReader(String filePathStr)
	{
		Path path = Paths.get(filePathStr);
		// BufferedReader reader = null;
		try (BufferedReader reader = Files.newBufferedReader(path);)
		{
			return reader;
		} catch (IOException e)
		{
			LOGGER.error("Error during reading file: {} ", path);
			e.printStackTrace();
		}

		return null;
	}

	static public String getFragment1(File file, long startPos, long endPos)
	{
		int ch;
		StringBuffer strBuf = new StringBuffer();

		try (FileInputStream fis = new FileInputStream(file);)
		{
			InputStreamReader inStreamReader = new InputStreamReader(fis,
					Charset.forName("CP1251"));
			BufferedReader buf = new BufferedReader(inStreamReader);

			buf.skip(startPos); // пропускаем нужное количество символов
			long i = startPos;
			
			do
			{
				ch = buf.read();
				strBuf.append((char)ch);
				i++;
			} while (ch != -1 && i < endPos);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return strBuf.toString();
	}

	/**
	 * Получаем фрагмент текста из файла
	 * 
	 * @param file
	 * @param startPos
	 * @param endPos
	 * @return
	 */
	static public String getFragment(File file, long startPos, long endPos)
	{
		String fragment = null;
		LOGGER.info("Text.getFragment from - " + file.getPath() + "|| S:"
				+ startPos + " E:" + endPos);
		StringBuffer buf = new StringBuffer();

		try (RandomAccessFile rfile = new RandomAccessFile(file, "r");)
		{ /* проверки ... */
			if ((endPos < startPos) || (endPos > rfile.length()))
			{
				LOGGER.error("WARN - Wrong positions:\n" + " StaPos:" + startPos
						+ "\n" + " EndPos:" + endPos + " File length:"
						+ rfile.length() + "\n");
				System.exit(1);
			}
			startPos = startPos > 0 ? startPos : 0;

			/* Вырезаем фрагмент в массив символов... */
			rfile.seek(startPos);
			for (long i = 0; i < endPos - startPos; i++)
			{
				buf.append((char) rfile.read());
			}

			fragment = new String(buf.toString().getBytes("ISO-8859-1"),
					Charset.forName("CP1251"));

		} catch (Exception e)
		{
			LOGGER.error("Error during getting fragment: " + " StaPos:"
					+ startPos + " EndPos:" + endPos + " Size fragment: "
					+ (endPos - startPos));
		}
		return fragment;
	};


}
