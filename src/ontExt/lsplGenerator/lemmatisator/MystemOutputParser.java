package ontExt.lsplGenerator.lemmatisator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * XML-парсер ответа (с наборов синонимов) от Yandex-переводчика
 * @author Lomov P.A.
 *
 */
public class MystemOutputParser extends DefaultHandler
{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MystemOutputParser.class);
	
	/**
	 * Список лемм
	 */
	private List<String> lemmas;

	/**
	 * Текст в текущем тэге.
	 */
	private String tempText;

	public MystemOutputParser()
	{
		super();
		this.lemmas = new ArrayList<>();
		
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		LOGGER.info("Start element: " + uri + " " + localName + " " + qName
				+ " " + attributes);

		switch (qName)
		{
		// === text синонима ===========
		case "w":
		{
			String lemma = this.getTempText();
			
			if (lemma != null && lemma.trim().length()>0)
			{
				//LOGGER.info("Add synonym: {}",syn);
				lemmas.add(this.getTempText());
				
			}
			break;
		}

		}

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException
	{
		LOGGER.info("End element: " + uri + " " + localName + " " + qName);

		switch (qName)
		{

		case "w":
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
		this.setTempText(String.copyValueOf(ch, start, length).trim());
		LOGGER.info("Read tag text: {} ", this.getTempText());
	}

	/*====================================================
	 * =============GET/SETTERS ===========================*/
	
	public List<String> getSynonyms()
	{
		return lemmas;
	}

	public void setSynonyms(List<String> synonyms)
	{
		this.lemmas = synonyms;
	}

	/**
	 * Текст внутри тэга
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
