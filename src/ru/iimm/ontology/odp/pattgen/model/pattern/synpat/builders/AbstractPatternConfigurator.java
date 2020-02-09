package ru.iimm.ontology.odp.pattgen.model.pattern.synpat.builders;

import java.io.File;
import java.util.List;

import org.semanticweb.owlapi.metrics.LogicalAxiomCount;
import org.semanticweb.owlapi.model.IRI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.iimm.ontology.odp.pattgen.model.pattern.TypeOfEntitiesCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.AbstractSyntCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.BagCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.CollectionCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.ComponencyCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.CoparticipationCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.EnumeratedCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.ListCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.NaryParticipationCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.ObjectRoleCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.PartOfCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.ParticipantRoleCDP2;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.ParticipationCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.PositionCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.RegionCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.SequenceCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.SetCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.SinglePositionCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.SyntSitbasedCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.TimeIntervalCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.additionStrategies.AbstarctAdditionStrategy;
import ru.iimm.ontology.ontAPI.Ontology;

public abstract class AbstractPatternConfigurator
{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractPatternConfigurator.class);

	/**
	 * {@link IRI} результирующей онтологии синтезируемого паттерна
	 */
	// private IRI newPatternIRI;

	/**
	 * Онтология начального паттерна-основы для синтезируемого
	 */
	// private Ontology basedPatternOnt;

	/**
	 * Файл с онтологией-паттерна, который будет использоваться как компонент
	 * при синтезе.
	 */
	// private File patternOntfile;

	/**
	 * Стратегия добавления текущего паттерна конфигуратора в паттерн
	 * предшествующего.
	 */
	private AbstarctAdditionStrategy additionStrategy;

	/**
	 * Предыдущий конфигуратор, производящий паттерн, который вставляется паттерн
	 * данного конфигуратора
	 */
	private AbstractPatternConfigurator prevConfigurator;
	
	/**
	 * Настраиваемый паттерн.
	 */
	AbstractSyntCDP synthesizedPattern;


	
	public AbstractPatternConfigurator(
			AbstractPatternConfigurator prevConfigurator,
			AbstractSyntCDP synthesizedPattern)
	{
		super();
		this.prevConfigurator = prevConfigurator;
		this.synthesizedPattern = synthesizedPattern;
	}

	public AbstractPatternConfigurator(
			AbstarctAdditionStrategy additionStrategy,
			AbstractPatternConfigurator prevConfigurator,
			AbstractSyntCDP synthesizedPattern)
	{
		super();
		this.additionStrategy = additionStrategy;
		this.prevConfigurator = prevConfigurator;
		this.synthesizedPattern = synthesizedPattern;
	}

	/**
	 * Используется для получения нужного билдера по IRI паттерна.
	 * 
	 * @TODO СДЕЛАНО - упростить используя проверку наследования
	 *       SuperClass.class.isAssignableFrom(Subclass)
	 * @param pattern
	 * @param prevBuilder
	 * @return
	 */
	static public AbstractPatternConfigurator getPatternConfigurator(
			AbstractSyntCDP pattern, AbstractPatternConfigurator prevBuilder)
	{
		/* Создаем добавляемый паттерн... */
		if (SyntSitbasedCDP.class.isAssignableFrom(pattern.getClass()))
			return new SitbasedPatternConfigurator(prevBuilder, pattern);
		else if (EnumeratedCDP.class.isAssignableFrom(pattern.getClass()))
			return new EnumeratedPatternConfigurator(prevBuilder, pattern);
		else if (PositionCDP.class.isAssignableFrom(pattern.getClass()))
			return new PositionPatternConfigurator(prevBuilder, pattern);
		else if (SequenceCDP.class.isAssignableFrom(pattern.getClass()))
			return new SequencePatternConfigurator(prevBuilder, pattern);
		else if (SinglePositionCDP.class.isAssignableFrom(pattern.getClass()))
			return new SinglePosPatternConfigurator(prevBuilder, pattern);
		else
		{
			LOGGER.error(
					"Can't create (what is constructor should be used for it?) configurator for pattern:"
							+ "\n[" + pattern.getIRI().getFragment());
			return null;
		}
	}

	/*
	 * ==========================================================
	 * gettersAndSetters --------------------------------
	 * =======================================================
	 */

	/**
	 * Возвращает {@linkplain ObservableList} паттернов, которые можно добавить
	 * в переданный.
	 * 
	 * @param pattern
	 * @return
	 */
	public ObservableList<IRI> getObservableAddCDPList(AbstractSyntCDP pattern)
	{
		if (pattern != null)
		{
			LOGGER.info("Get suitable patters for {}...", pattern.toString());
			ObservableList<IRI> patternsIRI = FXCollections
					.observableArrayList(pattern.getSuitablePattern());
			LOGGER.info("... we get {} patterns", patternsIRI.size());
			return patternsIRI;
		} else
		{
			LOGGER.info(
					"!!! Can't get suitable pattern list - pattern is null ");
			return null;
		}
	}

	abstract public ObservableList<IRI> getObservableAddCDPList();

	public AbstarctAdditionStrategy getAdditionStrategy()
	{
		return additionStrategy;
	}

	public AbstractPatternConfigurator getPrevBuilder()
	{
		return prevConfigurator;
	}

	public abstract AbstractSyntCDP getSynthesizedPattern();

	public void setAdditionStrategy(AbstarctAdditionStrategy additionStrategy)
	{
		this.additionStrategy = additionStrategy;
	}

	public void setPrevBuilder(AbstractPatternConfigurator prevBuilder)
	{
		this.prevConfigurator = prevBuilder;
	}

	public abstract void setSynthesizedPattern(
			AbstractSyntCDP synthesizedPattern);

}
