package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import java.io.File;
import java.util.HashSet;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;

import ru.iimm.ontology.odp.pattgen.model.pattern.ContentDesingPattern;
import ru.iimm.ontology.ontAPI.Ontology;

public class SequenceCDP extends SinglePropertyCDP
{
	/* Common const */
	public final static String ONT_IRI = "http://www.ontologydesignpatterns.org/cp/owl/sequence.owl";
	static String BASE_IRI = ONT_IRI + "#";
	public final static String FILENAME = "sequence.owl";

	/* Properties names */
	static String PRECEDES_IRI = BASE_IRI + "precedes";
	static String DIR_PRECEDES_IRI = BASE_IRI + "directlyPrecedes";
	static String FOLLOWS_IRI = BASE_IRI + "follows";
	static String DIR_FOLLOWS_IRI = BASE_IRI + "directlyFollows";

	private OWLObjectProperty precedes;
	private OWLObjectProperty directlyPrecedes;
	private OWLObjectProperty follows;
	private OWLObjectProperty directlyFollows;

	// private HashSet<OWLAxiom> situationAxioms;

	/**
	 * Концепт, на котором задается последовательность
	 */
	private OWLClass thing;
	/**
	 * Концепт-последовательность используется в случае ее добавления
	 * последовательноси в ситуацию в ситуацию.
	 */
	private OWLClass sequence;

	public SequenceCDP(File ontfile)
	{
		super(ontfile);
		init();
	}

	public SequenceCDP()
	{
		super(ContentDesingPattern.getCDPFile(SequenceCDP.FILENAME));
		init();
	}

	protected void init()
	{
		/* A) Задаем спец-ие поля паттерна */
		this.precedes = df.getOWLObjectProperty(IRI.create(PRECEDES_IRI));
		this.directlyPrecedes = df
				.getOWLObjectProperty(IRI.create(DIR_PRECEDES_IRI));
		this.follows = df.getOWLObjectProperty(IRI.create(FOLLOWS_IRI));
		this.directlyFollows = df
				.getOWLObjectProperty(IRI.create(DIR_FOLLOWS_IRI));
		this.thing = df.getOWLThing();
		this.sequence = df.getOWLClass(
				AbstractSyntCDP.getSubIRI(this.getBaseIRI() + "Sequence"));

		/* А1) Собираем отношения... */
		this.objProperties.add(precedes);
		this.objProperties.add(directlyPrecedes);
		this.objProperties.add(follows);
		this.objProperties.add(directlyFollows);

		/*
		 * А2) Собираем активные концепты (по которым паттерн будет
		 * сопрягаться)...
		 */
		this.activeConcepts.add(this.thing);

		/*
		 * А3) Собираем связанные концепты (по которым settingfor отношения
		 * будут генериться)...
		 */
		this.relatedConcepts.add(this.sequence);

	}

	/**
	 * Генерит аксиомы, добавляемые в ситуацию в случае комбинирования данной
	 * последовательности с ситуационным паттерном.
	 */

