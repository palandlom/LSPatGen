package ru.iimm.ontology.odp.pattgen.model.pattern.synpat.builders;

import org.semanticweb.owlapi.model.IRI;

import javafx.collections.ObservableList;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.AbstractSyntCDP;

public class EnumeratedPatternConfigurator extends AbstractPatternConfigurator
{

	public EnumeratedPatternConfigurator(
			AbstractPatternConfigurator prevBuilder,
			AbstractSyntCDP synthesizedPattern)
	{
		super(prevBuilder, synthesizedPattern);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ObservableList<IRI> getObservableAddCDPList()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractSyntCDP getSynthesizedPattern()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSynthesizedPattern(AbstractSyntCDP synthesizedPattern)
	{
		// TODO Auto-generated method stub
		
	}

}
