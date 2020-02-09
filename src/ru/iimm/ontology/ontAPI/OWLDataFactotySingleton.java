package ru.iimm.ontology.ontAPI;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;

public class OWLDataFactotySingleton
{

	private static OWLDataFactory instance;

	public static synchronized OWLDataFactory getInstance()
	{
		if (instance == null)
		{
			instance = new OWLManager().getFactory();
		}
		return instance;
	}
}
