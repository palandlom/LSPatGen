package ontExt.view;

import java.io.File;
import java.util.ArrayList;

import org.semanticweb.owlapi.model.IRI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import ontExt.MainApp;
import ontExt.lsplGenerator.ontElements.BranchImpl;
import ontExt.lsplGenerator.ontElements.BranchExtractor;
import ru.iimm.ontology.ontAPI.OWLOntologyAdapter;
import ru.iimm.ontology.ontAPI.Ontology;

public class ChooserAxController
{

    private static final Logger LOGGER = LoggerFactory.getLogger(ChooserAxController.class);

    private MainApp MainApp;

    // Таблица AllAxioms
    /**
     * Таблица всех веток в паттерне
     */
    @FXML
    private TableView<BranchImpl> allBranchesTable;

    /**
     * Колонка AllAxioms - содержимое Branch
     */
    @FXML
    private TableColumn<BranchImpl, String> allBranchesColumn;

    /**
     * Выбранные ветки
     */
    @FXML
    private TableView<BranchImpl> chosenBranTable;

    /**
     * Колонка содержимого выбранных веток Branch
     */
    @FXML
    private TableColumn<BranchImpl, String> chosenBranColumn;

    @FXML
    private Button nextButton;

    /**
     * Содержимое таблицы AllAxioms
     */
    private ObservableList<BranchImpl> branches;

    /**
     * Содержимое таблицы ChosenAxioms
     */
    private ObservableList<BranchImpl> chosenBranches;

    
    

    @FXML
    private void initialize()
    {
	/* Определяем обозреваемые источники данных для таблиц... */
	this.branches = FXCollections.observableList(new ArrayList<BranchImpl>());
	this.chosenBranches = FXCollections.observableList(new ArrayList<BranchImpl>());

	/* Включаем множественный выбор в таблицах... */
	this.allBranchesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	this.chosenBranTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

	// Определяем набор данных таблицы..
	this.allBranchesTable.setItems(this.branches);
	this.chosenBranTable.setItems(this.chosenBranches);

	// Определяем источник данных для колонки...
	this.allBranchesColumn
		.setCellValueFactory((CellDataFeatures<BranchImpl, String> listItem) -> new ReadOnlyObjectWrapper<String>(
			listItem.getValue().toString()));

	this.chosenBranColumn
		.setCellValueFactory((CellDataFeatures<BranchImpl, String> listItem) -> new ReadOnlyObjectWrapper<String>(
			listItem.getValue().toString()));

	// Кнопка next нективна пока аксиом не выбрано
	this.nextButton.disableProperty().bind(Bindings.isEmpty(this.chosenBranches));

	// Вешаем события - по двойным кликам перемещать аксиомы
	this.chosenBranTable.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
	{
	    if (e.getClickCount() == 2)
		this.handleRemoveBranch();
	});

	this.allBranchesTable.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
	{
	    if (e.getClickCount() == 2)
		this.handleAddBranch();
	});

    }

    public void loadPatternBraches(File CDPfile)
    {
	/* Загружаем паттерн.. */
	//Ontology PatternOnt = new Ontology(CDPfile, true, true);
	Ontology PatternOnt = new OWLOntologyAdapter();
	PatternOnt.init();
	IRI iri = IRI.create(CDPfile);
	String dir =CDPfile.getParent();
	PatternOnt.loadContent(IRI.create(CDPfile));
		
	
	/* Получаем ветви из онтологии... */
	this.branches.clear();
	this.branches.addAll(FXCollections.observableList(BranchExtractor.getBranches(PatternOnt)));

	// this.allBranchesTable.setPrefWidth(1000);
	// this.allBranchesColumn.setPrefWidth(900);

    }

    /* ====== HAndlers =============== */
    /**
     * Перемещает ветки в список выбранных.
     */
    @FXML
    private void handleAddBranch()
    {

	ObservableList<BranchImpl> chBranchs = this.allBranchesTable.getSelectionModel().getSelectedItems();
	this.chosenBranches.addAll(chBranchs);
	this.branches.removeAll(chBranchs);

    }

    /**
     * Перемещает из списка выбранных обратно.
     */
    @FXML
    private void handleRemoveBranch()
    {
	ObservableList<BranchImpl> chBranchs = this.chosenBranTable.getSelectionModel().getSelectedItems();
	this.branches.addAll(chBranchs);
	this.chosenBranches.removeAll(chBranchs);
    }

    /**
     * Переключает view на следующий.
     */
    @FXML
    private void handleNext()
    {
	/* Передаем список выбранных веток в след. контроллер.. */
	//this.getMainApp().getModBranchController().setChosenBranches(chosenBranches);
	this.getMainApp().getModBranchController().addChosenBranches(chosenBranches);

	/* Меняем центральный лаяут... */
	this.getMainApp().setCenterPane(this.getMainApp().getModBranchView());

	/* Подгоняет окно под размер лаяута... */
	// this.getMainApp().getPrimaryStage().sizeToScene();

	/* Размеры рута к размеру следующего лаяута.. */
	this.getMainApp().bindRootLayaoutMinSizeWith(this.getMainApp().getModBranchView());
	
	/*Обновляем ссылку на предыдущий view*/
	this.getMainApp().setPrevPane(this.getMainApp().getChooserAxView());
	
	this.getMainApp().getRootController().setMessage("Choose and update all branches.");


    }

    public ObservableList<BranchImpl> getChosenBranches()
    {
	return chosenBranches;
    }

    public void setChosenBranches(ObservableList<BranchImpl> chosenBranches)
    {
	this.chosenBranches = chosenBranches;

    }

    public MainApp getMainApp()
    {
	return MainApp;
    }

    public void setMainApp(MainApp mainApp)
    {
	MainApp = mainApp;
    }

}
