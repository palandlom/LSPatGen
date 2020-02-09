package ontExt.lsplGenerator.ontElements;

import java.util.List;

import org.semanticweb.owlapi.model.IRI;

/**
 * Абстрактный объект/класс в тройке/ребре/структурной аксиоме
 * 
 * @author lomov
 *
 */
public class AbsCDPObject
{
	/**
	 * Наименование концепта из его {@linkplain IRI}
	 */
	private String shortIRI;

	/**
	 * Основной текстовый лейбл (обычно на русском языке).
	 */
	private String label;

	/**
	 * Набор синонимов одного из лейблов.
	 */
	private List<String> synonyms;

	public AbsCDPObject(String shortIRI, String label, List<String> synonyms)
	{
		super();
		this.shortIRI = shortIRI;
		this.label = label;
		this.synonyms = synonyms;
	}
	

	/*
	 * =========================================================================
	 * = ======= getseters ======== ========================================
	 * ===================================================================
	 */

	public List<String> getSynonyms()
	{
		return synonyms;
	}

	public void setSynonyms(List<String> synonyms)
	{
		this.synonyms = synonyms;
	}

	public String getShortIRI()
	{
		return shortIRI;
	}

	public void setShortIRI(String shortIRI)
	{
		this.shortIRI = shortIRI;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}
	
	

}
