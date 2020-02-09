package ru.iimm.ontology.odp.pattgen.model.pattern.addoptions;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.iimm.ontology.odp.pattgen.model.pattern.ContentDesingPattern;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.AbstractSyntCDP;

public class AddConceptOpt extends AddOption
{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AddConceptOpt.class);

	private OWLClass concept;

	public AddConceptOpt(AbstractSyntCDP syntCDP, ContentDesingPattern sourceCDP,
			OWLClass concept)
	{
		super(syntCDP, sourceCDP);
		this.concept = concept;
	}

	@Override
	public void perform()
	{
		LOGGER.info("Perform option:\t{}", this.toString());

		/* Пополняем "память" ... */
		this.getSyntCDP().add2Memory(concept);

		/* Создаем аксиому + заносим ее в список синтезируемого ... */
		OWLAxiom axiom = this.getDf().getOWLSubClassOfAxiom(concept,
				this.getDf().getOWLThing());
		this.getSyntCDP().getOnt().add(axiom);

		/* Пополняем онтологию ... */
		this.getSyntCDP().getCDPOntology().mng
				.addAxiom(this.getSyntCDP().getCDPOntology().ontInMem, axiom);
	}

	/*
	 * ==========================================================
	 * ==========================================================
	 */

	public OWLClass getConcept()
	{
		return concept;
	}

	public void setConcept(OWLClass concept)
	{
		this.concept = concept;
	}

}
