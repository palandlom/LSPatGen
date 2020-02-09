package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import ru.iimm.ontology.odp.pattgen.model.pattern.ContentDesingPattern;
import ru.iimm.ontology.ontAPI.Ontology;

public class TimeIntervalCDP extends SinglePositionCDP
{
	/* Common const */
	public final static String ONT_IRI =
	 "http://www.ontologydesignpatterns.org/cp/owl/timeinterval.owl";
	 static String BASE_IRI = ONT_IRI + "#";
	 public final static String FILENAME = "timeinterval.owl";

	/* Class names */
	// static String HAS_REGION_IRI = Const.IRI_CDP_CATALOG + RegionCDP.FILENAME
	// + "#hasRegion";

	static String TIME_INTEVAL_IRI = Const.IRI_CDP_CATALOG
			+ TimeIntervalCDP.FILENAME + "#TimeInterval";

	/* Properties names */
	static String HAS_INTERVAL_DATA_IRI = Const.IRI_CDP_CATALOG
			+ TimeIntervalCDP.FILENAME + "#hasIntervalData";
	static String HAS_INTERVAL_END_DATA_IRI = Const.IRI_CDP_CATALOG
			+ TimeIntervalCDP.FILENAME + "#hasIntervalEndData";
	static String HAS_INTERVAL_START_DATA_IRI = Const.IRI_CDP_CATALOG
			+ TimeIntervalCDP.FILENAME + "#hasIntervalStartData";

	static String HAS_TIME_INTERVAL = Const.IRI_CDP_CATALOG
			+ TimeIntervalCDP.FILENAME + "#hasTimeInterval";
	static String IS_TIME_INTERVAL_OF = Const.IRI_CDP_CATALOG
			+ TimeIntervalCDP.FILENAME + "#isTimeIntervalOf";

	private OWLClass interval;

	private OWLDataProperty hasIntervalData;
	private OWLDataProperty hasIntervalEndData;
	private OWLDataProperty hasIntervalStartData;

	private OWLObjectProperty hasTimeInterval;
	private OWLObjectProperty isTimeIntervalOf;

	public TimeIntervalCDP()
	{
		super(ContentDesingPattern.getCDPFile(TimeIntervalCDP.FILENAME),
				IRI.create(TIME_INTEVAL_IRI));
		init();
	}

	public TimeIntervalCDP(IRI ontologyIRI)
	{
		super(ontologyIRI,
				new Ontology(ContentDesingPattern
						.getCDPFile(TimeIntervalCDP.FILENAME), true, true),
				IRI.create(TIME_INTEVAL_IRI));
	}

	@Override
	protected void init()
	{
		super.init();

		/* ... определяем спец поля класса */
		this.interval = df.getOWLClass(IRI.create(TIME_INTEVAL_IRI));

		this.hasIntervalData = df
				.getOWLDataProperty(IRI.create(HAS_INTERVAL_DATA_IRI));
		this.hasIntervalEndData = df
				.getOWLDataProperty(IRI.create(HAS_INTERVAL_END_DATA_IRI));
		this.hasIntervalStartData = df
				.getOWLDataProperty(IRI.create(HAS_INTERVAL_START_DATA_IRI));

		this.initHasIntervalProperty();

		/* ... определяем общие поля */
		this.objProperties.add(this.hasTimeInterval);
		this.objProperties.add(this.isTimeIntervalOf);

	}

	/**
	 * Генерит пару свойств принадлежности интервала - [hasTimeInterval] и
	 * Обратное.
	 */
	private void initHasIntervalProperty()
	{

		this.hasTimeInterval = df.getOWLObjectProperty(
				IRI.create(HAS_TIME_INTERVAL));
		this.isTimeIntervalOf = df.getOWLObjectProperty(
				IRI.create(IS_TIME_INTERVAL_OF));

		Set<OWLAxiom> axSet = new HashSet<>();
		axSet.add(df.getOWLObjectPropertyDomainAxiom(hasTimeInterval,
				df.getOWLThing()));
		axSet.add(df.getOWLObjectPropertyDomainAxiom(isTimeIntervalOf,
				this.getInterval()));
		axSet.add(df.getOWLObjectPropertyRangeAxiom(hasTimeInterval,
				this.getInterval()));
		axSet.add(df.getOWLObjectPropertyRangeAxiom(isTimeIntervalOf,
				df.getOWLThing()));
		axSet.add(df.getOWLInverseObjectPropertiesAxiom(hasTimeInterval,
				isTimeIntervalOf));

		this.getAxioms().addAll(axSet);
		this.getCDPOntology().mng.addAxioms(this.getCDPOntology().ontInMem,
				axSet);

	}


	/**
	 * @return the {@linkplain #hasIntervalData}
	 */
	public OWLDataProperty getHasIntervalData()
	{
		return hasIntervalData;
	}

	/**
	 * @param hasIntervalData
	 *            the {@linkplain #hasIntervalData} to set
	 */
	public void setHasIntervalData(OWLDataProperty hasIntervalData)
	{
		this.hasIntervalData = hasIntervalData;
	}

	/**
	 * @return the {@linkplain #hasIntervalEndData}
	 */
	public OWLDataProperty getHasIntervalEndData()
	{
		return hasIntervalEndData;
	}

	/**
	 * @param hasIntervalEndData
	 *            the {@linkplain #hasIntervalEndData} to set
	 */
	public void setHasIntervalEndData(OWLDataProperty hasIntervalEndData)
	{
		this.hasIntervalEndData = hasIntervalEndData;
	}

	/**
	 * @return the {@linkplain #hasIntervalStartData}
	 */
	public OWLDataProperty getHasIntervalStartData()
	{
		return hasIntervalStartData;
	}

	/**
	 * @param hasIntervalStartData
	 *            the {@linkplain #hasIntervalStartData} to set
	 */
	public void setHasIntervalStartData(OWLDataProperty hasIntervalStartData)
	{
		this.hasIntervalStartData = hasIntervalStartData;
	}

	public OWLClass getInterval()
	{
		return interval;
	}

	public void setInterval(OWLClass interval)
	{
		this.interval = interval;
	}

	public OWLObjectProperty getHasTimeInterval()
	{
		return hasTimeInterval;
	}

	public void setHasTimeInterval(OWLObjectProperty hasTimeInterval)
	{
		this.hasTimeInterval = hasTimeInterval;
	}

	public OWLObjectProperty getIsTimeIntervalOf()
	{
		return isTimeIntervalOf;
	}

	public void setIsTimeIntervalOf(OWLObjectProperty isTimeIntervalOf)
	{
		this.isTimeIntervalOf = isTimeIntervalOf;
	}
}
