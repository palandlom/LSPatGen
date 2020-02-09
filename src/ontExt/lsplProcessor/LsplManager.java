package ontExt.lsplProcessor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

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
import ontExt.utils.Property;
import ontExt.utils.TextUtils;
import ontExt.utils.Utils;

/**
 * Обрабатывает результатный файл от LSPL анализатора.
 * 
 * @author Lomov P. A.
 *
 */
public class LsplManager
{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(LsplManager.class);

	private String lsplComman;
	private File lsplCommandDir;
	private String inFilePath;
	private String patternPath;
	private String outFilePath;
	private String[] target;
	private List<Goal> goalList;

	public LsplManager(String inFilePath, String patternPath,
			String outFilePath, String... target)
	{
		super();

		this.inFilePath = inFilePath;
		this.patternPath = patternPath;
		this.outFilePath = outFilePath;
		// this.target = Arrays.asList(target);
		this.target = target;
		this.goalList = new ArrayList<>();
	}

	public void init()
	{
		Properties prop = Property.loadClasspathProperties("config.properties");
		this.lsplComman = prop.getProperty("LsplManager.lsplfind").replace("/",
				"\\");
		this.lsplCommandDir = new File(prop.getProperty("LsplManager.lspldir"));
	}

	/**
	 * Produce out file and return it.
	 * 
	 * @return
	 */
	public Optional<File> produceOutFile()
	{
		if (startLsplAnalysator())
		{
			return Utils.getFile(this.getOutFilePath());
		}
		return Optional.empty();
	}

	/**
	 * Get lspl start command for process builder.
	 * 
	 * @return
	 */
	private String[] getCommands()
	{
		//@formatter:off
		String[] cmds = { 
				this.getLsplComman(), 
				"-i", this.getInFilePath(),
				"-p", this.getPatternPath(), 
				"-o", this.getOutFilePath(),
				 };

		// add target array
		cmds = Stream.concat(Arrays.stream(cmds), Arrays.stream(this.getTarget()))
                .toArray(String[]::new);
		
		//@formatter:on
		LOGGER.info("Start command array: {}", Arrays.asList(cmds).stream()
				.reduce((a, b) -> a + " " + b).get());
		return cmds;
	}

