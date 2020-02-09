package ru.iimm.ontology.odp.pattgen.model.pattern.synpat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.IRI;

import ru.iimm.ontology.ontAPI.Ontology;

/**
 * Паттерны Свойствованые: (PartOf, Sequence, Componency) Описание: могут иметь
 * только свойства => тогда активный концепт Thing один объект + свойство,
 * связывающее его с сами собой (sequence, componency) => активный * концепт -
 * объект
 * 
 * @author lomov
 *
 */
public abstract class SinglePropertyCDP extends PositionCDP
{

	static public List<IRI> getSinglePropertyPatternIRIs()
	{
		List<IRI> patterns = new ArrayList<>();
		patterns.add(IRI.create(ComponencyCDP.ONT_IRI));
		patterns.add(IRI.create(PartOfCDP.ONT_IRI));
		patterns.add(IRI.create(SequenceCDP.ONT_IRI));
		
		return patterns;
	}

	
	/**
	 * Создает паттерн не меняя {@link IRI} исходной онтологии. Применяется для
	 * последующего использования как компонент при синтезе
	 * 
	 * @param ontfile
	 *            файл с паттерном-основой
	 */

	public SinglePropertyCDP(File ontfile)
	{
		super(ontfile);
	}

	/**
	 * Создает паттерн с новым {@link IRI}, который используется как основа для
	 * последующего синтеза
	 * 
	 * @param ontologyIRI
	 *            новый {@link IRI} создаваемого паттерна
	 * @param basedPatOnt
	 *            онтология паттерна-основы
	 */

	public SinglePropertyCDP(IRI ontologyIRI, Ontology basedPatOnt)
	{
		super(ontologyIRI, basedPatOnt);
	}

}
