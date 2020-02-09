package ontExt.lsplProcessor.XMLElements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Границы фрагмента
 * 
 * @author lomov
 *
 */
public class FragmentPosition
{

    private static final Logger LOGGER = LoggerFactory.getLogger(FragmentPosition.class);

    long startPos;
    long endPos;
    File textfile;

    /**
     * Смещает стартовую границу влево до ближайшего конца предложения.
     * 
     * @param extNum
     *            величина отрезка для поиска конца предложения
     */
    public void extendStartPos(int extNum)
    {
	LOGGER.info("Cur Start position:" + this.getStartPos());
	RandomAccessFile raf;
	StringBuffer strBuf;
	try
	{
	    raf = new RandomAccessFile(this.textfile, "r");
	    /* Чтение на каж. шаге делается от curReadPos до .startPos.. */
	    /* ... curReadPos на каждом шаге сдвигается влево на extNum */
	    /* ... nextReadPos - это curReadPos на след. шаге */
	    /* ... при чтении инктрементируетсмя cursor... */
	    long nextReadPos = this.startPos - extNum > 0 ? this.startPos - extNum : 0;
	    long curReadPos, sentEndPos, cursor;
	    String fragment = null;

	    do
	    {
		curReadPos = nextReadPos;
		cursor = curReadPos;
		raf.seek(curReadPos);
		strBuf = new StringBuffer();
		while ((cursor < this.startPos))
		{
		    strBuf.append((char) raf.read());
		    cursor++;
		}
		/* ... получаем фрагмент для поиска конца предложения... */
		fragment = new String(strBuf.toString().getBytes("ISO-8859-1"), Charset.forName("CP1251"));
		/* ... вычисляем nextReadPos (curReadPos для следующего шага) */
		nextReadPos = nextReadPos - extNum > 0 ? nextReadPos - extNum : 0;

		/* Определяем (последний) индекс конца предл. в тек. фрагменте */
		sentEndPos = this.getLastSentenceEnd(fragment);
		/* ... если индекса нет + не достиг.. начало продолжаем... */
	    } while (sentEndPos < 0 && curReadPos > 0);
	    /* Обновляем левую границу = под найденное начало пред-ия */
	    this.setStartPos(curReadPos + sentEndPos);

	} catch (Exception e)
	{
	    // TODO: handle exception
	}

	LOGGER.info("New Start position:" + this.getStartPos());
    }

    /**
     * Смещает стартовую границу влево до ближайшего конца предложения.
     * 
     * @param extNum
     *            величина отрезка для поиска конца предложения
     */
    public void extendEndPos(int extNum)
    {
	LOGGER.info("Cur End position:" + this.getEndPos());
	RandomAccessFile raf;
	StringBuffer strBuf;
	try
	{
	    raf = new RandomAccessFile(this.textfile, "r");
	    /* Чтение на каж. шаге делается от .endPos до curReadPos.. */
	    /* ... curReadPos на каждом шаге сдвигается вправо на extNum */
	    /* ... nextReadPos - это curReadPos на след. шаге */
	    /* ... при чтении инктрементируется cursor... */

	    long nextReadPos = this.endPos + extNum < raf.length() ? this.endPos + extNum : raf.length();
	    long curReadPos, sentEndPos, cursor;
	    String fragment = null;

	    do
	    {
		curReadPos = nextReadPos;
		cursor = this.endPos;
		raf.seek(cursor);
		strBuf = new StringBuffer();
		while ((cursor < curReadPos))
		{
		    strBuf.append((char) raf.read());
		    cursor++;
		}
		/* ... получаем фрагмент для поиска конца предложения... */
		fragment = new String(strBuf.toString().getBytes("ISO-8859-1"), Charset.forName("CP1251"));
		/* ... вычисляем nextReadPos (curReadPos для следующего шага) */
		nextReadPos = nextReadPos + extNum > raf.length() ? nextReadPos + extNum : raf.length();
		/* Определяем (первый) индекс конца предл в тек. фрагменте */
		sentEndPos = FragmentPosition.getFirstSentenceEnd(fragment);
		/* ... если индекса нет + не достиг.. конца, то продолжаем... */
	    } while (sentEndPos < 0 && curReadPos < raf.length());
	    /* Обновляем правую границу = под найденное начало пред-ия */
	    this.setEndPos(this.endPos + sentEndPos);

	} catch (Exception e)
	{
	    // TODO: handle exception
	}

	LOGGER.info("New End position:" + this.getStartPos());
    }

