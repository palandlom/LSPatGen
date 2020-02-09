package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.omg.CORBA.IRObjectOperations;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import ru.iimm.ontology.odp.pattgen.model.pattern.ContentDesingPattern;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.element.Branch;
import ru.iimm.ontology.ontAPI.Ontology;

/**
 * @author Lomov P. A.
 *
 */
public class SyntSitbasedCDP extends AbstractSyntCDP
{
	/*
	 * ============================== ==Common const=============
	 */
	public static final String ONT_IRI = "http://www.ontologydesignpatterns.org/cp/owl/situation.owl";
	public static final String BASE_IRI = ONT_IRI + "#";
	public static final String FILENAME = "situation.owl";

	/* Class names */
	public static final String SITUATION_IRI = BASE_IRI + "Situation";

	/* Properties names */
	public static final String HAS_SETTING_IRI = BASE_IRI + "hasSetting";
	public static final String IS_SETTING_FOR_IRI = BASE_IRI + "isSettingFor";
	/* =================== */

	/**
	 * дуги: [от_кого --отношение--> к_кому, (арность)]
	 * 
	 * @deprecated Пока не используются
	 */
	Set<Branch> sitComponents;

	/**
	 * Понятие ситуации, к которой нужно цеплять компонеты
	 */
	OWLClass situationConcept;

	OWLObjectProperty isSettingFor;
	OWLObjectProperty hasSetting;

	static public List<IRI> getSitbasedPatternIRIs()
	{
		List<IRI> patterns = new ArrayList<>();
		patterns.add(IRI.create(SyntSitbasedCDP.ONT_IRI));
		patterns.add(IRI.create(NaryParticipationCDP.ONT_IRI));
		patterns.add(IRI.create(ParticipantRoleCDP2.ONT_IRI));
		return patterns;
	}

	/**
	 * Создает паттернт с новым {@link IRI}, который используется как основа для
	 * последующего синтеза
	 * 
	 * @param ontologyIRI
	 * 
	 */
	public SyntSitbasedCDP(IRI synthPatOntologyIRI)
	{
		super(synthPatOntologyIRI, new Ontology(
				ContentDesingPattern.getCDPFile(SyntSitbasedCDP.FILENAME), true,
				true));

		this.init();
	}

	/**
	 * Создает паттерн не меняя {@link IRI} исходной онтологии. Применяется для
	 * последующего использования как компонент при синтезе
	 */
	public SyntSitbasedCDP()
	{
		super(ContentDesingPattern.getCDPFile(SyntSitbasedCDP.FILENAME));

		this.init();
	}

	/**
	 * Конструтор для использования в подклассах.
	 * 
	 * @param ontfile
	 */
	public SyntSitbasedCDP(File ontfile)
	{
		super(ontfile);
		this.init();
	}

	/**
	 * Конструтор для использования в подклассах.
	 * 
	 * @param ontfile
	 */

