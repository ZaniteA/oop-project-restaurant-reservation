package restaurant;

import java.sql.*;

import menu.LocalMenu;
import menu.RegularMenu;
import menu.SpecialMenu;
import order.Order;
import table.Table;

public abstract class Restaurant {

	// An abstract class of a restaurant, holding its ID and a connection.
	// All data related to the restaurant will be fetched through its methods.
	// Methods not implemented here are meant to be overridden by the ones in
	// LocalRestaurant and MainRestaurant because of differing behaviors.

	int id;
	Connection sql_connection;

	// Constructor to create a new restaurant given ID.
	public Restaurant(int id, Connection sql_connection) {
		this.id = id;
		this.sql_connection = sql_connection;
	}

	// Returns TRUE if the menu specified is in this restaurant.
	private Boolean menuInRestaurant(int menu_id) {
		try {
			PreparedStatement menu_pst = sql_connection
					.prepareStatement("select count(*) from MsMenu where RestaurantID = ? and MenuID = ?");
			menu_pst.setInt(1, this.id);
			menu_pst.setInt(2, menu_id);
			ResultSet menu_rs = menu_pst.executeQuery();

			// Check menu count
			if (menu_rs.next()) {
				int cnt = menu_rs.getInt(1);
				if (cnt > 0) {
					return true;
				}
			}

		} catch (Exception e) {
			System.out.println("PreparedStatement failure");
			e.printStackTrace();
		}

		return false;
	}

	// Returns TRUE if the menu specified has been ordered before.
	private Boolean menuOrdered(int menu_id) {
		try {
			PreparedStatement menu_pst = sql_connection
					.prepareStatement("select count(*) from OrderMenuTransaction where MenuID = ?");
			menu_pst.setInt(1, menu_id);

			ResultSet menu_rs = menu_pst.executeQuery();

			// Check menu count
			if (menu_rs.next()) {
				int cnt = menu_rs.getInt(1);
				if (cnt > 0) {
					return true;
				}
			}

		} catch (Exception e) {
			System.out.println("PreparedStatement failure");
			e.printStackTrace();
		}

		return false;
	}

	// Abstract methods to be implemented in subclasses.
	public abstract void viewMenu();

	public abstract Integer insertMenu(SpecialMenu new_menu);

	public abstract Integer insertMenu(LocalMenu new_menu);

	// Inserts a new menu.
	// Returns the newly inserted menu's ID if the insert was successful, and -1
	// otherwise.
	public Integer insertMenu(RegularMenu new_menu) {
		try {
			PreparedStatement insert_regular = sql_connection
					.prepareStatement("insert into MsRegularMenu (Name, Price) values (?, ?)");
			insert_regular.setString(1, new_menu.name);
			insert_regular.setDouble(2, new_menu.price);

			// This variable checks if the row was inserted correctly
			int check = insert_regular.executeUpdate();
			if (check == 0) {
				throw new Exception("RegularMenu table insertion failed");
			}

			// Get newly inserted RegularID
			PreparedStatement regular_pst = sql_connection.prepareStatement("select max(RegularID) from MsRegularMenu");
			ResultSet regular_rs = regular_pst.executeQuery();

			int regular_id = -1;
			if (regular_rs.next()) {
				regular_id = regular_rs.getInt(1);
			} else {
				throw new Exception("Failed to get new regular menu ID");
			}

			// Insert the RegularID into MsMenu
			PreparedStatement insert_menu = sql_connection.prepareStatement(
					"insert into MsMenu (RegularID, SpecialID, LocalID, RestaurantID) values (?, NULL, NULL, ?)");
			insert_menu.setInt(1, regular_id);
			insert_menu.setInt(2, this.id);

			check = insert_menu.executeUpdate();
			if (check == 0) {
				throw new Exception("Menu table insertion failed");
			}

			// Get newly inserted menu ID
			PreparedStatement menu_pst = sql_connection.prepareStatement("select max(MenuID) from MsMenu");
			ResultSet menu_rs = menu_pst.executeQuery();

			int menu_id = -1;
			if (menu_rs.next()) {
				menu_id = menu_rs.getInt(1);
			} else {
				throw new Exception("Failed to get new menu ID");
			}

			return menu_id;

		} catch (Exception e) {
			System.out.println("PreparedStatement failure");
			e.printStackTrace();
		}

		// If the insertion failed
		return -1;
	}

	// Returns TRUE if the order specified is in this restaurant.
	public Boolean orderInRestaurant(int order_id) {
		try {
			PreparedStatement order_pst = sql_connection
					.prepareStatement("select count(*) from RestaurantOrderMap where RestaurantID = ? and OrderID = ?");
			order_pst.setInt(1, this.id);
			order_pst.setInt(2, order_id);
			ResultSet order_rs = order_pst.executeQuery();

			// Check order count
			if (order_rs.next()) {
				int cnt = order_rs.getInt(1);
				if (cnt > 0) {
					return true;
				}
			}

		} catch (Exception e) {
			System.out.println("PreparedStatement failure");
			System.out.println(e.getMessage());
		}

		return false;
	}

