package ru.iimm.ontology.odp.pattgen.model.pattern.synpat.additionStrategies;

import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.AbstractSyntCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.SequenceCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.SyntSitbasedCDP;

public class SingleProperty2PositionStrategy
		extends All2SitbasedStrategy
{

	protected SyntSitbasedCDP addedPattern;




	public SingleProperty2PositionStrategy(AbstractSyntCDP targetPattern,
			AbstractSyntCDP addedPattern)
	{
		super(targetPattern, addedPattern);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void exec()
	{
		this.getTargetPattern().add(getAddedPattern());

	}

	@Override
	public SequenceCDP getAddedPattern()
	{
		// TODO Auto-generated method stub
		return (SequenceCDP) super.getAddedPattern();
	}

}
