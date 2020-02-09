package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;

import ru.iimm.ontology.odp.pattgen.model.pattern.ContentDesingPattern;
import ru.iimm.ontology.ontAPI.Ontology;

public class BagCDP extends ItemedCollectionCDP
{
	/* =========== Common const ============== */
	public final static String ONT_IRI = "http://www.ontologydesignpatterns.org/cp/owl/bag.owl";
	static String BASE_IRI = ONT_IRI + "#";
	public static String FILENAME = "bag.owl";

	/* Class names */
	static String BAG_IRI = BASE_IRI + "Bag";
	static String ITEM_IRI = BASE_IRI + "Item";

	/* Properties names */
	static String ITEM_CONTENT = BASE_IRI + "itemContent";
	static String HAS_ITEM = BASE_IRI + "hasItem";
	static String ITEM_OF = BASE_IRI + "itemOf";
	/* ===================================== */

	private OWLObjectProperty itemContent;
	private OWLObjectProperty hasItem;
	private OWLObjectProperty itemOf;

	/**
	 * Создает паттерн из файла-онтологии по-умолчанию см
	 * {@linkplain} ContentDesingPattern}
	 */
	public BagCDP()
	{
		super(ContentDesingPattern.getCDPFile(BagCDP.FILENAME));
		this.init();

	}

	/**
	 * Создает паттерн с новым {@link IRI}, который используется как основа для
	 * последующего синтеза
	 * 
	 * @param patternOntologyIRI
	 */

	public BagCDP(IRI patternOntologyIRI)
	{
		super(patternOntologyIRI, new Ontology(
				ContentDesingPattern.getCDPFile(BagCDP.FILENAME), true, true));
		this.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ru.iimm.ontology.odp.pattgen.model.pattern.synpat.EnumeratedCDP#init()
	 */
	@Override
	protected void init()	
	{
		super.init();
		this.collectionConcept = df.getOWLClass(IRI.create(BAG_IRI));
		//this.elementConcept = df.getOWLClass(IRI.create(ITEM_IRI));

		this.itemContent = df.getOWLObjectProperty(IRI.create(ITEM_CONTENT));
		this.hasItem = df.getOWLObjectProperty(IRI.create(HAS_ITEM));
		this.itemOf = df.getOWLObjectProperty(IRI.create(ITEM_OF));

		this.setSubCollection();
		this.setSubElement();
		
		/* Задаем аксиому: SubCollection --hasItem--> item...*/
		this.addAxioms(this.getCollectionMemberAxioms(this.collectionItem));
	}

	@Override
	public Set<OWLAxiom> getCollectionMemberAxioms(OWLClass collectionItem)
	{

		Set<OWLAxiom> axioms = new HashSet<>();

		OWLObjectSomeValuesFrom hasItemSomeRestriction = df
				.getOWLObjectSomeValuesFrom(this.getHasItem(), collectionItem);
		OWLObjectSomeValuesFrom itemOfSomeRestriction = df
				.getOWLObjectSomeValuesFrom(this.itemOf,
						this.collectionConcept);

		axioms.add(df.getOWLSubClassOfAxiom(this.collectionConcept,
				hasItemSomeRestriction));
		axioms.add(df.getOWLSubClassOfAxiom(this.collectionItem,
				itemOfSomeRestriction));

		return axioms;

	}

	@Override
	public OWLAxiom getItemContentAxiom(OWLClass content)
	{
		OWLObjectSomeValuesFrom itemHasContentSomeRestriction = df
				.getOWLObjectSomeValuesFrom(this.getItemContent(), content);
		return df.getOWLSubClassOfAxiom(this.collectionItem,
				itemHasContentSomeRestriction);
	}
	
	@Override
	public void setCollectionItem()
	{
	this.collectionItem = df.getOWLClass(IRI.create(ITEM_IRI));
		
	}

	/*
	 * =========================================================================
	 * = ==== getseters=========================================================
	 * =========================================================================
	 */

	public OWLObjectProperty getItemContent()
	{
		return itemContent;
	}

	public void setItemContent(OWLObjectProperty itemContent)
	{
		this.itemContent = itemContent;
	}

	public OWLObjectProperty getHasItem()
	{
		return hasItem;
	}

	public void setHasItem(OWLObjectProperty hasItem)
	{
		this.hasItem = hasItem;
	}

	public OWLObjectProperty getItemOf()
	{
		return itemOf;
	}

	public void setItemOf(OWLObjectProperty itemOf)
	{
		this.itemOf = itemOf;
	}



}
