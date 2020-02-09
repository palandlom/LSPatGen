package ru.iimm.ontology.odp.pattgen.model.pattern;

import java.util.List;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;

import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.AbstractSyntCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.PositionCDP;

public class TypeOfEntitiesCDP extends PositionCDP

{
	/* Common const */
	public static String ONT_IRI = "http://www.ontologydesignpatterns.org/cp/owl/typesofentities.owl";
	//static String BASE_IRI = ONT_IRI + "#";
	static String FILENAME = "typesofentities.owl";

	/* Class names */
	static String ENTITY_IRI = "Entity";
	static String ABSTRACT_IRI =  "Abstract";
	static String EVENT_IRI = "Event";
	static String QUALITY_IRI =  "Quality";
	static String OBJECT_IRI =  "Object";

	private OWLClass Entity;
	private OWLClass Abstract;
	private OWLClass Event;
	private OWLClass Quality;
	private OWLClass Object;

	/**
	 * Создает паттерн + подружает его онтологию.
	 */
	public TypeOfEntitiesCDP()
	{
		super(ContentDesingPattern.getCDPFile(FILENAME));
		this.init();
	}

	protected   void init()
	{
		this.Entity = df.getOWLClass(IRI.create(this.getBaseIRI()+ENTITY_IRI));
		this.Abstract = df.getOWLClass(IRI.create(this.getBaseIRI()+ABSTRACT_IRI));
		this.Event = df.getOWLClass(IRI.create(this.getBaseIRI()+EVENT_IRI));
		this.Quality = df.getOWLClass(IRI.create(this.getBaseIRI()+QUALITY_IRI));
		this.Object = df.getOWLClass(IRI.create(this.getBaseIRI()+OBJECT_IRI));
	}

	
	
	/**
	 * @return the {@linkplain #entity}
	 */
	public OWLClass getEntity()
	{
		
		return Entity;
	}

	/**
	 * @return the {@linkplain #abstract}
	 */
	public OWLClass getAbstract()
	{
		return Abstract;
	}

	/**
	 * @return the {@linkplain #event}
	 */
	public OWLClass getEvent()
	{
		return Event;
	}

	/**
	 * @return the {@linkplain #quality}
	 */
	public OWLClass getQuality()
	{
		return Quality;
	}

	/**
	 * @return the {@linkplain #object}
	 */
	public OWLClass getObject()
	{
		return Object;
	}

	

}
