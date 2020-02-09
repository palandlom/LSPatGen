package ontExt.lsplProcessor.XMLparser;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ontExt.lsplProcessor.XMLElements.Fragment;
import ontExt.lsplProcessor.XMLElements.Goal;
import ontExt.lsplProcessor.XMLElements.Match;
import ontExt.lsplProcessor.XMLElements.Result;

/**
 * XML-парсер файла с результатами LSPL анализатора.
 * @author lomov
 *
 */
public class LsplResultParser extends DefaultHandler
{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(LsplResultParser.class);

	/**
	 * Список результатов по паттернам-целямж
	 */
	private List<Goal> goalList;

	/**
	 * Паттерн-цель, которая парсится на данном шаге.
	 */
	private Goal currentGoal;
	/**
	 * Совпадение, которая парсится на данном шаге.
	 */
	private Match currentMatch;

	/**
	 * Текст в текущем тэге.
	 */
	private String tempText;

	public LsplResultParser()
	{
		super();
		this.goalList = new ArrayList<>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		LOGGER.info("Start element: " + uri + " " + localName + " " + qName
				+ " " + attributes);

		switch (qName)
		{
		// === Goal ===========
		case "goal":
		{
			Goal goal = new Goal();
			goal.setName(attributes.getValue("name"));
			this.setCurrentGoal(goal);
			break;
		}

			// === Matches in Goal ===========
		case "match":
		{
			Match match = new Match();
			match.setStartPos(
					Integer.parseInt(attributes.getValue("startPos")));
			match.setEndPos(Integer.parseInt(attributes.getValue("endPos")));
			this.setCurrentMatch(match);
			break;
		}

		}

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException
	{
		LOGGER.info("Start element: " + uri + " " + localName + " " + qName);

		switch (qName)
		{
		// === Goal ===========
		/* Прочитав весь Гоал - добавляем ее в список парсера */
		case "goal":
		{
			this.getGoalList().add(this.getCurrentGoal());
			break;
		}

			// === Matches in Goal ===========
		case "match":
		{
			/* Прочитав Матч - добавляем его в Гоал, тк он часть Гоалов */
			this.getCurrentGoal().getMatchList().add(this.getCurrentMatch());
			break;
		}

			// === Goal ===========
			/* Прочитав весь fragment - добавляем его матч */
		case "fragment":
		{
			Fragment fragment = new Fragment(this.getTempText());
			//fragment.setText(this.getTempText());
			this.getCurrentMatch().setFragment(fragment);
			break;
		}

			// === Goal ===========
			/* Прочитав весь result - добавляем его в список матча */
		case "result":
		{
			Result result = new Result();
			result.setText(this.getTempText());
			//this.getCurrentMatch().getResultList().add(result);
			this.getCurrentMatch().addResult(result);
			break;
		}

		}

	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		/* Делаем из массива символов ch строку и сохраняем ее */
		this.setTempText(String.copyValueOf(ch, start, length).trim());
		LOGGER.info("Read text: {} ", this.getTempText());
	}

	/*
	 * ============================================================
	 * ================= Get/Set-ers ==============================
	 * ============================================================
	 */

	public List<Goal> getGoalList()
	{
		return goalList;
	}

	public void setGoalList(List<Goal> goalList)
	{
		this.goalList = goalList;
	}

	public Goal getCurrentGoal()
	{
		return currentGoal;
	}

	public Match getCurrentMatch()
	{
		return currentMatch;
	}

	public void setCurrentGoal(Goal currentGoal)
	{
		this.currentGoal = currentGoal;
	}

	public void setCurrentMatch(Match currentMatch)
	{
		this.currentMatch = currentMatch;
	}

	public String getTempText()
	{
		return tempText;
	}

	public void setTempText(String tempText)
	{
		this.tempText = tempText;
	}

}
