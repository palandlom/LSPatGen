package ru.iimm.ontology.odp.pattgen.model.pattern.addoptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.iimm.ontology.odp.pattgen.model.pattern.ContentDesingPattern;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.SitbasedCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.AbstractSyntCDP;

/**
 * Включение всех концептов в добавляемую ситуацию
 * @author lomov
 *
 */
public class IncludeSitElementOpt extends AddOption
{
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(IncludeSitElementOpt.class);


	public IncludeSitElementOpt(AbstractSyntCDP syntCDP, SitbasedCDP sourceCDP)
	{
		super(syntCDP, sourceCDP);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void perform()
	{
		// TODO Auto-generated method stub

	}

}
