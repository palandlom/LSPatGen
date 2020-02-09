package ontExt.lsplGenerator.ontElements;

import org.semanticweb.owlapi.model.OWLAxiom;

public interface Branch
{

    Concept getLConcept();

  
    Concept getRConcept();

    Relation getRelation();


    OWLAxiom getInitialAxiom();

}