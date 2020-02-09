package ontExt;

import java.util.Arrays;

public class TestSplitRun
{

	public static void main(String[] args)
	{
	String pattern ="Space = {W}<0,4> =text> Space\n" + 
			"BranchABObjectEvent  = Object  Space RelationIsParticipantIn  Space Event  =text> Object  RelationIsParticipantIn  Event \n" + 
			"BranchBAEventObject  = Event  Space RevRelationIsParticipantIn  Space Object  =text> Event  RevRelationIsParticipantIn  Object \n" + 
			"Object = {N1 | Pn1 | N1 <Object> }<1> =text> N1 Pn1";
	
	String [] pats = pattern.split("\n");
	//String [] pats = pattern.split(System.lineSeparator());
	
	
	
//	String [] pats = pattern.replace("=text>", "").split("=");
	 
	Arrays.stream(pats).forEach(p->System.out.println(p.trim()));
	int i=0;
	for (String str : pats)
	{
		i++;
		System.err.println(i+":"+str);
		String tmp = str.substring(0,str.indexOf('=')).trim();
		System.out.println(i+":"+tmp);
	}

	}

}
