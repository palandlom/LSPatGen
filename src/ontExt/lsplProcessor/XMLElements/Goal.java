package ontExt.lsplProcessor.XMLElements;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Раздел с интересующим паттерном. Включает список совпадений
 * {@linkplain Match}
 * 
 * @author Lomov P. A.
 *
 */

public class Goal
{
	private String name;
	private List<Match> MatchList;

	public Goal()
	{
		this.MatchList = new ArrayList<>();
	}

	//formatter:off
	/*
	@Override
	public String toString()
	{
		StringBuffer res = new StringBuffer();
		res.append("Goal name: " + this.getName() + "\n");

		for (Match match : MatchList)
		{
			res.append("	Match S:" + match.getStartPos() + " E:"
					+ match.getEndPos() + "\n");

			if (match.getExtFragment() != null)
			{
				res.append(
						"	ExF:" + match.getExtFragment().getText() + "\n");
			}

			res.append("	F:" + match.getFragment().getText() + "\n");

			for (Result result : match.getResultList())
			{
				res.append("	R:" + result.getText() + "\n");
			}
			res.append(
					"	====================================================================\n");
		}

		return res.toString();
	}
	*/
	//formatter:on
	
	@Override
	public String toString()
	{
		return this.getName();
	}

	/*
	 * ============================================================
	 * ================= Get/Set-ers ==============================
	 * ============================================================
	 */

	@XmlElement(name = "match")
	public List<Match> getMatchList()
	{
		return MatchList;
	}

	public void setMatchList(List<Match> matchList)
	{
		MatchList = matchList;
	}

	 @XmlAttribute
	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}

}
