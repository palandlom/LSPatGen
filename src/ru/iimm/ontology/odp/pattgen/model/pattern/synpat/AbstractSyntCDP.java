package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.views.AbstractView;

import jdk.internal.dynalink.beans.StaticClass;
import ru.iimm.ontology.odp.pattgen.model.pattern.ContentDesingPattern;
import ru.iimm.ontology.odp.pattgen.model.pattern.TypeOfEntitiesCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.addoptions.AddConceptOpt;
import ru.iimm.ontology.odp.pattgen.model.pattern.addoptions.AddOption;
import ru.iimm.ontology.ontAPI.OWLDataFactotySingleton;
import ru.iimm.ontology.ontAPI.Ontology;

/**
 * Синтезируемый ПОС
 * 
 * @author Lomov P.A.
 *
 */
public abstract class AbstractSyntCDP extends ContentDesingPattern
		implements SyntCDPatInt
{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractSyntCDP.class);

	public static HashMap<IRI, Class> IRI2JavaClassMap = new HashMap<>();

	/**
	 * CDP добавленные в синтезируемый.
	 */
	private List<ContentDesingPattern> addedCDPs;

	/**
	 * Базовый CDP, на котором основан синтезируемый паттерн.
	 */
	// private ContentDesingPattern basedPat;

	/**
	 * Онтология формируемого паттерна в виде набора аксиом
	 */
	private Set<OWLAxiom> axioms;

	/**
	 * "Память" автомата. Концепты от которых зависит синтез: - концепты ToE -
	 * ситуации - множества
	 */
	// private Set<OWLClass> memory;

	/**
	 * IRI паттернов, которые можно добавлять.
	 * 
	 * @return
	 */
	public abstract List<IRI> getSuitablePattern();

	/**
	 * Создает CDP с онтологией из файла для использования при синтезе.
	 * 
	 * @param ontologyIRI
	 */

	protected AbstractSyntCDP(File ontfile)
	{
		super(ontfile);
		this.addedCDPs = new ArrayList<ContentDesingPattern>();
		this.axioms = this.getCDPOntology().ontInMem.getAxioms();

		// this.IRI2JavaClassMap.get(IRI.create("dd")).
	}

	/**
	 * Создает CDP с пустой онтологией для синтеза на основе паттерна из файла
	 * 
	 * @param ontologyIRI
	 *            {@link IRI} результирующей онтологии синтезируемого паттерна
	 * @param basedPat
	 *            паттерн-основа синтезируемого
	 */
	public AbstractSyntCDP(IRI ontologyIRI, Ontology basedPatOnt)
	{
		super(new Ontology(ontologyIRI));
		this.addedCDPs = new ArrayList<ContentDesingPattern>();
		this.axioms = new HashSet<OWLAxiom>();
		this.init(basedPatOnt);
	}

	/**
	 * Копирует содержание базового паттерна в синтезируемый
	 * 
	 * @param basedPat
	 */
	private void init(Ontology basedPatOnt)
	{

		/* Добавляем аксиомы базового паттерна в онтологию создаваемого... */
		this.getCDPOntology().mng.addAxioms(this.getCDPOntology().ontInMem,
				basedPatOnt.ontInMem.getAxioms());

		/* ... и в его набор аксиом */
		this.axioms.addAll(basedPatOnt.ontInMem.getAxioms());
		// Ontology basedPatternOnt = new Ontology(basedOntFile, true, true);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return this.getCDPOntology().getOntologyIRI().getFragment();
		// return this.getCDPOntology().getOntologyIRI().toString();
	}

	/**
	 * @param directoryPath
	 */
	public void save(URI directoryPath)
	{
		this.getCDPOntology().saveOntologies(directoryPath);
	}

	/**
	 * Добавляет аксиому в онтологию и набор аксиом паттерна
	 * 
	 * @param ax
	 */
	protected void addAxiom(OWLAxiom ax)
	{
		this.getCDPOntology().mng.addAxiom(this.getCDPOntology().ontInMem, ax);
		this.axioms.add(ax);
	}

	/**
	 * Добавляет аксиому в онтологию и набор аксиом паттерна
	 * 
	 * @param ax
	 */
	protected void addAxioms(Set<OWLAxiom> axSet)
	{
		this.getCDPOntology().mng.addAxioms(this.getCDPOntology().ontInMem,
				axSet);
		this.axioms.addAll(axSet);
	}

	/**
	 * Создает {@linkplain IRI} для разновидности {@linkplain OWLEntity}
	 * 
	 * @param primaryIRI
	 *            на основе данного IRI производиться генерация
	 * @return IRI для подсущности
	 */
	public static IRI getSubIRI(IRI primaryIRI)
	{
		Random rand = new Random();
		String primaryName = primaryIRI.getFragment();
		return IRI
				.create(primaryIRI.getNamespace() + Const.GENERATED_PREFFIX_IRI
						+ primaryName + "-" + (rand.nextInt(100) + 1) + "]");
	}

	/**
	 * Создает {@linkplain IRI} для разновидности {@linkplain OWLEntity}
	 * 
	 * @param primaryIRI
	 *            на основе данного IRI производиться генерация
	 * @return IRI для подсущности
	 */
	public static IRI getSubIRI(String primaryIRIStr)
	{
		return getSubIRI(IRI.create(primaryIRIStr));
	}

	/**
	 * Создает набор аксиом: домен-радиус-инверсия для 2 свойств
	 * 
	 * @param prp
	 * @param prpDomain
	 * @param prpRange
	 * @param inversePrp
	 * @return
	 */
	public static HashSet<OWLAxiom> getDomainRangeInverseAxioms(
			OWLObjectProperty prp, OWLClassExpression prpDomain,
			OWLClassExpression prpRange, OWLObjectProperty inversePrp)
	{
		HashSet<OWLAxiom> axioms = new HashSet<>();
		OWLDataFactory df = OWLDataFactotySingleton.getInstance();

		axioms.addAll(getDomainRangeAxioms(prp, prpDomain, prpRange));
		axioms.addAll(getDomainRangeAxioms(inversePrp, prpRange, prpDomain));
		axioms.add(df.getOWLInverseObjectPropertiesAxiom(prp, inversePrp));
		return axioms;
	}

	/**
	 * Создает набор аксиом: домен-радиус для 2 свойства
	 * 
	 * @param prp
	 * @param prpDomain
	 * @param prpRange
	 * @return
	 */
	public static HashSet<OWLAxiom> getDomainRangeAxioms(OWLObjectProperty prp,
			OWLClassExpression prpDomain, OWLClassExpression prpRange)
	{
		HashSet<OWLAxiom> axioms = new HashSet<>();
		OWLDataFactory df = OWLDataFactotySingleton.getInstance();

		axioms.add(df.getOWLObjectPropertyDomainAxiom(prp, prpDomain));
		axioms.add(df.getOWLObjectPropertyRangeAxiom(prp, prpRange));
		return axioms;
	}

	/**
	 * Создает подкласс переданного суперкласса + соответствующую
	 * {@linkplain OWLSubClassOfAxiom} аксиому. Подкласс можно потом получить -
	 * subClassAxiom.getOWLSubClass
	 * 
	 * @param superClass
	 * @return
	 */
	static protected OWLSubClassOfAxiom getSubClassAxiom(OWLClass superClass)
	{
		OWLDataFactory df = OWLDataFactotySingleton.getInstance();
		OWLClass subClass = df
				.getOWLClass(AbstractSyntCDP.getSubIRI(superClass.getIRI()));
		return df.getOWLSubClassOfAxiom(subClass, superClass);

	}

	/**
	 * Обобщенный конструктор - возвращает паттерн по IRI
	 * 
	 * @param patterIRI
	 * @return
	 */
	public static AbstractSyntCDP getCDP(IRI patterIRI)
	{
		if (patterIRI.equals(IRI.create(BagCDP.ONT_IRI)))
			return new BagCDP();
		else if (patterIRI.equals(IRI.create(CollectionCDP.ONT_IRI)))
			return new CollectionCDP();
		else if (patterIRI.equals(IRI.create(ComponencyCDP.ONT_IRI)))
			return new ComponencyCDP();
		else if (patterIRI.equals(IRI.create(CoparticipationCDP.ONT_IRI)))
			return new CoparticipationCDP();
		else if (patterIRI.equals(IRI.create(ListCDP.ONT_IRI)))
			return new ListCDP();
		else if (patterIRI.equals(IRI.create(NaryParticipationCDP.ONT_IRI)))
			return new NaryParticipationCDP();
		else if (patterIRI.equals(IRI.create(ObjectRoleCDP.ONT_IRI)))
			return new ObjectRoleCDP();
		else if (patterIRI.equals(IRI.create(ParticipantRoleCDP2.ONT_IRI)))
			return new ParticipantRoleCDP2();
		else if (patterIRI.equals(IRI.create(ParticipationCDP.ONT_IRI)))
			return new ParticipationCDP();
		else if (patterIRI.equals(IRI.create(PartOfCDP.ONT_IRI)))
			return new PartOfCDP();
		else if (patterIRI.equals(IRI.create(RegionCDP.ONT_IRI)))
			return new RegionCDP();
		else if (patterIRI.equals(IRI.create(SequenceCDP.ONT_IRI)))
			return new SequenceCDP();
		else if (patterIRI.equals(IRI.create(SetCDP.ONT_IRI)))
			return new SetCDP();
		else if (patterIRI.equals(IRI.create(SyntSitbasedCDP.ONT_IRI)))
			return new SyntSitbasedCDP();
		else if (patterIRI.equals(IRI.create(TimeIntervalCDP.ONT_IRI)))
			return new TimeIntervalCDP();
		else if (patterIRI.equals(IRI.create(TypeOfEntitiesCDP.ONT_IRI)))
			return new TypeOfEntitiesCDP();
		else
		{
			LOGGER.error(
					"Can't create {} . I don't know what constructor is used for it.",
					patterIRI);
			return null;
		}

	}

	/**
	 * Обобщенный конструктор (надстройка}) - возвращает указанный паттерн с
	 * новым IRI
	 * 
	 * @param patterIRI определяет какой IRI создавать
	 * @param newPatterIRI новый IRI у онтологии паттерна
	 * @return
	 */
	public static AbstractSyntCDP getCDP(IRI patterIRI, IRI newPatterIRI)
	{
		try
		{
			return AbstractSyntCDP.getCDP(patterIRI).getClass()
					.getConstructor(IRI.class).newInstance(newPatterIRI);

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * ================================================================
	 * GET/SETErs =====================================================
	 * ================================================================
	 */

	public List<ContentDesingPattern> getAddedCDPs()
	{
		return addedCDPs;
	}

	public void setAddedCDPs(List<ContentDesingPattern> addedCDP)
	{
		this.addedCDPs = addedCDP;
	}

	public Set<OWLAxiom> getOnt()
	{
		return axioms;
	}

	public void setOnt(Set<OWLAxiom> ont)
	{
		this.axioms = ont;
	}

	public Set<OWLAxiom> getAxioms()
	{
		return axioms;
	}

	public void setAxioms(Set<OWLAxiom> axioms)
	{
		this.axioms = axioms;
	}

	// метод опредляющий есть ли в добавленных ситуационные паттерны
	// метод опредляющий есть ли в добавленных множественные паттерны

}
