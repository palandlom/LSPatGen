package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import ru.iimm.ontology.odp.pattgen.model.pattern.ContentDesingPattern;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.element.Branch;
import ru.iimm.ontology.ontAPI.Ontology;

public class ObjectRoleCDP extends PositionCDP
{
	/* Common const ================================ */
	public final static String ONT_IRI = "http://www.ontologydesignpatterns.org/cp/owl/objectrole.owl";
	static String BASE_IRI = ONT_IRI + "#";
	public final static String FILENAME = "objectrole.owl";

	/* Class names */
	static String CONCEPT_IRI = BASE_IRI + "Concept";
	static String OBJECT_IRI = BASE_IRI + "Object";
	static String ROLE_IRI = BASE_IRI + "Role";

	/* Properties names */
	static String ROLE_OF_IRI = BASE_IRI + "isRoleOf";
	static String HAS_ROLE_IRI = BASE_IRI + "hasRole";
	/* ============================================== */

	private OWLClass Object;
	private OWLClass Role;

	private OWLObjectProperty IsRoleOf;
	private OWLObjectProperty HasRole;

	
	/**
	 * 
	 */
	public ObjectRoleCDP()
	{
		super(ContentDesingPattern.getCDPFile(ObjectRoleCDP.FILENAME));
		this.init();
	}

	/**
	 * Создает паттерн с новым {@link IRI}, который используется как основа для
	 * последующего синтеза 
	 * @param patternOntologyIRI
	 */
	public ObjectRoleCDP(IRI patternOntologyIRI)
	{
		super(patternOntologyIRI, new Ontology(
				ContentDesingPattern.getCDPFile(ObjectRoleCDP.FILENAME), true,
				true));

		/* ) Задаем спец-ие поля паттерна */
		this.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.iimm.ontology.odp.pattgen.model.pattern.synpat.PositionCDP#init()
	 */
	@Override
	protected void init()
	{
		/* ) Задаем спец-ие поля паттерна */
		this.Object = df.getOWLClass(IRI.create(OBJECT_IRI));
		this.Role = df.getOWLClass(IRI.create(ROLE_IRI));
		this.IsRoleOf = df.getOWLObjectProperty(IRI.create(ROLE_OF_IRI));
		this.HasRole = df.getOWLObjectProperty(IRI.create(HAS_ROLE_IRI));

		/*
		 * !!! Пока не надо - т.к. решили оставить исходныее связи между
		 * паттернами. Убираем из онтологии аксиомы, которые могут могут
		 * измениться... (эти аксиомы должны бвть сгенерены при добавлении)
		 */
		/*
		 * Set<OWLSubClassOfAxiom> removeSet =
		 * PositionCDP.getAnonSubClassAxioms(this.getCDPOntology().ontInMem);
		 * this.getCDPOntology().mng.removeAxioms(this.getCDPOntology().
		 * ontInMem, removeSet);
		 */
		// TODO надо проверять, что удаляются нужные аксиомы

		/* А) Собираем аксиомы доменов свойств... */
		this.objPropertiesDomainAxioms.addAll(this.getCDPOntology().ontInMem
				.getObjectPropertyDomainAxioms(HasRole));
		this.objPropertiesDomainAxioms.addAll(this.getCDPOntology().ontInMem
				.getObjectPropertyDomainAxioms(IsRoleOf));

		/* А1) Собираем отношения... */
		this.objProperties.add(HasRole);
		this.objProperties.add(IsRoleOf);

		/*
		 * А2) Собираем активные концепты (по которым паттерн будет
		 * сопрягаться)...
		 */
		this.activeConcepts.add(getObject());
		this.activeConcepts.add(getRole());

		/*
		 * А3) Собираем связанные концепты (по которым settingfor отношения
		 * будут генериться)...
		 */
		this.relatedConcepts.add(Object);
		this.relatedConcepts.add(Role);

		/* Б) Создаем аксиомы связей концептов в паттерне в виде дуг... */
		/* ... в онтологии паттерна связей между концептами нет, поэтому */

	}

	/**
	 * @return the {@linkplain #object}
	 */
	public OWLClass getObject()
	{
		return Object;
	}

	/**
	 * @return the {@linkplain #role}
	 */
	public OWLClass getRole()
	{
		return Role;
	}

	/**
	 * @return the {@linkplain #isRoleOf}
	 */
	public OWLObjectProperty getIsRoleOf()
	{
		return IsRoleOf;
	}

	/**
	 * @return the {@linkplain #hasRole}
	 */
	public OWLObjectProperty getHasRole()
	{
		return HasRole;
	}
}
