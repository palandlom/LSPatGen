package ontExt.lsplGenerator.ontology;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;

/**
 * Парсит левую и правую часть аксиомы.
 * @author lomov
 *
 */
public class PartParserVisitor implements OWLClassExpressionVisitor
{
    
    private ParsingResult result=null;
    
    

    @Override
    public void visit(OWLClass exp)
    {
	this.setResult(new OneClassResult(exp));
	
    }

    @Override
    public void visit(OWLObjectIntersectionOf arg0)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void visit(OWLObjectUnionOf arg0)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void visit(OWLObjectComplementOf arg0)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void visit(OWLObjectSomeValuesFrom exp)
    {
	OWLClassExpression valueExp = exp.getFiller();
	
	/*Если класс анонимный, то ничего не делаем..*/
	if (valueExp.isAnonymous())
	{
	    //-PartParserVisitor partVisitor = new PartParserVisitor();
	    //-valueExp.accept(partVisitor);
	    
	}
	/*... если нет - формируем результат*/
	else {
	    OWLClass owlClass = valueExp.asOWLClass();
	    OWLObjectProperty objProperty = exp.getProperty().getNamedProperty();
	    this.setResult(new ClassPropertyResult(owlClass, objProperty ));
	}

	
    }

    @Override
    public void visit(OWLObjectAllValuesFrom arg0)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void visit(OWLObjectHasValue arg0)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void visit(OWLObjectMinCardinality arg0)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void visit(OWLObjectExactCardinality arg0)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void visit(OWLObjectMaxCardinality arg0)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void visit(OWLObjectHasSelf arg0)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void visit(OWLObjectOneOf arg0)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void visit(OWLDataSomeValuesFrom arg0)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void visit(OWLDataAllValuesFrom arg0)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void visit(OWLDataHasValue arg0)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void visit(OWLDataMinCardinality arg0)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void visit(OWLDataExactCardinality arg0)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void visit(OWLDataMaxCardinality arg0)
    {
	// TODO Auto-generated method stub
	
    }

    public ParsingResult getResult()
    {
        return result;
    }

    public void setResult(ParsingResult result)
    {
        this.result = result;
    }

}