	// Methods to update various data of the menu, overloaded for updating name and
	// price.

	// Updates the name of a menu.
	// Returns TRUE if the update was successful.
	public Boolean updateMenu(int menu_id, String new_name) {
		if (!menuInRestaurant(menu_id)) {
			System.out.println("Menu does not exist in this restaurant");
			return false;
		}
		if (menuOrdered(menu_id)) {
			System.out.println("Can't update menu since some customers have ordered this menu before");
			return false;
		}

		try {
			PreparedStatement menu_pst = sql_connection
					.prepareStatement("select RegularID, SpecialID, LocalID from MsMenu where MenuID = ?");
			menu_pst.setInt(1, menu_id);
			ResultSet menu_rs = menu_pst.executeQuery();

			int type = -1; // 1 -> regular, 2 -> special, 3 -> local
			int submenu_id = -1, tmp = -1;

			// Check menu count
			if (menu_rs.next()) {
				// Check for each regular, special, and local id (only one is not null)

				tmp = menu_rs.getInt("RegularID");
				if (tmp != 0) {
					submenu_id = tmp;
					type = 1;
				}

				tmp = menu_rs.getInt("SpecialID");
				if (tmp != 0) {
					submenu_id = tmp;
					type = 2;
				}

				tmp = menu_rs.getInt("LocalID");
				if (tmp != 0) {
					submenu_id = tmp;
					type = 3;
				}
			} else {
				System.out.println("Menu ID not found!");
				return false;
			}

			if (type == -1) {
				System.out.println(
						"Menu is neither a regular, special, nor local menu. Please contact your administrator");
				return false;
			}

			String update_query = "";
			if (type == 1) {
				update_query = "update MsRegularMenu set Name = ? where RegularID = ?";
			} else if (type == 2) {
				update_query = "update MsSpecialMenu set Name = ? where SpecialID = ?";
			} else {
				update_query = "update MsLocalMenu set Name = ? where LocalID = ?";
			}
			PreparedStatement update_menu = sql_connection.prepareStatement(update_query);
			update_menu.setString(1, new_name);
			update_menu.setInt(2, submenu_id);

			int check = update_menu.executeUpdate();
			if (check == 0) {
				throw new Exception("Update failed");
			}

			return true;
		} catch (Exception e) {
			System.out.println("PreparedStatement failure");
			e.printStackTrace();
		}

		// If an exception happened
		return false;
	}

	// Updates the price of a menu.
	// Returns TRUE if the update was successful.
	public Boolean updateMenu(int menu_id, Double new_price) {
		if (!menuInRestaurant(menu_id)) {
			System.out.println("Menu does not exist in this restaurant");
			return false;
		}
		if (menuOrdered(menu_id)) {
			System.out.println("Can't update menu since some customers have ordered this menu before");
			return false;
		}

		try {
			PreparedStatement menu_pst = sql_connection
					.prepareStatement("select RegularID, SpecialID, LocalID from MsMenu where MenuID = ?");
			menu_pst.setInt(1, menu_id);
			ResultSet menu_rs = menu_pst.executeQuery();

			int type = -1; // 1 -> regular, 2 -> special, 3 -> local
			int submenu_id = -1, tmp = -1;

			// Check menu count
			if (menu_rs.next()) {
				// Check for each regular, special, and local id (only one is not null)

				tmp = menu_rs.getInt("RegularID");
				if (tmp != 0) {
					submenu_id = tmp;
					type = 1;
				}

				tmp = menu_rs.getInt("SpecialID");
				if (tmp != 0) {
					submenu_id = tmp;
					type = 2;
				}

				tmp = menu_rs.getInt("LocalID");
				if (tmp != 0) {
					submenu_id = tmp;
					type = 3;
				}
			} else {
				System.out.println("Menu ID not found!");
				return false;
			}
			if (type == -1) {
				System.out.println(
						"Menu is neither a regular, special, nor local menu. Please contact your administrator");
				return false;
			}

			String update_query;
			if (type == 1) {
				update_query = "update MsRegularMenu set Price = ? where RegularID = ?";
			} else if (type == 2) {
				update_query = "update MsSpecialMenu set Price = ? where SpecialID = ?";
			} else {
				update_query = "update MsLocalMenu set Price = ? where LocalID = ?";
			}
			PreparedStatement update_menu = sql_connection.prepareStatement(update_query);
			update_menu.setDouble(1, new_price);
			update_menu.setInt(2, submenu_id);

			int check = update_menu.executeUpdate();
			if (check == 0) {
				throw new Exception("Update failed");
			}

			return true;
		} catch (Exception e) {
			System.out.println("PreparedStatement failure");
			e.printStackTrace();
		}

		// If an exception happened
		return false;
	}

