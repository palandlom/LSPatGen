package ontExt.lsplProcessor.XMLElements;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Совпадение паттерна. Включает найденный фрагмент текста и список результатов
 * его интерпретации.
 * 
 * @author Lomov P. A.
 *
 */
@XmlType(propOrder = { "startPos", "endPos", "fragment", "extFragment", "resultList" })
public class Match
{

	private static final Logger LOGGER = LoggerFactory.getLogger(Match.class);

	
	private Fragment fragment;
	/**
	 * Расширенный фрагмент текста, в котором есть заявленный паттерн.
	 */
	
	private ExtendedFragment extFragment;
	private List<Result> resultList;
	private long startPos;
	private long endPos;

	/***************************/
	
	public Match()
	{
		this.resultList = new ArrayList<>();
	}

	public void addResult(Result result)
	{
		/* Пустые рез-ты не добавляем */
		if (result != null && result.getText() != null
				&& (result.getText().length() > 0))
		{
			if (!this.isExisted(result))
			{
				this.getResultList().add(result);
				return;
			}
		}
		LOGGER.info("Repeated result:{} ", result.getText());
	}

	/**
	 * Существуствует ли переданный результат в списке Матча.
	 * 
	 * @param checkResult
	 * @return
	 */
	public boolean isExisted(Result checkResult)
	{
		for (Result result : this.getResultList())
		{
			if (checkResult.equals(result))
				return true;
		}

		return false;
	}

	/**
	 * Включает ли данный матч тот, что передан в аргументе.
	 * 
	 * @param match
	 * @return
	 */
	public boolean isInclude(Match match)
	{
		/* Сам себя MATCH не содержит.. */
		if (this.equals(match))
			return false;

		if ((this.getStartPos() <= match.startPos)
				&& (this.getEndPos() >= match.getEndPos()))
		{
			return true;
		} else
			return false;
	}

	/**
	 * Добавляет Match в текущий.
	 * 
	 * @param match
	 * @return
	 */
	public Match addMatch(Match match)
	{
		/* Начальный - наибольший результат... */
		List<Result> results = this.getResultList();
		results.addAll(match.getResultList());

		Result biggestResult = results.isEmpty() ? null : results.get(0);

		/* ... ищем реальный больший... */
		for (Result result : results)
		{
			biggestResult = result.getText().length() > biggestResult.getText()
					.length() ? result : biggestResult;
		}
		/* ... его и оставляем. */
		this.getResultList().clear();
		this.getResultList().add(biggestResult);

		/* Больше пока нечего сливать */

		return null;
	}

	/**
	 * Дает компаратор для сравнения МАТЧЕЙ.
	 * @return
	 */
	public static Comparator<Match> getComparator()
	{
		Comparator<Match> comp = new Comparator<Match>() {
			/*
			 * Сравнивающая функция должна вернуть 0, если объекты равны,
			 * отрицательное число (обычно -1), если первый объект меньше
			 * второго, и положительное число (обычно 1), если первый больше.
			 */
			@Override
			public int compare(Match o1, Match o2)
			{
				if (o1.getStartPos()==o2.getStartPos() && o1.getEndPos()==o2.getEndPos())
					return 0;
				
				if (o1.getStartPos()<o2.getStartPos()) return -1;
				if (o1.getStartPos()>o2.getStartPos()) return 1;
 
				if (o1.getStartPos()==o2.getStartPos())
				{
					if (o1.getEndPos()<o2.getEndPos()) return -1;
					if (o1.getEndPos()>o2.getEndPos()) return 1;
				}
				return 0;
			}
		};
		
		return comp;
	}

	/*
	 * ============================================================
	 * ================= Get/Set-ers ==============================
	 * ============================================================
	 */
	public Fragment getFragment()
	{
		return fragment;
	}

	public void setFragment(Fragment fragment)
	{
		this.fragment = fragment;
	}

	//@XmlElementWrapper(name = "results")
	@XmlElement(name = "result")
	public List<Result> getResultList()
	{
		return resultList;
	}

	public void setResultList(List<Result> resultList)
	{
		resultList = resultList;
	}

	public void setEndPos(int endPos)
	{
		this.endPos = endPos;
	}

	
	public ExtendedFragment getExtFragment()
	{
		return extFragment;
	}

//-	@XmlJavaTypeAdapter(SpecialCharactersRemoverAdapter.class)
	public void setExtFragment(ExtendedFragment extFragment)
	{
		this.extFragment = extFragment;
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

	@Override
	public String toString()
	{	
		return this.resultList.isEmpty() ? "[--]" : this.resultList.get(0).getText();
	}

}
