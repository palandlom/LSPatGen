package ontExt.view;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import ontExt.MainApp;
import ontExt.lsplProcessor.LsplManager;
import ontExt.lsplProcessor.XMLElements.Goal;
import ontExt.lsplProcessor.XMLElements.Match;
import ontExt.utils.Utils;

public class TextProcessingController
{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TextProcessingController.class);

	MainApp mainApp;

	public static final String EXAMPLE_TEXT_FILE = "/text.txt";
	public static final String DEFAULT_RESULT_FILENAME = "result.txt";

	@FXML
	private TextArea fragmentArea;

	@FXML
	private TextArea extendedFragmentArea;

	@FXML
	private TextArea lsplPatternArea;

	@FXML
	private ListView<Goal> brachesListView;

	@FXML
	private ListView<Match> matchesListView;
	// private TreeView<String> matchesTree;

	@FXML
	private Button savePatternBut;
	@FXML
	private Button chooseFileBut;
	@FXML
	private Button setResultFileBut;
	
	@FXML
	private Button performProcessingBut;

	@FXML
	private Button backBut;
	@FXML
	private Button loadPatternBut;

	@FXML
	private TextField textFileField;
	@FXML
	private TextField resultFileField;
	@FXML
	private TextField targetField;


	private ObservableList<String> targets;
	private ObservableList<Match> matchList;
	private ObservableList<Goal> branchList;

	@FXML
	private void initialize()
	{

		try
		{
			File f = new File(
					MainApp.class.getResource(EXAMPLE_TEXT_FILE).toURI());
			this.textFileField.setText(f.getAbsolutePath());
			this.updateResultFileFieldWith(f);

		} catch (URISyntaxException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * this.lsplPatternArea.addEventHandler(Event.ANY, e -> {
		 * System.err.println(""); updateTargets(); });
		 */

		this.matchList = FXCollections.observableArrayList();
		// this.matchesList.setcell
		this.matchesListView.setItems(this.matchList);

		this.branchList = FXCollections.observableArrayList();
		this.brachesListView.setItems(this.branchList);
	}

	@FXML
	@Deprecated
	private void updateTargets()
	{
		this.targets.clear();

		String[] patterns = this.lsplPatternArea.getText().split("\n");
		for (String pattern : patterns)
		{
			if (pattern.indexOf('=') > 0)
			{
				String tmp = pattern.substring(0, pattern.indexOf('=')).trim();
				if (tmp.length() > 0)
					this.targets.add(tmp);

			}
		}
	}

	/**
	 * Get goals-targes-branches from patterns.
	 * 
	 * @return
	 */
	private String[] getTargetBranches()
	{
		List<String> targets = new ArrayList<>();

		String[] patterns = this.lsplPatternArea.getText().split("\n");
		for (String pattern : patterns)
		{
			if (pattern.startsWith("Branch") && pattern.indexOf('=') > 0)
			{
				String tmp = pattern.substring(0, pattern.indexOf('=')).trim();
				if (tmp.length() > 0)
					targets.add(tmp);
			}
		}

		String[] trg = new String[targets.size()];
		return targets.toArray(trg);
	}

	/**
	 * Переключает view.
	 */
	@FXML
	private void handleBack()
	{
		/* Передаем список выбранных веток в след. контроллер.. */
		// this.getMainApp().getModBranchController().setChosenBranches(chosenBranches);

		/* Меняем центральный лаяут... */
		this.getMainApp().setCenterPane(this.getMainApp().getModBranchView());

		/* Подгоняет окно под размер лаяута... */
		this.getMainApp().getPrimaryStage().sizeToScene();

		this.getMainApp().bindRootLayaoutMinSizeWith(
				this.getMainApp().getModBranchView());

		/* Обновляем предыдущий view */
		this.getMainApp().setPrevPane(this.getMainApp().getChooserAxView());

	}

	@FXML
	private void handleChooseTextFileButton()
	{
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter(
				"TXT files (*.txt)", "*.txt");
		// FileChooser.ExtensionFilter rdfFilter = new
		// FileChooser.ExtensionFilter("RDF files (*.rdf)", "*.rdf");

		fileChooser.getExtensionFilters().addAll(txtFilter);
		fileChooser.setInitialDirectory(new File("./"));

		File file = fileChooser.showOpenDialog(this.mainApp.getPrimaryStage());

		if (file != null && file.exists())
		{
			this.getTextFileField().setText(file.getAbsolutePath());
			// this.setMessage("File has been loaded: " + file.getName());

			this.updateResultFileFieldWith(file);
		}
	}

	private void updateResultFileFieldWith(File textFile)
	{
		this.resultFileField.setText(textFile.getParent() + File.separator
				+ DEFAULT_RESULT_FILENAME);
	}

	@FXML
	private void handleChooseResultFileButton()
	{
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter(
				"TXT files (*.txt)", "*.txt");
		// FileChooser.ExtensionFilter rdfFilter = new
		// FileChooser.ExtensionFilter("RDF files (*.rdf)", "*.rdf");

		fileChooser.getExtensionFilters().addAll(txtFilter);
		fileChooser.setInitialDirectory(new File("./"));

		File file = fileChooser.showSaveDialog(this.mainApp.getPrimaryStage());

		if (!file.exists())
		{
			this.getResultFileField().setText(file.getAbsolutePath());
			// this.setMessage("File has been loaded: " + file.getName());
		} else
		{
			this.getMainApp().getRootController().setMessage(String
					.format("File [%s] will be overwritten", file.getName()));
			/* @formatter:off
			Random rand = new Random();
			String fileName = FilenameUtils.getBaseName(file.getName());
			String fileExt = FilenameUtils.getExtension(file.getName());
			String path = FilenameUtils.getPath(file.getAbsolutePath());
			String newFile = path + fileName + rand.nextInt(100) + 1 + "."
					+ fileExt;
			 @formatter:on */
			this.getResultFileField().setText(file.getAbsolutePath());
		}
	}

	@FXML
	private void handlePerformProcessing()
	{

		Optional<File> textFile = Utils.getFile(this.textFileField.getText());
		Optional<File> resultFile = Utils
				.getFile(this.resultFileField.getText());
		Optional<File> lsplPatternFile = ModBranchController
				.savePatternsToTempFile(Collections
						.singletonList(this.lsplPatternArea.getText()));

		String[] targets = this.getTargetBranches();
		if (targets == null || targets.length == 0)
		{
			String mes = "Can't fing target branches in pattern.";
			LOGGER.error(mes);
			this.getMainApp().getRootController().setMessage(mes);
			return;
		}

		if (!textFile.isPresent())
		{
			String mes = "File [{}] not found.";
			LOGGER.error(mes, this.textFileField.getText());
			this.getMainApp().getRootController().setMessage(mes);
			return;
		}

		if (resultFile.isPresent())
		{
			resultFile.get().delete();
		}

		if (!lsplPatternFile.isPresent())
		{
			String mes = "File [{}] not found.";
			LOGGER.error(mes, ModBranchController.TEMP_LSP_FILENAME);
			this.getMainApp().getRootController().setMessage(mes);
			return;
		}

		this.getMainApp().getRootController()
				.setMessage(String.format(
						"Processing [%s] with [%d] targets, result [%s]...",
						this.textFileField.getText(), targets.length,
						this.resultFileField.getText()));
		
		// Start proccessing...
		LsplManager mng = new LsplManager(this.textFileField.getText(),
				lsplPatternFile.get().getAbsolutePath(),
				// this.resultFileField.getText(),
				// this.targetComboBox.getValue());
				this.resultFileField.getText(), targets);
		mng.init();
		mng.produceOutFile();
		File finalFile = mng.produceFinalResultFile();

		//this.updateMatchList(mng.getGoalList());
		this.updateBranchList(mng.getGoalList());

		this.getMainApp().getRootController()
				.setMessage(String.format(
						"Processing finished - results were saved to [%s]",
						finalFile.getName()));
	}

	private void updateMatchList(List<Goal> goalList)
	{
		this.matchList.clear();
		this.fragmentArea.clear();
		this.extendedFragmentArea.clear();

		if (goalList != null && !goalList.isEmpty())
		{
			// add mathes from first and one goal
			this.matchList.addAll(goalList.get(0).getMatchList());

		}
	}

	private void updateBranchList(List<Goal> goalList)
	{
		this.branchList.clear();
		this.branchList.addAll(goalList);
	}

	@FXML
	private void showCurrentBranch()
	{

		Goal selectedBranch = this.brachesListView.getSelectionModel()
				.getSelectedItem();
		if (selectedBranch != null)
		{
			this.matchList.clear();
			this.matchList.addAll(selectedBranch.getMatchList());
		}

	}

	@FXML
	private void showCurrentMatch()
	{

		Match selectedMatch = this.matchesListView.getSelectionModel()
				.getSelectedItem();
		if (selectedMatch != null)
		{
			this.fragmentArea.clear();
			this.extendedFragmentArea.clear();

			this.fragmentArea.setText(selectedMatch.getFragment().getText());
			this.extendedFragmentArea
					.setText(selectedMatch.getExtFragment().getText());

		}

	}

	/* =========== Get/Setters ========= */
	public MainApp getMainApp()
	{
		return mainApp;
	}

	public void setMainApp(MainApp mainApp)
	{
		this.mainApp = mainApp;
	}

	public TextArea getFragmentArea()
	{
		return fragmentArea;
	}

	public TextArea getExtendedFragmentArea()
	{
		return extendedFragmentArea;
	}

	public TextArea getLsplPatternArea()
	{
		return lsplPatternArea;
	}

	public Button getSavePatternBut()
	{
		return savePatternBut;
	}

	public Button getChooseFileBut()
	{
		return chooseFileBut;
	}

	public Button getSetResultFileBut()
	{
		return setResultFileBut;
	}

	

	public Button getPerformProcessingBut()
	{
		return performProcessingBut;
	}


	public Button getBackBut()
	{
		return backBut;
	}

	public Button getLoadPatternBut()
	{
		return loadPatternBut;
	}

	public TextField getTextFileField()
	{
		return textFileField;
	}

	public TextField getResultFileField()
	{
		return resultFileField;
	}

	public TextField getTargetField()
	{
		return targetField;
	}

	public void setFragmentArea(TextArea fragmentArea)
	{
		this.fragmentArea = fragmentArea;
	}

	public void setExtendedFragmentArea(TextArea extendedFragmentArea)
	{
		this.extendedFragmentArea = extendedFragmentArea;
	}

	public void setLsplPatternArea(TextArea lsplPatternArea)
	{
		this.lsplPatternArea = lsplPatternArea;
	}

	public void setSavePatternBut(Button savePatternBut)
	{
		this.savePatternBut = savePatternBut;
	}

	public void setChooseFileBut(Button chooseFileBut)
	{
		this.chooseFileBut = chooseFileBut;
	}

	public void setSetResultFileBut(Button setResultFileBut)
	{
		this.setResultFileBut = setResultFileBut;
	}



	public void setPerformProcessingBut(Button performProcessingBut)
	{
		this.performProcessingBut = performProcessingBut;
	}


	public void setBackBut(Button backBut)
	{
		this.backBut = backBut;
	}

	public void setLoadPatternBut(Button loadPatternBut)
	{
		this.loadPatternBut = loadPatternBut;
	}

	public void setTextFileField(TextField textFileField)
	{
		this.textFileField = textFileField;
	}

	public void setResultFileField(TextField resultFileField)
	{
		this.resultFileField = resultFileField;
	}

	public void setTargetField(TextField targetField)
	{
		this.targetField = targetField;
	}

}
