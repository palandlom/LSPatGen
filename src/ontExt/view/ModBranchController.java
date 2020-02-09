package ontExt.view;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.glass.ui.Application;
import com.sun.org.apache.bcel.internal.generic.CPInstruction;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import ontExt.MainApp;
import ontExt.lsplGenerator.LSPLGenerator;
import ontExt.lsplGenerator.lemmatisator.Lemmatisator;
import ontExt.lsplGenerator.lemmatisator.MstLemmatisator;
import ontExt.lsplGenerator.ontElements.Branch;
import ontExt.lsplGenerator.ontElements.BranchImpl;
import ontExt.lsplGenerator.synonyms.SynonymExtractor;
import ontExt.lsplGenerator.synonyms.YandexSynonymExtractor;
import ontExt.utils.TextUtils;
import ontExt.view.obsElements.ObservableBranch;
import ontExt.view.obsElements.ObservableBranchImpl;

public class ModBranchController
{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ModBranchController.class);
	
	public static final String TEMP_LSP_FILENAME = "_temp_lsp.lspl";

	/**
	 * Выбранные ветки
	 */
	@FXML
	private TableView<ObservableBranch> chosenBranTable;

	/**
	 * Колонка содержимого выбранных веток Branch
	 */
	@FXML
	private TableColumn<ObservableBranch, String> chosenBranColumn;

	@FXML
	private Button getLSPElementsBut;

	@FXML
	private Button generateLSPatternBut;

	@FXML
	private Button nextBut;

	@FXML
	private Button updateBranchBut;

	// ==== Contex menus

	@FXML
	private ContextMenu synATableConMenu;

	@FXML
	private MenuItem synATableConMenuItem;

	@FXML
	private ContextMenu synBTableConMenu;

	@FXML
	private MenuItem synBTableConMenuItem;

	@FXML
	private ContextMenu synRelTableConMenu;

	@FXML
	private MenuItem synRelTableConMenuItem;

	// ==== Labels

	@FXML
	private Label IRIclassA;

	@FXML
	private Label IRIRelation;

	@FXML
	private Label IRIclassB;

	@FXML
	private TextField nameClassA;

	@FXML
	private TextField nameRelation;

	@FXML
	private TextField nameClassB;

	@FXML
	private ComboBox<String> aLemmaCBox;

	@FXML
	private ComboBox<String> relLemmaCBox;

	@FXML
	private ComboBox<String> bLemmaCBox;

	@FXML
	private TableView<String> synATable;

	@FXML
	private TableColumn<String, String> synAColumn;

	@FXML
	private TableView<String> synBTable;

	@FXML
	private TableColumn<String, String> synBColumn;

	@FXML
	private TableView<String> synRelTable;

	@FXML
	private TableColumn<String, String> synRelColumn;

	@FXML
	private TextArea lspPaternTArea;

	MainApp mainApp;

	BooleanBinding allBranchUpdated;
	SimpleBooleanProperty[] branchStatuses;

	/**
	 * Текущий сренерированный файл с паттернами.
	 */
	Optional<File> generatedlsplFile;
	/**
	 * Содержимое таблицы ChosenAxioms
	 */
	// private ObservableList<Branch> chosenBranches;
	private ObservableList<ObservableBranch> chosenBranches;

	private ObservableList<String> lemmasA;
	private ObservableList<String> lemmasB;
	private ObservableList<String> lemmasRel;

	private ObservableList<String> synA;
	private ObservableList<String> synB;
	private ObservableList<String> synRel;

	/**
	 * Выбранная для редактирования ветка.
	 */
	private ObservableBranch editedBranch;

	private BooleanProperty isBrachChange;

	/** Создаем поставщик синонимов... */
	private SynonymExtractor synExt;

	public ModBranchController()
	{
		super();
		this.chosenBranches = FXCollections.observableArrayList();
		this.lemmasA = FXCollections.observableArrayList();
		this.lemmasB = FXCollections.observableArrayList();
		this.lemmasRel = FXCollections.observableArrayList();
		this.synA = FXCollections.observableArrayList();
		this.synB = FXCollections.observableArrayList();
		this.synRel = FXCollections.observableArrayList();
		this.isBrachChange = new SimpleBooleanProperty();

	}

	@FXML
	private void initialize()
	{
		// == Определяем набор данных таблицы веток..
		this.chosenBranTable.setItems(this.getChosenBranches());
		// - this.setBrachesTableViewListener(lst);

		// Определяем источник данных для колонки...
		this.chosenBranColumn.setCellValueFactory(
				(CellDataFeatures<ObservableBranch, String> listItem) -> {
					return new ReadOnlyObjectWrapper<String>(
							listItem.getValue().toString());
				});

		// Привязываем изменение фона клетки
		this.setBranchTableCellFactory();

		// == Определяем набор данных для лемм комбобоксов...
		this.aLemmaCBox.setItems(this.lemmasA);
		this.bLemmaCBox.setItems(this.lemmasB);
		this.relLemmaCBox.setItems(this.lemmasRel);

		// == Определяем наборы данных для таблиц синонимов...
		this.synATable.setItems(this.synA);
		this.synBTable.setItems(this.synB);
		this.synRelTable.setItems(this.synRel);

		this.synAColumn.setCellValueFactory((
				CellDataFeatures<String, String> synonym) -> new SimpleStringProperty(
						synonym.getValue()));
		this.synBColumn.setCellValueFactory((
				CellDataFeatures<String, String> synonym) -> new SimpleStringProperty(
						synonym.getValue()));
		this.synRelColumn.setCellValueFactory((
				CellDataFeatures<String, String> synonym) -> new SimpleStringProperty(
						synonym.getValue()));

		this.synATable.getSelectionModel()
				.setSelectionMode(SelectionMode.MULTIPLE);
		this.synBTable.getSelectionModel()
				.setSelectionMode(SelectionMode.MULTIPLE);
		this.synRelTable.getSelectionModel()
				.setSelectionMode(SelectionMode.MULTIPLE);

		// === Отключаем контекстные меню таблиц синонимов
		// === когда ничего не выбрано...
		this.synATableConMenuItem.disableProperty().bind(Bindings
				.isEmpty(synATable.getSelectionModel().getSelectedItems()));
		this.synBTableConMenuItem.disableProperty().bind(Bindings
				.isEmpty(synBTable.getSelectionModel().getSelectedItems()));
		this.synRelTableConMenuItem.disableProperty().bind(Bindings
				.isEmpty(synRelTable.getSelectionModel().getSelectedItems()));

		// === Вешаем на строки таблиц редактирование по double click
		this.synAColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		this.synBColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		this.synRelColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		// Привязка кнопки Update к свойству this.isBrachChange
		this.isBrachChange.setValue(false);
		this.updateBranchBut.disableProperty().bind(this.isBrachChange.not());
		this.setBrachChangeListener();

		/* Создаем и тестим поставщик синонимов... */
		this.synExt = new YandexSynonymExtractor();
		this.synExt.init();

		this.allBranchUpdated = Bindings.createBooleanBinding(
				() -> this.chosenBranches.stream()
						.allMatch(br -> br.getUpdateStatus()),
				this.isBrachChange);

		this.generateLSPatternBut.disableProperty()
				.bind(this.allBranchUpdated.not());
		this.nextBut.disableProperty()
				.bind(this.generateLSPatternBut.disableProperty());
	}

	/**
	 * Меняет внешний вид ячейки в зависимости от Brach.isUpdated поля
	 */
	private void setBranchTableCellFactory()
	{
		this.chosenBranColumn.setCellFactory(
				col -> new TableCell<ObservableBranch, String>() {
					@Override
					protected void updateItem(String item, boolean empty)
					{
						super.updateItem(item, empty);
						if (item == null || empty)
						{
							setText(null);
							setGraphic(null);
						} else
						{
							if (item.startsWith("!"))
							{
								// this.setStyle("-fx-background-color:
								// yellow");
								this.setStyle("-fx-font-weight: normal;");
								this.setText(item.substring(1));

							} else
							{
								this.setText(item);
								this.setStyle("-fx-font-weight: bold;");
								// this.setStyle("-fx-background-color: grey");
							}
						}

					};
				});

	}

	/**
	 * При изменении списков лемм и синонимов указывает, что
	 * {@linkplain BranchImpl} нужно обновить.
	 */
	private void setBrachChangeListener()
	{
		this.aLemmaCBox.valueProperty().addListener(change -> {
			this.isBrachChange.setValue(true);
			this.getLSPElementsBut.setDisable(false);
		});
		this.bLemmaCBox.valueProperty().addListener(change -> {
			this.isBrachChange.setValue(true);
			this.getLSPElementsBut.setDisable(false);
		});
		this.relLemmaCBox.valueProperty().addListener(change -> {
			this.isBrachChange.setValue(true);
			this.getLSPElementsBut.setDisable(false);
		});
		this.synA.addListener(
				(Change<? extends String> change) -> this.isBrachChange
						.setValue(true));
		this.synB.addListener(
				(Change<? extends String> change) -> this.isBrachChange
						.setValue(true));
		this.synRel.addListener(
				(Change<? extends String> change) -> this.isBrachChange
						.setValue(true));
	}

	/**
	 * Обрабатывает редактирование ячеек таблиц синонимов.
	 * 
	 * @param editEvent
	 */
	@FXML
	private void handleEditCommit(CellEditEvent<String, String> editEvent)
	{
		int rowIndex = editEvent.getTablePosition().getRow();
		String nVal = editEvent.getNewValue();

		Lemmatisator lem = new MstLemmatisator();
		lem.init();

		// Если значение пустое - оно удаляется
		if (nVal.trim().length() > 0)
		{// Нужно еще подумать над нормализацией правки
			// -List<String> lemmas = lem.getLemma(Text.getBiggestWord(nVal, "
			// "))
			// ;
			// -nVal = (lemmas.size()==1) ? lemmas.get(0) : nVal ;
			editEvent.getTableView().getItems().set(rowIndex, nVal);
		} else
			editEvent.getTableView().getItems().remove(rowIndex);

	}

	@FXML
	private void handleUpdateBranch()
	{

		this.updateBranch(this.editedBranch);
		this.isBrachChange.set(false);
		LOGGER.info("Are all brach updated?: {}",
				this.allBranchUpdated.getValue());
		LOGGER.info("Stream:{}", this.chosenBranches.stream()
				.allMatch(br -> br.getUpdateStatus()));

		int sss = this.chosenBranches.stream()
				.map(br -> br.getUpdateStatusProperty())
				.toArray(size -> new SimpleBooleanProperty[size]).length;

		LOGGER.info("Size statuses {}", sss);

		// Arrays.stream(this.branchStatuses).forEach(s -> LOGGER.info("
		// {}",s.getValue()));

	}

	/**
	 * Обрабатывает генерацию паттерна
	 */
	@FXML
	private void handleGenerateLSPattern()
	{
		/* Генерим паттерны... */
		LSPLGenerator generator = new LSPLGenerator("patternName");

		/* Получаем ЛС-паттерны из веток... */
		List<ObservableBranch> branches = new ArrayList<>(
				this.getChosenBranches());
		List<String> patterns = generator.getLSPLrules(branches);

		Optional<File> tempLspFile = this.savePatternsToTempFile(patterns);
		this.setGeneratedlsplFile(tempLspFile);

		/* Пишем паттерны в на форму.. */
		lspPaternTArea.clear();
		patterns.stream()
				.forEach(pattern -> lspPaternTArea.appendText(pattern + "\n"));

		if (this.getGeneratedlsplFile().isPresent())
			this.getMainApp().getRootController()
					.setMessage(String.format("Patterns is saved to [%s] ",
							this.getGeneratedlsplFile().get().getPath()));
		else
			this.getMainApp().getRootController()
					.setMessage(String.format("Can't save patterns to [%s] ",
							this.getGeneratedlsplFile().get().getPath()));
	}

	/**
	 * Загружает паттерна из файла.
	 * 
	 * @param cdpFile
	 * @param encoding
	 * @return
	 */
	public List<String> loadPatterns(File cdpFile, String encoding)
	{
		List<String> patterns = null;
		try
		{
			patterns = Files.lines(cdpFile.toPath(), Charset.forName(encoding))
					.collect(Collectors.toList());
		} catch (IOException e)
		{
			LOGGER.error("Can't read pattern file [{}]", cdpFile);
			e.printStackTrace();
		}
		return patterns != null ? patterns : Collections.emptyList();
	}

	/**
	 * Сохранает паттерн в файл.
	 * 
	 * @param patterns
	 * @return
	 */
	public static Optional<File> savePatternsToTempFile(List<String> patterns)
	{

		/* Создаем будущий LSPL файл... */
//			lsplFilename = FilenameUtils.removeExtension(cdpFile.getName());
		
		/* .. определяем имя файла... */
		File lsplFile = new File(TEMP_LSP_FILENAME);

		return savePatternsTo(lsplFile, "CP1251", patterns);
	}

	/**
	 * @param lsplFile
	 * @param encoding
	 * @param patterns
	 * @return
	 */
	public static Optional<File> savePatternsTo(File lsplFile, String encoding,
			List<String> patterns)
	{
		String lsplFilename = null;

		try
		{
			/* ... пишем в файл... */
			LOGGER.info("Save LSPL-patterns to file: {}", lsplFilename);
			LOGGER.info("=Patterns========================================");

			PrintWriter writer = new PrintWriter(lsplFile, encoding);
			for (String pattern : patterns)
			{
				LOGGER.info(" {}", pattern);
				writer.println(pattern);
			}
			writer.close();
		} catch (Exception e)
		{
			LOGGER.error("Can't save file [{}]", lsplFile.getPath());
			e.printStackTrace();
			return Optional.empty();
		}

		return Optional.of(lsplFile);
	}

	/**
	 * Обрабатывает нажатие кнопки Next
	 */
	@FXML
	private void handleNext()
	{
		/* Меняем центральный лаяут... */
		this.getMainApp()
				.setCenterPane(this.getMainApp().getTextProcessingView());

		/* Подгоняет окно под размер лаяута... */
		// this.getMainApp().getPrimaryStage().sizeToScene();

		/* Размеры рута к размеру следующего лаяута.. */
		this.getMainApp().bindRootLayaoutMinSizeWith(
				this.getMainApp().getTextProcessingView());

		/* Обновляем ссылку на предыдущий view */
		this.getMainApp().setPrevPane(this.getMainApp().getModBranchView());

		this.getMainApp().getRootController()
				.setMessage("Choose text file and target for processing.");

		/* Загружаем паттерн на лаяут обработки текста. */
		if (this.getGeneratedlsplFile().isPresent())
			this.getMainApp().getTextProcessingController().getLsplPatternArea()
					.setText(this
							.loadPatterns(this.getGeneratedlsplFile().get(),
									"CP1251")
							.stream().reduce((a, b) -> a + "\n" + b)
							.orElse(""));
	}

	/**
	 * Размещает выбранные ветви в списке для редактирования.
	 * 
	 * @param branches
	 */
	public void addChosenBranches(ObservableList<BranchImpl> branches)
	{
		this.chosenBranches.clear();
		// - this.chosenBranches.addAll(chosenBranches);
		branches.stream().forEach(
				br -> this.chosenBranches.add(new ObservableBranchImpl(br)));

		/*
		 * this.updatedBrachMap.clear(); branches.stream().forEach(br ->
		 * this.updatedBrachMap.put(br.toString(), false));
		 */
	}

	/**
	 * Перечитывает и отображает данные отображаемого view.
	 * 
	 * @deprecated
	 */
	public void refresh()
	{

	}

	/**
	 * Обнуляет данные отображаемого view.
	 */
	public void reset()
	{

	}

	/**
	 * Переключает view на следующий.
	 */
	@FXML
	private void handleBack()
	{
		/* Передаем список выбранных веток в след. контроллер.. */
		// this.getMainApp().getModBranchController().setChosenBranches(chosenBranches);

		/* Меняем центральный лаяут... */
		this.getMainApp().setCenterPane(this.getMainApp().getChooserAxView());

		/* Подгоняет окно под размер лаяута... */
		this.getMainApp().getPrimaryStage().sizeToScene();

		this.getMainApp().bindRootLayaoutMinSizeWith(
				this.getMainApp().getChooserAxView());

		/* Обновляем предыдущий view */
		this.getMainApp().setPrevPane(this.getMainApp().getModBranchView());

	}

	@FXML
	private void handleGetLemmas()
	{

		// LOGGER.info("Size " + this.chosenBranTable.getItems().size());

		// this.getChosenBranches().keySet()
		// this.chosenBranTable.getItems().stream().forEach(b -> LOGGER.info("
		// Branch:"+b.toString()));

		// this.getChosenBranches().keySet().stream().forEach(b -> LOGGER.info("
		// Chosen Branch:"+b.toString()));

		// this.chosenBranTable.setItems(FXCollections.observableArrayList(this.getChosenBranches().keySet()));

		this.setLemmas();
		this.getLSPElementsBut.setDisable(false);

	}

	@FXML
	private void handleRowDoubleclick()
	{

	}

	/**
	 * 
	 */
	@FXML
	private void handleBrachClick()
	{

		/* Берем выбранную ветку.. */
		this.editedBranch = this.chosenBranTable.getSelectionModel()
				.getSelectedItem();

		if (this.editedBranch != null)
			this.loadBranch(this.editedBranch);
	}

	/**
	 * Помещает выбранную ветвь в поля для редактирования.
	 * 
	 * @param br
	 */
	private void loadBranch(Branch br)
	{
		this.cleanLemmas();
		this.cleanSynonyms();

		// Пишем IRI онтологических элементов
		this.IRIclassA.setText(br.getLConcept().getShortIRI());
		this.IRIRelation.setText(br.getRelation().getShortIRI());
		this.IRIclassB.setText(br.getRConcept().getShortIRI());

		// В названия пишем лейблы онтологических элементов ветки...
		this.nameClassA.setText(br.getLConcept().getLabel());
		this.nameClassB.setText(br.getRConcept().getLabel());
		this.nameRelation.setText(br.getRelation().getLabel());

		// В леммы пишем корректированные лейблы элементов ветки...
		if (br.getLConcept().getLabel() != null)
			this.lemmasA.add(
					TextUtils.getBiggestWord(br.getLConcept().getLabel(), " "));

		if (br.getRConcept().getLabel() != null)
			this.lemmasB.add(
					TextUtils.getBiggestWord(br.getRConcept().getLabel(), " "));

		if (br.getRelation().getLabel() != null)
			this.lemmasRel.add(
					TextUtils.getBiggestWord(br.getRelation().getLabel(), " "));

		/* В леммовых комбобоксах помещаем первые леммы... */
		this.aLemmaCBox.setValue(
				this.lemmasA.isEmpty() ? "-empty-" : this.lemmasA.get(0));
		this.bLemmaCBox.setValue(
				this.lemmasB.isEmpty() ? "-empty-" : this.lemmasB.get(0));
		this.relLemmaCBox.setValue(
				this.lemmasRel.isEmpty() ? "-empty-" : this.lemmasRel.get(0));

		// В таблицы LSэлементов помещаем список синонимов из ветки или леммы...
		if (br.getLConcept().getSynonyms() != null
				&& !br.getLConcept().getSynonyms().isEmpty())
			this.synA.addAll(br.getLConcept().getSynonyms());
		else
			this.synA.add(this.aLemmaCBox.getValue());

		if (br.getRConcept().getSynonyms() != null
				&& !br.getRConcept().getSynonyms().isEmpty())
			this.synB.addAll(br.getRConcept().getSynonyms());
		else
			this.synB.add(this.bLemmaCBox.getValue());

		if (br.getRelation().getSynonyms() != null
				&& !br.getRelation().getSynonyms().isEmpty())
			this.synRel.addAll(br.getRelation().getSynonyms());
		else
			this.synRel.add(this.relLemmaCBox.getValue());

		/* Сообщение в статус бар.. */
		this.getMainApp().getRootController().setMessage("Branch "
				+ br.toString()
				+ " Correct labels, generate lemmas and LSP elements, then update the branch.");
	}

	/**
	 * Обновляет переданную ветку данными из формы.
	 * 
	 * @param branch
	 */
	private void updateBranch(ObservableBranch branch)
	{

		if (branch != null)
		{
			LOGGER.info("Update branch: {}", branch);
			/* === Обновляем имена концептов из лемм-боксов... */
			if (this.aLemmaCBox.getValue().isEmpty())
				branch.getLConcept().setLabel(this.aLemmaCBox.getValue());
			if (this.bLemmaCBox.getValue().isEmpty())
				branch.getRConcept().setLabel(this.bLemmaCBox.getValue());
			if (this.relLemmaCBox.getValue().isEmpty())
				branch.getRelation().setLabel(this.relLemmaCBox.getValue());

			/* === Обновляем списки синонимов у редактируемой ветки... */
			branch.getLConcept().getSynonyms().clear();
			branch.getRConcept().getSynonyms().clear();
			branch.getRelation().getSynonyms().clear();
			branch.getLConcept().getSynonyms().addAll(this.synA);
			branch.getRConcept().getSynonyms().addAll(this.synB);
			branch.getRelation().getSynonyms().addAll(this.synRel);

			/* Маркируем векту, как измененную в карте */
			branch.markAsUpdated();

			/* Обновляем таблицу "костылем" ... */
			chosenBranTable.refresh();
			chosenBranTable.requestFocus();

			// chosenBranTable.getColumns().clear();
			// chosenBranTable.getColumns().add(chosenBranColumn);

			// this.updatedBrachMap.put(branch.toString(), true);
			// this.chosenBranches.put(branch, true);

			LOGGER.info("Result branch: {}", branch);

		}
	}

	private void setLemmas()
	{
		Lemmatisator lem = new MstLemmatisator();
		lem.init();

		this.cleanLemmas();

		/* Берем наибольшее слово из названий и получаем его леммы... */
		this.lemmasA.addAll(lem.getLemma(
				TextUtils.getBiggestWord(this.nameClassA.getText(), " ")));
		this.lemmasB.addAll(lem.getLemma(
				TextUtils.getBiggestWord(this.nameClassB.getText(), " ")));
		this.lemmasRel.addAll(lem.getLemma(
				TextUtils.getBiggestWord(this.nameRelation.getText(), " ")));

		/* В леммовых комбобоксах помещаем первые леммы... */
		this.aLemmaCBox
				.setValue(this.lemmasA.isEmpty() ? "" : this.lemmasA.get(0));
		this.bLemmaCBox
				.setValue(this.lemmasB.isEmpty() ? "" : this.lemmasB.get(0));
		this.relLemmaCBox.setValue(
				this.lemmasRel.isEmpty() ? "" : this.lemmasRel.get(0));
	}

	/**
	 * Копирует содержимое леммовых комбо-боксов в таблицы элементов паттерна
	 * 
	 * @deprecated непригодился
	 */
	private void setLemmasToLSTables()
	{
		this.cleanSynonyms();
	}

	/**
	 * Обработчик взятия синонимов.
	 */
	@FXML
	private void handleGetLSPelements()
	{
		// this.chosenBranTable.setDisable(true);
		// this.setLemmas();
		this.cleanSynonyms();

		/* LSPL-элементы/синонимы на форму... */
		this.setLSPElements(this.synA, this.aLemmaCBox.getValue(),
				this.getSynExt());
		this.setLSPElements(this.synB, this.bLemmaCBox.getValue(),
				this.getSynExt());
		this.setLSPElements(this.synRel, this.relLemmaCBox.getValue(),
				this.getSynExt());

		/* Сообщение в статус бар */
		String message = "Class A [" + this.aLemmaCBox.getValue() + "] has "
				+ this.synA.size() + " synonyms // " + "Class B ["
				+ this.bLemmaCBox.getValue() + "] has " + this.synB.size()
				+ " synonyms // " + "Relation [" + this.relLemmaCBox.getValue()
				+ "] has " + this.synRel.size() + " synonyms";
		this.getMainApp().setStatusMessage(message);

		/* Указываем, что ветка изменилась */
		this.isBrachChange.setValue(true);
		this.getLSPElementsBut.setDisable(true);
	}

	@FXML
	private void handleLemmasCBoxValueChange()
	{

	}

	@FXML
	private void handleDeleteAConMenuItem()
	{
		this.deleteSelectedTableItems(this.synATable);

	}

	@FXML
	private void handleDeleteBConMenuItem()
	{
		this.deleteSelectedTableItems(this.synBTable);

	}

	@FXML
	private void handleDeleteRelConMenuItem()
	{
		this.deleteSelectedTableItems(this.synRelTable);

	}

	/**
	 * Удаляет из {@linkplain TableView} выбранные строки.
	 * 
	 * @param tableView
	 */
	private <S> void deleteSelectedTableItems(TableView<S> tableView)
	{
		tableView.getItems()
				.removeAll(tableView.getSelectionModel().getSelectedItems());
	}

	/**
	 * Заносит синонимы (LS-elements) в таблицы на форме.
	 */

	private int setLSPElements(ObservableList<String> lsplElementsList,
			String lemma, SynonymExtractor synExt)
	{
		/* Макс кол-во синонимов */
		/*
		 * Если синонимов много - то получаются длинные правила.. для отношений
		 * > 900 знаков, то парсер их не съедает
		 */
		int maxSynQuality = 7;

		List<String> synonyms;

		if (lemma != null && lemma.length() > 0)
		{
			synonyms = FXCollections.observableArrayList(
					SynonymExtractor.getSingledSynonym(synExt.getSynonyms(lemma,
							maxSynQuality, true, true)));
			synonyms.stream().distinct().forEach(s -> lsplElementsList.add(s));
		}

		return lsplElementsList.size();
	}

	/**
	 * Очищает таблицы синонимов на форме.
	 */
	private void cleanSynonyms()
	{
		this.synA.clear();
		this.synB.clear();
		this.synRel.clear();
	}

	/**
	 * Очищает связанные с комбо-боксами списки.
	 */
	private void cleanLemmas()
	{
		this.lemmasA.clear();
		this.lemmasB.clear();
		this.lemmasRel.clear();

		/* В леммовых комбобоксах помещаем первые леммы... */
		this.aLemmaCBox.setValue("");
		this.bLemmaCBox.setValue("");
		this.relLemmaCBox.setValue("");

	}

	public MainApp getMainApp()
	{
		return mainApp;
	}

	public void setMainApp(MainApp mainApp)
	{
		this.mainApp = mainApp;
	}

	public Branch getEditedBranch()
	{
		return editedBranch;
	}

	/**
	 * Возвращает генератор синонимов.
	 * 
	 * @return
	 */
	public SynonymExtractor getSynExt()
	{
		return synExt;
	}

	public ObservableList<ObservableBranch> getChosenBranches()
	{
		return chosenBranches;
	}

	public void setChosenBranches(
			ObservableList<ObservableBranch> chosenBranches)
	{
		this.chosenBranches = chosenBranches;
	}

	public Optional<File> getGeneratedlsplFile()
	{
		return generatedlsplFile;
	}

	public void setGeneratedlsplFile(Optional<File> generatedlsplFile)
	{
		this.generatedlsplFile = generatedlsplFile;
	}

}
