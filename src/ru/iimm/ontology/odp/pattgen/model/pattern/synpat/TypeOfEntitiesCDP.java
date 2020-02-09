package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import ru.iimm.ontology.odp.pattgen.model.pattern.ContentDesingPattern;
import ru.iimm.ontology.ontAPI.Ontology;

public class TypeOfEntitiesCDP extends SinglePositionCDP

{
	/* Common const */
	static String ONT_IRI = "http://www.ontologydesignpatterns.org/cp/owl/typesofentities.owl";
	static String BASE_IRI = ONT_IRI + "#";
	static String FILENAME = "typesofentities.owl";

	/* Class names */
	private static String ENTITY_FRAGMENT_IRI = "Entity";
	private static String ABSTRACT_FRAGMENT_IRI = "Abstract";
	private static String EVENT_FRAGMENT_IRI = "Event";
	private static String QUALITY_FRAGMENT_IRI = "Quality";
	private static String OBJECT_FRAGMENT_IRI = "Object";
	
	/* Полные IRI концептов - нужны для указания класса активного концепта при создании этого паттерна*/
	static String ENTITY_IRI = Const.IRI_CDP_CATALOG + TypeOfEntitiesCDP.FILENAME +"#Entity";
	static String ABSTRACT_IRI = Const.IRI_CDP_CATALOG + TypeOfEntitiesCDP.FILENAME +"#Abstract";
	static String EVENT_IRI = Const.IRI_CDP_CATALOG + TypeOfEntitiesCDP.FILENAME +"#Event";
	static String QUALITY_IRI = Const.IRI_CDP_CATALOG + TypeOfEntitiesCDP.FILENAME +"#Quality";
	static String OBJECT_IRI = Const.IRI_CDP_CATALOG + TypeOfEntitiesCDP.FILENAME +"#Object";
	

	private OWLClass Entity;
	private OWLClass Abstract;
	private OWLClass Event;
	private OWLClass Quality;
	private OWLClass Object;

	/**
	 * Класс концепта, выбранный пользователем для использования при синтезе.
	 * Другие классы концепты паттерна в паттерне не используются.
	 */
//	private IRI activeConceptClassIRI;

	/**
	 * @param activeConceptClassIRI
	 *            {@link IRI} класса, выбранного пользователем для создания
	 *            концепта, используемого при синтезе. Другие концепты паттерна
	 *            в паттерне не используются
	 */
	public TypeOfEntitiesCDP(IRI activeConceptClassIRI)
	{
		super(ContentDesingPattern.getCDPFile(TypeOfEntitiesCDP.FILENAME), activeConceptClassIRI);
		this.activeConceptClassIRI = activeConceptClassIRI;
		init();
	}

	public TypeOfEntitiesCDP(IRI activeConceptClassIRI, IRI ontologyIRI)
	{
		super(ontologyIRI, new Ontology(
				ContentDesingPattern.getCDPFile(TypeOfEntitiesCDP.FILENAME),
				true, true), activeConceptClassIRI);
		this.activeConceptClassIRI = activeConceptClassIRI;
		init();
	}

	@Override
	
	protected void init()
	{
		/* Иниц-м поля паттерна */
		this.Entity = df
				.getOWLClass(IRI.create(this.getBaseIRI() + ENTITY_FRAGMENT_IRI));
		this.Abstract = df
				.getOWLClass(IRI.create(this.getBaseIRI() + ABSTRACT_FRAGMENT_IRI));
		this.Event = df.getOWLClass(IRI.create(this.getBaseIRI() + EVENT_FRAGMENT_IRI));
		this.Quality = df
				.getOWLClass(IRI.create(this.getBaseIRI() + QUALITY_FRAGMENT_IRI));
		this.Object = df
				.getOWLClass(IRI.create(this.getBaseIRI() + OBJECT_FRAGMENT_IRI));

		/*
		 * Для этого паттерна оставляем только аксиомы, связанные с классом
		 * активного концепта т.е. или с Entity, или с Abstract или с др....
		 */
		OWLClass conceptClass = df.getOWLClass(this.activeConceptClassIRI);
		this.setAxioms(this
				.getRefferedAxioms(conceptClass));

		/*
		 * А2) Заполняем список активных концептов... в данном паттерне в нем будет
		 * только один новый концепт, выбранного ранее класса...
		 * Изменение 1: будет как раз выбранный класс, т.е. наследника не создаем
		 */
		//-OWLClass activeConcept = this.df.getOWLClass(AbstractSyntCDP.getSubIRI(activeConceptClassIRI));
		OWLClass activeConcept = this.df.getOWLClass(activeConceptClassIRI);
		this.activeConcepts.add(activeConcept);
		this.relatedConcepts.add(activeConcept);
		
		/* ... дополняем список аксиом - добавляем аксиому subclassOf для созданного концепта
		 * Не нужно из-за Изменение 1*/
		//-OWLSubClassOfAxiom conceptOfClassAxiom = df.getOWLSubClassOfAxiom(activeConcept, conceptClass);
		//-this.getAxioms().add(conceptOfClassAxiom);
		//-this.getCDPOntology().mng.addAxiom(this.getCDPOntology().ontInMem, conceptOfClassAxiom);
		

		/* А) Собираем аксиомы доменов свойств... */
		/* отсутвуют */

		/* А1) Собираем отношения... */
		/* отсутвуют */

		/*
		 * А2) Собираем активные концепты (по которым паттерн будет
		 * сопрягаться)...
		 */
		/* сделано в констукторе */

		/* Б) Создаем аксиомы связей концептов в паттерне в виде дуг... */
		/* ... в онтологии паттерна связей между концептами нет, поэтому */

	}

	/**
	 * Возвращает аксиомы, относящиеся только к активному концепту.
	 * 
	 * @param activeConcept
	 * @return
	 */
	private Set<OWLAxiom> getRefferedAxioms(OWLClass activeConcept)
	{
		HashSet<OWLAxiom> axioms = new HashSet<>();
		// OWLClass patternActiveObject = this.activeConcepts.

		/* Entity всега должна быть */
		axioms.add(this.df.getOWLSubClassOfAxiom(this.getEntity(),
				df.getOWLThing()));

		/* Если активный концепт не entity, то делам его аксиому... */
		if (!activeConcept.equals(this.getEntity()))
		{
			axioms.add(
					df.getOWLSubClassOfAxiom(activeConcept, this.getEntity()));
		}

		return axioms;
	}

	/*
	 * =============================================== Get/Setters
	 * ==================================
	 * ==============================================
	 */

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
