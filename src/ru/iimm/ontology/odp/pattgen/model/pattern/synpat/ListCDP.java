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

public class ListCDP extends ItemedCollectionCDP
{

	/* =========== Common const ============== */
	public final static String ONT_IRI = "http://www.ontologydesignpatterns.org/cp/owl/list.owl";
	static String BASE_IRI = ONT_IRI + "#";
	public final static String FILENAME = "list.owl";

	/* Class names */
	static String LIST_IRI = BASE_IRI + "List";
	static String LIST_ITEM_IRI = BASE_IRI + "ListItem";

	/* Properties names */
	static String HAS_FIRST_ITEM = BASE_IRI + "hasFirstItem";
	static String HAS_FIRST_ITEM_OF = BASE_IRI + "firstItemOf";
	static String HAS_LAST_ITEM = BASE_IRI + "hasLastItem";
	static String HAS_LAST_ITEM_OF = BASE_IRI + "hasLastItemOf";
	static String PREVIOUS_ITEM = BASE_IRI + "previousItem";
	static String NEXT_ITEM = BASE_IRI + "nextItem";

	static String ITEM_CONTENT = BASE_IRI + "itemContent";

	/* ===================================== */
	/* =====Native elem ============ */
	private OWLObjectProperty hasFirstItem;
	private OWLObjectProperty isFirstItemOf;
	private OWLObjectProperty hasLastItem;
	private OWLObjectProperty isLastItemOf;
	private OWLObjectProperty previousItem;
	private OWLObjectProperty nextItem;
	private OWLObjectProperty itemContent;

	/**
	 * Создает паттерн из файла-онтологии по-умолчанию см
	 * {@linkplain} ContentDesingPattern}
	 */
	public ListCDP()
	{
		super(ContentDesingPattern.getCDPFile(ListCDP.FILENAME));
		this.init();

	}

	/**
	 * Создает паттерн с новым {@link IRI}, который используется как основа для
	 * последующего синтеза
	 * 
	 * @param patternOntologyIRI
	 */

	public ListCDP(IRI patternOntologyIRI)
	{
		super(patternOntologyIRI, new Ontology(
				ContentDesingPattern.getCDPFile(ListCDP.FILENAME), true, true));
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
		/* === Native Properties ================== */
		this.hasFirstItem = df.getOWLObjectProperty(IRI.create(HAS_FIRST_ITEM));
		this.isFirstItemOf = df
				.getOWLObjectProperty(IRI.create(HAS_FIRST_ITEM_OF));
		this.hasLastItem = df.getOWLObjectProperty(IRI.create(HAS_LAST_ITEM));
		this.isLastItemOf = df
				.getOWLObjectProperty(IRI.create(HAS_LAST_ITEM_OF));
		this.previousItem = df.getOWLObjectProperty(IRI.create(PREVIOUS_ITEM));
		this.nextItem = df.getOWLObjectProperty(IRI.create(NEXT_ITEM));
		this.itemContent = df.getOWLObjectProperty(IRI.create(ITEM_CONTENT));

		/* === Native Concept ================== */
		this.collectionConcept = df.getOWLClass(IRI.create(LIST_IRI));
//		this.elementConcept = df.getOWLClass(IRI.create(LIST_ITEM_IRI));

		this.setSubCollection();
		this.setSubElement();

		/* Задаем аксиому: SubCollection --hasItem--> item... */
		this.addAxioms(this.getCollectionMemberAxioms(this.collectionItem));

	}

	@Override
	public Set<OWLAxiom> getCollectionMemberAxioms(OWLClass collectionItem)
	{
		Set<OWLAxiom> axioms = new HashSet<>();

		OWLObjectSomeValuesFrom hasFirstMemberSomeRestriction = df
				.getOWLObjectSomeValuesFrom(this.getHasFirstItem(),
						collectionItem);
		OWLObjectSomeValuesFrom hasLastMemberSomeRestriction = df
				.getOWLObjectSomeValuesFrom(this.getHasLastItem(),
						collectionItem);

		OWLObjectSomeValuesFrom isFirstItemSomeRestriction = df
				.getOWLObjectSomeValuesFrom(this.getIsFirstItemOf(),
						this.collectionConcept);
		OWLObjectSomeValuesFrom isLastMemberSomeRestriction = df
				.getOWLObjectSomeValuesFrom(this.getIsLastItemOf(),
						this.collectionConcept);

		axioms.add(df.getOWLSubClassOfAxiom(this.collectionConcept,
				hasFirstMemberSomeRestriction));
		axioms.add(df.getOWLSubClassOfAxiom(this.collectionConcept,
				hasLastMemberSomeRestriction));

		axioms.add(df.getOWLSubClassOfAxiom(this.collectionItem,
				isFirstItemSomeRestriction));
		axioms.add(df.getOWLSubClassOfAxiom(this.collectionItem,
				isLastMemberSomeRestriction));

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
		//BagCDP bagCDP = new BagCDP();

		this.collectionItem = df.getOWLClass(IRI.create(LIST_ITEM_IRI));

	}

	/*
	 * =======================================================================
	 * === GETSETERS =====================================================
	 * ==============================================================
	 */

	public OWLObjectProperty getHasFirstItem()
	{
		return hasFirstItem;
	}

	public void setHasFirstItem(OWLObjectProperty hasFirstItem)
	{
		this.hasFirstItem = hasFirstItem;
	}

	public OWLObjectProperty getIsFirstItemOf()
	{
		return isFirstItemOf;
	}

	public void setIsFirstItemOf(OWLObjectProperty isFirstItemOf)
	{
		this.isFirstItemOf = isFirstItemOf;
	}

	public OWLObjectProperty getHasLastItem()
	{
		return hasLastItem;
	}

	public void setHasLastItem(OWLObjectProperty hasLastItem)
	{
		this.hasLastItem = hasLastItem;
	}

	public OWLObjectProperty getIsLastItemOf()
	{
		return isLastItemOf;
	}

	public void setIsLastItemOf(OWLObjectProperty isLastItemOf)
	{
		this.isLastItemOf = isLastItemOf;
	}

	public OWLObjectProperty getPreviousItem()
	{
		return previousItem;
	}

	public void setPreviousItem(OWLObjectProperty previousItem)
	{
		this.previousItem = previousItem;
	}

	public OWLObjectProperty getNextItem()
	{
		return nextItem;
	}

	public void setNextItem(OWLObjectProperty nextItem)
	{
		this.nextItem = nextItem;
	}

	public OWLObjectProperty getItemContent()
	{
		return itemContent;
	}

	public void setItemContent(OWLObjectProperty itemContent)
	{
		this.itemContent = itemContent;
	}

}
