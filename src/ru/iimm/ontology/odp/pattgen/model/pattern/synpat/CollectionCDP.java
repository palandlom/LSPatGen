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

public class CollectionCDP extends SimpleCollectionCDP
{
	/* =========== Common const ============== */
	public final static String ONT_IRI = "http://www.ontologydesignpatterns.org/cp/owl/collectionentity.owl";
	static String BASE_IRI = ONT_IRI + "#";
	public static String FILENAME = "collectionentity.owl";

	/* Class names */
	static String COLLECTION_IRI = BASE_IRI + "Collection";

	/* Properties names */
	static String HAS_MEMBER_IRI = BASE_IRI + "hasMember";
	static String IS_MEMBER_OF_IRI = BASE_IRI + "isMemberOf";
	/* ===================================== */

	private OWLObjectProperty hasMember;
	private OWLObjectProperty isMemberOf;

	/**
	 * Создает паттерн из файла-онтологии по-умолчанию см
	 * {@linkplain} ContentDesingPattern}
	 */
	public CollectionCDP()
	{
		super(ContentDesingPattern.getCDPFile(CollectionCDP.FILENAME));
		this.init();
	}

	/**
	 * Создает паттерн с новым {@link IRI}, который используется как основа для
	 * последующего синтеза
	 * 
	 * @param patternOntologyIRI
	 */

	public CollectionCDP(IRI patternOntologyIRI)
	{
		super(patternOntologyIRI,
				new Ontology(
						ContentDesingPattern.getCDPFile(CollectionCDP.FILENAME),
						true, true));
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
		this.collectionConcept = df.getOWLClass(IRI.create(COLLECTION_IRI));
		this.contentConcept = df.getOWLThing();
		this.hasMember = df.getOWLObjectProperty(IRI.create(HAS_MEMBER_IRI));
		this.isMemberOf = df.getOWLObjectProperty(IRI.create(IS_MEMBER_OF_IRI));

		this.setSubCollection();
		// this.setSubElement();

	}

	@Override
	public Set<OWLAxiom> getCollectionMemberAxioms(OWLClass content)
	{
		Set<OWLAxiom> axioms = new HashSet<>();

		OWLObjectSomeValuesFrom itemHasContentSomeRestriction = df
				.getOWLObjectSomeValuesFrom(this.getHasMember(), content);

		axioms.add(df.getOWLSubClassOfAxiom(this.collectionConcept,
				itemHasContentSomeRestriction));

		return axioms;
	}

	/*
	 * =========================================================
	 * getseters=================================================
	 * ===============================================================
	 */

	public OWLObjectProperty getHasMember()
	{
		return hasMember;
	}

	public void setHasMember(OWLObjectProperty hasMember)
	{
		this.hasMember = hasMember;
	}

	public OWLObjectProperty getIsMemberOf()
	{
		return isMemberOf;
	}

	public void setIsMemberOf(OWLObjectProperty isMemberOf)
	{
		this.isMemberOf = isMemberOf;
	}

}
