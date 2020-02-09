package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import java.net.URI;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.util.OWLEntityRenamer;

public class _run2
{

	public _run2()
	{
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args)
	{
		
		//ObjectRoleCDP myObjRole = new ObjectRoleCDP(IRI.create("http://ont/myObjectRole.owl"));
		ObjectRoleCDP myObjRole = new ObjectRoleCDP();
		
		ParticipationCDP participation = new ParticipationCDP();
		CoparticipationCDP copart = new CoparticipationCDP();
		ListCDP list = new ListCDP();
		
//		ListCDP list = new ListCDP(IRI.create("http://ont/myList.owl"));
		//SyntSitbasedCDP mySitPat = new SyntSitbasedCDP(IRI.create("http://ont/mySit.owl"));
		SyntSitbasedCDP mySitPat = new NaryParticipationCDP(IRI.create("http://ont/mySit.owl"));
		
		myObjRole.add(list, myObjRole.getObject());
		
		mySitPat.add(myObjRole);
		
	//	myObjRole.add(participation);
	//	myObjRole.add(copart);
	//	myObjRole.save(URI.create("file:/D:/temp/_patternSynth/"));
	//	list.save(URI.create("file:/D:/temp/_patternSynth/"));
		
		mySitPat.save(URI.create("file:/D:/temp/_patternSynth/"));
		//OWLEntityRenamer rename = new OWLEntityRenamer(owlOntologyManager, ontologies)
		//myObjRole.getCDPOntology().df.
		
/*
		ParticipationCDP myPartic = new ParticipationCDP(IRI.create("http://ont/myParticip.owl"));
		CoparticipationCDP copart = new CoparticipationCDP();
		myPartic.add(copart);
		myPartic.save(URI.create("file:/D:/temp/_patternSynth/"));
	*/	

	}

}
