package ru.iimm.ontology.odp.pattgen.view;

import org.semanticweb.owlapi.model.IRI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import ru.iimm.ontology.odp.pattgen.MainApp;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.AbstractSyntCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.SyntSitbasedCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.builders.AbstractPatternConfigurator;

public class PattEditorController
{

	/**
	 * Биндится с списком выбора добавляемого паттерна на форме.
	 */
	@FXML
	ListView<IRI> suitablePatternIRIs;

	/**
	 * Кнопка перехода к добавлению паттерна.
	 */
	@FXML
	Button addSelectedPatternButton;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PattEditorController.class);

	/** Стартовое приложение */
	MainApp mainApp;

	public PattEditorController()
	{
		this.suitablePatternIRIs = new ListView<>();
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
	 * Отображает компоненты билднрп на форме.
	 * 
	 * @param builder
	 */
	public void showBuilder(AbstractPatternConfigurator builder)
	{
		LOGGER.info("Show builder: {}", this.mainApp.getMainBuilder());
		// this.addingPatterns.setCellFactory(value);

		LOGGER.info("->"+builder.getObservableAddCDPList().size());
		
		this.suitablePatternIRIs.setItems(builder.getObservableAddCDPList());
//		 this.addingPatterns.setCellFactory(ComboBoxListCell.forListView(builder.getObservableAddCDPList()));

		// this.addingPatterns.setCellFactory(CheckBoxListCell.forListView(callvback)stView(builder.getObservableAddCDPList()));
		// this.addingPatterns.setCellFactory(cellData -> cellData.);
		// listView.setCellFactory(ComboBoxListCell.forListView(names));

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

	/**
	 * Создает билдер добавляемого паттерна + заполняет визуальные кладки его
	 * содержимым.
	 */
	@FXML
	private void handleAddSelectedPatternButton()
	{
		// Получаем IRI выбранного паттерна(ПП) из списка
		IRI chosenPatternIRI = this.suitablePatternIRIs.getSelectionModel().getSelectedItem();
		LOGGER.info("Chosen pattern for adding: {} ", chosenPatternIRI);

		// Создаем паттерн ...
		// TODO надо создавать с помощью абстрактной фабрики в АбстрактномПаттерне
		//AbstractPatternBulider.getBuilder(synthesizedPattern, prevBuilder);
		AbstractSyntCDP syntPatt = new SyntSitbasedCDP();

		// Создаем билдер (ПБ) для паттерна
		AbstractPatternConfigurator newBuilder = AbstractPatternConfigurator
				.getBuilder(syntPatt.getIRI(), this.mainApp.getMainBuilder());
		LOGGER.info(".. Configurator for adding pattern: {} ", newBuilder.getClass());
		
		// Заполняем инфой билдера интерфейс
		this.showBuilder(newBuilder);
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

	public ListView<IRI> getSuitablePatternIRIs()
	{
		return suitablePatternIRIs;
	}

	public void setSuitablePatternIRIs(ListView<IRI> suitablePatternIRIs)
	{
		this.suitablePatternIRIs = suitablePatternIRIs;
	}

}
