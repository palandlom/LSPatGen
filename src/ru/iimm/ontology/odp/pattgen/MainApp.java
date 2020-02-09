package ru.iimm.ontology.odp.pattgen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.IRI;

import ch.qos.logback.core.db.dialect.MySQLDialect;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.AbstractSyntCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.CollectionCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.SyntSitbasedCDP;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.builders.AbstractPatternConfigurator;
import ru.iimm.ontology.odp.pattgen.model.pattern.synpat.builders.SitbasedPatternConfigurator;
import ru.iimm.ontology.odp.pattgen.view.PattEditorController;
import ru.iimm.ontology.odp.pattgen.view.RootController;

public class MainApp extends Application
{
	/** Основное окно */
	private Stage primaryStage;

	/** Основной лаяут в Основном окне */
	private BorderPane rootLayout;

	private RootController rootContlroller;

	private PattEditorController pattEditorController;

	private AnchorPane pattEditorLayout;

	/**
	 * Первый=основной билдер паттерна.
	 */
	private AbstractPatternConfigurator patternConfigurator;

	@Override
	public void start(Stage primaryStage)
	{

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("PattGen");
		this.initConfigurator();

		// загрузка лаяутов + их расположение
		initRootLayout();
		initPattEditorLayout();
		this.rootLayout.setCenter(this.pattEditorLayout);

		// 3 Запердоливаем возвращенное лоадером в сцену (кусок окна)
		Scene scene = new Scene(this.rootLayout);

		// 4 сцену добавляем в окно (Stage)
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * Загружает общий лаяут со строкой меню.
	 */
	public void initRootLayout()
	{
		try
		{
			// 1 грузим файло с разметкой в лоадер
			FXMLLoader rootloader = new FXMLLoader();
			rootloader.setLocation(
					MainApp.class.getResource("view/RootView.fxml"));

			// 2 пинаем лоадер и (зная что он вернет Borderpane) преобразуем то,
			// что он возвращает
			this.rootLayout = (BorderPane) rootloader.load();

			// Настраиваем контроллеры лаяутов
			this.rootContlroller = rootloader.getController();

			this.rootContlroller.setMainApp(this);

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Лаяут конфигурирования паттерна.
	 */
	public void initPattEditorLayout()
	{
		try
		{
			// 1 грузим файло с разметкой в лоадер
			FXMLLoader pattEditorLayoutLoader = new FXMLLoader();
			pattEditorLayoutLoader.setLocation(
					MainApp.class.getResource("view/PattEditorView.fxml"));

			// 2 пинаем лоадер и (зная что он вернет Borderpane) преобразуем то,
			// что он возвращает
			this.pattEditorLayout = (AnchorPane) pattEditorLayoutLoader.load();

			// 3 Запердоливаем возвращенное лоадером в сцену (кусок окна)
			// Scene scene = new Scene(rootLayout);

			// Настраиваем контроллеры лаяутов
			this.pattEditorController = pattEditorLayoutLoader.getController();

			this.pattEditorController.setMainApp(this);

			// Показываем начальный билдер
			this.pattEditorController.showBuilder(this.getPatternConfigurator());

		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Конфигурирует билдер при запуске
	 */
	private void initConfigurator()
	{
		// Создаем основной паттерн...
		AbstractSyntCDP mySit = AbstractSyntCDP.getCDP(
				IRI.create(
						"http://www.ontologydesignpatterns.org/cp/owl/situation.owl"),
				IRI.create("http://ont/mySit.owl"));

		// Получаем нужный конфигуратор + Пихаем паттерн в конфигуратор ...
		this.patternConfigurator = AbstractPatternConfigurator
				.getPatternConfigurator(mySit, null);
		this.patternConfigurator.setAdditionStrategy(null);
		

		// this.mainBuilder.setAddCDPList(null);
		// this.mainBuilder.setAdditionStrategy(null);
		// this.mainBuilder.setNewPatternIRI(IRI.create("http://ont/mySit.owl"));
		// this.mainBuilder.setPrevBuilder(null);
		// список паттернов задать
		/*
		 * List<AbstractSyntCDP> addPattList = new ArrayList<>();
		 * addPattList.add(new CollectionCDP()); addPattList.add(new
		 * SyntSitbasedCDP()); this.mainBuilder.setAddCDPList(addPattList);
		 */
		// вывести лист при загрузке

	}

	public static void main(String[] args)
	{
		launch(args);
	}

	public Stage getPrimaryStage()
	{
		return primaryStage;
	}

	public BorderPane getRootLayout()
	{
		return rootLayout;
	}

	public RootController getRootContlroller()
	{
		return rootContlroller;
	}

	public PattEditorController getPattEditorController()
	{
		return pattEditorController;
	}

	public AnchorPane getPattEditorLayout()
	{
		return pattEditorLayout;
	}

	public AbstractPatternConfigurator getPatternConfigurator()
	{
		return patternConfigurator;
	}

	public void setPrimaryStage(Stage primaryStage)
	{
		this.primaryStage = primaryStage;
	}

	public void setRootLayout(BorderPane rootLayout)
	{
		this.rootLayout = rootLayout;
	}

	public void setRootContlroller(RootController rootContlroller)
	{
		this.rootContlroller = rootContlroller;
	}

	public void setPattEditorController(PattEditorController pattEditorController)
	{
		this.pattEditorController = pattEditorController;
	}

	public void setPattEditorLayout(AnchorPane pattEditorLayout)
	{
		this.pattEditorLayout = pattEditorLayout;
	}

	public void setPatternConfigurator(
			AbstractPatternConfigurator patternConfigurator)
	{
		this.patternConfigurator = patternConfigurator;
	}


}
