package ru.iimm.ontology.odp.pattgen.model.pattern.synpat.element;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * Ветка: leftC --property--> rightC
 * @author Lomov P. A.
 *
 */
public  class Branch
{
	private OWLClass leftConcept;
	
	private OWLClass rightConcept;
	
	private OWLObjectProperty owlObjectProperty;
	
	private Boolean isSomeRestriction;
	
	private Boolean isAllRestriction;
	
	private int arity;
	
	
	
	
	
	public Branch(OWLClass leftConcept, OWLClass rightConcept,
			OWLObjectProperty owlObjectProperty, Boolean isSomeRestriction,
			Boolean isAllRestriction)
	{
		super();
		this.leftConcept = leftConcept;
		this.rightConcept = rightConcept;
		this.owlObjectProperty = owlObjectProperty;
		this.isSomeRestriction = isSomeRestriction;
		this.isAllRestriction = isAllRestriction;
		this.arity = 0;
	}

	/***************************************
	 * geters/setters*************************
	 * ***************************************/

	public OWLClass getLeftConcept()
	{
		return leftConcept;
	}

	public OWLClass getRightConcept()
	{
		return rightConcept;
	}

	public OWLObjectProperty getOwlObjectProperty()
	{
		return owlObjectProperty;
	}

	public Boolean getIsSomeRestriction()
	{
		return isSomeRestriction;
	}

	public Boolean getIsAllRestriction()
	{
		return isAllRestriction;
	}

	public int getArity()
	{
		return arity;
	}

	public void setLeftConcept(OWLClass leftConcept)
	{
		this.leftConcept = leftConcept;
	}

	public void setRightConcept(OWLClass rightConcept)
	{
		this.rightConcept = rightConcept;
	}

	public void setOwlObjectProperty(OWLObjectProperty owlObjectProperty)
	{
		this.owlObjectProperty = owlObjectProperty;
	}

	public void setIsSomeRestriction(Boolean isSomeRestriction)
	{
		if (isSomeRestriction)
		{
			this.isAllRestriction=false;
		}
		this.isSomeRestriction = isSomeRestriction;
	}

	public void setIsAllRestriction(Boolean isAllRestriction)
	{
		if (isAllRestriction)
		{
			this.isSomeRestriction=false;
		}
		
		this.isAllRestriction = isAllRestriction;
	}

	public void setArity(int arity)
	{
		this.arity = arity;
	}
	
	

}