	public HashSet<OWLAxiom> getSituationAxioms()
	{
		HashSet<OWLAxiom> axioms = new HashSet<>();
		/* Создаем классы и отношения... */
		OWLClass superSegment = df
				.getOWLClass(IRI.create(this.getBaseIRI() + "Segment"));
		OWLClass superSequence = df
				.getOWLClass(IRI.create(this.getBaseIRI() + "Sequence"));
		OWLClass subSegment = df.getOWLClass(
				AbstractSyntCDP.getSubIRI(this.getBaseIRI() + "Segment"));
		
		OWLObjectProperty hasSegment = df.getOWLObjectProperty(
				IRI.create(this.getBaseIRI() + "hasSegment"));
		OWLObjectProperty isSegment = df.getOWLObjectProperty(
				IRI.create(this.getBaseIRI() + "isSegmentOf"));
		OWLObjectProperty hasRelationTo = df.getOWLObjectProperty(
				IRI.create(this.getBaseIRI() + "hasRelationTo"));
		OWLObjectProperty isRelationOf = df.getOWLObjectProperty(
				IRI.create(this.getBaseIRI() + "isRelationOf"));

		/* Создаем аксиомы... */
		/* ...SUBCLASS axioms */
		axioms.add(df.getOWLSubClassOfAxiom(subSegment, superSegment));
		axioms.add(df.getOWLSubClassOfAxiom(this.sequence, superSequence));
		
		/* ...DOMAIN и RANGE axioms */
		axioms.addAll(AbstractSyntCDP.getDomainRangeInverseAxioms(hasSegment,
				superSequence, superSegment, isSegment));
		axioms.addAll(AbstractSyntCDP.getDomainRangeInverseAxioms(hasRelationTo,
				superSegment, df.getOWLThing(), isRelationOf));

		/* ...subSequence --hasSegment--> subSegment... */
		OWLObjectSomeValuesFrom hasSegmentSomeSegment = df
				.getOWLObjectSomeValuesFrom(hasSegment, subSegment);
		axioms.add(
				df.getOWLSubClassOfAxiom(this.sequence, hasSegmentSomeSegment));
		OWLObjectSomeValuesFrom isSegmentSomeSequence = df
				.getOWLObjectSomeValuesFrom(isSegment, this.sequence);
		axioms.add(df.getOWLSubClassOfAxiom(subSegment, isSegmentSomeSequence));

		/* ....superSegment --hasRelationTo--> thing... */
		OWLObjectSomeValuesFrom hasRelationToSomeThing = df
				.getOWLObjectSomeValuesFrom(hasRelationTo, df.getOWLThing());
		axioms.add(
				df.getOWLSubClassOfAxiom(superSegment, hasRelationToSomeThing));
		
		/* Если в последовательности упорядочиывается контретный объект (не thing),
		 *  то создаем аксиомы для него...*/
		/* ....subSegment --hasRelationTo--> this.thing... */		
		if ((this.thing != null) && (this.thing!= df.getOWLThing()))
		{
			OWLObjectSomeValuesFrom hasRelationToSomeObject = df
					.getOWLObjectSomeValuesFrom(hasRelationTo, this.thing);
			axioms.add(
					df.getOWLSubClassOfAxiom(subSegment, hasRelationToSomeObject));
			
		}

		/* ....subSegment --hasDirNext--> subSegment... */
		OWLObjectSomeValuesFrom hasDirNextSomeSegment = df
				.getOWLObjectSomeValuesFrom(this.directlyFollows, subSegment);
		axioms.add(df.getOWLSubClassOfAxiom(subSegment, hasDirNextSomeSegment));
		OWLObjectSomeValuesFrom hasDirPrevSomeSegment = df
				.getOWLObjectSomeValuesFrom(this.directlyPrecedes, subSegment);
		axioms.add(df.getOWLSubClassOfAxiom(subSegment, hasDirPrevSomeSegment));

		return axioms;
	}

	/**
	 * Генерит аксиомы, добавляемые в позиционный паттерн. Аксиомы =
	 * последовательность this.thing.
	 * 
	 * @return
	 */
	public HashSet<OWLAxiom> getPositionAxioms()
	{
		HashSet<OWLAxiom> axioms = new HashSet<>();
		/* ....thing --hasNext--> thing... */
		OWLObjectSomeValuesFrom hasNextSomeSegment = df
				.getOWLObjectSomeValuesFrom(this.follows, this.thing);
		axioms.add(df.getOWLSubClassOfAxiom(this.thing, hasNextSomeSegment));
		OWLObjectSomeValuesFrom hasPrevSomeSegment = df
				.getOWLObjectSomeValuesFrom(this.precedes, this.thing);
		axioms.add(df.getOWLSubClassOfAxiom(this.thing, hasPrevSomeSegment));

		/* ...thing --hasDirNext--> thing... */
		OWLObjectSomeValuesFrom hasDirNextSomeSegment = df
				.getOWLObjectSomeValuesFrom(this.directlyFollows, this.thing);
		axioms.add(df.getOWLSubClassOfAxiom(this.thing, hasDirNextSomeSegment));
		OWLObjectSomeValuesFrom hasDirPrevSomeSegment = df
				.getOWLObjectSomeValuesFrom(this.directlyPrecedes, this.thing);
		axioms.add(df.getOWLSubClassOfAxiom(this.thing, hasDirPrevSomeSegment));

		return axioms;
	}

	/*
	 * ======================================================================
	 * GETSETers ======================================================
	 * ====================================================================
	 */

	/**
	 * @return the {@linkplain #pRECEDES_IRI}
	 */
	public static String getPRECEDES_IRI()
	{
		return PRECEDES_IRI;
	}

	/**
	 * @return the {@linkplain #precedes}
	 */
	public OWLObjectProperty getPrecedes()
	{
		return precedes;
	}

	/**
	 * @return the {@linkplain #directlyPrecedes}
	 */
	public OWLObjectProperty getDirectlyPrecedes()
	{
		return directlyPrecedes;
	}

	/**
	 * @return the {@linkplain #follows}
	 */
	public OWLObjectProperty getFollows()
	{
		return follows;
	}

	/**
	 * @return the {@linkplain #directlyFollows}
	 */
	public OWLObjectProperty getDirectlyFollows()
	{
		return directlyFollows;
	}

	public OWLClass getThing()
	{
		return thing;
	}

	public void setThing(OWLClass thing)
	{
		this.thing = thing;
	}

}
