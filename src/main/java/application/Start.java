package application;

import java.io.IOException;

import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import controller.TimingController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import util.Bundle;
import view.MenuView;
import view.OrdersViewController;
import view.RestaurantKeeper;
import view.StartViewController;

/**
 * Class for starting the demo program.
 * 
 * @author Samu Rinne
 *
 */
public class Start extends Application implements IStart {

	private Stage primaryStage;
	private AnchorPane rootLayout;
	private GridPane orderUi;
	FXMLLoader loader;
	private volatile TimingController timeOut;
	public Locale curLocale = new Locale("fi", "FI"); // Default Finland
	ResourceBundle bundle;
	String appConfigPath = "app.properties";
	Properties properties;

	/**
	 * init method for Start
	 */
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
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Not found, using default");
		}
		Bundle.changeBundle(curLocale);
		bundle = Bundle.getInstance();
	}

	/**
	 * Start method for start
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(bundle.getString("headerText"));
		startDemo();
	}

	/**
	 * Opens default window for customer ui
	 */
	public void initUI() {
		try {
			loader = new FXMLLoader();
			loader.setLocation(Start.class.getResource("/view/StartView.fxml"));
			loader.setResources(bundle);
			rootLayout = (AnchorPane) loader.load();
			Scene scene = new Scene(rootLayout);
			scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent t) {
					if (t.getCode() == KeyCode.ESCAPE) {
						startDemo();
					}
				}
			});
			if (timeOut != null)
				timeOut.reset();
			primaryStage.setHeight(700.0);
			primaryStage.setWidth(1100.0);
			primaryStage.setScene(scene);
			primaryStage.centerOnScreen();
			primaryStage.show();
			StartViewController startViewController = loader.getController();
			startViewController.setController(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Opens menu windor for customer ui
	 */
	public void startOrder() {
		timeoutWake();
		BorderPane menuLayout;
		try {
			loader = new FXMLLoader();
			loader.setLocation(Start.class.getResource("/view/CustomerUI.fxml"));
			loader.setResources(bundle);
			menuLayout = (BorderPane) loader.load();

			Scene scene = new Scene(menuLayout);
			scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent t) {
					if (t.getCode() == KeyCode.ESCAPE) {
						startDemo();
						if (timeOut != null)
							timeOut.cease();
						System.out.println("Esc");
					}
				}
			});
			scene.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
				timeoutWake();
			});
			primaryStage.setHeight(800.0);
			primaryStage.setWidth(1200.0);
			primaryStage.setScene(scene);
			primaryStage.centerOnScreen();
			primaryStage.show();
			MenuView menuView = loader.getController();
			menuView.setStart(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Opens Restaurants owner ui.
	 */
	public void startRestaurant() {
		try {
			loader = new FXMLLoader();
			loader.setLocation(Start.class.getResource("/view/restaurantKeeperView.fxml"));
			loader.setResources(bundle);
			rootLayout = (AnchorPane) loader.load();
			Scene scene = new Scene(rootLayout);
			scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent t) {
					if (t.getCode() == KeyCode.ESCAPE) {
						startDemo();
					}
				}
			});
			primaryStage.setHeight(600.0);
			primaryStage.setWidth(1440.0);
			primaryStage.setScene(scene);
			primaryStage.centerOnScreen();
			primaryStage.show();
			RestaurantKeeper restaurantKeeper = loader.getController();
			restaurantKeeper.setStart(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Opens orders ui
	 */
	public void startOrders() {
		try {
			loader = new FXMLLoader();
			loader.setLocation(Start.class.getResource("/view/orders.fxml"));
			loader.setResources(bundle);
			orderUi = (GridPane) loader.load();
			Scene scene = new Scene(orderUi);
			scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent t) {
					if (t.getCode() == KeyCode.ESCAPE) {
						startDemo();
						primaryStage.setMinWidth(0);
						primaryStage.setMinHeight(0);
					}
				}
			});
			primaryStage.setWidth(1200);
			primaryStage.setHeight(600);
			primaryStage.setScene(scene);
			primaryStage.centerOnScreen();
			primaryStage.show();
			OrdersViewController ordersViewController = loader.getController();
			ordersViewController.setStart(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Opens demo ui.
	 */
	public void startDemo() {
		TilePane tile = new TilePane();
		tile.setAlignment(Pos.CENTER);
		Button owner = new Button("Owner");
		Button customer = new Button("Customer");
		Button orders = new Button("Orders");
		owner.setPrefSize(200, 200);
		customer.setPrefSize(200, 200);
		orders.setPrefSize(200, 200);
		HBox hBox = new HBox();
		hBox.getChildren().addAll(owner, customer, orders);
		tile.getChildren().add(hBox);
		Scene scene = new Scene(tile, 600, 300);
		primaryStage.setHeight(300.0);
		primaryStage.setWidth(600.0);
		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();
		primaryStage.show();
		Start instance = this;
		EventHandler<MouseEvent> goCustomer = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (timeOut == null)
					timeOut = new TimingController();
				timeOut.setControllable(instance);
				timeOut.setDaemon(true);
				timeOut.start();
				initUI();
			}
		};
		EventHandler<MouseEvent> goOwner = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				startRestaurant();
			}
		};
		EventHandler<MouseEvent> goOrders = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				startOrders();
			}
		};
		customer.addEventHandler(MouseEvent.MOUSE_PRESSED, goCustomer);
		owner.addEventHandler(MouseEvent.MOUSE_PRESSED, goOwner);
		orders.addEventHandler(MouseEvent.MOUSE_PRESSED, goOrders);

	}

	/**
	 * Changes language in default customer window.
	 */
	public void setLanguage(String l, String c) {
		String language = properties.getProperty(l);
		String country = properties.getProperty(c);
		curLocale = new Locale(language, country);
		Locale.setDefault(curLocale);
		Bundle.changeBundle(curLocale);
		bundle = Bundle.getInstance();
		this.primaryStage.setTitle(bundle.getString("headerText"));
		initUI();
	}
	
	/**
	 * Changes language in menu view.
	 */

	public void setLanguageCustomer(String l, String c) {
		System.out.println("Kieli vaihdetaan " + l + " " + c);
		String language = properties.getProperty(l);
		String country = properties.getProperty(c);
		curLocale = new Locale(language, country);
		Locale.setDefault(curLocale);
		Bundle.changeBundle(curLocale);
		bundle = Bundle.getInstance();
		this.primaryStage.setTitle(bundle.getString("headerText"));
	}

	/**
	 * Changes language in owners ui
	 */
	public void setLanguageRestaurantKeeper(String l, String c) {
		String language = properties.getProperty(l);
		String country = properties.getProperty(c);
		curLocale = new Locale(language, country);
		Locale.setDefault(curLocale);
		Bundle.changeBundle(curLocale);
		bundle = Bundle.getInstance();
		this.primaryStage.setTitle(bundle.getString("headerText"));
		startRestaurant();
	}

	/**
	 * Starts timer.
	 */
	public void timeoutWake() {
		if (timeOut != null)
			timeOut.update();
	}

	/**
	 * Gives warning if you have idled too long
	 */
	public void timeOutWarning() {
		MenuView menuViewController = loader.getController();
		menuViewController.timeOutWarning();
	}

	/**
	 * main
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
