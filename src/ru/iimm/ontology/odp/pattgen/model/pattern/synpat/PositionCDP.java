/**
 * 
 */
package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.element.Branch;
import ru.iimm.ontology.ontAPI.Ontology;

/**
 * Componency PartOf ActingFor Objectrole Parameter Classification Time interval
 * Region Co-participation Participation Description
 * 
 * @author lomov
 *
 */
public abstract class PositionCDP extends AbstractSyntCDP
{
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PositionCDP.class);
	
	static public List<IRI> getPositionPatternIRIs()
	{
		List<IRI> patterns = new ArrayList<>();
	//	patterns.addAll(SingePositionCDP.getSingePositionPatternIRIs());
	//	patterns.addAll(SinglePropertyCDP.getSinglePropertyPatternIRIs());
		patterns.add(IRI.create(CoparticipationCDP.ONT_IRI));
		patterns.add(IRI.create(ObjectRoleCDP.ONT_IRI));
		patterns.add(IRI.create(ParticipationCDP.ONT_IRI));
		return patterns;
	}


	/* ============================================== */
	/**
	 * дуги: [от_кого --отношение--> к_кому, (арность)]
	 * 
	 * @deprecated не используется
	 */
	Set<Branch> branches;

	/**
	 * Аксиомы доменов объектных отношений, применяемые в паттерне. Домены
	 * данных свойств надо будет менять в онтологии паттерна при добавлении его
	 * в ситуацию, если решено будет переопределять отношения через ситуацию
	 * 
	 * @deprecated пока не понадобились
	 */
	Set<OWLObjectPropertyDomainAxiom> objPropertiesDomainAxioms;

	/**
	 * Объектные отношения, применяемые в паттерне
	 */
	Set<OWLObjectProperty> objProperties;

	/**
	 * Концепты, по которым паттерн может соединяться с другими.
	 */
	Set<OWLClass> activeConcepts;

	/**
	 * Концепты, имеющие отношения внутри паттерна. Т.е. если концепт имеет 2
	 * отношения внутри паттерна, то он дважды будет находиться в данном списке.
	 * Сам список используется при включении паттерна в ситуацию для генерации
	 * правильно числа отношений settingfFor = числу аксиом с отношениями внутри
	 * паттерна.
	 */
	ArrayList<OWLClass> relatedConcepts;

	/**
	 * Список ситуаций, дабавленных в данный паттерн. Используется при
	 * добавлении данного патерна в ситуационный.
	 * 
	 */
	ArrayList<SequenceCDP> sequences;

	/**
	 * Аксиомы, задающие иерархию концептов (именованных классов)
	 */
	// Set<OWLSubClassOfAxiom> hieracyAxioms;

	/**
	 * Создает паттерн не меняя {@link IRI} исходной онтологии. Применяется для
	 * последующего использования как компонент при синтезе
	 * 
	 * @param ontfile
	 *            файл с паттерном-основой
	 */
	protected PositionCDP(File ontfile)
	{
		super(ontfile);
		this.branches = new HashSet<>();
		this.objPropertiesDomainAxioms = new HashSet<>();
		this.objProperties = new HashSet<>();
		this.activeConcepts = new HashSet<>();
		this.relatedConcepts = new ArrayList<>();
		this.sequences = new ArrayList<>();

	}

	/**
	 * Создает паттерн с новым {@link IRI}, который используется как основа для
	 * последующего синтеза
	 * 
	 * @param ontologyIRI
	 *            новый {@link IRI} создаваемого паттерна
	 * @param basedPatOnt
	 *            онтология паттерна-основы
	 */
	public PositionCDP(IRI ontologyIRI, Ontology basedPatOnt)
	{
		super(ontologyIRI, basedPatOnt);
		this.branches = new HashSet<>();
		this.objPropertiesDomainAxioms = new HashSet<>();
		this.objProperties = new HashSet<>();
		this.activeConcepts = new HashSet<>();
		this.relatedConcepts = new ArrayList<>();
		this.sequences = new ArrayList<>();
	}

	/**
	 * A) Задаем спец-ие поля паттерна А0) deprecated Собираем аксиомы доменов
	 * свойств... А1) Собираем отношения А2) Собираем активные концепты (по
	 * которым паттерн будет сопрягаться)... А3) Собираем связанные концепты (по
	 * которым settingfor отношения будут генериться)... Б) deprecated Создаем
	 * аксиомы связей концептов в паттерне в виде дуг...
	 */
	protected abstract void init();

	/**
	 * Выбирает из онтологии аксиомы иерархии.
	 * 
	 * @param ont
	 * @return TODO перенести в класс утилит
	 */
	public static Set<OWLSubClassOfAxiom> getHieracyAxioms(OWLOntology ont)
	{
		Set<OWLSubClassOfAxiom> hieracyAxiomSet = new HashSet<>();

		for (OWLSubClassOfAxiom sbAxiom : ont.getAxioms(AxiomType.SUBCLASS_OF))
		{
			if (!sbAxiom.getSubClass().isAnonymous()
					&& !sbAxiom.getSuperClass().isAnonymous())
			{
				hieracyAxiomSet.add(sbAxiom);
			}
		}
		return hieracyAxiomSet;
	}

	/**
	 * Выбирает из онтологии аксиомы вида [OWLClass subclassOf
	 * AnonymousSubclass], которые задают структуру патерна (
	 * "связи между концептами")
	 * 
	 * @param ont
	 * @return TODO перенести в класс утилит
	 */
	public static Set<OWLSubClassOfAxiom> getAnonSubClassAxioms(OWLOntology ont)
	{
		Set<OWLSubClassOfAxiom> anonSbAxiomSet = new HashSet<>();

		for (OWLSubClassOfAxiom sbAxiom : ont.getAxioms(AxiomType.SUBCLASS_OF))
		{
			if (!sbAxiom.getSubClass().isAnonymous()
					&& sbAxiom.getSuperClass().isAnonymous())
			{
				anonSbAxiomSet.add(sbAxiom);
			}
		}
		return anonSbAxiomSet;
	}

	/**
	 * Добавляет позиц-ый паттерн в позиционный. Реализует добавление
	 * по-умолчанию - просто объединяет их компоненты.
	 * 
	 * @param addPat
	 *            TODO дописать логмессажд => сделать метод для вывода имени
	 *            паттерна.
	 * 
	 */
	public void add(PositionCDP addPat)
	{
		LOGGER.info("Use Default Addition for:" + this + "");

		this.activeConcepts.addAll(addPat.getActiveConcepts());
		this.branches.addAll(addPat.getBranches());
		this.objProperties.addAll(addPat.getObjProperties());
		this.objPropertiesDomainAxioms
				.addAll(addPat.getObjPropertiesDomainAxioms());

		/* Объединяем онтологии... */
		// -this.getCDPOntology().mng.addAxioms(this.getCDPOntology().ontInMem,
		// addPat.getCDPOntology().ontInMem.getAxioms());
		this.addAxioms(addPat.getAxioms());

		/*
		 * Находим общие(наследованные) концепты... и создаем аксиомы-связки меж
		 * ними
		 */
		this.addAxioms(this.getLinkedAxioms(addPat.activeConcepts));

	}

	/**
	 * Добавляет паттерн-отношение в текущий т.е. определяет аксиому этого
	 * отношения.
	 * 
	 * @param spPat
	 *            добавляемый {@link SinglePropertyCDP}
	 * @param prop
	 *            свойство из {@link SinglePropertyCDP}
	 * @param subject
	 *            концепт расширяемого паттерна (или добавляемого), который
	 *            станет носителем свойства
	 * @param object
	 *            концепт расширяемого паттерна (или добавляемого), который
	 *            станет значением свойства
	 */
	public void add(SinglePropertyCDP spPat, OWLObjectProperty prop,
			OWLClass subject, OWLClass object)
	{
		/* Комбинируем элементы... */
		this.activeConcepts.addAll(spPat.getActiveConcepts());
		// this.branches.addAll(spPat.getBranches());
		// this.objProperties.addAll(spPat.getObjProperties());
		this.objPropertiesDomainAxioms
				.addAll(spPat.getObjPropertiesDomainAxioms());
		/* Объединяем онтологии... */
		this.addAxioms(spPat.getAxioms());
		
		/* Создаем аксиому для связи subject с object... */
		OWLObjectSomeValuesFrom propExpr = this.df
				.getOWLObjectSomeValuesFrom(prop, object);
		OWLAxiom subAx = this.df.getOWLSubClassOfAxiom(subject, propExpr);
		/* TODO тут бы еще обратную аксиому генерить */
		this.addAxiom(subAx);

		/*
		 * добавляем концепт spPat в возможные концепты-компоненты ситуации...
		 */
		if (spPat.getActiveConcepts().contains(object))
			this.relatedConcepts.add(object);
		else
			this.relatedConcepts.add(subject);

	}

	/**
	 * Добавляет множественный паттерн в позиционный, цепляя его коллекцию и
	 * элемент к указанным концептам позиционного паттерна.
	 * 
	 * @param spPat
	 * @param newCollection
	 *            концепт позиционного паттерна, который будет подклассом
	 *            коллекции
	 * @param newElement
	 *            концепт позиционного паттерна, который будет подклассом
	 *            элемента коллекции
	 */
	public void add(ItemedCollectionCDP spPat, OWLClass elementContent)
	{
		/* Меняем концепты в добавляемом... */
		// spPat.changeCollectionConcept(newCollection);
		// spPat.changeElementConcept(newElement);

		/* Комбинируем элементы... */
		this.activeConcepts.add(spPat.getCollectionConcept());
		// this.activeConcepts.add(spPat.getElementConcept());

		/* Объединяем онтологии... */
		this.addAxioms(spPat.getAxioms());
		//this.getCDPOntology().mng.addAxioms(this.getCDPOntology().ontInMem,	spPat.getCDPOntology().ontInMem.getAxioms());

		/*
		 * Создаем аксиому для определения содержания коллекции или элемента
		 * коллекции ...
		 */
		OWLAxiom contentAxiom = spPat.getItemContentAxiom(elementContent);
		this.addAxiom(contentAxiom);

		/*
		 * добавляем концепт коллекции в возможные концепты-компоненты
		 * ситуации...
		 */
		this.relatedConcepts.add(spPat.getCollectionConcept());
		/*
		 * ... удаляем из компонентов ситуации 1 концепт-содержание коллекции,
		 * т.е. он будет входить в ситуацию как содержащийся во множестве
		 */
		this.relatedConcepts.remove(elementContent);

	}
	
	public void add(SimpleCollectionCDP spPat, OWLClass elementContent)
	{
		/* Меняем концепты в добавляемом... */
		// spPat.changeCollectionConcept(newCollection);
		// spPat.changeElementConcept(newElement);

		/* Комбинируем элементы... */
		this.activeConcepts.add(spPat.getCollectionConcept());
		// this.activeConcepts.add(spPat.getElementConcept());

		/* Объединяем онтологии... */
		this.addAxioms(spPat.getAxioms());

		/*
		 * Создаем аксиому для определения содержания коллекции или элемента
		 * коллекции ...
		 */
		Set<OWLAxiom> contentAxioms = spPat.getCollectionMemberAxioms(elementContent);
		this.addAxioms(contentAxioms);

		/*
		 * добавляем концепт коллекции в возможные концепты-компоненты
		 * ситуации...
		 */
		this.relatedConcepts.add(spPat.getCollectionConcept());
		/*
		 * ... удаляем из компонентов ситуации 1 концепт-содержание коллекции,
		 * т.е. он будет входить в ситуацию как содержащийся во множестве
		 */
		this.relatedConcepts.remove(elementContent);

	}
	

	/**
	 * Добавляет последовательность в паттерн
	 * 
	 * @param seqPat
	 *            последовательность
	 * @param prop
	 *            свойство следования
	 * @param subject
	 *            концепт текущего паттерна для установления следования
	 */
	public void add(SequenceCDP seqPat, OWLObjectProperty prop,
			OWLClass subject)
	{
		/* Объединяем онтологии... */
		this.getCDPOntology().mng.addAxioms(this.getCDPOntology().ontInMem,
				seqPat.getCDPOntology().ontInMem.getAxioms());

		/* Создаем аксиому задания последовательности на subject... */
		seqPat.setThing(subject);
		this.addAxioms(seqPat.getPositionAxioms());

		/*
		 * Сохраняем добавленную последовательность. Она понадобиться при
		 * добавлении этого позиционного паттерна в ситуационный
		 */
		this.sequences.add(seqPat);
	}

	/**
	 * Создает список аксиом subClassOf между классами текущего и добавляемого
	 * паттерна для их связывания.
	 * 
	 * @param addActiveConcepts
	 *            классы добавляемого паттерна.
	 * @return
	 */
	private Set<OWLAxiom> getLinkedAxioms(Set<OWLClass> addActiveConcepts)
	{
		CDPOntology.getInstance();
		LOGGER.info("Generate SubClassOfAxiom between...");

		Set<OWLAxiom> axioms = new HashSet<>();

		for (OWLClass curClass : this.activeConcepts)
		{

			for (OWLClass addClass : addActiveConcepts)
			{
				this.addAxioms(CDPOntology.getLinkedAxiom(curClass, addClass));
			}
		}
		return axioms;
	}

	/*
	 * ===================================================
	 * GETSETers=========================================
	 * ===================================================
	 */

	public Set<Branch> getBranches()
	{
		return branches;
	}

	public void setBranches(Set<Branch> branches)
	{
		this.branches = branches;
	}

	public Set<OWLObjectPropertyDomainAxiom> getObjPropertiesDomainAxioms()
	{
		return objPropertiesDomainAxioms;
	}

	public void setObjPropertiesDomainAxioms(
			Set<OWLObjectPropertyDomainAxiom> objPropertiesDomainAxioms)
	{
		this.objPropertiesDomainAxioms = objPropertiesDomainAxioms;
	}

	public Set<OWLObjectProperty> getObjProperties()
	{
		return objProperties;
	}

	public void setObjProperties(Set<OWLObjectProperty> objProperties)
	{
		this.objProperties = objProperties;
	}

	public Set<OWLClass> getActiveConcepts()
	{
		return activeConcepts;
	}

	public void setActiveConcepts(Set<OWLClass> activeConcepts)
	{
		this.activeConcepts = activeConcepts;
	}

	public ArrayList<OWLClass> getRelatedConcepts()
	{
		return relatedConcepts;
	}

	public void setRelatedConcepts(ArrayList<OWLClass> relatedConcepts)
	{
		this.relatedConcepts = relatedConcepts;
	}
	
	/* (non-Javadoc)
	 * @see ru.iimm.ontology.odp.pattgen.model.pattern.synpat.AbstractSyntCDP#getSuitablePattern()
	 */
	public List<IRI> getSuitablePattern()
	{
		ArrayList<IRI> patterns = new ArrayList<>();
		patterns.addAll(EnumeratedCDP.getEnumeratedPatternIRIs());
		patterns.addAll(SimpleCollectionCDP.getSimpleCollectionPatternIRIs());
		patterns.addAll(ItemedCollectionCDP.getItemedCollectionPatternIRIs());
		
		patterns.addAll(PositionCDP.getPositionPatternIRIs());
		patterns.addAll(SinglePositionCDP.getSingePositionPatternIRIs());
		patterns.addAll(SinglePropertyCDP.getSinglePropertyPatternIRIs());


		
		return patterns;
	}


}
