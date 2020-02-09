package ontExt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import ontExt.lsplProcessor.XMLparser.LsplResultParser;
import ontExt.utils.TextUtils;

public class start
{

	private static final Logger LOGGER = LoggerFactory.getLogger(start.class);

	/* Метод для тестовых запусков */
	public static void main(String[] args)
			throws ParserConfigurationException, SAXException, IOException
	{
		if (args.length < 1)
			throw new RuntimeException("The name of the XML file is required!");

		/* Обработка файла с результатами ... */
		String resFileStr = args[0];
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();
		LsplResultParser handler = new LsplResultParser();
		File resFile = new File(resFileStr);
		/* ... парсим файл с результатами... */
		parser.parse(resFile, handler);

		/*
		 * for (Goal goal : handler.getGoalList()) {
		 * System.out.println(goal.toString()); }
		 */

		/* Обработка файла с текстом + дополнение рез-в ... */
		String textFileStr = args[1]; // файл с исходным текстом
		File textFile = new File(textFileStr);

		for (Goal goal : handler.getGoalList())
		{
			/* == Объединяем матчи === */
			/* Формируем карту вхождения МАТЧЕЙ... */
			Map<Match, Set<Match>> coverMap = getCoverMatchMap(
					goal.getMatchList());
			/* ... находим те, котрые не включаются в другие.. */
			List<Match> matches = getBiggestMatches(coverMap);
			
			/* ... сортируем МАТЧИ...*/
			Collections.sort(matches, Match.getComparator()); 
			
			/* ... только их и оставляем в goal...*/
			goal.setMatchList(matches);

			/* ... добавляем в наибольшие матчи - наименньшие */
			for (Match match : matches)
			{
				for (Match smallMatch : coverMap.get(match))
					match.addMatch(smallMatch);
			}
			/* == END - Объединяем матчи === */
			/* Завершаем обработку отобранных матчей... */
			for (Match match : matches)
			{
				/*
				 * Расширяем границы фрагментов по исходному тексту +
				 * корректируем их до начала предложений
				 */
				FragmentPosition position = new FragmentPosition();
				position.setTextfile(textFile);

				position.setStartPos(
						match.getStartPos() - TextUtils.EXTENSION_FRAGMENT_SIZE);
				position.extendStartPos(30);

				position.setEndPos(
						match.getEndPos() + TextUtils.EXTENSION_FRAGMENT_SIZE);
				position.extendEndPos(30);

				/* Оформляем фрагмент + добавляем его в Матч */
				String fragmentText = TextUtils.getFragment(position.getTextfile(),
						position.getStartPos(), position.getEndPos());
				ExtendedFragment extFragment = new ExtendedFragment(
						fragmentText, position);
				match.setExtFragment(extFragment);
			}
		}
		
		/* Пишем результаты в файл... */
		Path path = Paths.get(resFile.getParent() + File.separator + "mod-"
				+ resFile.getName());
		try (BufferedWriter writer = Files.newBufferedWriter(path,
				Charset.forName("cp1251")))
		{
			for (Goal goal : handler.getGoalList())
			{
				writer.write(goal.toString());
			}
		}


	}

	/**
	 * Возвращяаем карту включения matchей
	 */
	public static Map<Match, Set<Match>> getCoverMatchMap(List<Match> mathes)
	{
		/* Инициализируем карту - ключ = match */
		Map<Match, Set<Match>> coverMap = new HashMap<>();
		for (Match match : mathes)
			coverMap.put(match, new HashSet<>());

		/*
		 * Если componentMatch входит, то кладем его в нужную ячейку карты...
		 */
		for (Match match : mathes)
		{
			for (Match componentMatch : mathes)
			{
				if (match.isInclude(componentMatch))
					coverMap.get(match).add(componentMatch);
			}
		}

		LOGGER.info("Cover map - completed. Mathes quality: {}",
				coverMap.size());
		return coverMap;

	}

	/**
	 * Возвращяет список mathей, не соддержащихся в других.
	 * 
	 * @param coverMap
	 * @return
	 */
	public static List<Match> getBiggestMatches(Map<Match, Set<Match>> coverMap)
	{
		List<Match> biggestMaches = new ArrayList<>();

		Iterator<Match> matchIter = coverMap.keySet().iterator();
		Boolean isContained = false;
		/* Для каждого матча ... */
		while (matchIter.hasNext())
		{
			Match match = matchIter.next();
			Iterator<Set<Match>> containedMatchesSetsItertor = coverMap.values()
					.iterator();
			/* ... проверяем нет ли его внутри других - их containedMatches */
			while (!isContained && containedMatchesSetsItertor.hasNext())
			{
				Set<Match> containedMatches = containedMatchesSetsItertor
						.next();
				if (containedMatches.contains(match))
					isContained = true;
			}
			/* ... если нету - то это биггест матч */
			if (!isContained)
			{
				LOGGER.info("Biggest match is founded: \n " + match
						+ " contains: " + coverMap.get(match));
				biggestMaches.add(match);
			}

			isContained = false;
		}

		LOGGER.info("Finding of BiggestMatches - completed:\n   All Matches: "
				+ coverMap.keySet().size() + "\n   Biggest mathes quality: "
				+ biggestMaches.size());
		return biggestMaches;
	}


	
}
