package ontExt.lsplGenerator.ontology;

import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.SWRLRule;

public class AxiomParserVisitor implements OWLAxiomVisitor
{
    private OWLClass leftClass;
    private OWLClass rightClass;
    private OWLObjectProperty property;
    private Boolean isSuccesful = false;
    private OWLAxiom initialAxiom;

    
    
    /**
     * Обработка результатов, добытых дочерним посетителем.
     * @param leftResult
     * @param rightResult
     * @return
     */
    private Boolean handleResult(ParsingResult leftResult, ParsingResult rightResult)
    {
	if ((leftResult != null) && (rightResult != null) && (rightResult instanceof ClassPropertyResult)
		&& (leftResult instanceof OneClassResult))
	{
	    OneClassResult leftExp = (OneClassResult) leftResult;
	    ClassPropertyResult rightExp = (ClassPropertyResult) rightResult;
	    this.setLeftClass(leftExp.getOwlClass());
	    this.setRightClass(rightExp.getOwlClass());
	    this.setProperty(rightExp.getOwlObjectProperty());
	    return true;
	}
	return false;
    }
    
    /**
     * Очищает результаты в данном посетителе.
     */
    public void reset()
    {
	this.setLeftClass(null);
	this.setRightClass(null);
	this.setProperty(null);
	this.setInitialAxiom(null);
	this.isSuccesful=false;
    }

    @Override
    public void visit(OWLAnnotationAssertionAxiom arg0)
    {
	this.setInitialAxiom(arg0);
	

    }

    @Override
    public void visit(OWLSubAnnotationPropertyOfAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLAnnotationPropertyDomainAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLAnnotationPropertyRangeAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLDeclarationAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLSubClassOfAxiom axiom)
    {
	this.setInitialAxiom(axiom);
	OWLClassExpression leftSide = axiom.getSubClass(); 
	OWLClassExpression rightSide = axiom.getSuperClass();
	PartParserVisitor leftPartVis = new PartParserVisitor();
	PartParserVisitor rightPartVis = new PartParserVisitor();
	
	leftSide.accept(leftPartVis);
	rightSide.accept(rightPartVis);
	
	this.isSuccesful = this.handleResult(leftPartVis.getResult(), rightPartVis.getResult());
    }

    @Override
    public void visit(OWLNegativeObjectPropertyAssertionAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLAsymmetricObjectPropertyAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLReflexiveObjectPropertyAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLDisjointClassesAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLDataPropertyDomainAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLObjectPropertyDomainAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLEquivalentObjectPropertiesAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLNegativeDataPropertyAssertionAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLDifferentIndividualsAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLDisjointDataPropertiesAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLDisjointObjectPropertiesAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLObjectPropertyRangeAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLObjectPropertyAssertionAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLFunctionalObjectPropertyAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLSubObjectPropertyOfAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLDisjointUnionAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLSymmetricObjectPropertyAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLDataPropertyRangeAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLFunctionalDataPropertyAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLEquivalentDataPropertiesAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLClassAssertionAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLEquivalentClassesAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLDataPropertyAssertionAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLTransitiveObjectPropertyAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLIrreflexiveObjectPropertyAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLSubDataPropertyOfAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLInverseFunctionalObjectPropertyAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLSameIndividualAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLSubPropertyChainOfAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLInverseObjectPropertiesAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLHasKeyAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(OWLDatatypeDefinitionAxiom arg0)
    {
	this.setInitialAxiom(arg0);

    }

    @Override
    public void visit(SWRLRule arg0)
    {
	this.setInitialAxiom(arg0);

    }

    public OWLClass getLeftClass()
    {
	return leftClass;
    }

    public void setLeftClass(OWLClass leftClass)
    {
	this.leftClass = leftClass;
    }

    public OWLClass getRightClass()
    {
	return rightClass;
    }

    public void setRightClass(OWLClass rightClass)
    {
	this.rightClass = rightClass;
    }

    public OWLObjectProperty getProperty()
    {
	return property;
    }

    public void setProperty(OWLObjectProperty property)
    {
	this.property = property;
    }

    public Boolean getIsSuccesful()
    {
        return isSuccesful;
    }

    public OWLAxiom getInitialAxiom()
    {
        return initialAxiom;
    }

    public void setInitialAxiom(OWLAxiom initialAxiom)
    {
        this.initialAxiom = initialAxiom;
    }

}
