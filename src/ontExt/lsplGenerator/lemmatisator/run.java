package ontExt.lsplGenerator.lemmatisator;

public class run
{

	public static void main(String[] args)
	{
		Lemmatisator lmzt = new MstLemmatisator();
		lmzt.init();
		
		lmzt.getLemma("иметь часть");
		lmzt.getLemma("имеет часть");
		lmzt.getLemma("содержит");
	
		lmzt.getLemma("связан с");
		lmzt.getLemma("ходит под");
		
		lmzt.getLemma("Nary parcticipation");
	}

}
