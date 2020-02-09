package ru.iimm.ontology.ontAPI;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.AutoIRIMapper;

import ru.iimm.ontology.odp.pattgen.model.pattern.ContentDesingPattern;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.CollectionCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.SyntSitbasedCDP;

public class rrr
{

	public rrr()
	{
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args)
	{
		File file = new File("path to ontology file");
		
		//file = new File("D://Lomov//_Eclipse//common_workspace//PattGen.app//res//cdpOnt//objectrole.owl");
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		
		/*  Create AutoIRIMapper to dir: " + file.getParent()); */
		AutoIRIMapper mapperThs = new AutoIRIMapper(new File(file.getParent()),
				true);
		manager.addIRIMapper(mapperThs);
		
		System.out.println(file.getParent());

		/* Load ontology with its imports ... */
		OWLOntology ont = null;
		try
		{
			ont = manager.loadOntologyFromOntologyDocument(file);
		} catch (OWLException e)
		{
			System.out.println("!!! The ontology" + file.getAbsolutePath()
					+ " could not be loaded/created: \n" + "!!! "
					+ e.getMessage());
		}
		
			
		System.out.println("Ontology: "+ont.getOntologyID().getOntologyIRI());
		
		
		
	}

}
