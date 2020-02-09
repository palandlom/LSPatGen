package ontExt.lsplGenerator.ontElements;

import org.semanticweb.owlapi.model.OWLAxiom;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;

/**
 * Ребро, по которому генериться патерн.
 * 
 * @author lomov
 *
 */
public class BranchImpl implements Branch
{
	private Concept LConcept;
	private Concept RConcept;
	private Relation relation;
	
	private OWLAxiom initialAxiom;
	
	

	
	public BranchImpl(Concept lConcept, Relation relation, Concept rConcept, OWLAxiom initialAxiom)
	{
	    super();
	    LConcept = lConcept;
	    RConcept = rConcept;
	    this.relation = relation;
	    this.initialAxiom = initialAxiom;
	}


	@Override
	public String toString()
	{
		return "[" + this.getLConcept().getLabel() + " ==" + this.getRelation().getLabel()
				+ "=> " + this.getRConcept().getLabel() + "]";
	}

	/* (non-Javadoc)
	 * @see ontExt.lsplGenerator.ontElements.BranchI#getLConcept()
	 */
	@Override
	public Concept getLConcept()
	{
		return LConcept;
	}


	public void setLConcept(Concept lConcept)
	{
		LConcept = lConcept;
	}


	public Concept getRConcept()
	{
		return RConcept;
	}


	public void setRConcept(Concept rConcept)
	{
		RConcept = rConcept;
	}


	public Relation getRelation()
	{
		return relation;
	}

	public void setRelation(Relation relation)
	{
		this.relation = relation;
	}




	public OWLAxiom getInitialAxiom()
	{
	    return initialAxiom;
	}

	
	public void setInitialAxiom(OWLAxiom initialAxiom)
	{
	    this.initialAxiom = initialAxiom;
	}

	@Override
	public int hashCode()
	{
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((initialAxiom == null) ? 0 : initialAxiom.hashCode());
	    return result;
	}

	@Override
	public boolean equals(Object obj)
	{
	    if (this == obj)
		return true;
	    if (obj == null)
		return false;
	    if (getClass() != obj.getClass())
		return false;
	    BranchImpl other = (BranchImpl) obj;
	    if (initialAxiom == null)
	    {
		if (other.initialAxiom != null)
		    return false;
	    } else if (!initialAxiom.equals(other.initialAxiom))
		return false;
	    return true;
	}

}
