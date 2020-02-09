package ontExt.lsplProcessor.XMLparser;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Удаляет из текста фрагмента спецзнаки \r \n \t,
 * т.к. в XML они выглядят стремно.
 * @author Lomov P. A.
 *
 */
public class SpecialCharactersRemoverAdapter extends XmlAdapter<String, String>
{

	@Override
	public String marshal(String memoryString) throws Exception
	{
		String text = memoryString;
		// \t) и два символа перевода строки (\r и \n
		text = text.replaceAll("\t", " ");
		text = text.replaceAll("\r", " ");
		text = text.replaceAll("\n", " ");
		return text;
	}

	@Override
	public String unmarshal(String fileString) throws Exception
	{

		String text = fileString;
		// \t) и два символа перевода строки (\r и \n
		text = text.replaceAll("\t", " ");
		text = text.replaceAll("\r", " ");
		text = text.replaceAll("\n", " ");
		return text;
	}

}