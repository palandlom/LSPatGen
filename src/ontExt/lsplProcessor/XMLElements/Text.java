package ontExt.lsplProcessor.XMLElements;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Класс - root элемент XML файла с результатами LSPL.
 * Содержит goals list.
 * 
 * @author lomov
 *
 */
@XmlRootElement
public class Text
{
    private List<Goal> goalList;

    public Text()
    {
	super();
	// TODO Auto-generated constructor stub
    }

    @XmlElementWrapper(name = "goals")
    @XmlElement(name = "goal")
    public List<Goal> getGoalList()
    {
	return goalList;
    }

    public void setGoalList(List<Goal> goalList)
    {
	this.goalList = goalList;
    }

}
