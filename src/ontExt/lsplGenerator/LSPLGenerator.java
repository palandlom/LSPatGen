package ontExt.lsplGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ontExt.lsplGenerator.ontElements.Branch;
import ontExt.lsplGenerator.ontElements.BranchImpl;
import ontExt.view.obsElements.ObservableBranch;

/**
 * Генератор LSPL паттерна.
 * 
 * @author lomov
 *
 */
public class LSPLGenerator
{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(LSPLGenerator.class);

	/*
	 * 
	 * Objsa = {N1<функция>|N1<задача>|N1}<1> Space {V<оказываться> <N1.n=V.n,
	 * N1.g=V.g> | V<Являться> <N1.n=V.n, N1.g=V.g> | V<оказываться> <Pn1.n=V.n,
	 * Pn1.g=V.g> | V<Являться> <Pn1.n=V.n, Pn1.g=V.g>}<1> Space {N2|Pn2}<1>
	 * =text> N1 Pn1 " - " V " - " N2 Pn2 Objsa = {N2|Pn2}<1> Space
	 * {V<оказываться> <N1.n=V.n, N1.g=V.g, V.r=yes> | V<Являться> <N1.n=V.n,
	 * N1.g=V.g, V.r=yes> | V<оказываться> <Pn1.n=V.n, Pn1.g=V.g, V.r=yes> |
	 * V<Являться> <Pn1.n=V.n, Pn1.g=V.g, V.r=yes>}<1> Space
	 * {N1<функция>|N1<задача>|N1}<1> =text> N2 Pn2 " - " V " - " N1 Pn1
	 * 
	 */

	private String patternName;

	/* =============================================== */

	public LSPLGenerator(String patternName)
	{
		super();
		this.patternName = patternName;
	}

	public List<String> getLSPLrules(List<ObservableBranch> branches)
	{
		List<String> rules = new ArrayList<>();

		/* Добавляем правило наличия слов между концептами... */
		rules.add("Space = {W}<0,4> =text> Space");

		int branchNum = 1;
		for (Branch branch : branches)
		{
			if (!rules.contains(branch))
				rules.addAll(this.getLSPLrules(branch, branchNum++));
		}

		return rules;
	}