	public SyntSitbasedCDP(IRI ontologyIRI, Ontology basedPatOnt)
	{
		super(ontologyIRI, basedPatOnt);
		this.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ru.iimm.ontology.odp.pattgen.model.pattern.synpat.AbstractSyntCDP#init()
	 */
	// @Override
	protected void init()
	{
		OWLDataFactory df = this.getCDPOntology().df;

		/*
		 * Задаем концепт-ситуация: Создаем концепт-наследника стандартной
		 * ситуации и "переключаем" текущий паттерн на него (с ним далее и
		 * работаем) ...
		 */
		OWLClass situation = df.getOWLClass(IRI.create(SITUATION_IRI));
		OWLClass subSituation = df.getOWLClass(
				SyntSitbasedCDP.getSubIRI(IRI.create(SITUATION_IRI)));
		OWLSubClassOfAxiom subSituationAx = df
				.getOWLSubClassOfAxiom(subSituation, situation);
		this.situationConcept = subSituation;
		this.addAxiom(subSituationAx);

		// this.situationConcept = df.getOWLClass(IRI.create(SITUATION_IRI));
		this.sitComponents = new HashSet<>();

		/* Задаем отношения в ситуации */
		this.isSettingFor = df
				.getOWLObjectProperty(IRI.create(IS_SETTING_FOR_IRI));
		this.hasSetting = df.getOWLObjectProperty(IRI.create(HAS_SETTING_IRI));

		/*
		 * Добавляем компонент ситуации - Thing OWLClass entity =
		 * df.getOWLThing(); this.sitComponents.add(new
		 * Branch(this.situationConcept, entity, isSetting, true, false));
		 * this.sitComponents.add(new Branch(entity, this.situationConcept,
		 * hasSetting, true, false));
		 */

	}

	/*
	 * Add методы; аргументы - добавляемые паттерны - АПат из формального
	 * представление АПат + формального представления ИПат = делают новый набор
	 * аксиом <ННА> ННА + (скорее всего) НА от АПат добавляется в онтологию ИПат
	 * и его набор аксиом
	 * 
	 * Примечание: НА от АПат может не добавляться тк может быть заново создан в
	 * виде ННА, но это доп. затраты поэтому просто добавляем
	 * 
	 */

	/**
	 * @param addedSit
	 */
	public void add(SyntSitbasedCDP addedSit)
	{
		/*
		 * Получаем новый набор аксиом(НА) включения компонента в текущую
		 * ситуацию...
		 */
		Set<OWLAxiom> newAxioms = this
				.getIncludingAxioms(addedSit.situationConcept);

		/*
		 * ... добавляем его и аксиомы добав-го паттерна в онтологию и НА
		 * текущего паттерна
		 */
		this.addAxioms(newAxioms);
		this.addAxioms(addedSit.getAxioms());
		/*
		 * this.getCDPOntology().mng.addAxioms(this.getCDPOntology().ontInMem,
		 * newAxioms);
		 * this.getCDPOntology().mng.addAxioms(this.getCDPOntology().ontInMem,
		 * addedSit.getAxioms());
		 * 
		 */
	}

	/**
	 * Добавление коллекционных паттернов = включение концепта множества в
	 * компоненты ситуации
	 * 
	 * @param addedEnumeration
	 */
	public void add(EnumeratedCDP addedEnumeration)
	{

		/*
		 * Получаем новый набор аксиом(НА) включения компонента в текущую
		 * ситуацию...
		 */
		Set<OWLAxiom> newAxioms = this
				.getIncludingAxioms(addedEnumeration.getSetConcept());

		/*
		 * ... добавляем НА и аксиомы добав-го паттерна в НА текущего паттерна и
		 * в его онтологию
		 */
		this.addAxioms(newAxioms);
		this.addAxioms(addedEnumeration.getAxioms());
	}

	/**
	 * Добавление позиционных паттернов в компоненты ситуации
	 * 
	 * @param addedEnumeration
	 */
	public void add(PositionCDP addedPos)
	{
		/* Меняем добавляемый если в нем есть последовательность... */
		for (SequenceCDP seq : addedPos.sequences)
		{
			/*
			 * ... удаляем аксиомы от простого добавления последовательности в
			 * позиционный
			 */
			addedPos.getCDPOntology().mng.removeAxioms(
					addedPos.getCDPOntology().ontInMem,
					seq.getPositionAxioms());
			addedPos.getAxioms().removeAll(seq.getPositionAxioms());

			/* ... добавляем в позиционный аксиомы реифицированной ситуации */
			addedPos.addAxioms(seq.getSituationAxioms());
			addedPos.getRelatedConcepts().addAll(seq.relatedConcepts);
		}

		/*
		 * Создаем отношения членства в Situation = как подотношения settingFor
		 * + c именем, похожем на отношнение добавляемого паттерна + формируем
		 * аксиомы членства в ситуации
		 */

		/*
		 * Получаем новый набор аксиом(НА) включения компонента в текущую
		 * ситуацию, используя список связанных концептов добавляемого паттерна
		 * - addedPos.relatedConcepts ...
		 */
		Set<OWLAxiom> newAxioms = this
				.getIncludingAxioms(addedPos.getRelatedConcepts());
				// - .getIncludingAxioms(addedPos.getActiveConcepts());

		/*
		 * ... добавляем НА и аксиомы добав-го паттерна в НА текущего паттерна и
		 * в его онтологию
		 */
		this.addAxioms(newAxioms);
		this.addAxioms(addedPos.getAxioms());
		/*
		 * this.getAxioms().addAll(newAxioms);
		 * this.getAxioms().addAll(addedPos.getAxioms());
		 * this.getCDPOntology().mng.addAxioms(this.getCDPOntology().ontInMem,
		 * newAxioms);
		 * this.getCDPOntology().mng.addAxioms(this.getCDPOntology().ontInMem,
		 * addedPos.getAxioms());
		 */

	}

	public void add(SequenceCDP addedPos)
	{
		/*
		 * Получаем новый набор аксиом(НА) включения компонента в текущую
		 * ситуацию - это единственный концепт-ситуация
		 */
		Set<OWLAxiom> newAxioms = this
				.getIncludingAxioms(addedPos.getRelatedConcepts());

		/*
		 * ... добавляем аксиомы-компонентов + аксиомы-исходные +
		 * аксиомы-последовательности-в-ситуации = в НА текущего паттерна и в
		 * его онтологию
		 */
		this.addAxioms(newAxioms);
		this.addAxioms(addedPos.getAxioms());
		this.addAxioms(addedPos.getSituationAxioms());

	}

	/**
	 * Добавляет активный концепт в компоненты ситуации + аксиомы его
	 * суперклассов.
	 * 
	 * @param toePat
	 * @deprecated метод получился обычным для позиционных
	 */
	public void addDep(TypeOfEntitiesCDP toePat)
	{
		/*
		 * Получаем новый набор аксиом(НА) включения компонента в текущую
		 * ситуацию...
		 */
		Set<OWLAxiom> newAxioms = this
				.getIncludingAxioms(toePat.getActiveConcepts());

		/*
		 * ... добавляем НА и аксиомы добав-го паттерна в НА текущего паттерна и
		 * в его онтологию
		 */
		this.addAxioms(newAxioms);
		this.addAxioms(toePat.getAxioms());

	}

	/**
	 * Генерит аксиомы добавления компонента ситуации
	 * 
	 * @param component
	 *            будущий компонент ситуации.
	 * @return
	 */
	protected Set<OWLAxiom> getIncludingAxioms(OWLClass component)
	{
		HashSet<OWLAxiom> axioms = new HashSet<>();
		OWLDataFactory df = this.getCDPOntology().df;

		/* ... сгенерить подотношения setting/issetting ... */
		OWLObjectProperty subIsSettingFor = df.getOWLObjectProperty(
				SyntSitbasedCDP.getSubIRI(isSettingFor.getIRI()));
		OWLObjectProperty subHasSetting = df.getOWLObjectProperty(
				SyntSitbasedCDP.getSubIRI(hasSetting.getIRI()));

		/*
		 * ... создать 2 аксиомы SubPropertyOf для созданных подототношений..
		 */
		axioms.add(df.getOWLSubObjectPropertyOfAxiom(subIsSettingFor,
				isSettingFor));
		axioms.add(
				df.getOWLSubObjectPropertyOfAxiom(subHasSetting, hasSetting));

		/* ... создать по 2 аксиомы subclassOf - прямую и обратную */
		/* .........Axiom: <component> subclassOf hasSetting <sit> */
		/*
		 * Изменение 2: компонент не обязательно должен входить в ситуацию -> не
		 * надо
		 */
		// -OWLObjectSomeValuesFrom hasSettingRestr =
		// df.getOWLObjectSomeValuesFrom(subHasSetting, this.situationConcept);
		// -axioms.add(df.getOWLSubClassOfAxiom(component, hasSettingRestr));

		/* ........Axiom: <sit> subclassOf isSettingFor <component> */
		OWLObjectSomeValuesFrom isSettingRestr = df
				.getOWLObjectSomeValuesFrom(subIsSettingFor, component);
		axioms.add(df.getOWLSubClassOfAxiom(this.situationConcept,
				isSettingRestr));
		return axioms;
	}

	/**
	 * Генерит аксиомы добавления элементов позиционного паттерна в ситуацию
	 * 
	 * @param component
	 * @return
	 */
	protected Set<OWLAxiom> getIncludingAxioms(Collection<OWLClass> components)
	{
		HashSet<OWLAxiom> axioms = new HashSet<>();

		/* Для каждого компонента... */
		for (OWLClass component : components)
		{
			axioms.addAll(this.getIncludingAxioms(component));
		}

		return axioms;
	}

	protected void getIncludingAxAll(AbstractSyntCDP addedPat)
	{
		HashSet<OWLAxiom> axioms = new HashSet<>();

		Set<OWLAxiom> addPatternAxiomst = addedPat.getAxioms();
		Set<OWLObjectProperty> usedObjProperties = new HashSet<>();

		Set<OWLObjectProperty> tmpObjProperties;

		for (OWLAxiom ax : addPatternAxiomst)
		{
			/* Если аксиома может содержать отношение с другим классом... */
			if (ax.isLogicalAxiom()
					&& (ax.getAxiomType() == AxiomType.EQUIVALENT_CLASSES
							|| ax.getAxiomType() == AxiomType.SUBCLASS_OF))
			{
				/* ... получаем из аксиомы отношения (свойства)... */
				tmpObjProperties = ax.getObjectPropertiesInSignature();

				/*
				 * ... создаем для каждого свойства - аксиому членства. При этом
				 * концепт-компонент = радиусу свойства
				 */
				for (OWLObjectProperty prp : tmpObjProperties)
				{
					axioms.addAll(this.getPropertyIncludeAxioms(prp));

					/* Заносим обработанные свойства в список... */
					usedObjProperties.add(prp);
				}

			}

			/*
			 * Сравниваем обработанные свойства с теми, что есть в паттерне...
			 */

			/* ... для необработанных - создаем аксиомы членства */

		}

	}

	/**
	 * @param prp
	 * @return
	 */
	private Set<OWLAxiom> getPropertyIncludeAxioms(OWLObjectProperty prp)
	{
		Set<OWLAxiom> axioms = new HashSet<>();
		OWLDataFactory df = this.getCDPOntology().df;

		/*
		 * Получаем выражение из радиуса свойства - оно будет компонентом
		 * ситуации...
		 */
		/*
		 * TODO правильней было бы парсить исходную аксиому-отношение (и брать
		 * оттуда концепт-радиус), а не само свойство
		 */
		OWLClassExpression component = this.getRangeClass(prp,
				this.getCDPOntology().ontInMem);

		/* ... сгенерить подотношения setting/issetting ... */
		OWLObjectProperty subIsSettingFor = df.getOWLObjectProperty(
				SyntSitbasedCDP.getSubIRI(isSettingFor.getIRI()));
		OWLObjectProperty subHasSetting = df.getOWLObjectProperty(
				SyntSitbasedCDP.getSubIRI(hasSetting.getIRI()));

		/*
		 * ... создать 2 аксиомы SubPropertyOf для созданных подототношений..
		 */
		axioms.add(df.getOWLSubObjectPropertyOfAxiom(subIsSettingFor,
				isSettingFor));
		axioms.add(
				df.getOWLSubObjectPropertyOfAxiom(subHasSetting, hasSetting));

		/* ... создать по 2 аксиомы subclassOf - прямую и обратную */
		/* .........Axiom: <component> subclassOf hasSetting <sit> */
		OWLObjectSomeValuesFrom hasSettingRestr = df.getOWLObjectSomeValuesFrom(
				subHasSetting, this.situationConcept);
		axioms.add(df.getOWLSubClassOfAxiom(component, hasSettingRestr));

		/* .........reverse Axiom ... */
		OWLObjectSomeValuesFrom isSettingRestr = df
				.getOWLObjectSomeValuesFrom(subIsSettingFor, component);
		axioms.add(df.getOWLSubClassOfAxiom(this.situationConcept,
				isSettingRestr));

		return axioms;
	}

	/**
	 * Возвращает 1-й радиус свойства.
	 * 
	 * @param prp
	 * @param ont
	 * @return первый {@link OWLClassExpression} радиус, если раиус не задан то
	 *         Thing.
	 */
	private OWLClassExpression getRangeClass(OWLObjectProperty prp,
			OWLOntology ont)
	{
		OWLDataFactory df = this.getCDPOntology().df;
		OWLClass cls = df.getOWLThing();
		Set<OWLClassExpression> ranges = prp.getRanges(ont);

		if (ranges.size() > 0)
		{
			return ranges.iterator().next();

		} else
		{
			return cls;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.iimm.ontology.odp.pattgen.model.pattern.synpat.AbstractSyntCDP#
	 * getSuitablePattern()
	 */
	public List<IRI> getSuitablePattern()
	{
		ArrayList<IRI> patterns = new ArrayList<>();
		patterns.addAll(EnumeratedCDP.getEnumeratedPatternIRIs());
		patterns.addAll(SimpleCollectionCDP.getSimpleCollectionPatternIRIs());
		patterns.addAll(ItemedCollectionCDP.getItemedCollectionPatternIRIs());

		patterns.addAll(SyntSitbasedCDP.getSitbasedPatternIRIs());

		patterns.addAll(PositionCDP.getPositionPatternIRIs());
		patterns.addAll(SinglePositionCDP.getSingePositionPatternIRIs());

		return patterns;
	}

}
