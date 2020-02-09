package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import ru.iimm.ontology.odp.pattgen.model.pattern.ContentDesingPattern;
import ru.iimm.ontology.ontAPI.Ontology;

public class CoparticipationCDP extends PositionCDP
{
	/* Common const */
	public final static String ONT_IRI = "http://www.ontologydesignpatterns.org/cp/owl/coparticipation.owl";
	static String BASE_IRI = ONT_IRI + "#";
	public final static String FILENAME = "coparticipation.owl";

	/* Class names */
	//static String EVENT_IRI = BASE_IRI + "Event";
//	static String OBJECT_IRI = BASE_IRI + "Object";

	/* Properties names */
	static String HAS_COPARTICIPATES_WITH_IRI = BASE_IRI + "coparticipatesWith";
	//static String HAS_PARTICIPANT_IRI = BASE_IRI + "hasParticipant ";
//	static String IS_PARTICIPANT_OF_IRI = BASE_IRI + "isParticipantIn ";

	private OWLObjectProperty coparticipatesWith;

	private OWLClass event;
	private OWLClass object;

	private OWLObjectProperty hasParticipant;
	private OWLObjectProperty isParticipantIn;

	/**
	 * 
	 */
	public CoparticipationCDP()
	{
		super(ContentDesingPattern.getCDPFile(CoparticipationCDP.FILENAME));
		this.init();
	}

	/**
	 * Создает паттерн с новым {@link IRI}, который используется как основа для
	 * последующего синтеза
	 * 
	 * @param patternOntologyIRI
	 */
	public CoparticipationCDP(IRI patternOntologyIRI)
	{
		super(patternOntologyIRI, new Ontology(
				ContentDesingPattern.getCDPFile(CoparticipationCDP.FILENAME),
				true, true));
		this.init();
	}

	protected void init()
	{
		ParticipationCDP participationCDP = new ParticipationCDP();
		
		/* ) Задаем спец-ие поля паттерна */
		this.event = participationCDP.getEvent();
		this.object = participationCDP.getObject();

		this.hasParticipant = participationCDP.getHasParticipant();
		this.isParticipantIn = participationCDP.getIsParticipantIn();
		this.coparticipatesWith = df
				.getOWLObjectProperty(IRI.create(HAS_COPARTICIPATES_WITH_IRI));

		/* А) Собираем аксиомы доменов свойств... */
		this.objPropertiesDomainAxioms.addAll(this.getCDPOntology().ontInMem
				.getObjectPropertyDomainAxioms(hasParticipant));
		this.objPropertiesDomainAxioms.addAll(this.getCDPOntology().ontInMem
				.getObjectPropertyDomainAxioms(isParticipantIn));
		this.objPropertiesDomainAxioms.addAll(this.getCDPOntology().ontInMem
				.getObjectPropertyDomainAxioms(coparticipatesWith));

		/* А1) Собираем отношения... */
		this.objProperties.add(hasParticipant);
		this.objProperties.add(isParticipantIn);
		this.objProperties.add(coparticipatesWith);

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
	 * @return the {@linkplain #coparticipatesWith}
	 */
	public OWLObjectProperty getCoparticipatesWith()
	{
		return coparticipatesWith;
	}

	public OWLClass getEvent()
	{
		return event;
	}

	public void setEvent(OWLClass event)
	{
		this.event = event;
	}

	public OWLClass getObject()
	{
		return object;
	}

	public void setObject(OWLClass object)
	{
		this.object = object;
	}
}
