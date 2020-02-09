package ontExt.lsplGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ontExt.lsplGenerator.lemmatisator.Lemmatisator;
import ontExt.lsplGenerator.lemmatisator.MstLemmatisator;
import ontExt.lsplGenerator.ontElements.Branch;
import ontExt.lsplGenerator.ontElements.BranchImpl;
import ontExt.lsplGenerator.ontElements.Concept;
import ontExt.lsplGenerator.ontElements.Relation;
import ontExt.lsplGenerator.synonyms.YandexSynonymExtractor;

public class RunLsplGen
{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RunLsplGen.class);

	public static void main(String[] args)
	{
		Lemmatisator lmzt = new MstLemmatisator();
		lmzt.init();
		// =========================================

		YandexSynonymExtractor ex = new YandexSynonymExtractor();
		ex.init();
		// System.out.println(ex.getSynonyms("РєРѕР·Р°", false));
		// =========================================

		//String[] words = "РїРµСЂРёРѕРґР° РІС‹РїР»Р°С‚С‹ РїРµРЅСЃРёРё".split(" ");
		//String[] words = "СЃС‚Р°Р¶ РІРєР»СЋС‡Р°СЋС‚СЃСЏ РїРµСЂРёРѕРґС‹".split(" ");
		String[] words = "РЅРµС‡С‚Рѕ СЂРµРіСѓР»РёСЂРѕРІР°С‚СЊ РєРµРј-С‚Рѕ".split(" ");
		
		String lemmaLCon=  lmzt.getLemma(words[0]).get(0);
		String lemmaRel=  lmzt.getLemma(words[1]).get(0);
		String lemmaRCon=  lmzt.getLemma(words[2]).get(0);
		 
		System.out.println(String.format("Lemmas: %s --%s-- %s", lemmaLCon, lemmaRel, lemmaRCon  ));

		Concept lCon = new Concept("WA",words[0], ex.getSynonyms(lemmaLCon,4, true, true));
		Concept rCon = new Concept("WB",words[2], ex.getSynonyms(lemmaRCon,4, true, true));
		Relation rel = new Relation("Rel",words[1], ex.getSynonyms(lemmaRel,4, true, true));
		
//		Concept lCon = new Concept("WA",words[0], new ArrayList<>());
//		Concept rCon = new Concept("WB",words[2], new ArrayList<>());
//		Relation rel = new Relation("Rel",words[1], new ArrayList<>());


		Branch branch = new BranchImpl(lCon, rel, rCon, null);

		LSPLGenerator gen = new LSPLGenerator("Obj");
		List<String> rules = gen.getLSPLrules(branch,1);

		System.out.println("\n========================================\n");
		for (String rule : rules)
		{

			System.out.println(rule);
		}

	}

	public static void getSynonyms(YandexSynonymExtractor ex)
	{

		//ex.getSynonyms("РєРѕР·Р°", false);

	}

}