	// Deletes a menu from the restaurant.
	// Returns TRUE if the update was successful.
	public Boolean deleteMenu(int menu_id) {
		if (!menuInRestaurant(menu_id)) {
			System.out.println("Menu does not exist in this restaurant");
			return false;
		}
		if (menuOrdered(menu_id)) {
			System.out.println("Can't delete menu since some customers have ordered this menu before");
			return false;
		}

		try {
			PreparedStatement menu_pst = sql_connection
					.prepareStatement("select RegularID, SpecialID, LocalID from MsMenu where MenuID = ?");
			menu_pst.setInt(1, menu_id);
			ResultSet menu_rs = menu_pst.executeQuery();

			int type = -1; // 1 -> regular, 2 -> special, 3 -> local
			int submenu_id = -1, tmp = -1;

			// Check menu count
			if (menu_rs.next()) {
				// Check for each regular, special, and local id (only one is not null)
				tmp = menu_rs.getInt("RegularID");
				if (tmp != 0) {
					submenu_id = tmp;
					type = 1;
				}

				tmp = menu_rs.getInt("SpecialID");
				if (tmp != 0) {
					submenu_id = tmp;
					type = 2;
				}

				tmp = menu_rs.getInt("LocalID");
				if (tmp != 0) {
					submenu_id = tmp;
					type = 3;
				}
			} else {
				System.out.println("Menu ID not found!");
				return false;
			}
			if (type == -1) {
				System.out.println(
						"Menu is neither a regular, special, nor local menu. Please contact your administrator");
				return false;
			}

			String delete_query;
			if (type == 1) {
				delete_query = "delete from MsRegularMenu where RegularID = ?";
			} else if (type == 2) {
				delete_query = "delete from MsSpecialMenu where SpecialID = ?";
			} else {
				delete_query = "delete from MsLocalMenu where LocalID = ?";
			}
			PreparedStatement delete_menu = sql_connection.prepareStatement(delete_query);
			delete_menu.setInt(1, submenu_id);

			int check = delete_menu.executeUpdate();
			if (check == 0) {
				throw new Exception("Update failed");
			}

			return true;
		} catch (Exception e) {
			System.out.println("PreparedStatement failure");
			e.printStackTrace();
		}

		// If an exception happened
		return false;
	}

	// Views all tables in the restaurant.
	public void viewTable() {
		try {
			PreparedStatement table_pst = sql_connection
					.prepareStatement("select TableID from RestaurantTableMap WHERE RestaurantID = ?");
			table_pst.setInt(1, this.id);
			ResultSet table_rs = table_pst.executeQuery();

			System.out.println("Table List");
			System.out.println("==================================================");
			while (table_rs.next()) {
				Table curr_table = Table.createFromID(table_rs.getInt("TableID"), sql_connection);
				curr_table.print();
				System.out.println("==================================================");
			}
			System.out.println();

		} catch (Exception e) {
			System.out.println("PreparedStatement failure");
			e.printStackTrace();
		}
	}

	// Views all orders in the restaurant.
	public void viewOrder() {
		try {
			PreparedStatement order_pst = sql_connection
					.prepareStatement("select OrderID from RestaurantOrderMap where RestaurantID = ?");
			order_pst.setInt(1, this.id);
			ResultSet order_rs = order_pst.executeQuery();

			System.out.println("Order List");
			System.out.println("==================================================");
			while (order_rs.next()) {
				Order curr_order = Order.createFromID(order_rs.getInt("OrderID"), sql_connection);
				curr_order.print();
				System.out.println("==================================================");
			}
			System.out.println();

		} catch (Exception e) {
			System.out.println("PreparedStatement failure");
			e.printStackTrace();
		}
	}

	// Creates a new order in the restaurant.
	// Returns the new order's ID if the creation was successful, and -1 otherwise.
	public Integer createOrder(Order new_order) {
		try {
			// Insert to MsOrder table
			PreparedStatement insert_order = sql_connection
					.prepareStatement("insert into MsOrder (CustomerName, Persons, Status) values (?, ?, ?)");
			insert_order.setString(1, new_order.customer_name);
			insert_order.setInt(2, new_order.persons);
			insert_order.setInt(3, new_order.status);

			// This variable checks if the row was inserted correctly
			int check = insert_order.executeUpdate();
			if (check == 0) {
				throw new Exception("MsOrder table insertion failed");
			}

			// Get newly inserted order ID
			PreparedStatement order_pst = sql_connection.prepareStatement("select max(OrderID) from MsOrder");
			ResultSet order_rs = order_pst.executeQuery();

			int order_id = -1;
			if (order_rs.next()) {
				order_id = order_rs.getInt(1);
			} else {
				throw new Exception("Failed to get new order ID");
			}
			new_order.id = order_id;

			// Insert to RestaurantOrderMap
			PreparedStatement insert_rob = sql_connection
					.prepareStatement("insert into RestaurantOrderMap (RestaurantID, OrderID) values (?, ?)");
			insert_rob.setInt(1, this.id);
			insert_rob.setInt(2, new_order.id);

			check = insert_rob.executeUpdate();
			if (check == 0) {
				throw new Exception("RestaurantOrderMap table insertion failed");
			}

			new_order.setOrderStatus(0, sql_connection);
			return order_id;

		} catch (Exception e) {
			System.out.println("PreparedStatement failure");
			System.out.println(e.getMessage());
		}

		return -1;
	}

}
