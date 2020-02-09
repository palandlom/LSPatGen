package ontExt.lsplGenerator.lemmatisator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ontExt.utils.Property;

public class MstLemmatisator extends Lemmatisator
{

    private static final Logger LOGGER = LoggerFactory.getLogger(MstLemmatisator.class);

    /**
     * Путь к mystem.exe
     */
    private String mystPath = null;

    /**
     * @deprecated не нужно
     */
    private String mystDir = null;

    public MstLemmatisator()
    {
	super();
    }

    public void init()
    {
	/* Подгружаем файл с лемматизатором... */
	Properties prop = Property.loadClasspathProperties("config.properties");
	String mystemPath = prop.getProperty("MstLemmatisator.mystemPath");
	File mystem = new File(mystemPath);
	
	
	
	try
	{
	    if (!mystem.exists())
	    {
		LOGGER.error("Set right path to Mystem in config! Current {} ", mystem.getPath());
		this.mystPath = null;
		this.mystDir = null;
	    } else
	    {
		this.mystPath = mystem.getCanonicalPath();
		this.mystDir = mystem.getParent();
	    }

	} catch (Exception e)
	{
	}

    }

    @Override
    public List<String> getLemma(String word)
    {
	if (this.mystPath == null)
	{
	    LOGGER.error("Mystem is no set - run init at first");
	    return null;
	}

	return this.getMystemLemma(this.mystPath, word);
    }

    /**
     * Получает лемму слова из лемматизатора Mystem
     * 
     * @param mystem
     * @param word
     * @return
     */
    private List<String> getMystemLemma(String mystem, String word)
    {
	String res = null;
	List<String> lemmas = new ArrayList<>();
	try
	{
	    /* = Запускаем процесс mystem ... */
	    ProcessBuilder pb = new ProcessBuilder(mystem, "-n", "-");
	    LOGGER.info("" + mystem + " out");
	    pb.redirectErrorStream(true);
	    Process process = pb.start();

	    /* = Делаем ввод слова... */
	    OutputStream os = process.getOutputStream();
	    OutputStreamWriter osw = new OutputStreamWriter(os, Charset.forName("UTF8"));
	    BufferedWriter bw = new BufferedWriter(osw);
	    bw.write(word);
	    bw.flush();
	    bw.close();

	    /* Читаем вывод... */
	    InputStream is = process.getInputStream();
	    InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF8"));
	    BufferedReader br = new BufferedReader(isr);
	    /* Парсим вывод.. */
	    if ((res = br.readLine()) != null)
	    {
		lemmas.addAll(this.parseMystemRes(res));
	    }
	    

	} catch (IOException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return lemmas;
    }

    /**
     * Вырезает лемму из вывода Mystema (dogs{dogs??})
     * 
     * @param res
     * @return
     */
    private List<String> parseMystemRes(String res)
    {
	// связан{связанный=A=ед,кр,муж|связывать=V=прош,ед,прич,страд}
	if (res == null)   return null;

	/* Вырезаем из скобок: */
	String str;
	if (res.indexOf('?') > 0)
	{
	    str = res.substring(res.indexOf('{') + 1, res.indexOf('?'));
	} else
	    str = res.substring(res.indexOf('{') + 1, res.indexOf('}'));

	/* Делим на куски по | */
	String[] lemmas = str.split("\\|");

	return Arrays.asList(lemmas);

    }

    /**
     * @param result
     * @return
     * @deprecated с выводом в консоль не работает
     */
    private static List<String> parseMystemXMLResult(InputStream result)
    {

	/* ... вызываем парсер ответа... */
	MystemOutputParser mstParser = new MystemOutputParser();
	try
	{
	    SAXParserFactory parserFactor = SAXParserFactory.newInstance();
	    SAXParser parser = parserFactor.newSAXParser();
	    mstParser = new MystemOutputParser();
	    parser.parse(result, mstParser);
	    /* ... теперь в mstParser леммы */
	} catch (Exception e)
	{
	    LOGGER.error("Error during read output of Mystem");
	    LOGGER.error("Exeption {}", e.getMessage());
	}

	return mstParser.getSynonyms();
    }
}
