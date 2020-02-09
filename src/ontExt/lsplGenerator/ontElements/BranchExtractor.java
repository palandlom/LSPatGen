package ontExt.lsplGenerator.ontElements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ontExt.lsplGenerator.LSPLGenerator;
import ontExt.lsplGenerator.lemmatisator.Lemmatisator;
import ontExt.lsplGenerator.lemmatisator.MstLemmatisator;
import ontExt.lsplGenerator.ontology.AxiomParserVisitor;
import ontExt.lsplGenerator.synonyms.SynonymExtractor;
import ontExt.lsplGenerator.synonyms.YandexSynonymExtractor;
import ru.iimm.ontology.ontAPI.OWLOntologyAdapter;
import ru.iimm.ontology.ontAPI.Ontology;

public class BranchExtractor
{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BranchExtractor.class);

	public static void main(String[] args)
	{
		if (args.length < 1)
			throw new RuntimeException("The name of the OWL file is required!");
		File CDPfile = new File(args[0]); // Файл с OWL-паттерном

		LOGGER.info("nnn");

		/* Загружаем паттерн.. */
		//Ontology PatternOnt = new Ontology(CDPfile, true, true);
		Ontology PatternOnt = new OWLOntologyAdapter();
		PatternOnt.init();
		PatternOnt.loadContent(IRI.create(CDPfile));
			

		/* Получаем ветви из онтологии... */
		List<BranchImpl> branches = BranchExtractor.getBranches(PatternOnt);

		/* Генерим по ветвям соответствующие LSPL-шаблоны */
		LSPLGenerator gen = new LSPLGenerator("ccc");
		List<String> lspl = gen.getLSPLrules(branches.get(0),1);

		for (String string : lspl)
		{
			LOGGER.info(string);
		}

		/* Сохнаняем шаблоны в онтологии и файле */
	}

	/**
	 * Получает список веток из аксиом онтологии.
	 * 
	 * @param ont
	 * @return
	 */
	public static List<BranchImpl> getBranches(Ontology ont)
	{
		AxiomParserVisitor vis = new AxiomParserVisitor();
		List<BranchImpl> branchList = new ArrayList<>();
		/* Выбираем аксиомы для генерации ветвей .. */
		for (OWLLogicalAxiom axiom : ont.getOnt().getLogicalAxioms())
		{
			axiom.accept(vis);
			if (vis.getIsSuccesful())
			{
				LOGGER.info("Branch axiom: "
						+ OWLOntologyAdapter.getShortIRI(vis.getLeftClass()) + " =="
						+ OWLOntologyAdapter.getShortIRI(vis.getProperty()) + "=> "
						+ OWLOntologyAdapter.getShortIRI(vis.getRightClass()));
				BranchImpl branch = BranchExtractor.getBranch(vis, ont);
				branchList.add(branch);
				vis.reset();
			}
		}

		return branchList;
	}

	
	/**
	 * @param ont
	 * @return
	 * @deprecated пока не пригодился
	 */
	private List<OWLAxiom> getAxiomList(Ontology ont)
	{
		AxiomParserVisitor vis = new AxiomParserVisitor();
		List<OWLAxiom> axiomList = new ArrayList<>();
		/* Выбираем аксиомы для генерации ветвей .. */
		for (OWLLogicalAxiom axiom : ont.getOnt().getLogicalAxioms())
		{
			axiom.accept(vis);
			if (vis.getIsSuccesful())
			{
				LOGGER.info("Branch axiom: "
						+ OWLOntologyAdapter.getShortIRI(vis.getLeftClass()) + " =="
						+ OWLOntologyAdapter.getShortIRI(vis.getProperty()) + "=> "
						+ OWLOntologyAdapter.getShortIRI(vis.getRightClass()));

				vis.reset();
			}
		}
		return null;
	}


	/**
	 * Получает ветку их результатов обработки аксиомы посетителем.
	 * 
	 * @param vis
	 * @param ont
	 * @return
	 */
	private static BranchImpl getBranch(AxiomParserVisitor vis, Ontology ont)
	{
		/* Получаем идентиф-ры из IRI концептов... */
		String lConShortIRI = OWLOntologyAdapter.getShortIRI(vis.getLeftClass());
		String rConShortIRI = OWLOntologyAdapter.getShortIRI(vis.getRightClass());
		String propShortIRI = OWLOntologyAdapter.getShortIRI(vis.getProperty());

		/* Получаем читаемые названия из лейблов... */
		String lConName = BranchExtractor.getName(vis.getLeftClass(), ont);
		String rConName = BranchExtractor.getName(vis.getRightClass(), ont);
		String propName = BranchExtractor.getName(vis.getProperty(), ont);

		/* Пользуем лематизатор */
		// Lemmatisator lmtor = new MstLemmatisator();
		// lmtor.init();

		/* .. создаем элементы ветки c пустыми синонимами ... */
		Concept lConcept = new Concept(lConShortIRI, lConName,
				new ArrayList<>());
		Concept rConcept = new Concept(rConShortIRI, rConName,
				new ArrayList<>());
		Relation relation = new Relation(propShortIRI, propName,
				new ArrayList<>());

		/* .. получаем синонимы и создаем элементы ветки... */
		/*
		 * Concept lConcept = new Concept(lConShortIRI, lConName,
		 * BranchExtractor.getSynonums(lConName)); Concept rConcept = new
		 * Concept(rConShortIRI, rConName,
		 * BranchExtractor.getSynonums(rConName)); Relation relation = new
		 * Relation(propShortIRI, propName,
		 * BranchExtractor.getSynonums(propName));
		 */

		return new BranchImpl(lConcept, relation, rConcept, vis.getInitialAxiom());
	}

	/**
	 * Дает список синонимов слова.
	 * 
	 * @param word
	 * @return
	 */
	private static List<String> getSynonums(String word)
	{
		String SpacelessWord = word.replace(' ', '_');
		SynonymExtractor synExt = new YandexSynonymExtractor();
		synExt.init();
		List<String> synonyms = synExt.getSynonyms(SpacelessWord,5, true,true);

		return synonyms;
	}

	/**
	 * Возвращает название сущности из онтологии.
	 * 
	 * @param ent
	 * @param ont
	 * @return
	 */
	private static String getName(OWLEntity ent, Ontology ont)
	{
		String name;
		name = ont.getLabel(ent, "ru");
		if (name != null && name.length() > 0)
			return name;

		name = ont.getLabel(ent, "en");
		if (name != null && name.length() > 0)
			return name;

		name = ont.getLabel(ent);
		if (name != null && name.length() > 0)
			return name;

		LOGGER.warn("!!! There is no ANY labels of <{}>",
			OWLOntologyAdapter.getShortIRI(ent.getIRI()));
		LOGGER.warn("!!! Use short IRI instead");
		return OWLOntologyAdapter.getShortIRI(ent);

	}

}
