/**
 * 
 */
package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;

import ru.iimm.ontology.ontAPI.Ontology;

/**
 * Коллекция, которая непосредственно содержит сущности предметной области, те
 * без предварительной инкапсуляции их в элементы коллекции (коллекция ==>
 * концепт предметной области).
 * 
 * @author Lomov P.A.
 *
 */
public abstract class SimpleCollectionCDP extends EnumeratedCDP
{
	
	static public List<IRI> getSimpleCollectionPatternIRIs()
	{
		List<IRI> patterns = new ArrayList<>();
		patterns.add(IRI.create(CollectionCDP.ONT_IRI));
		patterns.add(IRI.create(SetCDP.ONT_IRI));
		return patterns;
	}


	/**
	 * @param ontfile
	 */
	public SimpleCollectionCDP(File ontfile)
	{
		super(ontfile);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param ontologyIRI
	 * @param basedPatOnt
	 */
	public SimpleCollectionCDP(IRI ontologyIRI, Ontology basedPatOnt)
	{
		super(ontologyIRI, basedPatOnt);
		// TODO Auto-generated constructor stub
	}

}
