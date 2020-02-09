package ontExt.lsplProcessor;

public class Run
{
	public static void main(String[] args)
	{
		String dir = "D:\\Working\\_Eclipse\\ontExt.int\\lspl\\test\\";
		//String dir = "D:\\Lomov\\_Eclipse\\workspace_oxygen\\ontExt.int\\lspl\\test\\";
		//String dir = ".\\lspl\\test\\";
		String inFilePath = dir + "text.txt";
		String patternPath = dir + "templ";
		String outFilePath = dir + "out.txt";
		//String target[] = {"Invspec","Space"};
		String target[] = {"Invspec"};

		LsplManager mng = new LsplManager(inFilePath, patternPath, outFilePath,
				target);
		mng.init();
		mng.produceOutFile();
		
	//	mng.produceFinalResultFile();
	};
}
