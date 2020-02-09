package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import ru.iimm.ontology.odp.pattgen.model.pattern.ContentDesingPattern;
import ru.iimm.ontology.ontAPI.Ontology;

public class ParticipantRoleCDP2 extends SyntSitbasedCDP
{
	/* Common const */
	public final static String ONT_IRI = "http://www.ontology.se/odp/content/owl/ParticipantRole";
	static String BASE_IRI = ONT_IRI + "#";
	public final static String FILENAME = "ParticipantRole.owl";

	/* Class names */
	static String PARTICIPANT_ROLE_IRI = BASE_IRI + "ParticipantRole";

	/* Properties names */
	static String EVENT_INCLUDED_IN_IRI = BASE_IRI + "eventIncludedIn";
	static String OBJECT_INCLUDED_IN_IRI = BASE_IRI + "objectIncludedIn";
	static String ROLE_INCLUDED_IN_IRI = BASE_IRI + "roleIncludedIn";

	static String OBJECT_PARTIC_IN_IRI = BASE_IRI + "objectParticipating";
	static String EVENT_IRI = BASE_IRI + "participatingInEvent";
	static String ROLE_OF_PARTIC_IRI = BASE_IRI + "roleOfParticipant";

	private OWLClass participantRole;
	private OWLClass event;
	private OWLClass object;

	private OWLObjectProperty eventIncludedIn;
	private OWLObjectProperty objectIncludedIn;
	private OWLObjectProperty roleIncludedIn;

	private OWLObjectProperty objectParticipating;
	private OWLObjectProperty participatingInEvent;
	private OWLObjectProperty roleOfParticipant;

	/**
	 * Создает паттерн из файла-онтологии по-умолчанию см
	 * {@linkplain} ContentDesingPattern}
	 */
	public ParticipantRoleCDP2()
	{
		super(ContentDesingPattern.getCDPFile(ParticipantRoleCDP2.FILENAME));
		this.init();
	}

	/**
	 * Создает паттерн с новым {@link IRI}, который используется как основа для
	 * последующего синтеза
	 * 
	 * @param patternOntologyIRI
	 */

	public ParticipantRoleCDP2(IRI patternOntologyIRI)
	{
		super(patternOntologyIRI, new Ontology(
				ContentDesingPattern.getCDPFile(ParticipantRoleCDP2.FILENAME),
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
		
		this.participantRole = df.getOWLClass(IRI.create(PARTICIPANT_ROLE_IRI));

		this.objectIncludedIn = df
				.getOWLObjectProperty(IRI.create(OBJECT_INCLUDED_IN_IRI));
		this.roleIncludedIn = df
				.getOWLObjectProperty(IRI.create(ROLE_INCLUDED_IN_IRI));

		this.objectParticipating = df
				.getOWLObjectProperty(IRI.create(OBJECT_PARTIC_IN_IRI));
		this.participatingInEvent = df
				.getOWLObjectProperty(IRI.create(EVENT_IRI));
		this.roleOfParticipant = df
				.getOWLObjectProperty(IRI.create(ROLE_OF_PARTIC_IRI));

		ParticipationCDP participCDP = new ParticipationCDP();
		this.event = participCDP.getEvent();
		this.object = participCDP.getObject();

		this.situationConcept = this.participantRole;
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

	public OWLClass getParticipantRole()
	{
		return participantRole;
	}

	public void setParticipantRole(OWLClass participantRole)
	{
		this.participantRole = participantRole;
		this.situationConcept = participantRole;
	}

	public OWLObjectProperty getEventIncludedIn()
	{
		return eventIncludedIn;
	}

	public void setEventIncludedIn(OWLObjectProperty eventIncludedIn)
	{
		this.eventIncludedIn = eventIncludedIn;
	}

	public OWLObjectProperty getObjectIncludedIn()
	{
		return objectIncludedIn;
	}

	public void setObjectIncludedIn(OWLObjectProperty objectIncludedIn)
	{
		this.objectIncludedIn = objectIncludedIn;
	}

	public OWLObjectProperty getRoleIncludedIn()
	{
		return roleIncludedIn;
	}

	public void setRoleIncludedIn(OWLObjectProperty roleIncludedIn)
	{
		this.roleIncludedIn = roleIncludedIn;
	}

	public OWLObjectProperty getObjectParticipating()
	{
		return objectParticipating;
	}

	public void setObjectParticipating(OWLObjectProperty objectParticipating)
	{
		this.objectParticipating = objectParticipating;
	}

	public OWLObjectProperty getParticipatingInEvent()
	{
		return participatingInEvent;
	}

	public void setParticipatingInEvent(OWLObjectProperty participatingInEvent)
	{
		this.participatingInEvent = participatingInEvent;
	}

	public OWLObjectProperty getRoleOfParticipant()
	{
		return roleOfParticipant;
	}

	public void setRoleOfParticipant(OWLObjectProperty roleOfParticipant)
	{
		this.roleOfParticipant = roleOfParticipant;
	}

	public void setEvent(OWLClass event)
	{
		this.event = event;
	}

	public void setObject(OWLClass object)
	{
		this.object = object;
	}
}