    /**
     * Дает индекс последнего конца предложения в тексте
     * @param str текст для анализа
     * @return индекс или -1
     */
    static public long getLastSentenceEnd(String str)
    {
	String regexStr = "[.?!](\\s|\\Z)";
	Pattern pattern = Pattern.compile(regexStr);
	Matcher matcher = pattern.matcher(str);

	long sentenceEndIndex = -1;

	while (matcher.find())
	    sentenceEndIndex = matcher.end();

	if (sentenceEndIndex > 0)
	{
	    LOGGER.info("Sentence end has found at: " + sentenceEndIndex);
	    return sentenceEndIndex;
	} else
	    return -1;
    }

    /**
     * Дает индекс первого конца предложения
     * @param str текст для анализа
     * @return индекс или -1
     */
    static public long getFirstSentenceEnd(String str)
    {
	// String regexStr = "[.?!] ";
	// String regexStr = ". ";
	// String regexStr = "[.?!] ";
	String regexStr = "[.?!](\\s|\\Z)";
	Pattern pattern = Pattern.compile(regexStr);
	Matcher matcher = pattern.matcher(str);
	Boolean isFound = false;
	long sentenceEndIndex = -1;

	if (matcher.find())
	{
	    sentenceEndIndex = matcher.end();
	    LOGGER.info("Sentence end has found at: " + sentenceEndIndex);
	    return sentenceEndIndex;
	} else
	    return -1;
    }

    /**
     * @param extNum
     * @deprecated reset() требует mark(), которые не может работать с long
     */
    public void extendStartPos1(int extNum)
    {
	/* Сдвигаем стартовую позицию на extNum или до 0-ля... */
	this.setStartPos(startPos - extNum > 0 ? startPos - extNum : 0);

	/* Сдвигаем стартовую позицию до начала предложения или до 0-ля... */
	int ch;
	StringBuffer strBuf = new StringBuffer();
	long startSentPos = -1;

	try (FileInputStream fis = new FileInputStream(this.textfile);)
	{
	    InputStreamReader inStreamReader = new InputStreamReader(fis, Charset.forName("CP1251"));
	    BufferedReader buf = new BufferedReader(inStreamReader);
	    // Integer i = theLong != null ? theLong.intValue() : null;
	    //-buf.mark(this.startPos);
	    long nextReadPos = this.startPos - extNum > 0 ? this.startPos - extNum : 0;
	    long curReadPos;

	    do
	    {
		curReadPos = nextReadPos;

		buf.skip(curReadPos);
		while ((curReadPos < this.startPos))
		{
		    strBuf.append(buf.read());
		    curReadPos++;
		}
		LOGGER.info("Fragment to seek:" + strBuf.lastIndexOf("."));
		nextReadPos = curReadPos - extNum > 0 ? curReadPos - extNum : 0;
		startSentPos = strBuf.lastIndexOf(". ");
		buf.reset();
		boolean nnn = startSentPos < 0 && curReadPos > 0;
		LOGGER.info("->" + nnn);
	    } while (startSentPos < 0 && curReadPos > 0);

	    // && containSubstring(strBuf, "[.?!] ")
	} catch (Exception e)
	{
	    // TODO: handle exception
	}

	this.setStartPos(startSentPos > 0 ? startSentPos : 0);
	LOGGER.info("Extended start position has setted to :" + this.getStartPos());
	return;

    }

    static public boolean containSubstring(StringBuffer strBuf, String regexStr)
    {
	Pattern pattern = Pattern.compile(regexStr);
	Matcher matcher = pattern.matcher(strBuf);

	if (matcher.find())
	{
	    String tmp = matcher.group(0);
	    LOGGER.info("[" + tmp + "]" + "\n");
	}

	return matcher.find();
    }

    @XmlAttribute
    public long getStartPos()
    {
	return startPos;
    }

    public void setStartPos(long startPos)
    {
	this.startPos = startPos;
    }

    @XmlAttribute
    public long getEndPos()
    {
	return endPos;
    }

    public void setEndPos(long endPos)
    {
	this.endPos = endPos;
    }

    
    @XmlAttribute(name = "text-file")
    public File getTextfile()
    {
	return textfile;
    }

    public void setTextfile(File textfile)
    {
	this.textfile = textfile;
    }

}
