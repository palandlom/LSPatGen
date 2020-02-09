package ontExt.lsplGenerator.ontElements;

import java.util.ArrayList;
import java.util.List;

/**
 * Объект/класс в тройке/ребре/структурной аксиоме
 * @author lomov
 *
 */
public class Concept extends AbsCDPObject
{

	public Concept(String shortIRI, String label, List<String> synonyms)
	{
		
		super(shortIRI, label, synonyms);
		// TODO Auto-generated constructor stub
	}


	
	
	public Concept(String shortIRI, String label)
	{
		super(shortIRI, label, new ArrayList<>());
		// TODO Auto-generated constructor stub
	}




	@Override
	public String toString()
	{	
		return this.getShortIRI();
	}
	
	



}