	/**
	 * Формирует паттерны для одной ветки {@linkplain BranchImpl}
	 * 
	 * @param branch
	 * @param branchNum
	 *            номер ветки (добавляетя в паттерны)
	 * @return
	 */
	public List<String> getLSPLrules(Branch branch, int branchNum)
	{
		char branchNumChar = (char) (branchNum + 64);
		List<String> rules = new ArrayList<>();
		StringBuffer buf = new StringBuffer();

		LOGGER.info("=== Generate rules for branch: {}", branch);

		/* Формируем названия LSPL паттернов для одной ветки */
		String ClassA = LSPLGenerator
				.getCamCase(LSPLGenerator
						.removeNumbersFrom(branch.getLConcept().getShortIRI()))
				+ branchNumChar + " ";
		String ClassB = LSPLGenerator
				.getCamCase(LSPLGenerator
						.removeNumbersFrom(branch.getRConcept().getShortIRI()))
				+ branchNumChar + " ";

		String BranchAB = "BranchAB" + LSPLGenerator.getCamCase(ClassA + ClassB)
				+ " ";
		String BranchBA = "BranchBA" + LSPLGenerator.getCamCase(ClassB + ClassA)
				+ " ";
		String Relation = "Relation"
				+ LSPLGenerator.getCamCase(LSPLGenerator.removeNumbersFrom(branch.getRelation().getShortIRI()))
				+ branchNumChar + " ";
		String RevRelation = "RevRelation"
				+ LSPLGenerator.getCamCase(LSPLGenerator.removeNumbersFrom(branch.getRelation().getShortIRI()))
				+ branchNumChar + " ";

		/* 1 Правило ================= */
		// BranchAB = ClassA Space Relation Space ClassB
		// =text> ClassA Relation ClassB
		buf.append(BranchAB + " = ").append(ClassA).append(" Space ")
				.append(Relation).append(" Space ").append(ClassB);
		buf.append(" =text> ").append(ClassA).append(' ').append(Relation)
				.append(' ').append(ClassB);
		rules.add(buf.toString()); // --добавляем и сбрасываем буффер
		buf = new StringBuffer();

		/* 2 Правило ================= */
		// BranchBA = ClassB Space RevRelation Space ClassA
		// =text> ClassB RevRelation ClassA
		buf.append(BranchBA + " = ").append(ClassB).append(" Space ")
				.append(RevRelation).append(" Space ").append(ClassA);
		buf.append(" =text> ").append(ClassB).append(' ').append(RevRelation)
				.append(' ').append(ClassA);
		rules.add(buf.toString()); // --добавляем и сбрасывем буффер
		buf = new StringBuffer();

		/* 3 Правило ================= */
		// ClassA = {N1 | Pn1 | N1 <synonymA1> | ... | N1 <synonymAn> }<1>
		// =text> N1 Pn1
		buf.append(ClassA + "= {N1 | Pn1 ");
		//buf.append(ClassA + "= { Pn1 ");
		for (String syn : branch.getLConcept().getSynonyms())
		{
			buf.append("| N1 <" + syn + "> ");
		}
		buf.append("}<1> =text> N1 Pn1");
		rules.add(buf.toString()); // --добавляем и сбрасывем буффер
		buf = new StringBuffer();

		/* 4 Правило ================= */
		// ClassB = {N2 | Pn2 | N2 <synonymB1> | ... | N2 <synonymBn> }<1>
		// =text> N2 Pn2
		buf.append(ClassB + "= {N2 | Pn2 ");
		//-buf.append(ClassB + "= { Pn2 ");
		for (String syn : branch.getRConcept().getSynonyms())
		{ // надо разбить метода + решить что должно выводиться - если список
			// синонимов пуст
			buf.append("| N2 <" + syn + "> ");
		}
		buf.append("}<1> =text> N2 Pn2");
		rules.add(buf.toString()); // --добавляем и сбрасывем буффер
		buf = new StringBuffer();

		/* 5 Правило ================= */
		// Relation = {V<synonymRel1> <N1.n=V.n, N1.g=V.g> | ... |
		// V<synonymReln> <N1.n=V.n, N1.g=V.g> | V<synonymRel1>
		// <Pn1.n=V.n, Pn1.g=V.g> | ... | V<synonymReln> <Pn1.n=V.n,
		// Pn1.g=V.g>}<1> =text> V
		/** @TODO Разбить метод */
		/*
		 * @TODO иногда пустая прав. часть RelationParticipationIncludes = {}<1>
		 * =text> V
		 */
		buf.append(Relation).append("= { ");
		for (String syn : branch.getRelation().getSynonyms())
		{
			buf.append(" V<" + syn + "> <N1.n=V.n, N1.g=V.g> |");
		}

		for (String syn : branch.getRelation().getSynonyms())
		{
			buf.append(" V<" + syn + "> <Pn1.n=V.n, Pn1.g=V.g> |");
		}
		buf.deleteCharAt(buf.length() - 1); // удаляем лишнюю "|"
		buf.append("}<1>  =text> V");
		rules.add(buf.toString()); // --добавляем и сбрасывем буффер
		buf = new StringBuffer();

		/* 6 Правило ================= */
		// RevRelation = {V<synonymRel1> <N1.n=V.n, N1.g=V.g, V.r=yes> | ... |
		// V<synonymReln>
		// <N1.n=V.n, N1.g=V.g, V.r=yes> | V<synonymRel1> <Pn1.n=V.n, Pn1.g=V.g,
		// V.r=yes> | ... | V<synonymReln>
		// <Pn1.n=V.n, Pn1.g=V.g, V.r=yes>}<1> =text> V
		buf.append(RevRelation).append("= { ");
		for (String syn : branch.getRelation().getSynonyms())
		{
			buf.append(" V<" + syn + "> <N1.n=V.n, N1.g=V.g, V.r=yes > |");
		}
		for (String syn : branch.getRelation().getSynonyms())
		{
			buf.append(" V<" + syn + "> <Pn1.n=V.n, Pn1.g=V.g, V.r=yes > |");
		}
		buf.deleteCharAt(buf.length() - 1); // удаляем лишнюю "|"
		buf.append("}<1>  =text> V");
		rules.add(buf.toString()); // --добавляем и сбрасывем буффер
		buf = new StringBuffer();

		return rules;
	}

	private static String removeNumbersFrom(String str)
	{
		String result = str.replaceAll("[0-9]", "");
		if (result.isEmpty())
		{
			return LSPLGenerator.getRandomString(5);
		} else
			return result;
	}

