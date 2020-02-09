package ru.iimm.ontology.odp.pattgen.model.pattern.realizations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;

import ru.iimm.ontology.odp.pattgen.model.pattern.ODPRealization;
import ru.iimm.ontology.odp.pattgen.model.pattern.SituationCDP;
/**
 * 
 * Билдер для паттерна {@linkplain SituationCDP}.
 * @author Danilov
 * @version 0.2
 */
public class SituationRealization extends ODPRealization
{
	private ArrayList<OWLClass> entitys;
	private OWLClass situation;
	
	/**
	 * {@linkplain SituationRealization}
	 */
	private SituationRealization()
	{
		this.entitys = new ArrayList<OWLClass>();
	}

	public ArrayList<OWLClass> getEntitys()
	{
		return entitys;
	}

	public OWLClass getSituation()
	{
		return situation;
	}
	
	public static Builder newBuilder(SituationCDP pattern)
	{
		return new SituationRealization().new Builder(pattern);
	}
	
	public class Builder
	{
		private SituationCDP pattern;
		
		private Builder(SituationCDP pattern)
		{
			this.pattern = pattern;
		}
		public Builder setSituation(OWLClass situation)
		{
			SituationRealization.this.situation = situation;
			return this;
		}

		public Builder setEntity(OWLClass entity)
		{
			SituationRealization.this.entitys.add(entity);
			return this;
		}
		
		public Builder setEntity(OWLClass ... entitys)
		{
			for(OWLClass entity : entitys)
			{
				SituationRealization.this.entitys.add(entity);
			}
			return this;
		}
		
		public Builder setEntity(Collection<OWLClass> entitys)
		{
			for(OWLClass entity : entitys)
			{
				SituationRealization.this.entitys.add(entity);
			}
			return this;
		}
		/**Создание объекта реализации паттерна.*/
		public SituationRealization build()
		{	
			//Создаем новый объект
			SituationRealization realization = new SituationRealization();			
			realization.situation = SituationRealization.this.situation;
			realization.entitys = SituationRealization.this.entitys;
			realization.setPattern(this.pattern);
			
			OWLDataFactory df = pattern.getOWLDataFactory();
			
			//Заполняем аксиомами
			Set<OWLLogicalAxiom> structuralAxList = realization.getStructualAxiomSet();
			structuralAxList.add(df.getOWLSubClassOfAxiom(situation, pattern.getSituation()));
			
			for(OWLClass entity : entitys)
			{
				OWLObjectSomeValuesFrom hasSetting = df.getOWLObjectSomeValuesFrom(pattern.getHasSetting(), entity);
				OWLObjectSomeValuesFrom isSettingFor = df.getOWLObjectSomeValuesFrom(pattern.getIsSettingFor(), situation);
				structuralAxList.add(df.getOWLSubClassOfAxiom(situation, hasSetting));
				structuralAxList.add(df.getOWLSubClassOfAxiom(entity, isSettingFor));
			}
			
			return realization;
		}
	}
}
