package ru.iimm.ontology.odp.pattgen.model.pattern.synpat.builders;

import java.io.File;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.collections.ObservableList;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.AbstractSyntCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.SyntSitbasedCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.additionStrategies.AbstarctAdditionStrategy;

public class SitbasedPatternBuilder extends AbstractPatternBulider
{

	public SitbasedPatternBuilder(
			AbstractPatternBulider prevBuilder,
			AbstractSyntCDP synthesizedPattern)
	{
		super( prevBuilder, synthesizedPattern);
		// TODO Auto-generated constructor stub
	}

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SitbasedPatternBuilder.class);

	@Override
	public ObservableList<IRI> getObservableAddCDPList()
	{
		return super.getObservableAddCDPList(this.synthesizedPattern);		
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