	private boolean startLsplAnalysator()
	{
		try
		{
			/* = Запускаем процесс mystem ... */
			ProcessBuilder pb = new ProcessBuilder(this.getCommands());
			pb.directory(this.getLsplCommandDir());
			pb.redirectErrorStream(true);

			Process process = pb.start();

			/* Читаем вывод... */
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is,
					Charset.forName("UTF8"));
			BufferedReader br = new BufferedReader(isr);
			String res;
			if ((res = br.readLine()) != null)
			{
				System.err.println(String.format("LSPL message - [%s]", res));
			}

		} catch (IOException e)
		{
			LOGGER.error("Error during launch of lspl: {}");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Processes lspl-result file and produces final one.
	 */
	public File produceFinalResultFile()
	{
		File lsplResultFile = new File(this.getOutFilePath());
		File textFile = new File(this.getInFilePath());
		/* Берет рез-файл из командной строки... */
		// File lsplResultFile = new File(args[0]); // Файл с LSPL результатами
		// File textFile = new File(args[1]); // файл с исходным текстом

		/* Парсим файл с LSPL-результатами ... */
		LsplResultParser parser = null;
		try
		{
			parser = this.parsingLSPLResultFile(lsplResultFile);
		} catch (ParserConfigurationException | SAXException | IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		List<Goal> tmpGoalList = parser.getGoalList();

		/* Уплотняем список для каждой goal */
		tmpGoalList.stream()
				.forEach(g -> g.setMatchList(this.getDensedMatchList(g)));

		/* Расширение фагментов в GOAL ... */
		this.goalList
				.addAll(this.getExtendedGoalsFragments(tmpGoalList, textFile));

		/* Создаем TEXT - контейнер для GoalList... */
		Text text = new Text();
		text.setGoalList(this.goalList);

		/* Сочиняем путь+имя выходного файла и пишем в него.. */
		Path outFilePath = Paths.get(lsplResultFile.getParent() + File.separator
				+ "mod-" + lsplResultFile.getName());
		// - LSPLHandler.saveExtendedRelusts(goalList, outFilePath);

		/* ... сохраняем в XML... */
		File outFile = new File(lsplResultFile.getParent() + File.separator
				+ "mod-" + lsplResultFile.getName());

		try
		{
			this.saveEtendedResultsXML(text, outFile);
		} catch (JAXBException e)
		{
			e.printStackTrace();
		}
		return outFile;
	}

	/**
	 * Парсит LSPL файл с результами.
	 * 
	 * @param resultFile
	 *            Файл с LSPL результатами
	 * @return парсер с результатами
	 */
	private LsplResultParser parsingLSPLResultFile(File resultFile)
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
	private List<Goal> getExtendedGoalsFragments(List<Goal> goalList,
			File primaryTextFile)
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
				position.setStartPos(match.getStartPos()
						- TextUtils.EXTENSION_FRAGMENT_SIZE);
				position.setEndPos(
						match.getEndPos() + TextUtils.EXTENSION_FRAGMENT_SIZE);

				/* ... сдвигаем их далее до границы предложений */
				position.extendStartPos(30);
				position.extendEndPos(30);

				/* Получаем расширенный фрагмент + добавляем его в Матч */
				String fragmentText = TextUtils.getFragment(
						position.getTextfile(), position.getStartPos(),
						position.getEndPos());
				ExtendedFragment extFragment = new ExtendedFragment(
						fragmentText, position);

				match.setExtFragment(extFragment);

				/* Удаляем из MATCH дубли/вхождения результатов... */
				match.setResultList(
						this.removeRepeatedResults(match.getResultList()));
			}
		}
		return goalList;
	}

	/**
	 * Вливает большие match в меньшие.
	 * 
	 * @param goal
	 * @return
	 */
	private List<Match> getDensedMatchList(Goal goal)
	{
		/* == Объединяем матчи === */
		/* Формируем карту вхождения МАТЧЕЙ... */
		Map<Match, Set<Match>> coverMap = getCoverMatchMap(goal.getMatchList());
		/* ... находим те, котрые не включаются в другие.. */
		List<Match> matches = getBiggestMatches(coverMap);

		/* ... сортируем МАТЧИ... */
		Collections.sort(matches, Match.getComparator());

		/* ... только их и оставляем в goal... */
		goal.setMatchList(matches);

		/* ... добавляем в наибольшие матчи - наименньшие */
		for (Match match : matches)
		{
			for (Match smallMatch : coverMap.get(match))
				match.addMatch(smallMatch);
		}
		/* == END - Объединяем матчи === */
		return matches;
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
	 * Сохраняем результаты в файл
	 * 
	 * @param goalList
	 * @param outFile
	 * @throws IOException
	 */
	private void saveExtendedRelusts(List<Goal> goalList, Path outFile)
			throws IOException
	{
		LOGGER.info("Write result to {}", outFile);

		try (BufferedWriter writer = Files.newBufferedWriter(outFile,
				Charset.forName("cp1251")))
		{
			for (Goal goal : goalList)
			{
				writer.write(goal.toString());
			}
		}

	}

	private void saveEtendedResultsXML(Text text, File outFile)
			throws JAXBException
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
	private List<Result> removeRepeatedResults(List<Result> resultList)
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

	/* ====== get/setters ============ */
	/* =============================== */

	public String getLsplComman()
	{
		return lsplComman;
	}

	public String getInFilePath()
	{
		return inFilePath;
	}

	public String getPatternPath()
	{
		return patternPath;
	}

	public String getOutFilePath()
	{
		return outFilePath;
	}

	public String[] getTarget()
	{
		return target;
	}

	public File getLsplCommandDir()
	{
		return lsplCommandDir;
	}

	public void setLsplCommandDir(File lsplCommandDir)
	{
		this.lsplCommandDir = lsplCommandDir;
	}

	public List<Goal> getGoalList()
	{
		return goalList;
	}

}
