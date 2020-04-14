package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import model.Category;
import model.CategoryAccessObject;
import model.FoodItem;
import model.FoodItemAccessObject;
import model.ICategoryDao;
import model.IFoodItemDao;
import model.IIngredientDao;
import model.IOrderDao;
import model.Ingredient;
import model.IngredientAccessObject;
import model.Order;
import model.OrderAccessObject;
import model.ShoppingCart;
import view.IMenuView;

public class CustomerController implements ICustomerController {
	
	// AccessObjects for the database connections.
	private IFoodItemDao foodDao;
	private ICategoryDao categoryDao;
	private IIngredientDao ingredientDao;
	private IOrderDao orderDao;
	// Shopping cart object: contains the selected fooditems.
	private ShoppingCart shoppingCart;
	// View layer
	private IMenuView menuController;

	/**
	 * Initial actions: creating DAO-objects and ShoppingCart object
	 * @param m
	 */
	public CustomerController(IMenuView m) {
		this.menuController = m;
		this.foodDao = new FoodItemAccessObject();
		this.categoryDao = new CategoryAccessObject();
		this.orderDao = new OrderAccessObject();
		this.ingredientDao = new IngredientAccessObject();
		this.shoppingCart = new ShoppingCart();
	}

	/**
	 * Method for initializing the category list for the customer UI.
	 */
	@Override
	public void initMenu() {
		Category[] allCategories = categoryDao.readCategories();
		menuController.createCategoryList(allCategories);
		for (Category c : allCategories) {
			System.out.println(c.getName());
		}
		String categoryName = allCategories[0].getName();
		readCategories(categoryName);
		menuController.setSum();
		// TODO: what if no categories?
		
	}

	/**
	 * Method for initializing the creating of the customer menu in a category.
	 */
	@Override
	public void readCategories(String name) {
		FoodItem[] items = foodDao.readFoodItemsCategory(name);
		menuController.setItems(items);
		if (items.length != 0) {
			menuController.createMenu();
		}else {
			menuController.emptyCategory();
		}
		
	}

	/**
	 * Method for creating a new order to the database.
	 */
	@Override
	public void createOrder(int orderNumber, Map<FoodItem, Integer> shoppingCart, String additionalInfo) {
		Order order = new Order(orderNumber, shoppingCart);
		order.setAdditionalInfo(additionalInfo);
		orderDao.createOrder(order);
	}

	/**
	 * Removable ingredients of an item are retrieved from the database ie. original ingredients.
	 * @param foodItem Fooditem of which ingredients are retrieved.
	 * @return Ingredients of the database object.
	 */
	@Override
	public ArrayList<String> getDatabaseIngredients(FoodItem foodItem) {
		
		ArrayList<String> ingredientsOfItem = new ArrayList<String>();
		String[] ingredientsNames;

		// If foodItem has no ingredients in the database return null.
		if (foodDao.readFoodItemByName(foodItem.getName()).getIngredientsAsList() == null ) {
			ingredientsOfItem = null;
			return ingredientsOfItem;
		}
		else {
			ingredientsNames = foodDao.readFoodItemByName(foodItem.getName()).getIngredientsAsList();

			// Checks which ingredients are removable
			for (int i = 0; i < ingredientsNames.length; i++) {
				Ingredient ingredientsAsIngredients= ingredientDao.readIngredientByName(ingredientsNames[i]);
				if (ingredientsAsIngredients.isRemoveable()) {
					ingredientsOfItem.add(ingredientsNames[i]);
				}
			}
			Collections.sort(ingredientsOfItem);
			return ingredientsOfItem;
		}
	}
	
	/**
	 * Method for emptying the shopping cart object.
	 */
	@Override
	public void emptyShoppingCart() {
		shoppingCart.emptyShoppingCart();
	}
	
	/**
	 * Getter for all the item ids of the shopping cart fooditems.
	 */
	@Override
	public int[] getAllItemId() {
		return shoppingCart.getAllItemId();
	}

	/**
	 * Setter for the amount of a certain fooditem in the shopping cart.
	 */
	@Override
	public void setAmount(int itemId, int amount) {
		shoppingCart.setAmount(itemId, amount);
	}

	/**
	 * Getter for the amount of a certain fooditem in the shopping cart.
	 * @return amount of specific food item in shopping cart
	 */
	@Override
	public int getAmount(int itemId) {
		return shoppingCart.getAmount(itemId);
	}

	/**
	 * Method for adding an item of certain amount to the shopping cart.
	 */
	@Override
	public void addToShoppingCart(FoodItem foodItem, int amount) {
		shoppingCart.addToShoppingCart(foodItem, amount);
	}

	/**
	 * Method for removing an item from the shopping cart.
	 */
	@Override
	public void removeFromShoppingCart(FoodItem foodItem) {
		shoppingCart.removeFromShoppingCart(foodItem);
	}

	/**
	 * Shopping cart toString-method.
	 * @return shopping cart as a string
	 */
	@Override
	public String shoppingCartToString() {
		return shoppingCart.toString();
	}

	/**
	 * Getter for the shopping cart object.
	 * @return shopping cart object
	 */
	@Override
	public Map<FoodItem, Integer> getShoppingCart() {
		return shoppingCart.getShoppingCart();
	}

	/**
	 * Getter for the sum of the shopping cart.
	 * @return shopping carts sum
	 */
	@Override
	public double getShoppingCartSum() {
		return shoppingCart.getSum();
	}

	/**
	 * Getter for foodItems in the shopping cart.
	 * @return list of items in shopping cart
	 */
	@Override
	public FoodItem[] getFoodItems() {
		return shoppingCart.getFoodItems();
	}

}
