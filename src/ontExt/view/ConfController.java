package ontExt.view;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import ontExt.MainApp;
import util.simpleconf.Configuration;

/**
 * Контроллер изменения конфигурации <br>
 * Для использования нужно подправить: <br>
 * <ul>
 * <li>initConfiguration()
 * <li>handleBackButton()
 * </ul>
 * 
 * @author lomov
 * 
 *
 */
public class ConfController
{

    /**
     * Конфигурация приложения.
     */
    private ObservableMap<String, String> propMap;

    /**
     * Конфигурационный файл.
     */
    private File propertiesFile;

    private MainApp mainApp;

    /* Визуальные элементы..... */
    @FXML
    TableView<Entry<String, String>> confTable;

    @FXML
    TableColumn<Entry<String, String>, String> propertyCol;
    @FXML
    TableColumn<Entry<String, String>, String> valueCol;

    @FXML
    Button saveButton;

    @FXML
    Button backButton;

    
    
    // Таблица AllAxioms
    @FXML
    private void initialize()
    {
	this.initConfiguration();

	/* Определяем обозреваемые источники данных для таблиц... */
	/* ... переделываем map в список тк таблица с мэпом не работает.. */
	ObservableList<Entry<String, String>> entrySet = FXCollections.observableArrayList(this.propMap.entrySet());
	this.confTable.setItems(entrySet);

	/* Включаем множественный выбор в таблицах... */
	// this.allBranchesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

	// Определяем набор данных таблицы..
	// this.allBranchesTable.setItems(this.branches);

	// Определяем источник данных для колонки...
	this.propertyCol.setCellValueFactory(
		(CellDataFeatures<Map.Entry<String, String>, String> entry) -> new SimpleStringProperty(
			entry.getValue().getKey()));

	this.valueCol.setCellValueFactory(
		(CellDataFeatures<Map.Entry<String, String>, String> entry) -> new SimpleStringProperty(
			entry.getValue().getValue()));

	// Делает редактируемые ячейки...
	this.valueCol.setCellFactory(TextFieldTableCell.forTableColumn());

	// включаем кнопку save при изменении спика свойств
	confTable.getItems().addListener((ListChangeListener.Change<? extends Map.Entry<String, String>> change) ->
	{
	    this.saveButton.disableProperty().set(false);
	    System.out.println("List is changed");

	});
    }

    /**
     * Загружает кон-ю из файла с пом-ю методов библиотеки simpleConf. Должен
     * инициализировать поле this.propMap.
     * 
     */
    private void initConfiguration()
    {
	/* Загружаем свойства из файла... */
	/* ... получаем файл */
	this.propertiesFile = Configuration.getClassPathFile(Configuration.DEF_PROP_FILENAME);
	// this.propertiesFile =
	// Configuration.getJARPathFile(Configuration.DEF_PROP_FILENAME);

	/* ... загружаем файл */
	Properties properties = Configuration.loadClasspathProperties("config.properties");
	Configuration.printProperties(properties);

	/* Получаем карту свойств... */
	// this.propMap = FXCollections.observableMap(new HashMap<>());
	this.propMap = FXCollections.observableMap(Configuration.getProperties2Map(properties));

    }

    /* ====== HAndlers =============== */

    /**
     * Обрабатывает редактирование ячейки колонки value
     * 
     * @param editEvent
     */
    @FXML
    private void handleValueCellEdit(CellEditEvent<Map.Entry<String, String>, String> editEvent)
    {
	// Получаем номер редактируемой строки + новое значение
	int rowIndex = editEvent.getTablePosition().getRow();
	String nVal = editEvent.getNewValue();

	// Пишем в него новое содержание из event
	// editEvent.getTableView().getItems().set(rowIndex, nVal);
	editEvent.getTableView().getItems().get(rowIndex).setValue(nVal);

	////
	// System.out.println("Map========");
	// Configuration.printProperties(this.propMap);

	/* активируем кнопку записи свойств */
	this.saveButton.disableProperty().set(false);
	// this.propMap.entrySet().stream().forEach(entry ->
	// System.out.println(entry.toString()));

	// System.out.println("Displayed list========");
	// this.propMap.entrySet().stream().forEach(entry ->
	// System.out.println(entry.toString()));

    }

    @FXML
    private void handleBackButton()
    {
	/* Меняем центральный лаяут... */
	this.getMainApp().getRootView().setCenter(this.getMainApp().getPrevPane());
    }

    @FXML
    private void handleSaveButton()
    {
	// Configuration.saveProperties(propertiesFile, propMap);
	this.saveButton.disableProperty().set(true);

	// Если есть русские символы то надо пользовать этот метод
	Configuration.saveProperties(propertiesFile, Configuration.getMap2Properties(propMap));

    }

    public ObservableMap<String, String> getPropMap()
    {
	return propMap;
    }

    public void setPropMap(ObservableMap<String, String> propMap)
    {
	this.propMap = propMap;
    }

    public MainApp getMainApp()
    {
	return mainApp;
    }

    public void setMainApp(MainApp mainApp)
    {
	this.mainApp = mainApp;
    }

}