	private static String getRandomString(int length)
	{
		StringBuilder bul = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < length; i++)
		{
			bul.append(String.valueOf((random.nextInt(90 - 65) + 65)));
		}
		return bul.toString();
	}

	/**
	 * @param branch
	 * @return
	 * @deprecated
	 */
	public List getLSPLrules2(Branch branch)
	{
		List<String> rules = new ArrayList<>();
		StringBuffer buf = new StringBuffer();
		// String spaceRule
		/* Добавляем наборы слов между концептами... */
		rules.add("Space = {W}<0,4> =text> Space");

		/* 1 Правило ================= */
		buf.append(getPatternName() + " = ");
		/* добавляем 1 концепт... */
		// {N1<функция>|N1<задача>|N1}<1> Space {V<оказываться> <N1.n=V.n,
		// N1.g=V.g>
		buf.append("{N1 | Pn1 ");
		for (String syn : branch.getLConcept().getSynonyms())
		{
			buf.append("| N1 <" + syn + "> ");
		}
		buf.append("}<1>");
		buf.append("Space");

		/* добавляем отношение... */
		/*
		 * {V<оказываться> <N1.n=V.n, N1.g=V.g> | V<Являться> <N1.n=V.n,
		 * N1.g=V.g> | V<оказываться> <Pn1.n=V.n, Pn1.g=V.g> | V<Являться>
		 * <Pn1.n=V.n, Pn1.g=V.g>}<1>
		 */
		buf.append("{");
		for (String syn : branch.getRelation().getSynonyms())
		{
			buf.append(" V<" + syn + "> <N1.n=V.n, N1.g=V.g> |");
		}
		buf.deleteCharAt(buf.length() - 1);
		buf.append("}<1>");

		buf.append("Space");
		/* добавляем 2й концепт + завершение... */
		/* ... {N2|Pn2}<1> =text> N1 Pn1 " - " V " - " N2 Pn2 */
		buf.append("{N2 | Pn2 }<1>");
		buf.append("=text> N1 Pn1 \" - \" V \" - \" N2 Pn2");

		rules.add(buf.toString());
		buf.setLength(0);

		/* 2 Правило ================================================= */
		buf.append(getPatternName() + " = ");
		/* добавляем 1 концепт... */
		// {N2|Pn2}<1> Space ....
		buf.append("{N2 | Pn2 }<1>");
		buf.append("Space");

		/* добавляем отношение... */
		/*
		 * {V<оказываться> <N1.n=V.n, N1.g=V.g, V.r=yes> | V<Являться>
		 * <N1.n=V.n, N1.g=V.g, V.r=yes> | V<оказываться> <Pn1.n=V.n, Pn1.g=V.g,
		 * V.r=yes> | V<Являться> <Pn1.n=V.n, Pn1.g=V.g, V.r=yes>}<1> Space
		 */
		buf.append("{");
		for (String syn : branch.getRelation().getSynonyms())
		{
			buf.append(" V<" + syn + "> <N1.n=V.n, N1.g=V.g, V.r=yes > |");
		}
		buf.deleteCharAt(buf.length() - 1);
		buf.append("}<1>");
		buf.append(" Space ");

		/* добавляем 2й концепт + завершение... */
		/*
		 * ... {N1<функция>|N1<задача>|N1}<1> =text> N1 Pn1 " - " V " - " N2 Pn2
		 */

		buf.append("{N1 | Pn1 ");
		for (String syn : branch.getLConcept().getSynonyms())
		{
			buf.append("| N1 <" + syn + "> ");
		}
		buf.append("}<1>");
		buf.append("=text> N1 Pn1 \" - \" V \" - \" N2 Pn2");
		rules.add(buf.toString());
		buf.setLength(0);

		return rules;

	}

	/**
	 * Переводит строку в CamelCase нотацию
	 * 
	 * @param word
	 * @return
	 */
	private static String getCamCase(String word)
	{
		if (word == null || word.trim().length() <= 0)
		{
			LOGGER.warn("!!! Can do nothing with {}", word);
			return word;
		}
		String nWord = word.trim();
		StringBuffer buf = new StringBuffer();
		Boolean nextUp = true;
		for (int i = 0; i < nWord.length(); i++)
		{
			if (nWord.charAt(i) == ' ' || nWord.charAt(i) == '-'
					|| nWord.charAt(i) == '_')
				nextUp = true;
			else if (nextUp)
			{
				buf.append(Character.toUpperCase(nWord.charAt(i)));
				nextUp = false;
			} else
				buf.append(nWord.charAt(i));
		}

		nWord = buf.toString();
		LOGGER.info("Convert <" + word + "> to CameCase <" + nWord + ">");
		return nWord;
	}

	/*
	 * =============================================
	 * ============================================
	 */
	public String getPatternName()
	{
		return patternName;
	}

	public void setPatternName(String patternName)
	{
		this.patternName = patternName;
	}

}
