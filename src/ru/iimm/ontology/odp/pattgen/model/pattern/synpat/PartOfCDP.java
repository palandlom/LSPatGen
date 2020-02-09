package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import java.io.File;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import ru.iimm.ontology.odp.pattgen.model.pattern.ContentDesingPattern;
import ru.iimm.ontology.ontAPI.Ontology;

public class PartOfCDP extends SinglePropertyCDP
{
	/* Common const */
	public final static String ONT_IRI = "http://www.ontologydesignpatterns.org/cp/owl/partof.owl";
	static String BASE_IRI = ONT_IRI + "#";
	public final static String FILENAME = "partof.owl";

	/* Properties names */
	static String HAS_PART_IRI = BASE_IRI + "hasPart";
	static String IS_PART_OF_IRI = BASE_IRI + "isPartOf";

	private OWLObjectProperty hasPart;
	private OWLObjectProperty isPartOf;

	private OWLClass thing;

	
	
	public PartOfCDP(IRI ontologyIRI)
	{
		super(ontologyIRI, new Ontology(
				ContentDesingPattern.getCDPFile(PartOfCDP.FILENAME), true, true));
		init();
	}

	public PartOfCDP()
	{
		super(ContentDesingPattern.getCDPFile(PartOfCDP.FILENAME));
		init();
	}

	@Override
	protected void init()
	{
		/* A) Задаем спец-ие поля паттерна */
		this.hasPart = df.getOWLObjectProperty(IRI.create(HAS_PART_IRI));
		this.isPartOf = df.getOWLObjectProperty(IRI.create(IS_PART_OF_IRI));
		this.thing = df.getOWLThing();

		/* А1) Собираем отношения... */
		this.objProperties.add(isPartOf);
		this.objProperties.add(hasPart);

		/*
		 * А2) Собираем активные концепты (по которым паттерн будет
		 * сопрягаться)...
		 */
		this.activeConcepts.add(this.thing);

		/*
		 * А3) Собираем связанные концепты (по которым settingfor отношения
		 * будут генериться)...
		 */
		this.relatedConcepts.add(thing);
	}

	/*
	 * ===================================== ======= =========
	 * ========Get/Setters =================
	 * =====================================
	 */

	/**
	 * @return the {@linkplain #hasPart}
	 */
	public OWLObjectProperty getHasPart()
	{
		return hasPart;
	}

	/**
	 * @return the {@linkplain #isPartOf}
	 */
	public OWLObjectProperty getIsPartOf()
	{
		return isPartOf;
	}

	public OWLClass getThing()
	{
		return thing;
	}

	public void setThing(OWLClass thing)
	{
		this.thing = thing;
	}

	public void setHasPart(OWLObjectProperty hasPart)
	{
		this.hasPart = hasPart;
	}

	public void setIsPartOf(OWLObjectProperty isPartOf)
	{
		this.isPartOf = isPartOf;
	}

}
