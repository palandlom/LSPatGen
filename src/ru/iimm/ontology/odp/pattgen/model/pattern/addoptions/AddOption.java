package ru.iimm.ontology.odp.pattgen.model.pattern.addoptions;

import java.util.ArrayList;

import org.semanticweb.owlapi.model.OWLDataFactory;

import ru.iimm.ontology.odp.pattgen.model.pattern.ContentDesingPattern;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.AbstractSyntCDP;

/**
 * Вариант добавления CDP в синтезируемый. Для каждой группы
 * 
 * @author lomov
 *
 */
public abstract class AddOption
{
	private AbstractSyntCDP syntCDP;

	private ContentDesingPattern sourceCDP;
	
	private OWLDataFactory df;

	/**
	 * Дейстия, выполняемые в данном варианте добавления.
	 */
	// ArrayList<AddAction> AddActons;

	public AddOption(AbstractSyntCDP syntCDP, ContentDesingPattern sourceCDP)
	{
		super();
		this.syntCDP = syntCDP;
		this.sourceCDP = sourceCDP;
		this.df = sourceCDP.getOWLDataFactory();
	}

	/**
	 * Выполняет данный способ добавления
	 */
	public abstract void perform();

	/*
	 * ===========================================================
	 * ===========================================================
	 */

	public AbstractSyntCDP getSyntCDP()
	{
		return syntCDP;
	}

	public void setSyntCDP(AbstractSyntCDP syntCDP)
	{
		this.syntCDP = syntCDP;
	}

	public ContentDesingPattern getSourceCDP()
	{
		return sourceCDP;
	}

	public void setSourceCDP(ContentDesingPattern sourceCDP)
	{
		this.sourceCDP = sourceCDP;
	}

	public OWLDataFactory getDf()
	{
		return df;
	}

}
