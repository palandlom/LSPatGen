package ru.iimm.ontology.odp.pattgen.model.pattern.synpat.additionStrategies;

import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.AbstractSyntCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.EnumeratedCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.PositionCDP;

public class Enumerated2PositionStrategy extends All2SitbasedStrategy
{

	
	public Enumerated2PositionStrategy(AbstractSyntCDP targetPattern,
			AbstractSyntCDP addedPattern)
	{
		super(targetPattern, addedPattern);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void exec()
	{
		this.getTargetPattern().add(getAddedPattern());
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public EnumeratedCDP getAddedPattern()
	{
		// TODO Auto-generated method stub
		 return (EnumeratedCDP) super.getAddedPattern();
	}
	
	

}
