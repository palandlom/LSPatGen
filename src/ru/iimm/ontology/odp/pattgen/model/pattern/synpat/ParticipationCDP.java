package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import ru.iimm.ontology.odp.pattgen.model.pattern.ContentDesingPattern;
import ru.iimm.ontology.ontAPI.Ontology;

public  class ParticipationCDP extends PositionCDP
{
	/* Common const */
	public final static String ONT_IRI = "http://www.ontologydesignpatterns.org/cp/owl/participation.owl";
	static String BASE_IRI = ONT_IRI + "#";	
	public final static String FILENAME = "participation.owl";
	
	/* Class names */
	static String EVENT_IRI = BASE_IRI + "Event";
	static String OBJECT_IRI = BASE_IRI + "Object";
	
	/* Properties names */
	static String HAS_PARTICIPANT_IRI = BASE_IRI + "hasParticipant ";
	static String IS_PARTICIPANT_OF_IRI = BASE_IRI + "isParticipantIn ";
	
	private OWLClass event;
	private OWLClass object;
	
	private OWLObjectProperty hasParticipant;
	private OWLObjectProperty isParticipantIn;
	
	/**
	 * 
	 */
	public ParticipationCDP()
	{	
		super(ContentDesingPattern.getCDPFile(ParticipationCDP.FILENAME));
		this.init();
	}

	/**
	 * Создает паттерн с новым {@link IRI}, который используется как основа для
	 * последующего синтеза 
	 * @param patternOntologyIRI
	 */
	public ParticipationCDP(IRI patternOntologyIRI)
	{
		super(patternOntologyIRI, new Ontology(
				ContentDesingPattern.getCDPFile(ParticipationCDP.FILENAME), true,
				true));
		this.init();
	}

	
	protected void init()
	{
		/* ) Задаем спец-ие поля паттерна */
		this.event = df.getOWLClass(IRI.create(EVENT_IRI));
		this.object = df.getOWLClass(IRI.create(OBJECT_IRI));
		
		this.hasParticipant = df.getOWLObjectProperty(IRI.create(HAS_PARTICIPANT_IRI));
		this.isParticipantIn = df.getOWLObjectProperty(IRI.create(IS_PARTICIPANT_OF_IRI));
		
		/* А) Собираем аксиомы доменов свойств... */
		this.objPropertiesDomainAxioms.addAll(this.getCDPOntology().ontInMem
				.getObjectPropertyDomainAxioms(hasParticipant));
		this.objPropertiesDomainAxioms.addAll(this.getCDPOntology().ontInMem
				.getObjectPropertyDomainAxioms(isParticipantIn));

		/* А1) Собираем отношения... */
		this.objProperties.add(hasParticipant);
		this.objProperties.add(isParticipantIn);

		/*
		 * А2) Собираем активные концепты (по которым паттерн будет
		 * сопрягаться)...
		 */
		this.activeConcepts.add(getObject());
		this.activeConcepts.add(getEvent());

		/*
		 * А3) Собираем связанные концепты (по которым settingfor отношения
		 * будут генериться)...
		 */
		this.relatedConcepts.add(getObject());
		this.relatedConcepts.add(getEvent());

		/* Б) Создаем аксиомы связей концептов в паттерне в виде дуг... */
		/* ... в онтологии паттерна связей между концептами нет, поэтому */

		
	}

	/**
	 * @return the {@linkplain #event}
	 */
	public OWLClass getEvent()
	{
		return event;
	}

	/**
	 * @return the {@linkplain #object}
	 */
	public OWLClass getObject()
	{
		return object;
	}

	/**
	 * @return the {@linkplain #hasParticipant}
	 */
	public OWLObjectProperty getHasParticipant()
	{
		return hasParticipant;
	}

	/**
	 * @return the {@linkplain #isParticipantIn}
	 */
	public OWLObjectProperty getIsParticipantIn()
	{
		return isParticipantIn;
	}
}
