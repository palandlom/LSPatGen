package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import ru.iimm.ontology.odp.pattgen.model.pattern.ContentDesingPattern;
import ru.iimm.ontology.ontAPI.Ontology;

/**
 * Sequence Bag Collection List Set
 * 
 * @author lomov
 *
 */
/**
 * Паттерн-коллекция 
 * @author lomov
 *
 */
public abstract class EnumeratedCDP extends AbstractSyntCDP
{

	/**
	 * Концепт, определяющий множество
	 */
	OWLClass collectionConcept;

	/**
	 * Класс=содержание элемента множества.
	 */
	OWLClass contentConcept;

	/**
	 * Концепт, определяющий элемент множества
	 */
	//-OWLClass elementConcept;
	
	static public List<IRI> getEnumeratedPatternIRIs()
	{
		List<IRI> patterns = new ArrayList<>();
	
		return patterns;
	}


	/**
	 * @param ontfile
	 */
	public EnumeratedCDP(File ontfile)
	{
		super(ontfile);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param ontologyIRI
	 * @param basedPatOnt
	 */
	public EnumeratedCDP(IRI ontologyIRI, Ontology basedPatOnt)
	{
		super(ontologyIRI, basedPatOnt);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Меняет класс-коллекцию паттерна на указанную
	 * 
	 * @param newCollection
	 */
	public void changeCollectionConcept(OWLClass newCollection)
	{
		this.changeConcept(this.collectionConcept, newCollection);
		this.collectionConcept = newCollection;
	}

	/**
	 * Меняет класс-элемент паттерна на указанный
	 * 
	 * @param newElement
	 */
	public void changeElementConcept(OWLClass newElement)
	{
		this.changeConcept(this.collectionConcept, newElement);
		this.collectionConcept = newElement;
	}

	/**
	 * Меняет класс в наборе аксиом и онтологии паттерна
	 * 
	 * @param curConcept
	 * @param newConcept
	 */
	private void changeConcept(OWLClass curConcept, OWLClass newConcept)
	{
		/*
		 * Переменовываем(заменяем) IRI старой коллекции (subClass) в онтологии
		 * паттерна..
		 */
		this.getCDPOntology().renameEntity(curConcept, newConcept.getIRI());
		/*
		 * Заменяем аксиому в наборе аксиом... .......................... ...
		 * ...Удаляем старую аксиому: TODO может просто исземенную онтоогиюю
		 * превратить в набо аксиом (subClass) -subclassOf- Collection
		 */
		OWLSubClassOfAxiom removeAxiom = null;
		OWLClass defaultSuperClass = null;
		Iterator<OWLAxiom> it = this.getAxioms().iterator();
		while (it.hasNext() && removeAxiom == null)
		{
			if (((OWLAxiom) it).isOfType(AxiomType.SUBCLASS_OF)
					&& ((OWLSubClassOfAxiom) it)
							.getSubClass() == this.collectionConcept
					&& !((OWLSubClassOfAxiom) it).getSuperClass().isAnonymous())
			{
				removeAxiom = (OWLSubClassOfAxiom) it;
				defaultSuperClass = removeAxiom.getSuperClass().asOWLClass();
			}
		}

		/* ... Создаем новую аксиому подкласса... */
		OWLSubClassOfAxiom ax = AbstractSyntCDP
				.getSubClassAxiom(defaultSuperClass);
		this.addAxiom(ax);

	}

	/**
	 * Создает аксимому принадлежности класса текущей коллекции.
	 * @param member
	 * @return
	 */
	abstract public Set<OWLAxiom> getCollectionMemberAxioms(OWLClass member);

	/**
	 * Заменяет стандартный класс-коллекцию на сгенерированный подкласс.
	 */
	protected void setSubCollection()
	{
		OWLSubClassOfAxiom sbAxiom = this
				.getSubClassAxiom(this.collectionConcept);
		this.setCollectionConcept(sbAxiom.getSubClass().asOWLClass());
		this.addAxiom(sbAxiom);
	}


	
	/**
	 * Инициализирует поля setConcept и elementConcept;
	 */
	protected abstract void init();

	/*
	 * ======================================== =
	 * GET/SETers===========================
	 * ========================================
	 */
	public OWLClass getSetConcept()
	{
		return collectionConcept;
	}

	public void setSetConcept(OWLClass setConcept)
	{
		this.collectionConcept = setConcept;
	}


	public OWLClass getCollectionConcept()
	{
		return collectionConcept;
	}

	public void setCollectionConcept(OWLClass collectionConcept)
	{
		this.collectionConcept = collectionConcept;
	}
	
	/* (non-Javadoc)
	 * @see ru.iimm.ontology.odp.pattgen.model.pattern.synpat.AbstractSyntCDP#getSuitablePattern()
	 */
	public List<IRI> getSuitablePattern()
	{
		ArrayList<IRI> patterns = new ArrayList<>();
		return patterns;
	}


}
