package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import controller.TimingController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import util.Bundle;
import view.MenuViewController;
import view.StartViewController;

public class Start extends Application {
	
	private Stage primaryStage;
	private AnchorPane rootLayout;
	FXMLLoader loader;
	private TimingController control;
	public Locale curLocale = new Locale("fi", "FI"); // Default Finland
	ResourceBundle bundle = Bundle.getInstance(curLocale);
	String appConfigPath = "app.properties";
	Properties properties;
	
	@Override
	public void init() {
		properties = new Properties();
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(appConfigPath);
			properties.load(inputStream);
			String language = properties.getProperty("languageEn");
			String country = properties.getProperty("countryEn");
			curLocale = new Locale(language, country);
			Locale.setDefault(curLocale);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Not found, using default");
		}
		Bundle.changeBundle(curLocale);
		bundle = Bundle.getInstance(curLocale);
	}
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(bundle.getString("headerText"));
		
		control = new TimingController();
		control.setControllable(this);
		control.setDaemon(true);
		control.start();
		
		initUI();
	}
	
	public void initUI() {
		try {
			loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/StartView.fxml"));
			loader.setResources(bundle);
			rootLayout = (AnchorPane) loader.load();
			
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			StartViewController startViewController = loader.getController();
			startViewController.setController(this);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startOrder() {
		control.update();
		BorderPane menuLayout;
		try {
			loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/CustomerUI.fxml"));
			loader.setResources(bundle);
			menuLayout = (BorderPane) loader.load();
			Scene scene = new Scene(menuLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			MenuViewController menuViewController = loader.getController();
			menuViewController.setController(this);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setLanguage(String l, String c) {
		String language = properties.getProperty(l);
		String country = properties.getProperty(c);
		curLocale = new Locale(language, country);
		Locale.setDefault(curLocale);
		Bundle.changeBundle(curLocale);
		bundle = Bundle.getInstance(curLocale);
		this.primaryStage.setTitle(bundle.getString("headerText"));
		initUI();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
