package ontExt.lsplProcessor.XMLElements;

import javax.xml.bind.annotation.XmlValue;

/**
 * Результат интерпретации найденного фрагмента.
 * 
 * @author Lomov P. A.
 *
 */
public class Result
{
    /**
     * Текст измененного фрагмента.
     */

    private String text;

    public Result()
    {
	super();
	// TODO Auto-generated constructor stub
    }


    /*
     * ============================================================
     * ================= Get/Set-ers ==============================
     * ============================================================
     */
    @XmlValue
    public String getText()
    {
	return text;
    }

    public void setText(String text)
    {
	this.text = text;
    }

    @Override
    public int hashCode()
    {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((text == null) ? 0 : text.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj)
    {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Result other = (Result) obj;
	if (text == null)
	{
	    if (other.text != null)
		return false;
	} else if (!text.equals(other.text))
	    return false;
	return true;
    }

    /**
     * Входит в данный результат тот, что передан в аргументе.
     * 
     * @param biggerResult
     * @return
     */
    public Boolean isContained(Result biggerResult)
    {
	if (biggerResult.getText().indexOf(biggerResult.getText()) > 0)
	{
	    return true;
	} else
	    return false;

    }

}
