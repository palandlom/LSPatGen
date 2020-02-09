package ontExt.lsplGenerator.synonyms;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * XML-парсер ответа (с наборов синонимов) от Yandex-переводчика
 * 
 * @author Lomov P.A.
 *
 */
public class YandexSynParser extends DefaultHandler
{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(YandexSynParser.class);

	/**
	 * Список синонимов
	 */
	private List<String> synonyms;

	/**
	 * Текст в текущем тэге.
	 */
	private String tempText;

	private String curElementQName = "";

	public YandexSynParser()
	{
		super();
		this.synonyms = new ArrayList<>();

	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		LOGGER.info("Start: " + uri + " " + qName + " " + attributes);
		this.curElementQName = qName;
		switch (qName)
		{

		}

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException
	{
		LOGGER.info("End: " + uri + " " + localName + " " + qName);
		switch (qName)
		{

		case "text":
		{

			break;
		}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		/* Делаем из массива символов ch строку и сохраняем ее */
		if (this.curElementQName == "text")
		{
			String synonym = String.copyValueOf(ch, start, length).trim();
			synonyms.add(synonym);
			LOGGER.info("Add synonym: {}", synonym);

		}

	}

	/*
	 * ====================================================
	 * =============GET/SETTERS ===========================
	 */

	public List<String> getSynonyms()
	{
		return synonyms;
	}

	public void setSynonyms(List<String> synonyms)
	{

		this.synonyms = synonyms;
	}

	/**
	 * Текст внутри тэга
	 * 
	 * @return
	 */
	public String getTempText()
	{
		return tempText;
	}

	public void setTempText(String tempText)
	{
		this.tempText = tempText;
	}

}
