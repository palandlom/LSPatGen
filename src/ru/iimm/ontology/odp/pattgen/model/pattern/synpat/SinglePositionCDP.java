/**
 * 
 */
package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import ru.iimm.ontology.odp.pattgen.model.pattern.ContentDesingPattern;
import ru.iimm.ontology.ontAPI.Ontology;

/**
 * Паттерн, имеющий после создания только один активный концепт + одно
 * отношение. При добавление в позиционный (а может и любой) паттерн - концепт
 * этого паттерна должен связываться с концептом другого паттерна этим
 * отношением.
 * Паттерны: ToE, Region, TimeInverval
 * @author lomov
 *
 */
public abstract class SinglePositionCDP extends PositionCDP
{

	/**
	 * Класс концепта, выбранный пользователем для использования при синтезе.
	 * 
	 */
	protected IRI activeConceptClassIRI;

	/**
	 * 
	 */
	OWLClass activeConcept;

	static public List<IRI> getSingePositionPatternIRIs()
	{
		List<IRI> patterns = new ArrayList<>();
		patterns.add(IRI.create(RegionCDP.ONT_IRI));
		patterns.add(IRI.create(TimeIntervalCDP.ONT_IRI));
		patterns.add(IRI.create(TypeOfEntitiesCDP.ONT_IRI));
		return patterns;
	}
		
	
	/**
	 * Создает паттерн не меняя {@link IRI} исходной онтологии. Применяется для
	 * последующего использования как компонент при синтезе
	 * @param ontfile  файл с паттерном-основой
	 * @param activeConceptClassIRI
	 */
	public SinglePositionCDP(File ontfile, IRI activeConceptClassIRI)
	{
		super(ontfile);
		this.activeConceptClassIRI = activeConceptClassIRI;
		/* init(); будет вызван из подкласссовского init() */

	}

	/**
	 * Создает паттерн с новым {@link IRI}, который используется как основа для
	 * последующего синтеза
	 * @param ontologyIRI новый {@link IRI} создаваемого паттерна
	 * @param basedPatOnt онтология паттерна-основы
	 * @param activeConceptClassIRI
	 */
	public SinglePositionCDP(IRI ontologyIRI, Ontology basedPatOnt,
			IRI activeConceptClassIRI)
	{
		super(ontologyIRI, basedPatOnt);
		this.activeConceptClassIRI = activeConceptClassIRI;
		/* init(); будет вызван из подкласссовского init() */

	}

	@Override
	protected void init()
	{
		/*
		 * Создаем новый концепт - подкласс оригинального класса. Например:
		 * мойРегион - подкласс Region
		 */
		OWLClass superActiveConcept = this.df
				.getOWLClass(this.activeConceptClassIRI);

		OWLClass activeConcept = this.df
				.getOWLClass(AbstractSyntCDP.getSubIRI(activeConceptClassIRI));
		this.activeConcepts.add(activeConcept);
		this.relatedConcepts.add(activeConcept);
		this.activeConcept = activeConcept;

		/*
		 * ... дополняем список аксиом - добавляем аксиому subclassOf для нового
		 * концепта
		 */
		OWLSubClassOfAxiom conceptOfClassAxiom = df
				.getOWLSubClassOfAxiom(activeConcept, superActiveConcept);
		this.getAxioms().add(conceptOfClassAxiom);
		this.getCDPOntology().mng.addAxiom(this.getCDPOntology().ontInMem,
				conceptOfClassAxiom);
	}

	/*
	 * =========================================================
	 * ======GET/SETers========================================
	 * ========================================================
	 */

	public OWLClass getActiveConcept()
	{
		return activeConcept;
	}

}
