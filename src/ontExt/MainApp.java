package ontExt;

import java.io.IOException;
import java.util.Observable;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ontExt.view.ChooserAxController;
import ontExt.view.ConfController;
import ontExt.view.ModBranchController;
import ontExt.view.RootController;
import ontExt.view.TextProcessingController;



public class MainApp extends Application
{
    Stage primaryStage;
    BorderPane rootView;
    AnchorPane modBranchView;
    AnchorPane chooserAxView;
    AnchorPane confView;
    AnchorPane textProcessingView;
    
    /**
     * Предыдущий view т.е. который отображался до текущего.
     */
    AnchorPane prevPane;

    RootController rootController;
    ModBranchController modBranchController;
    ChooserAxController chooserAxController;
    ConfController confController;
    TextProcessingController textProcessingController;
    
    @Override
    public void start(Stage primaryStage)
    {
	try
	{
	    this.primaryStage = primaryStage;
	    this.primaryStage.setTitle("LSPatGen");
	    this.primaryStage.sizeToScene();

	    /* Загружам корневой вид и подвид.. */
	    initRootLayout();
	    initChooserAxView();
	    initModBranchView();
	    initConfView();
	    initTextProcessingView();
	    

	    Scene scene = new Scene(this.rootView);
	    this.rootView.setCenter(this.chooserAxView);
	    /* Задаем предыдущий вид - начальное значение = текущему*/
	    this.setPrevPane(this.getChooserAxView());


	    /* Связываем размеры корневого лаяута с его дочерним... */
	    this.bindRootLayaoutMinSizeWith(this.chooserAxView);
	    // this.rootView.minWidthProperty().bind(this.chooserAxView.minWidthProperty());
	    // this.rootView.minHeightProperty().bind(this.chooserAxView.minWidthProperty());

	    this.primaryStage.setScene(scene);
	    this.primaryStage.show();

	    /* Связываем размеры стейджа с размерами корневого лаяута... */
	    primaryStage.minHeightProperty().bind(this.rootView.minHeightProperty());
	    primaryStage.minWidthProperty().bind(this.rootView.minWidthProperty());
	    
	    /* Грузим иконку... */
	    this.primaryStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/icon.png")));


	} catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public static void main(String[] args)
    {
	launch(args);
    }

    public void initRootLayout()
    {
	try
	{
	    // 1 грузим файло с разметкой в лоадер
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(MainApp.class.getResource("./view/RootView.fxml"));

	    /* 2 пинаем лоадер */
	    this.rootView = (BorderPane) loader.load();

	    /* Назначаем контроллер */
	    this.rootController = loader.getController();
	    this.rootController.setMainApp(this);

	} catch (IOException e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * Связывает размеры корневого лаяута с переданным.
     * 
     * @param layout
     */
    public void bindRootLayaoutMinSizeWith(Pane layout)
    {
	this.rootView.minWidthProperty().bind(layout.minWidthProperty());
	this.rootView.minHeightProperty().bind(layout.minHeightProperty());
    }

    public void initModBranchView()
    {
	try
	{
	    // 1 грузим файло с разметкой в лоадер
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(MainApp.class.getResource("view/ModBranchView.fxml"));

	    /* 2 пинаем лоадер */
	    this.modBranchView = (AnchorPane) loader.load();

	    /* Назначаем контроллер */
	    this.modBranchController = loader.getController();
	    this.modBranchController.setMainApp(this);

	} catch (IOException e)
	{
	    e.printStackTrace();
	}
    }

    public void initChooserAxView()
    {
	try
	{
	    // 1 грузим файло с разметкой в лоадер
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(MainApp.class.getResource("view/ChooserAxView.fxml"));

	    /*
	     * 2 пинаем лоадер и (зная что он вернет Borderpane) преобразуем то,
	     * что он возвращает
	     */
	    this.chooserAxView = (AnchorPane) loader.load();

	    /* Назначаем контроллер */
	    this.chooserAxController = loader.getController();
	    this.chooserAxController.setMainApp(this);

	} catch (IOException e)
	{
	    e.printStackTrace();
	}
    }
    
    public void initConfView()
    {
	try
	{
	    // 1 грузим файло с разметкой в лоадер
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(MainApp.class.getResource("view/ConfView.fxml"));

	    /*
	     * 2 пинаем лоадер и (зная что он вернет Borderpane) преобразуем то,
	     * что он возвращает
	     */
	    this.confView = (AnchorPane) loader.load();

	    /* Назначаем контроллер */
	    this.confController = loader.getController();
	    this.confController.setMainApp(this);

	} catch (IOException e)
	{
	    e.printStackTrace();
	}
    }

    
    public void initTextProcessingView()
    {
	try
	{
	    // 1 грузим файло с разметкой в лоадер
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(MainApp.class.getResource("view/TextProcessingView.fxml"));

	    /* 2 пинаем лоадер */
	    this.textProcessingView = (AnchorPane) loader.load();

	    /* Назначаем контроллер */
	    this.textProcessingController = loader.getController();
	    this.textProcessingController.setMainApp(this);

	} catch (IOException e)
	{
	    e.printStackTrace();
	}
    }

    
    /**
     * Показывает статусное сообщение
     * @param mes
     */
    public void setStatusMessage(String mes)
    {
	this.getRootController().setMessage(mes);
	
    }

    /**
     * Уcтанавливает лаяут в центре корневого
     * 
     * @param pane
     */
    public void setCenterPane(Pane pane)    
    {
	/* Очищает статус бар */
	this.getRootController().setMessage("");
	this.rootView.setCenter(pane);
    }

    public Stage getPrimaryStage()
    {
	return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage)
    {
	this.primaryStage = primaryStage;
    }

    public RootController getRootController()
    {
	return rootController;
    }

    public void setRootController(RootController rootController)
    {
	this.rootController = rootController;
    }

    public ModBranchController getModBranchController()
    {
	return modBranchController;
    }

    public void setModBranchController(ModBranchController modBranchController)
    {
	this.modBranchController = modBranchController;
    }

    public ChooserAxController getChooserAxController()
    {
	return chooserAxController;
    }

    public void setChooserAxController(ChooserAxController chooserAxController)
    {
	this.chooserAxController = chooserAxController;
    }

    public BorderPane getRootView()
    {
	return rootView;
    }

    public void setRootView(BorderPane rootView)
    {
	this.rootView = rootView;
    }

    public AnchorPane getModBranchView()
    {
	return modBranchView;
    }

    public void setModBranchView(AnchorPane modBranchView)
    {
	this.modBranchView = modBranchView;
    }

    public AnchorPane getChooserAxView()
    {
	return chooserAxView;
    }

    public void setChooserAxView(AnchorPane chooserAxView)
    {
	this.chooserAxView = chooserAxView;
    }

    public ConfController getConfController()
    {
        return confController;
    }

    public void setConfController(ConfController confController)
    {
        this.confController = confController;
    }

    public AnchorPane getConfView()
    {
        return confView;
    }

    public void setConfView(AnchorPane confView)
    {
        this.confView = confView;
    }

    public AnchorPane getPrevPane()
    {
        return prevPane;
    }

    public void setPrevPane(AnchorPane prevPane)
    {
        this.prevPane = prevPane;
    }

	public AnchorPane getTextProcessingView()
	{
		return textProcessingView;
	}

	public void setTextProcessingView(AnchorPane textProcessingView)
	{
		this.textProcessingView = textProcessingView;
	}

	public TextProcessingController getTextProcessingController()
	{
		return textProcessingController;
	}

	public void setTextProcessingController(
			TextProcessingController textProcessingController)
	{
		this.textProcessingController = textProcessingController;
	}

}
