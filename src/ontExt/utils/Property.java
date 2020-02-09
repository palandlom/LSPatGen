package ontExt.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Представляет конфиг программы.
 * @author Lomov P. A.
 *
 */
public class Property
{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(Property.class);

	/**
	 * Сохраняет свойства в файл
	 * 
	 * @param propFile
	 */
	public static void saveProperties(String propFilename)
	{
		Properties prop = new Properties();

		try (OutputStream output = new FileOutputStream(propFilename);)
		{
			// set the properties value
			/*
			 * prop.setProperty("database", "localhost");
			 * prop.setProperty("dbuser", "mkyong");
			 * prop.setProperty("dbpassword", "password");
			 */
			// save properties to project root folder
			prop.store(output, null);

		} catch (Exception e)
		{
			System.out.println(
					"Error during save properties in -" + propFilename);
		}
	}

	/**
	 * Загружает свойства из файла класспатха
	 * 
	 * @param propFilename
	 *            имя файла в класспатхе
	 * @return
	 */
	public static Properties loadClasspathProperties(String propFilename)
	{
		Properties prop = new Properties();

		try (InputStream input = Property.class.getClassLoader()
				.getResourceAsStream(propFilename))
		{
			if (input == null)
			{
				LOGGER.error("Sorry, unable to find {}", propFilename);
				//LOGGER.error("Path {}", Property.class.);
				System.exit(1);
				return null;
			}

			// load a properties file from class path, inside static method
			prop.load(input);
		} catch (Exception e)
		{
			System.out.println(
					"Error during loading properites from - " + propFilename);
		}
		return prop;
	}

	/**
	 * Загружает свойства из файла класспатха
	 * 
	 * @param propFilename
	 *            имя файла в класспатхе
	 * @return
	 */
	public static Properties loadProperties(File propFile)
	{
		Properties prop = new Properties();

		try (InputStream input = new FileInputStream(propFile))
		{
			// load a properties file
			prop.load(input);
		} catch (Exception e)
		{
			System.out.println(
					"Error during loading properites from - " + propFile);
		}
		return prop;
	}

	/**
	 * Загружает свойства из файла из катагора с джаром
	 * 
	 * @param propFilename
	 *            имя файла в класспатхе
	 * @return
	 */
	public static Properties loadJARlocationProperties(String propFilename)
	{
		Properties prop = new Properties();
		File propFile=null;
		/* Получаем урл файла со свойствами */
		CodeSource src = Property.class.getProtectionDomain().getCodeSource();
		if (src != null)
		{
			URL url=null;
			try
			{
				url = new URL(src.getLocation(), propFilename);
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
			System.out.println("Properties file location is " + url.getPath());
			propFile = new File(url.getPath());
		}

		/* Загружаем файл со свойствами */
		try (InputStream input = new FileInputStream(propFile))
		{
			prop.load(input);
		} catch (Exception e)
		{
			System.out.println(
					"Error during loading properites from - " + propFile);
		}
		return prop;
	}

	/**
	 * Выводит свойства на экран
	 * 
	 * @param prop
	 */
	public static void printProperties(Properties prop)
	{
		try
		{
			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements())
			{
				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				System.out.println("Key : " + key + ", Value : " + value);
			}
		} catch (Exception e2)
		{
			// TODO: handle exception
		}
	}

}
