package ontExt.lsplGenerator.synonyms;

/**
 * Тостовый класс для проверки взятия синонимов.
 * @author Lomov P. A.
 *
 */
public class runSynonyms
{

	public static void main(String[] args)
	{
		YandexSynonymExtractor ex = new YandexSynonymExtractor();
		
		ex.init();
		//ex.setProxied(true);
		
		System.out.println(ex.getSynonyms("регулировать",10,true,false));
	

	}

}
