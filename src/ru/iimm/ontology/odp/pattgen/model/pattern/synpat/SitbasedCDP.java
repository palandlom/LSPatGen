package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import java.io.File;

import org.semanticweb.owlapi.model.IRI;

import ru.iimm.ontology.odp.pattgen.model.pattern.ContentDesingPattern;
import ru.iimm.ontology.ontAPI.Ontology;

/**
 * NaryParticipation ParticipantRole Situation
 * 
 * @author lomov
 *
 */
public abstract class SitbasedCDP extends ContentDesingPattern
{

	public SitbasedCDP(Ontology СDPOntology)
	{
		super(СDPOntology);
		// TODO Auto-generated constructor stub
	}

	public SitbasedCDP(File ontfile)
	{
		super(ontfile);
		// TODO Auto-generated constructor stub
	}

}
