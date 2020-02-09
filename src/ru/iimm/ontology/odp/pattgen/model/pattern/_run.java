package ru.iimm.ontology.odp.pattgen.model.pattern;

import java.net.URI;
import java.util.List;

import org.semanticweb.owlapi.model.IRI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.iimm.ontology.odp.pattgen.model.pattern.addoptions.AddOption;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.AbstractSyntCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.SyntSitbasedCDP;



public class _run
{

private static final Logger LOGGER = LoggerFactory.getLogger(_run.class);

	public static void main(String[] args)
	{
		/* Создаем пустой паттерн */
		SyntSitbasedCDP sitCdp = new SyntSitbasedCDP(IRI.create("http://ont/myPattern.owl"), );

		/* Формируем и выводим SOP..*/
		
		/* Получаем Паттерн для добавления */
		TypeOfEntitiesCDP toePattern = new TypeOfEntitiesCDP();

		/* Получаем опции 1-го добавления... */
		List<AddOption> addOpts = synCdp.getAddOptions(toePattern);
		
		/* Выводим опции для выбора пользователем...*/
		for (AddOption addOption : addOpts)
		{
			LOGGER.info("AddOption:\t{}", addOption);
		}
		
		/*Далее пользователь выбирает опцию...
		 * она выполняется.*/
		addOpts.get(2).perform();
		
		
		/*****************************************
		 **** 2 add pattern **********************
		 *****************************************/
		NaryParticipationCDP naryPattern = new NaryParticipationCDP();
		
		/* Получаем опции 2-го добавления... */
		addOpts = synCdp.getAddOptions(naryPattern);
		
		/* Выводим опции для выбора пользователем...*/
		for (AddOption addOption : addOpts)
		{
			LOGGER.info("AddOption:\t{}", addOption);
		}
		
		/*Далее пользователь выбирает опцию...
		 * она выполняется.*/
		addOpts.get(2).perform();
		
		
		
		/* Сохраняем полученный паттерн*/
		synCdp.save(URI.create("file:/D:/temp/_patternSynth/"));
		

	}

}
