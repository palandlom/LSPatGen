package ontExt.lsplGenerator.synonyms;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ontExt.utils.Property;

/**
 * Получатель синонимов от Yandex-словаря.
 * 
 * @author Lomov P. A.
 *
 */
public class YandexSynonymExtractor extends SynonymExtractor
{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(YandexSynonymExtractor.class);

	/**
	 * URL запроса синонимов.
	 */
	private String URL;
	/**
	 * Агент для указания в заголовке запроса.
	 */
	private String userAgent;

	private Boolean useProxy = false;

	private HttpHost proxy;

	private Properties prop;

	/**
	 * Данные для аутентификации через прокси.
	 */
	private UsernamePasswordCredentials creds;

	// private String userAgent;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ontExt.synonyms.SynonymExtractor#init()
	 */
	public void init()
	{
		/* Грузим конфиг со свойствами... */
		LOGGER.info("== Load config:");
		this.prop = Property.loadClasspathProperties("config.properties");
		// -Property.printProperties(prop);

		this.setUserAgent(prop.getProperty("YandexSynonymExtractor.userAgent"));

		/* Собираем URL... */
		StringBuffer URLbuf = new StringBuffer();
		URLbuf.append(prop.getProperty("YandexSynonymExtractor.uri"));
		URLbuf.append("?key=")
				.append(prop.getProperty("YandexSynonymExtractor.key"));
		URLbuf.append("&lang=")
				.append(prop.getProperty("YandexSynonymExtractor.lang"));
		URLbuf.append("&text=");
		this.setURL(URLbuf.toString());

		/* Грузим настройки прокси... */
		this.setProxied(Boolean.parseBoolean(
				prop.getProperty("YandexSynonymExtractor.useProxy")));
		if (this.useProxy)
		{
			this.initProxySettings(prop);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ontExt.synonyms.SynonymExtractor#getSynonyms(java.lang.String)
	 */
	public List<String> getSynonyms(String word, int maxQuantity, Boolean addPrimaryWord, Boolean onlySingleWordSynonym)
	{
		if (this.getURL() == null)
		{
			LOGGER.error("Extractor is not initialized... run .init()!!!");
			return null;
		}

		/* Собираем конфиг запроса... */
		RequestConfig config = this.useProxy
				? RequestConfig.custom().setProxy(proxy).build()
				: RequestConfig.custom().build();
		LOGGER.info("Use proxy: {}", this.getProxy());

		/* Формируем URL запроса */
		HttpGet req = new HttpGet();
		req.setConfig(config);
		req.setURI(URI.create(this.getURL() + word));
		req.setHeader("User-Agent", this.getUserAgent());
		LOGGER.info("Request: {}", this.getURL() + word);
		LOGGER.info("Req opt: {}", req.getConfig());
		InputStream inputStream = null;
		YandexSynParser yandexAnsHandler = new YandexSynParser();

		/* Выполняем запрос c авторизацией... */
		try (CloseableHttpClient client = useProxy ? HttpClients.custom()
				.setDefaultCredentialsProvider(this.getCredsProvider()).build()
				: HttpClients.custom().build();
				CloseableHttpResponse response = client.execute(req))
		{
			/* ... берем ответ ... */
			inputStream = response.getEntity().getContent();
			/* ... вызываем парсер ответа... */
			SAXParserFactory parserFactor = SAXParserFactory.newInstance();
			SAXParser parser = parserFactor.newSAXParser();
			parser.parse(inputStream, yandexAnsHandler);
			/* ... теперь в handler есть синонимы. */
		} catch (Exception e)
		{
			LOGGER.error("!!! Request failed - {}", e.getMessage());

		}

		// Корректируем пустой список синонимов...
		List<String> synList;
		if (yandexAnsHandler.getSynonyms().size() > 0)
		{
			synList = yandexAnsHandler.getSynonyms();
		} else
		{
			synList = new ArrayList<>();
			if (addPrimaryWord)
				synList.add(word);
		}
		
		// Берем из многословных синонимов 1 слово 
		if (onlySingleWordSynonym)
		{
			//Вырезаем синонимы с двойными словами
			//synList = synList.stream().filter(w -> w.split(" ").length==1).collect(Collectors.toList());
			synList = SynonymExtractor.getSingledSynonym(synList);
		}
		
		// Корректируем размер списка
		synList = synList.size()>maxQuantity? synList.subList(0, maxQuantity) : synList;
		

		LOGGER.info("Word: {}", word);
		LOGGER.info("Synonyms: {}", synList);
		LOGGER.info("Synonym count: {}", synList.size() - 1);
		return synList;
	}

	/**
	 * @param prop
	 */
	private void initProxySettings(Properties prop)
	{
		try
		{ /* Данные о прокси... */
			String proxyIP = prop.getProperty("YandexSynonymExtractor.proxyIP");
			String proxyScheme = prop
					.getProperty("YandexSynonymExtractor.proxyScheme");
			int proxyPort = Integer.parseInt(
					prop.getProperty("YandexSynonymExtractor.proxyPort"));
			String proxyUser = prop
					.getProperty("YandexSynonymExtractor.proxyUser");
			String proxyPass = prop
					.getProperty("YandexSynonymExtractor.proxyPass");
			HttpHost proxy = new HttpHost(proxyIP, proxyPort, proxyScheme);
			this.setProxy(proxy);
			this.setProxied(Boolean.parseBoolean(
					prop.getProperty("YandexSynonymExtractor.useProxy")));

			/* Проверка корректности настроек . */
			if (proxyIP == null || proxyIP.length() == 0 || proxyScheme == null
					|| proxyScheme.length() == 0 || proxyUser == null
					|| proxyUser.length() == 0 || proxyPass == null
					|| proxyPass.length() == 0)
			{
				throw new Exception("");
			}

			/* Учетка прокси пользователя... */
			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(
					proxyUser, proxyPass);
			this.setCreds(creds);

		} catch (Exception e)
		{
			LOGGER.info(
					"Error during loading Proxy setting. Switch off proxy.");
			this.setProxied(false);
		}

	}

	/**
	 * Собирает авторизационного провайдера из текущего прокси и учетки для
	 * использования в запросе
	 * 
	 * @return
	 */
	public CredentialsProvider getCredsProvider()
	{

		if (this.getProxy() != null && this.getCreds() != null)
		{
			CredentialsProvider prov = new BasicCredentialsProvider();
			prov.setCredentials(new AuthScope(this.getProxy()),
					this.getCreds());
			return prov;

		} else
		{
			LOGGER.warn(
					"!!! Proxy of credential have not been loaded - can't build CredentialsProvider !!!");
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ontExt.lsplGenerator.synonyms.SynonymExtractor#checkExtractor()
	 */
	@Override
	public String checkExtractor()
	{
		LOGGER.info("== Checking of YandexSynonymExtractor...");
		String testWord = this.prop.getProperty("YandexSynonymExtractor.lang")
				.equals("ru") ? "солнце" : "sun";

		List<String> synonyms = null;
		try
		{
			synonyms = this.getSynonyms(testWord, 1, false, true);
		} catch (Exception e)
		{
			LOGGER.error("YandexSynonymExtractor check failed - ",
					e.getMessage());
		}
		String message = synonyms == null || synonyms.isEmpty()
				? "Checking of YandexSynonymExtractor failed - see logs and check configuration"
				: "Checking of YandexSynonymExtractor is succesful";
		LOGGER.info("== {}" + message);

		return message;
	}

	/*
	 * ========= Get/Setters ============================
	 * =================================================
	 */

	public String getURL()
	{
		return URL;
	}

	public void setURL(String uRL)
	{
		URL = uRL;
	}

	public String getUserAgent()
	{
		return userAgent;
	}

	public void setUserAgent(String userAgent)
	{
		this.userAgent = userAgent;
	}

	public Boolean isProxied()
	{
		return useProxy;
	}

	public void setProxied(Boolean useProxy)
	{
		this.useProxy = useProxy;
	}

	public HttpHost getProxy()
	{
		return proxy;
	}

	public void setProxy(HttpHost proxy)
	{
		this.proxy = proxy;
	}

	public UsernamePasswordCredentials getCreds()
	{
		return creds;
	}

	public void setCreds(UsernamePasswordCredentials creds)
	{
		this.creds = creds;
	}

}
