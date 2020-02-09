package ru.iimm.ontology.odp.pattgen.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import ru.iimm.ontology.odp.pattgen.MainApp;

public class RootController
{
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RootController.class);

	
	/** Стартовое приложение */
	MainApp mainApp;

	public RootController()
	{
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize()
	{

	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp)
	{
		this.mainApp = mainApp;
	}

	/** Загрузка персоны */
	@FXML
	private void handleLoadPersData()
	{

	}
	
	
	/** Сохранение персоны */
	@FXML
	private void handlSavePersData()
	{

	}


	/** == Opens an about dialog. */
	@FXML
	private void handleAbout()
	{
	    
	    
	
	}

	/** === Closes the application. */
	@FXML
	private void handleExit()
	{
		LOGGER.info("Exit");
		

		System.exit(0);
	}

	public MainApp getMainApp()
	{
		return mainApp;
	}


}
