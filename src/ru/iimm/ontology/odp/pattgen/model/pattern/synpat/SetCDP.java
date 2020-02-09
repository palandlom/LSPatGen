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

public class SetCDP extends SimpleCollectionCDP
{

	/* =========== Common const ============== */
	public final static String ONT_IRI = "http://www.ontologydesignpatterns.org/cp/owl/set.owl";
	static String BASE_IRI = ONT_IRI + "#";
	public final static String FILENAME = "set.owl";

	/* Class names */
	static String SET_IRI = BASE_IRI + "Set";

	/* Properties names */
	/* берутся из коллекции*/
	/* ===================================== */

	private OWLObjectProperty hasMember;
	private OWLObjectProperty isMemberOf;

	/**
	 * Создает паттерн из файла-онтологии по-умолчанию см
	 * {@linkplain} ContentDesingPattern}
	 */
	public SetCDP()
	{
		super(ContentDesingPattern.getCDPFile(SetCDP.FILENAME));
		this.init();

	}

	/**
	 * Создает паттерн с новым {@link IRI}, который используется как основа для
	 * последующего синтеза
	 * 
	 * @param patternOntologyIRI
	 */

	public SetCDP(IRI patternOntologyIRI)
	{
		super(patternOntologyIRI, new Ontology(
				ContentDesingPattern.getCDPFile(SetCDP.FILENAME), true, true));
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
		this.collectionConcept = df.getOWLClass(IRI.create(SET_IRI));
		this.contentConcept = df.getOWLThing();
		CollectionCDP collectionCDP = new CollectionCDP();
		this.hasMember = collectionCDP.getHasMember();
		this.isMemberOf = collectionCDP.getIsMemberOf();
		
		this.setSubCollection();
//		this.setSubElement();

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
