package view;

import java.util.ArrayList;

import java.util.List;

import application.IStart;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.IOrderDao;
import model.Order;
import model.OrderAccessObject;

/**
 * ViewControlelr for orders view
 * 
 * @author Samu Rinne
 *
 */
public class OrdersViewController {

	private IStart start;

	@FXML
	private GridPane activeOrders, readyOrders;

	@FXML
	private Text activeText, readyText;

	private IOrderDao orderDao = new OrderAccessObject();

	private Order[] allOrders = orderDao.readOrders();

	private List<Order> activeOrdersArrayList = new ArrayList<Order>();

	private List<Order> readyOrdersArrayList = new ArrayList<Order>();

	private void updateOrders() {
		updateActiveOrders();
		Order[] activeOrdersList = getPrimitiveList(activeOrdersArrayList);
		Order[] readyOrdersList = getPrimitiveList(readyOrdersArrayList);
		System.out.println(allOrders.length);
		System.out.println(activeOrdersList.length);
		int i = 0;
		int j = 0;
		if (activeOrdersList.length != 0) {
			for (int y = 0; y < 5; y++) {
				for (int x = 0; x < 5; x++) {
					if (i < activeOrdersList.length) {
						int orderNumber = activeOrdersList[i].getOrderNumber();
						String stringToAdd = Integer.toString(orderNumber);
						if (orderNumber < 10) {
							stringToAdd = 0 + stringToAdd;
						}
						Label text = new Label();
						text.setFont(new Font("Arial", 30));
						text.setText(stringToAdd);
						GridPane.setHalignment(text, HPos.CENTER);
						activeOrders.add(text, x, y);
						int a = i;
						EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent e) {
								updateList(activeOrdersList[a]);
							}
						};
						text.addEventHandler(MouseEvent.MOUSE_PRESSED, eventHandler);
						i++;
					}
				}
			}
		}
		if (readyOrdersList.length != 0) {
			for (int y = 0; y < 5; y++) {
				for (int x = 0; x < 5; x++) {
					if (j < readyOrdersList.length) {
						int orderNumber = readyOrdersList[j].getOrderNumber();
						String stringToAdd = Integer.toString(orderNumber);
						if (orderNumber < 10) {
							stringToAdd = 0 + stringToAdd;
						}
						Label text = new Label();
						text.setFont(new Font("Arial", 30));
						text.setText(stringToAdd);
						GridPane.setHalignment(text, HPos.CENTER);
						readyOrders.add(text, x, y);
						int b = j;
						EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent e) {
								removeFromReady(readyOrdersList[b]);
							}
						};
						text.addEventHandler(MouseEvent.MOUSE_PRESSED, eventHandler);
						j++;
					}
				}
			}
		}

	}

	/**
	 * init method
	 */
	public void initialize() {
		updateOrders();
	}

	private Order[] getPrimitiveList(List list) {
		Order[] returnOrders = new Order[list.size()];
		return (Order[]) list.toArray(returnOrders);
	}

	private void updateActiveOrders() {
		activeOrdersArrayList.clear();
		activeOrders.getChildren().clear();
		for (Order o : allOrders) {
			if (o.isStatus() == false) {
				activeOrdersArrayList.add(o);
			}
		}
	}

	/**
	 * Setter for Start
	 * @param s
	 */
	public void setStart(IStart s) {
		this.start = s;
	}

	private void updateList(Order o) {
		System.out.println(o.getOrderId());
		o.setStatus(true);
		readyOrdersArrayList.add(o);
		orderDao.updateOrderStatus(o);
		updateOrders();
	}

	private void removeFromReady(Order o) {
		readyOrdersArrayList.remove(o);
		readyOrders.getChildren().clear();
		updateOrders();
	}

}
