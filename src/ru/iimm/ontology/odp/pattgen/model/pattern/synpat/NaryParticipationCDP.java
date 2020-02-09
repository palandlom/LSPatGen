package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import ru.iimm.ontology.odp.pattgen.model.pattern.ContentDesingPattern;
import ru.iimm.ontology.ontAPI.Ontology;

public class NaryParticipationCDP extends SyntSitbasedCDP
{
	/* Common const */
	public final static String ONT_IRI = "http://www.ontologydesignpatterns.org/cp/owl/participation.owl";
	static String BASE_IRI = ONT_IRI + "#";
	public final static String FILENAME = "participation.owl";

	/* Class names */
	static String EVENT_IRI = BASE_IRI + "Event";
	static String OBJECT_IRI = BASE_IRI + "Object";
	static String NARY_SITUAT_IRI = BASE_IRI + "NaryParticipation";

	/* Properties names */
	static String HAS_PARTICIPANT_IRI = BASE_IRI + "hasParticipant ";
	static String IS_PARTICIPANT_OF_IRI = BASE_IRI + "isParticipantIn ";

	private OWLClass event;
	private OWLClass object;
	private OWLClass naryParticipation;
	private OWLClass timeInterval;

	private OWLObjectProperty hasParticipant;
	private OWLObjectProperty isParticipantIn;

	/**
	 * Создает паттерн из файла-онтологии по-умолчанию см
	 * {@linkplain} ContentDesingPattern}
	 */
	public NaryParticipationCDP()
	{
		super(ContentDesingPattern.getCDPFile(NaryParticipationCDP.FILENAME));
		this.init();
	}

	/**
	 * Создает паттерн с новым {@link IRI}, который используется как основа для
	 * последующего синтеза
	 * 
	 * @param patternOntologyIRI
	 */

	public NaryParticipationCDP(IRI patternOntologyIRI)
	{
		super(patternOntologyIRI, new Ontology(
				ContentDesingPattern.getCDPFile(NaryParticipationCDP.FILENAME),
				true, true));
		this.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ru.iimm.ontology.odp.pattgen.model.pattern.synpat.SyntSitbasedCDP#init()
	 */
	protected void init()
	{
		super.init();
		/* Назначаем концепты ======== */
		ParticipationCDP participCDP = new ParticipationCDP();
		TimeIntervalCDP timeIntervalCDP = new TimeIntervalCDP();
		this.event = participCDP.getEvent();
		this.object = participCDP.getObject();

		/* Назначаем свойства ======== */
		this.naryParticipation = df.getOWLClass(IRI.create(NARY_SITUAT_IRI));
		this.timeInterval = timeIntervalCDP.getInterval();

		this.hasParticipant = df
				.getOWLObjectProperty(IRI.create(HAS_PARTICIPANT_IRI));
		this.isParticipantIn = df
				.getOWLObjectProperty(IRI.create(IS_PARTICIPANT_OF_IRI));

		/* Назначаем концепты ситуации ======== */
		this.situationConcept = this.naryParticipation;
	}

	/*
	 * =====================================================================
	 * getsetters =========================================================
	 * ===============================================================
	 */

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

	public OWLClass getNaryParticipation()
	{
		return naryParticipation;
	}

	public void setNaryParticipation(OWLClass naryParticipation)
	{
		this.naryParticipation = naryParticipation;
		this.situationConcept = naryParticipation;
	}

	public OWLClass getTimeInterval()
	{
		return timeInterval;
	}

	public void setTimeInterval(OWLClass timeInterval)
	{
		this.timeInterval = timeInterval;
	}

	public void setEvent(OWLClass event)
	{
		this.event = event;
	}

	public void setObject(OWLClass object)
	{
		this.object = object;
	}

	public void setHasParticipant(OWLObjectProperty hasParticipant)
	{
		this.hasParticipant = hasParticipant;
	}

	public void setIsParticipantIn(OWLObjectProperty isParticipantIn)
	{
		this.isParticipantIn = isParticipantIn;
	}
}
