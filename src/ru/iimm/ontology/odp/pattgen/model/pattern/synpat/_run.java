package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import java.net.URI;

import org.semanticweb.owlapi.model.IRI;

public class _run
{

	public static void main(String[] args)
	{
		/* Создаем пустой паттерн... */
		SyntSitbasedCDP mySitPat = new SyntSitbasedCDP(IRI.create("http://ont/myPattern.owl"));
		
		/* Создаем пустой паттерн... */
//		SyntSitbasedCDP myObjectRole = new ObjectRoleCDP(IRI.create("http://ont/myObjectRole.owl"));
		RegionCDP regionCDP = new RegionCDP(IRI.create("http://ont/myRegion.owl"));

	

	//	mySitPat.add(objectRole);
//		mySitPat.add(seq);
	//	mySitPat.add(toe);
	//	mySitPat.add(region);
	//	mySitPat.add(timeInt);
		mySitPat.save(URI.create("file:/D:/temp/_patternSynth/"));
		
		regionCDP.save(URI.create("file:/D:/temp/_patternSynth/"));
		//partOf.save(URI.create("file:/D:/temp/_patternSynth/"));
		
		/* */
		


	}

}
