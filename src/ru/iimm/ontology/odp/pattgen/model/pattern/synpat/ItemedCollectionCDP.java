/**
 * 
 */
package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import com.sun.jmx.snmp.Enumerated;

import ru.iimm.ontology.ontAPI.Ontology;

/**
 * Паттерн, описывающий коллекцию включающую непосредственно элементы, которые в
 * свою очередь сопрягаются с копцептами-содержанием. т.е. (коллекция ==>
 * элемент ==> концепт)
 * 
 * @author lomov
 *
 */

public abstract class ItemedCollectionCDP extends EnumeratedCDP
{
	
	static public List<IRI> getItemedCollectionPatternIRIs()
	{
		List<IRI> patterns = new ArrayList<>();
		patterns.add(IRI.create(BagCDP.ONT_IRI));
		patterns.add(IRI.create(ListCDP.ONT_IRI));
		return patterns;
	}


	/**
	 * Концепт-элемент коллекции, который содержит уже элемент предметной
	 * области.
	 */
	OWLClass collectionItem;

	public ItemedCollectionCDP(File ontfile)
	{
		super(ontfile);
		// TODO Auto-generated constructor stub
	}

	public ItemedCollectionCDP(IRI ontologyIRI, Ontology basedPatOnt)
	{
		super(ontologyIRI, basedPatOnt);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Создает аксиому принадлежности класса-контента элементу текущей коллекции.
	 * 
	 * @param content
	 * @return
	 */
	abstract public OWLAxiom getItemContentAxiom(OWLClass content);

	@Override
	protected void init()
	{
		this.setCollectionItem();

	}

	/**
	 * Устанавливает зависящий от конкретной коллекции класс = элемент
	 * коллекции.
	 */
	public abstract void setCollectionItem();

	/**
	 * Заменяет стандартный класс-элемент на сгенерированный подкласс.
	 */
	protected void setSubElement()
	{
		OWLSubClassOfAxiom sbAxiom = this.getSubClassAxiom(this.collectionItem);
		this.collectionItem = sbAxiom.getSubClass().asOWLClass();
		this.addAxiom(sbAxiom);
	}

	/*
	 * ======================================================================
	 * === GETSETTERS =======================================================
	 * ==========================================================
	 */

	public OWLClass getCollectionItem()
	{
		return collectionItem;
	}

}
