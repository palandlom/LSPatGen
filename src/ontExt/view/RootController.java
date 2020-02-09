package ontExt.view;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import ontExt.MainApp;

public class RootController
{
    private MainApp mainApp;

    /**
     * Файл с паттерном содержания.
     */
    private File cdpFile;

    @FXML
    private Label mesLabel;

    @FXML
    private void initialize()
    {
    }

   

    /* =======Handlers========= */
    /** == Opens an about dialog. */
    @FXML
    private void handleAbout()
    {
	/*
	 * Alert alert = new Alert(AlertType.INFORMATION); alert.setTitle(
	 * "О программе"); alert.setHeaderText("Information Alert");
	 * 
	 * String s = "This is an example of JavaFX 8 Dialogs... ";
	 * alert.setContentText(s); alert.show();
	 */
    }

    /* === Closes the application. */
    @FXML
    private void handleExit()
    {

	System.exit(0);
    }

    /**
     * Обработчик открытия файла.
     */
    @FXML
    private void handleLoadPattern()
    {
	FileChooser fileChooser = new FileChooser();

	// Set extension filter
	FileChooser.ExtensionFilter owlFilter = new FileChooser.ExtensionFilter("OWL files (*.owl)", "*.owl");
	FileChooser.ExtensionFilter rdfFilter = new FileChooser.ExtensionFilter("RDF files (*.rdf)", "*.rdf");

	fileChooser.getExtensionFilters().addAll(owlFilter, rdfFilter);
	fileChooser.setInitialDirectory(new File("./"));

	File file = fileChooser.showOpenDialog(this.mainApp.getPrimaryStage());

	if (file != null && file.exists())
	{
	    this.setCdpFile(file);
	    this.mainApp.getChooserAxController().loadPatternBraches(file);
	    this.setMessage("File has been loaded: " + file.getName());
	}

    }

    /**
     * Вызывает конфигурацию приложения из пункта меню.
     */
    @FXML
    private void handleConfigurationMenuItem()
    {
	/* Меняем центральный лаяут... */
	this.mainApp.setCenterPane(this.mainApp.getConfView());

    }

    public void setSizeBinding()
    {

    }

    /**
     * Размещает сообщение в статусбаре.
     * 
     * @param mes
     */
    public void setMessage(String mes)
    {
	this.mesLabel.setText(mes);
    }

    /* ========GET/SETesers=============================== */

    public void setMainApp(MainApp mainApp)
    {
	this.mainApp = mainApp;
    }

    public File getCdpFile()
    {
	return cdpFile;
    }

    public void setCdpFile(File cdpFile)
    {
	this.cdpFile = cdpFile;
    }

}
