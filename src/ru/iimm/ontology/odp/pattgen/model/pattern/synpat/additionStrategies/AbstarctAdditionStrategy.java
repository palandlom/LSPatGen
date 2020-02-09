package ru.iimm.ontology.odp.pattgen.model.pattern.synpat.additionStrategies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.AbstractSyntCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.EnumeratedCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.PositionCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.SequenceCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.SinglePositionCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.SyntSitbasedCDP;

public abstract class AbstarctAdditionStrategy
{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstarctAdditionStrategy.class);

	/**
	 * Целевой паттерн для добавления.
	 */
	private AbstractSyntCDP targetPattern;

	/**
	 * Добавляемый паттерн.
	 */
	private AbstractSyntCDP addedPattern;

	AbstarctAdditionStrategy(AbstractSyntCDP targetPattern,
			AbstractSyntCDP addedPattern)
	{
		super();
		this.targetPattern = targetPattern;
		this.addedPattern = addedPattern;
	}

	/**
	 * Выполнение данной стратегии
	 */
	public abstract void exec();

	/**
	 * Используется для получения нужной стратегии для добавления одного
	 * паттерна в другой.
	 * 
	 * @param targetPattern
	 *            целевой паттерн
	 * @param addedPattern
	 *            паттерн для добавления
	 * @return
	 * @TODO упростить используя проверку наследования
	 *       SuperClass.class.isAssignableFrom(Subclass)
	 * 
	 */
	static public AbstarctAdditionStrategy getAdditionStrategy(
			AbstractSyntCDP targetPattern, AbstractSyntCDP addedPattern)
	{

		/* При добавлении ЛЮБЫХ паттернов в СИТУАЦИОННЫЕ = один билдер */
		if (SyntSitbasedCDP.class.isAssignableFrom(targetPattern.getClass()))
			return new All2SitbasedStrategy(targetPattern, addedPattern);

		/* При добавлении ENUMERATED паттернов в ПОЗИЦИОННЫЕ ... */
		else if (EnumeratedCDP.class.isAssignableFrom(addedPattern.getClass())
				&& PositionCDP.class.isAssignableFrom(targetPattern.getClass()))
			return new Enumerated2PositionStrategy(targetPattern, addedPattern);

		/* При добавлении SINGLE_PROPERTY паттернов в ПОЗИЦИОННЫЕ ... */
		else if (SinglePositionCDP.class
				.isAssignableFrom(addedPattern.getClass())
				&& PositionCDP.class.isAssignableFrom(targetPattern.getClass()))
			return new SingleProperty2PositionStrategy(targetPattern,
					addedPattern);

		/* При добавлении SEQUENCE паттерна в ПОЗИЦИОННЫЕ ... */
		else if (SequenceCDP.class.isAssignableFrom(addedPattern.getClass())
				&& PositionCDP.class.isAssignableFrom(targetPattern.getClass()))
			return new Sequence2PositionStrategy(targetPattern, addedPattern);

		/* При добавлении ПОЗИЦИОННЫХ паттернов в ПОЗИЦИОННЫЕ ... */
		else if (PositionCDP.class.isAssignableFrom(addedPattern.getClass())
				&& PositionCDP.class.isAssignableFrom(targetPattern.getClass()))
			return new Position2PositionStrategy(targetPattern, addedPattern);

		else
		{
			LOGGER.error(
					"Can't create (what constructor should be used for it) strategy for pattern pair:"
							+ "\n[" + addedPattern.getIRI().getFragment() + " to "
							+ targetPattern.getIRI().getFragment() + "]\n");

			return null;
		}

		/*
		 * else if (addPattIRI.equals(IRI.create(CoparticipationCDP.ONT_IRI)) &&
		 * PositionCDP.class.isAssignableFrom(
		 * prevBuilder.getSynthesizedPattern().getClass())) return new
		 * Position2PositionBuilder(prevBuilder, addPatt); else if
		 * (addPattIRI.equals(IRI.create(NaryParticipationCDP.ONT_IRI)) &&
		 * PositionCDP.class.isAssignableFrom(
		 * prevBuilder.getSynthesizedPattern().getClass())) return new
		 * Position2PositionBuilder(prevBuilder, addPatt); else if
		 * (addPattIRI.equals(IRI.create(ObjectRoleCDP.ONT_IRI)) &&
		 * PositionCDP.class.isAssignableFrom(
		 * prevBuilder.getSynthesizedPattern().getClass())) return new
		 * Position2PositionBuilder(prevBuilder, addPatt); else if
		 * (addPattIRI.equals(IRI.create(ParticipantRoleCDP2.ONT_IRI)) &&
		 * PositionCDP.class.isAssignableFrom(
		 * prevBuilder.getSynthesizedPattern().getClass())) return new
		 * Position2PositionBuilder(prevBuilder, addPatt); else if
		 * (addPattIRI.equals(IRI.create(ParticipationCDP.ONT_IRI)) &&
		 * PositionCDP.class.isAssignableFrom(
		 * prevBuilder.getSynthesizedPattern().getClass())) return new
		 * Position2PositionBuilder(prevBuilder, addPatt); else if
		 * (addPattIRI.equals(IRI.create(RegionCDP.ONT_IRI)) &&
		 * PositionCDP.class.isAssignableFrom(
		 * prevBuilder.getSynthesizedPattern().getClass())) return new
		 * Position2PositionBuilder(prevBuilder, addPatt); else if
		 * (addPattIRI.equals(IRI.create(SyntSitbasedCDP.ONT_IRI)) &&
		 * PositionCDP.class.isAssignableFrom(
		 * prevBuilder.getSynthesizedPattern().getClass())) return new
		 * Position2PositionBuilder(prevBuilder, addPatt); else if
		 * (addPattIRI.equals(IRI.create(TimeIntervalCDP.ONT_IRI)) &&
		 * PositionCDP.class.isAssignableFrom(
		 * prevBuilder.getSynthesizedPattern().getClass())) return new
		 * Position2PositionBuilder(prevBuilder, addPatt); else if
		 * (addPattIRI.equals(IRI.create(TypeOfEntitiesCDP.ONT_IRI)) &&
		 * PositionCDP.class.isAssignableFrom(
		 * prevBuilder.getSynthesizedPattern().getClass())) return new
		 * Position2PositionBuilder(prevBuilder, addPatt); -
		 */

	}

	/* ===================================================== */

	/**
	 * Возвращает целевой паттерн, приводя его к нужному типу
	 * 
	 * @return
	 */
	public AbstractSyntCDP getTargetPattern()
	{
		return targetPattern;
	}

	/**
	 * Возвращает добавляемый паттерн, приводя его к нужному типу
	 * 
	 * @return
	 */
	public AbstractSyntCDP getAddedPattern()
	{
		return addedPattern;
	}

}
