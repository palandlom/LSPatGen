package ontExt.lsplProcessor.XMLElements;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ontExt.lsplProcessor.XMLparser.SpecialCharactersRemoverAdapter;



@XmlType(propOrder = { "position", "text" })
public class ExtendedFragment
{
	/**
	 * Текст фрагмента
	 */
	private String text;

    
	/**
	 * Позиция фрагмента в тексте (его стартовая и конечная границы).
	 */
	private FragmentPosition position;



    public ExtendedFragment(String text, FragmentPosition position)
    {
	super();
	this.setText(text);
	this.position = position;
    }


    public FragmentPosition getPosition()
    {
        return position;
    }


    public void setPosition(FragmentPosition position)
    {
        this.position = position;
    }

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
