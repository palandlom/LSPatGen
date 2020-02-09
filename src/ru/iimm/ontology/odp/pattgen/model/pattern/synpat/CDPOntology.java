package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.iimm.ontology.ontAPI.Ontology;

public class CDPOntology
{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CDPOntology.class);

	private static Ontology instance;
	private static String ontFilePath = "res/cdpOnt/CDPOntology.owl";

	public static synchronized Ontology getInstance()
	{
		if (instance == null)
		{
			File ontFile = new File(ontFilePath);
			instance = new Ontology(ontFile, false, true);
		}
		return instance;
	}

	public static boolean isSubclassOf(OWLClass subclass, OWLClass superClass)
	{
		OWLSubClassOfAxiom subClassOfSuperClass = getInstance().df
				.getOWLSubClassOfAxiom(subclass, superClass);
		return CDPOntology.getInstance().reas.isEntailed(subClassOfSuperClass);
	}

	public static boolean isEqualClass(OWLClass classA, OWLClass classB)
	{
		OWLEquivalentClassesAxiom equivAxiom = getInstance().df
				.getOWLEquivalentClassesAxiom(classA, classB);
		return CDPOntology.getInstance().reas.isEntailed(equivAxiom);
	}

	public static boolean isSameClass(OWLClass classA, OWLClass classB)
	{

		return classA.getIRI().equals(classB.getIRI());
	}

	/**
	 * Определяет как связаны классы и возвращает соответствующие аксиомы
	 * 
	 * @param classA
	 * @param classB
	 * @return
	 */
	public static Set<OWLAxiom> getLinkedAxiom(OWLClass classA, OWLClass classB)
	{
		HashSet<OWLAxiom> axioms = new HashSet<>();
		if (isSameClass(classA, classB))
		{
			LOGGER.info("  <add:" + classA.getIRI().getFragment()
					+ "> hasSameIRI <cur:" + classB.getIRI().getFragment()
					+ ">");

		} else if (isEqualClass(classA, classB))
		{
			LOGGER.info("  <add:" + classA.getIRI().getFragment()
					+ "> equalTo <cur:" + classB.getIRI().getFragment() + ">");

			axioms.add(getInstance().df.getOWLEquivalentClassesAxiom(classA,
					classB));

		} else if (isSubclassOf(classA, classB))
		{
			LOGGER.info("  <add:" + classA.getIRI().getFragment()
					+ "> subclassOf <cur:" + classB.getIRI().getFragment()
					+ ">");

			axioms.add(getInstance().df.getOWLSubClassOfAxiom(classA, classB));

		}
		else if (isSubclassOf(classB, classA))
		{
			LOGGER.info("  <add:" + classB.getIRI().getFragment()
					+ "> subclassOf <cur:" + classA.getIRI().getFragment()
					+ ">");

			axioms.add(getInstance().df.getOWLSubClassOfAxiom(classB, classA));

		}

		return axioms;
	}

}
