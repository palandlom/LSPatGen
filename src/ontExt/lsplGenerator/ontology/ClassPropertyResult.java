package ontExt.lsplGenerator.ontology;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class ClassPropertyResult extends ParsingResult
{
    private OWLClass owlClass;
    
    private OWLObjectProperty owlObjectProperty;

    public ClassPropertyResult(OWLClass owlClass, OWLObjectProperty owlObjectProperty)
    {
	super();
	this.owlClass = owlClass;
	this.owlObjectProperty = owlObjectProperty;
    }

    public OWLClass getOwlClass()
    {
        return owlClass;
    }

    public void setOwlClass(OWLClass owlClass)
    {
        this.owlClass = owlClass;
    }

    public OWLObjectProperty getOwlObjectProperty()
    {
        return owlObjectProperty;
    }

    public void setOwlObjectProperty(OWLObjectProperty owlObjectProperty)
    {
        this.owlObjectProperty = owlObjectProperty;
    }
    
    

}
