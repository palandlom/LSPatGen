package ru.iimm.ontology.odp.pattgen.model.pattern.synpat.additionStrategies;

import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.AbstractSyntCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.SyntSitbasedCDP;

public  class All2SitbasedStrategy extends AbstarctAdditionStrategy
{

	public All2SitbasedStrategy(AbstractSyntCDP targetPattern,
			AbstractSyntCDP addedPattern)
	{
		super(targetPattern, addedPattern);
		// TODO Auto-generated constructor stub
	}
	
	

	@Override
	public SyntSitbasedCDP getTargetPattern()
	{
		// TODO Auto-generated method stub
		return (SyntSitbasedCDP) super.getTargetPattern();
	}



	@Override
	public void exec()
	{
		// TODO Auto-generated method stub
		
	}

}
