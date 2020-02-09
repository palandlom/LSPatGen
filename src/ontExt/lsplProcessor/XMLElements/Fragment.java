package ontExt.lsplProcessor.XMLElements;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ontExt.lsplProcessor.XMLparser.SpecialCharactersRemoverAdapter;

/**
 * Фрагмент текста, соответствующий некоторому совпадению.
 * 
 * @author Lomov P. A.
 *
 */
public class Fragment
{
	/**
	 * Текст фрагмента
	 */
	private String text;

	public Fragment()
	{
		super();
	}

	public Fragment(String text)
	{
		super();
		this.text = text;
	}

	/*
	 * ============================================================
	 * ================= Get/Set-ers ==============================
	 * ============================================================
	 */

	
	@XmlJavaTypeAdapter(SpecialCharactersRemoverAdapter.class)
	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

}
