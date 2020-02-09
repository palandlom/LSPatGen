package ontExt.lsplGenerator.ontElements;

import java.util.ArrayList;
import java.util.List;

/**
 * Отношение/свойство в тройке/ребре.
 * @author lomov
 *
 */
public class Relation extends AbsCDPObject
{

	public Relation(String shortIRI, String label, List<String> synonyms)
	{
		super(shortIRI, label, synonyms);
		// TODO Auto-generated constructor stub
	}

	
	public Relation(String label, List<String> synonyms)
	{
		super(label, label, synonyms);
		// TODO Auto-generated constructor stub
	}

	public Relation(String label)
	{
		super(label, label, new ArrayList<>());
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString()
	{	
		return this.getShortIRI();
	}
	


}
