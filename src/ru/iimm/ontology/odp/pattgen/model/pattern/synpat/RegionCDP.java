package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import java.io.File;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import ru.iimm.ontology.odp.pattgen.model.pattern.ContentDesingPattern;
import ru.iimm.ontology.ontAPI.Ontology;

public class RegionCDP extends SinglePositionCDP
{
	/* Common const */
	public final static String ONT_IRI = "http://www.ontologydesignpatterns.org/cp/owl/region.owl";
	static String BASE_IRI = ONT_IRI + "#";
	public final static String FILENAME = "region.owl";

	/* Class names */
	static String REGION_IRI = Const.IRI_CDP_CATALOG + RegionCDP.FILENAME
			+ "#Region";

	/* Properties names */
	static String HAS_REGION_IRI = Const.IRI_CDP_CATALOG + RegionCDP.FILENAME
			+ "#hasRegion";
	static String IS_REGION_FOR_IRI = Const.IRI_CDP_CATALOG + RegionCDP.FILENAME
			+ "#isRegionFor";
	static String IS_REGION_DATA_VALUE_IRI = Const.IRI_CDP_CATALOG
			+ RegionCDP.FILENAME + "#hasRegionDataValue";

	private OWLClass region;
	private OWLObjectProperty hasRegion;
	private OWLObjectProperty isRegionFor;
	private OWLDataProperty hasRegionDataValue;

	/**
	 * Создает паттерн не меняя {@link IRI} исходной онтологии. Применяется для
	 * последующего использования как компонент при синтезе
	 */
	public RegionCDP()
	{
		super(ContentDesingPattern.getCDPFile(RegionCDP.FILENAME),
				IRI.create(REGION_IRI));
		init();
	}

	/**
	 * Создает паттерн с новым {@link IRI}, который используется как основа для
	 * последующего синтеза
	 * 
	 * @param ontologyIRI
	 *            новый {@link IRI} создаваемого паттерна
	 */
	public RegionCDP(IRI ontologyIRI)
	{
		super(ontologyIRI,
				new Ontology(
						ContentDesingPattern.getCDPFile(RegionCDP.FILENAME),
						true, true),
				IRI.create(REGION_IRI));
		init();
	}

	@Override
	protected void init()
	{
		super.init();
		/* ... определяем спец поля класса */
		this.region = df.getOWLClass(IRI.create(REGION_IRI));

		this.hasRegion = df.getOWLObjectProperty(IRI.create(HAS_REGION_IRI));
		this.isRegionFor = df
				.getOWLObjectProperty(IRI.create(IS_REGION_FOR_IRI));

		this.hasRegionDataValue = df
				.getOWLDataProperty(IRI.create(IS_REGION_DATA_VALUE_IRI));

		/* ... определяем общие поля */
		this.objProperties.add(this.hasRegion);
		this.objProperties.add(this.isRegionFor);

	}

	/**
	 * @return the {@linkplain #region}
	 */
	public OWLClass getRegion()
	{
		return region;
	}

	/**
	 * @return the {@linkplain #hasRegion}
	 */
	public OWLObjectProperty getHasRegion()
	{
		return hasRegion;
	}

	/**
	 * @return the {@linkplain #isRegionFor}
	 */
	public OWLObjectProperty getIsRegionFor()
	{
		return isRegionFor;
	}

	/**
	 * @return the {@linkplain #hasRegionDataValue}
	 */
	public OWLDataProperty getHasRegionDataValue()
	{
		return hasRegionDataValue;
	}
}
