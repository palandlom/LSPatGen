package ontExt.lsplGenerator.lemmatisator;

import java.util.List;

public abstract class Lemmatisator
{

	abstract public List<String> getLemma(String word);

	abstract public void init();

	
}
