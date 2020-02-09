package ontExt.lsplGenerator.ontology;

import org.semanticweb.owlapi.model.OWLClass;

public class OneClassResult extends ParsingResult
{
    private OWLClass owlClass;

    public OneClassResult(OWLClass owlClass)
    {
	super();
	this.owlClass = owlClass;
    }

    public OWLClass getOwlClass()
    {
        return owlClass;
    }

    public void setOwlClass(OWLClass owlClass)
    {
        this.owlClass = owlClass;
    }
    
    

}
