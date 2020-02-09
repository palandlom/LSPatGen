/**
 * 
 */
package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import java.io.File;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import ru.iimm.ontology.odp.pattgen.model.pattern.ContentDesingPattern;
import ru.iimm.ontology.ontAPI.Ontology;

/**
 * @author lomov
 *
 */
public class ComponencyCDP extends SinglePropertyCDP
{

	/* Common const */
	public final static String ONT_IRI = "http://www.ontologydesignpatterns.org/cp/owl/componency.owl";
	static String BASE_IRI = ONT_IRI + "#";
	public final static String FILENAME = "componency.owl";

	/* Properties names */
	static String HAS_PART_IRI = BASE_IRI + "hasPart";
	static String IS_PART_OF_IRI = BASE_IRI + "isPartOf";
	static String HAS_COMPONENT_IRI = BASE_IRI + "hasComponent";
	static String IS_COMPONENT_OF_IRI = BASE_IRI + "isComponentOf";

	/**
	 * //http://www.ontologydesignpatterns.org/cp/owl/componency.owl#
	 * hasComponent
	 */
	private OWLObjectProperty hasComponent;

	/**
	 * http://www.ontologydesignpatterns.org/cp/owl/componency.owl#isComponentOf
	 */
	private OWLObjectProperty isComponentOf;

	private OWLClass thing;

	/**
	 * Создает паттерн с новым {@link IRI}, который используется как основа для
	 * последующего синтеза
	 * 
	 * @param ontologyIRI
	 *            новый {@link IRI} создаваемого паттерна
	 */
	public ComponencyCDP(IRI ontologyIRI)
	{
		super(ontologyIRI,
				new Ontology(
						ContentDesingPattern.getCDPFile(ComponencyCDP.FILENAME),
						true, true));
		init();

	}

	/**
	 * Создает паттерн не меняя {@link IRI} исходной онтологии. Применяется для
	 * последующего использования как компонент при синтезе
	 * 
	 */
	public ComponencyCDP()
	{
		super(ContentDesingPattern.getCDPFile(ComponencyCDP.FILENAME));
		init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.iimm.ontology.odp.pattgen.model.pattern.synpat.PositionCDP#init()
	 */
	@Override
	protected void init()
	{
		/* A) Задаем спец-ие поля паттерна */
		this.isComponentOf = df
				.getOWLObjectProperty(IRI.create(IS_COMPONENT_OF_IRI));
		this.hasComponent = df
				.getOWLObjectProperty(IRI.create(HAS_COMPONENT_IRI));

		this.thing = df.getOWLClass(IRI.create(
				"http://www.ontologydesignpatterns.org/cp/owl/componency.owl#Object"));

		/* А1) Собираем отношения... */
		this.objProperties.add(isComponentOf);
		this.objProperties.add(hasComponent);

		/*
		 * А2) Собираем активные концепты (по которым паттерн будет
		 * сопрягаться)...
		 */
		this.activeConcepts.add(this.thing);

		/*
		 * А3) Собираем связанные концепты (по которым settingfor отношения
		 * будут генериться)...
		 */
		this.relatedConcepts.add(thing);

	}

	/*
	 * ====================================== =================== ========
	 * GET/SETTers =================== ==========================
	 * ==============================================
	 */

	public OWLObjectProperty getHasComponent()
	{
		return hasComponent;
	}

	public void setHasComponent(OWLObjectProperty hasComponent)
	{
		this.hasComponent = hasComponent;
	}

	public OWLObjectProperty getIsComponentOf()
	{
		return isComponentOf;
	}

	public void setIsComponentOf(OWLObjectProperty isComponentOf)
	{
		this.isComponentOf = isComponentOf;
	}

}
