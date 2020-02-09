package ontExt.view.obsElements;

import java.util.Observable;

import org.semanticweb.owlapi.model.OWLAxiom;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import ontExt.lsplGenerator.ontElements.Branch;
import ontExt.lsplGenerator.ontElements.Concept;
import ontExt.lsplGenerator.ontElements.Relation;

public class ObservableBranchImpl implements ObservableBranch
{
    private Branch branch;

    private SimpleBooleanProperty isUpdated;

    public ObservableBranchImpl(Branch branch)
    {
	super();
	this.branch = branch;
	
	this.isUpdated = new SimpleBooleanProperty(false);
    }

    @Override
    public Concept getLConcept()
    {
	// TODO Auto-generated method stub
	return this.branch.getLConcept();
    }

    @Override
    public Concept getRConcept()
    {
	// TODO Auto-generated method stub
	return this.branch.getRConcept();
    }

    @Override
    public Relation getRelation()
    {
	// TODO Auto-generated method stub
	return this.branch.getRelation();
    }

    @Override
    public OWLAxiom getInitialAxiom()
    {
	return this.branch.getInitialAxiom();
    }

   

    @Override
    public void markAsUpdated()
    {
	this.setIsUpdated(true);

    }

    /* =============================================== */
    public Branch getBranch()
    {
	return branch;
    }

    public void setBranch(Branch branch)
    {
	this.branch = branch;
    }


    @Override
    public String toString()
    {
	String res = this.isUpdated.getValue() ? "" : "!";
	return res + this.getBranch().toString();
    }

    public final SimpleBooleanProperty isUpdatedProperty()
    {
        return this.isUpdated;
    }
    

    public final boolean isIsUpdated()
    {
        return this.isUpdatedProperty().get();
    }
    

    public final void setIsUpdated(final boolean isUpdated)
    {
        this.isUpdatedProperty().set(isUpdated);
    }

    @Override
    public Boolean getUpdateStatus()
    {
	
	return this.isUpdated.getValue();
    }

    @Override
    public ObservableBooleanValue getUpdateStatusProperty()
    {
	
	return this.isUpdated;
    }
    

}
