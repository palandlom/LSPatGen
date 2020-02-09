package ontExt.lsplProcessor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import ontExt.lsplProcessor.XMLElements.ExtendedFragment;
import ontExt.lsplProcessor.XMLElements.FragmentPosition;
import ontExt.lsplProcessor.XMLElements.Goal;
import ontExt.lsplProcessor.XMLElements.Match;
import ontExt.lsplProcessor.XMLElements.Result;
import ontExt.lsplProcessor.XMLElements.Text;
import ontExt.lsplProcessor.XMLparser.LsplResultParser;
import ontExt.utils.TextUtils;

/**
 * Обрабатывает результатный файл от LSPL анализатора.
 * <p>
 * Аргументы:
 * <ul>
 * <li>Файл с LSPL результатами</li>
 * <li>Файл с исходным текстом</li>
 * </ul>
 * 
 * @author lomov
 * @deprecated all methods are transfered in {@link LsplManager} 
 *
 */
public class LSPLHandler
{

    private static final Logger LOGGER = LoggerFactory.getLogger(LSPLHandler.class);

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, JAXBException
    {
	/* Берет рез-файл из командной строки... */
	if (args.length < 1)
	    throw new RuntimeException("The name of the XML file is required!");
	File lsplResultFile = new File(args[0]); // Файл с LSPL результатами
	File textFile = new File(args[1]); // файл с исходным текстом

	/* Парсим файл с LSPL-результатами ... */
	LsplResultParser parser = LSPLHandler.parsingLSPLResultFile(lsplResultFile);
	List<Goal> goalList = parser.getGoalList();

	/* Расширение фагментов в GOAL ... */
	goalList = LSPLHandler.getExtendedGoalsFragments(goalList, textFile);
	
	
	/* Создаем TEXT - контейнер для GoalList... */
	Text text = new Text();
	text.setGoalList(goalList);

	/* Сочиняем путь+имя выходного файла и пишем в него.. */
	Path outFilePath = Paths.get(lsplResultFile.getParent() + File.separator + "mod-" + lsplResultFile.getName());
	// - LSPLHandler.saveExtendedRelusts(goalList, outFilePath);

	/* в XML... */
	File outFile = new File(lsplResultFile.getParent() + File.separator + "mod-" + lsplResultFile.getName());
	LSPLHandler.saveEtendedResultsXML(text, outFile);

    }

    /**
     * Парсит LSPL файл с результами.
     * 
     * @param resultFile
     *            Файл с LSPL результатами
     * @return парсер с результатами
     */
    private static LsplResultParser parsingLSPLResultFile(File resultFile)
	    throws ParserConfigurationException, SAXException, IOException
    {
	/* Создаем парсер для LSPL файла */
	LsplResultParser lsplParser = new LsplResultParser();

	/* Обработка файла с результатами (resultFile) ... */
	SAXParserFactory parserFactor = SAXParserFactory.newInstance();
	SAXParser parser = parserFactor.newSAXParser();

	/* ... парсим файл с результатами... */
	parser.parse(resultFile, lsplParser);

	return lsplParser;
    }

    /**
     * Расширяет текстовые фрагменты, соответвующие найденным цепочкам.
     * 
     * @param goalList
     *            список GOAL с фрагментами.
     * @param primaryTextFile
     *            исходный текст.
     */
    private static List<Goal> getExtendedGoalsFragments(List<Goal> goalList, File primaryTextFile)
    {
	/* Обработка файла с текстом + дополнение рез-в ... */
	for (Goal goal : goalList)
	{
	    for (Match match : goal.getMatchList())
	    {
		/*
		 * У текущего MATCH расширяем границы фрагмента + корректируем
		 * их до начала предложений
		 */
		FragmentPosition position = new FragmentPosition();
		position.setTextfile(primaryTextFile);
		/* ... сдвигаем исходные позиции */
		position.setStartPos(match.getStartPos() - TextUtils.EXTENSION_FRAGMENT_SIZE);
		position.setEndPos(match.getEndPos() + TextUtils.EXTENSION_FRAGMENT_SIZE);

		/* ... сдвигаем их далее до границы предложений */
		position.extendStartPos(30);
		position.extendEndPos(30);

		/* Получаем расширенный фрагмент + добавляем его в Матч */
		String fragmentText = TextUtils.getFragment(position.getTextfile(), position.getStartPos(),
			position.getEndPos());
		ExtendedFragment extFragment = new ExtendedFragment(fragmentText, position);

		match.setExtFragment(extFragment);

		/* Удаляем из MATCH дубли/вхождения результатов...*/
		match.setResultList(LSPLHandler.removeRepeatedResults(match.getResultList()));
	    }
	}
	return goalList;
    }

    /**
     * Сохраняем результаты в файл
     * 
     * @param goalList
     * @param outFile
     * @throws IOException
     */
    private static void saveExtendedRelusts(List<Goal> goalList, Path outFile) throws IOException
    {
	LOGGER.info("Write result to {}", outFile);

	try (BufferedWriter writer = Files.newBufferedWriter(outFile, Charset.forName("cp1251")))
	{
	    for (Goal goal : goalList)
	    {
		writer.write(goal.toString());
	    }
	}

    }

    private static void saveEtendedResultsXML(Text text, File outFile) throws JAXBException
    {
	LOGGER.info("Write result to {}", outFile);
	JAXBContext jc = JAXBContext.newInstance(Text.class);
	Marshaller m = jc.createMarshaller();
	m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	m.marshal(text, outFile);
    }

    /**
     * Убирает повторы и включения из результатов.
     * 
     * @param resultList
     * @return
     */
    private static List<Result> removeRepeatedResults(List<Result> resultList)
    {
	List<Result> newResultList = new ArrayList<>();
	Boolean isRepeated = false;
	Iterator<Result> iter;

	/* В перебираем текущие рез-ты... */
	for (Result checkingRes : resultList)
	{
	    isRepeated = false;
	    iter = resultList.iterator();

	    /* .. те, что не содержаться */
	    while (!isRepeated && iter.hasNext())
	    {

		Result res = iter.next();
		if ((checkingRes != res) && checkingRes.isContained(res))
		    isRepeated = true;
	    }
	    /* ... добавляются в результатный список */
	    if (!isRepeated)
	    {
		newResultList.add(checkingRes);
	    }

	}

	return newResultList;
    }

}
