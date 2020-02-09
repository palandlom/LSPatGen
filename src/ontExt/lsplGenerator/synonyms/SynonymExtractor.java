package ontExt.lsplGenerator.synonyms;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Получатель синонимов.
 * 
 * @author Lomov P.A.
 *
 */
public abstract class SynonymExtractor
{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SynonymExtractor.class);

	/**
	 * Возвращает набор синонимов указанного слова
	 * 
	 * 
	 * @param word
	 * @param maxQuantity
	 * @param addPrimaryWord
	 *            добавляет в результат исходное слово - word
	 * @param onlySingleWordSynonym
	 *            брать только однословные синонимы
	 * @return
	 * 
	 */
	public abstract List<String> getSynonyms(String word, int maxQuantity,
			Boolean addPrimaryWord, Boolean onlySingleWordSynonym);

	/**
	 * Загружает значения из конфига
	 */
	public abstract void init();

	/**
	 * Тестирует работу экстрактора
	 * 
	 * @return сообщение о результате проверки.
	 */
	public abstract String checkExtractor();

	/**
	 * Из каждого синонима берет первое слово (чтобы не было многословных
	 * синонимов)
	 * 
	 * @param synonyms
	 *            список синонимов
	 * @return
	 */
	public static <T extends List<String>> T getSingledSynonym(T synonyms)
	{
		/* Сохраняем во временный список... */
		ArrayList<String> lst = new ArrayList<String>();
		lst.addAll(synonyms);
		synonyms.clear();

		/* Переписываем в результирующий только первые слова... */
		lst.stream().map(s -> {
			String[] words = s.split(" ");
			return words.length > 0 ? words[0] : "";
		}).forEach(s -> synonyms.add(s));
		return synonyms;
	}

}
