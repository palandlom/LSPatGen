package ontExt.view.obsElements;

import javafx.beans.value.ObservableBooleanValue;
import ontExt.lsplGenerator.ontElements.Branch;

public interface ObservableBranch extends Branch
{
    
    ObservableBooleanValue getUpdateStatusProperty();
    
    Boolean getUpdateStatus();
    void markAsUpdated();

}