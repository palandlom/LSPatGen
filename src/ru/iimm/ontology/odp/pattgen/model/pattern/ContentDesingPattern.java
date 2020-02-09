package ru.iimm.ontology.odp.pattgen.model.pattern;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.iimm.ontology.odp.pattgen.model.pattern.addoptions.AddOption;
import ru.iimm.ontology.ontAPI.Ontology;

public abstract class ContentDesingPattern extends OntologyDesignPattern
{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContentDesingPattern.class);

	private Ontology CDPOntology;

	// private String BaseIRIstr;

	// private ArrayList<ContentDesingPattern> Componenst;

	/**
	 * Пустой конструктор.
	 */
	protected ContentDesingPattern()
	{
	}

	/**
	 * Создает паттерн с переданной онтологией
	 * 
	 * @param СDPOntology
	 */
	public ContentDesingPattern(Ontology СDPOntology)
	{
		this.CDPOntology = СDPOntology;
	}

	/**
	 * Создает ПОС из файла его онтологией.
	 *
	 * @param ontologyIRI
	 * @param dirPath
	 * @param filename
	 */
	public ContentDesingPattern(File ontfile)
	{
		LOGGER.info("Load CDP <" + ontfile.getName() + "> from: "
				+ Const.DIR_CDPONT);

		this.CDPOntology = new Ontology(ontfile, true, true);

	}

	/**
	 * Дает набор классов в онтологии паттерна.
	 */
	public Set<OWLClass> getClasses()
	{

		return this.CDPOntology.getNamedClassList();
		// return null;
	}

	/**
	 * Возвращяет файл с CDP по имени его файла
	 * 
	 * @param filename
	 * @return
	 */
	static public File getCDPFile(String filename)
	{
		// LOGGER.info(Const.DIR_CDPONT + filename);
		File file = new File(Const.DIR_CDPONT + filename);
		if (!file.exists())
		{
			LOGGER.error("File not found: {} ", file.getAbsolutePath());
			return null;
		} else
			return file;

	}

	/**
	 * Дает основу для IRI элементов онтологии паттерна. (ex: http://myont.owl#)
	 * 
	 * @return
	 */
	public String getBaseIRI()
	{
		return this.CDPOntology.getOntologyIRI().toString() + "#";
	}
	
	
	/**
	 * Дает IRI онтологии паттерна. (ex: http://patterntName.owl)
	 * 
	 * @return
	 */
	public IRI getIRI()
	{
		return this.CDPOntology.getOntologyIRI();
	}


	// public Set<AddOption> getAddOptions(OWLClass extConcept, )

	///////////////////////////////////////////////////////
	// GET-SETters ////////////////////////////////////////
	///////////////////////////////////////////////////////

	public Ontology getCDPOntology()
	{

		return CDPOntology;
	}

	public void setCDPOntology(Ontology cDPOntology)
	{
		CDPOntology = cDPOntology;
	}

}
