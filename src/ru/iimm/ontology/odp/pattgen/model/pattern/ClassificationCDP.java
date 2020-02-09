package ru.iimm.ontology.odp.pattgen.model.pattern;

import java.io.File;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class ClassificationCDP extends ContentDesingPattern
{
	/* Common const */
	/*
	 * static String ONT_IRI =
	 * "http://www.ontologydesignpatterns.org/cp/owl/classification.owl"; static
	 * String BASE_IRI = ONT_IRI + "#";
	 * 
	 */
	static String FILENAME = "classification.owl";
	/* Class names */
	static String CONCEPT_IRI = "Concept";

	/* Properties names */
	static String HAS_CLASSIFIES_IRI = "classifies";
	static String IS_CLASSIFIED_BY_IRI = "isClassifiedBy";

	private OWLClass concept;

	private OWLObjectProperty classifies;
	private OWLObjectProperty isClassifiedBy;

	/**
	 * Создает паттерн из файла по-умолчанию
	 */
	public ClassificationCDP()
	{
		super(ContentDesingPattern.getCDPFile(FILENAME));
		this.init();
	}

	/**
	 * Создает паттерн + подружает его онтологию.
	 */
	private void init()
	{
		this.concept = df
				.getOWLClass(IRI.create(this.getBaseIRI() + CONCEPT_IRI));
		this.classifies = df.getOWLObjectProperty(
				IRI.create(this.getBaseIRI() + HAS_CLASSIFIES_IRI));
		this.isClassifiedBy = df.getOWLObjectProperty(
				IRI.create(this.getBaseIRI() + IS_CLASSIFIED_BY_IRI));
	}

	/**
	 * @return the {@linkplain #concept}
	 */
	public OWLClass getConcept()
	{
		return concept;
	}

	/**
	 * @return the {@linkplain #classifies}
	 */
	public OWLObjectProperty getClassifies()
	{
		return classifies;
	}

	/**
	 * @return the {@linkplain #isClassifiedBy}
	 */
	public OWLObjectProperty getIsClassifiedBy()
	{
		return isClassifiedBy;
	}

}
