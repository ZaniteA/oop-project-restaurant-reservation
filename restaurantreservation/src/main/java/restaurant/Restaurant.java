package restaurant;

import java.sql.*;

import menu.LocalMenu;
import menu.Menu;
import menu.RegularMenu;
import menu.SpecialMenu;
import order.Order;

public abstract class Restaurant {
	int id;
	Connection sql_connection; 

	public Restaurant(int id, Connection sql_connection) {
		// Create Connection
		this.id = id;
		this.sql_connection = sql_connection;
	}
	
	private Boolean menuInRestaurant(int menu_id) {
		try {
			PreparedStatement select_menu = sql_connection.prepareStatement("SELECT COUNT(*) FROM RestaurantMenuMap "
					+ "WHERE RestaurantID = ? AND MenuID = ?");
			select_menu.setInt(1, this.id);
			select_menu.setInt(2, menu_id);
			
			ResultSet res_menu =  select_menu.executeQuery();
			
			// Check menu count
			if (res_menu.next()) {    
			    int cnt = res_menu.getInt(1);
			    if(cnt > 0){
			    	return true;
			    }
			} 
			
		} catch (Exception e) {
			System.out.println("PreparedStatement Failed");
			e.printStackTrace();
		}
		
		
		return false;
	}
	
	
	private Boolean menuOrdered(int menu_id) {
		try {
			PreparedStatement select_menu = sql_connection.prepareStatement("SELECT COUNT(*) FROM OrderMenuMap WHERE MenuID = ?");
			select_menu.setInt(1, menu_id);
			
			ResultSet res_menu =  select_menu.executeQuery();
			
			// Check menu count
			if (res_menu.next()) {    
			    int cnt = res_menu.getInt(1);
			    if(cnt > 0){
			    	return true;
			    }
			} 
			
			
		} catch (Exception e) {
			System.out.println("PreparedStatement Failed");
			e.printStackTrace();
		}
		
		
		return false;
	}
	
	public abstract void viewMenu();
	public abstract Integer insertMenu(SpecialMenu new_menu);
	public abstract Integer insertMenu(LocalMenu new_menu);
	
	public Integer insertMenu(RegularMenu new_menu) {
		try {
			PreparedStatement insert_regular = sql_connection.prepareStatement("INSERT INTO MsRegularMenu (Name, Price)"
					+ "VALUES (?, ?)");
			insert_regular.setString(1, new_menu.name);
			insert_regular.setDouble(2, new_menu.price);
			
			// This variable check if row inserted correctly
			int check;
			
			check = insert_regular.executeUpdate();
			if(check == 0) {
				throw new Exception("Regular Menu Table Insert Failed");
			}
			
			// Get newly inserted regular id
			PreparedStatement select_regular = sql_connection.prepareStatement("SELECT MAX(RegularID)FROM MsRegularMenu");
			
			ResultSet res_regular = select_regular.executeQuery();
			
			int regular_id = -1;
			if(res_regular.next()) {
				regular_id = res_regular.getInt(1);
			} else {
				throw new Exception("Failed to get new regular menu ID");
			}
			
			PreparedStatement insert_menu = sql_connection.prepareStatement("INSERT INTO MsMenu (RegularID, SpecialID, LocalID, RestaurantID)"
					+ "VALUES (?, NULL, NULL, ?");
			insert_menu.setInt(1, regular_id);
			insert_menu.setInt(2, this.id);
			
			check = insert_menu.executeUpdate();
			if(check == 0) {
				throw new Exception("Menu Table Insert Failed");
			}
			
			// Get newly inserted menu id
			PreparedStatement select_menu = sql_connection.prepareStatement("SELECT MAX(MenuID)FROM MsMenu");
			
			ResultSet res_menu = select_menu.executeQuery();
			
			int menu_id = -1;
			if(res_menu.next()) {
				menu_id = res_menu.getInt(1);
			} else {
				throw new Exception("Failed to get new menu ID");
			}
			
			return menu_id;
		} catch(Exception e){
			System.out.println("PreparedStatement Failed");
			e.printStackTrace();
		}
		return -1;
	}
	
	public Boolean orderInRestaurant(int order_id) {
		try {
			PreparedStatement select_order = sql_connection.prepareStatement("SELECT COUNT(*) FROM RestaurantOrderMap "
					+ "WHERE RestaurantID = ? AND OrderID = ?");
			select_order.setInt(1, this.id);
			select_order.setInt(2, order_id);
			
			ResultSet res_order =  select_order.executeQuery();
			
			// Check order count
			if (res_order.next()) {    
			    int cnt = res_order.getInt(1);
			    if(cnt > 0){
			    	return true;
			    }
			} 
			
			
		} catch (Exception e) {
			System.out.println("PreparedStatement Failed");
			System.out.println(e.getMessage());
		}
		
		
		return false;
	}
	
	public void updateMenu(int menu_id, String new_name) {
		if(!menuInRestaurant(menu_id)) {
			System.out.println("Menu does not exist in this restaurant");
			return;
		}
		
		if(menuOrdered(menu_id)) {
			System.out.println("Can't update menu since some customers have ordered this menu before");
			return;
		}
		
		try {
			PreparedStatement select_menu = sql_connection.prepareStatement("SELECT RegularID, SpecialID, LocalID FROM MsMenu WHERE MenuID = ?");
			select_menu.setInt(1, menu_id);
			
			ResultSet res_menu =  select_menu.executeQuery();
			
			int type = -1; // 1 -> regular, 2 -> special, 3 -> local
			int submenu_id = -1, tmp;
			
			// Check menu count
			if (res_menu.next()) {    
				// Check for each regular, special, and local id (only one is not null)
				
				tmp = res_menu.getInt("RegularID");
				if(tmp != 0) {
					submenu_id = tmp;
					type = 1;
				}
				
				tmp = res_menu.getInt("SpecialID");
				if(tmp != 0) {
					submenu_id = tmp;
					type = 2;
				}
				
				tmp = res_menu.getInt("LocalID");
				if(tmp != 0) {
					submenu_id = tmp;
					type = 3;
				}
			} else {
				System.out.println("Menu ID not found!");
				return;
			}
			if(type == -1) {
				System.out.println("Menu is not a regular, special, nor local. Please contact your administrator");;
				return;
			}
			
			String update_query;
			if(type == 1) {
				update_query = "UPDATE MsRegularMenu SET Name = ? WHERE RegularID = ?";
			} else if(type == 2) {
				update_query = "UPDATE MsSpecialMenu SET Name = ? WHERE SpecialID = ?";
			} else {
				update_query = "UPDATE MsLocalMenu SET Name = ? WHERE LocalID = ?";
			}
			PreparedStatement update_menu = sql_connection.prepareStatement(update_query);
			update_menu.setString(1,  new_name);
			update_menu.setInt(2, submenu_id);
			
			int check = update_menu.executeUpdate();
			if(check == 0) {
				throw new Exception("Update failed");
			}
		} catch (Exception e) {
			System.out.println("PreparedStatement Failed");
			e.printStackTrace();
		}
	}
	
	public void updateMenu(int menu_id, Double new_price) {
		if(!menuInRestaurant(menu_id)) {
			System.out.println("Menu does not exist in this restaurant");
			return;
		}
		
		if(menuOrdered(menu_id)) {
			System.out.println("Can't update menu since some customers have ordered this menu before");
			return;
		}
		
		try {
			PreparedStatement select_menu = sql_connection.prepareStatement("SELECT RegularID, SpecialID, LocalID FROM MsMenu WHERE MenuID = ?");
			select_menu.setInt(1, menu_id);
			
			ResultSet res_menu =  select_menu.executeQuery();
			
			int type = -1; // 1 -> regular, 2 -> special, 3 -> local
			int submenu_id = -1, tmp;
			
			// Check menu count
			if (res_menu.next()) {    
				// Check for each regular, special, and local id (only one is not null)
				
				tmp = res_menu.getInt("RegularID");
				if(tmp != 0) {
					submenu_id = tmp;
					type = 1;
				}
				
				tmp = res_menu.getInt("SpecialID");
				if(tmp != 0) {
					submenu_id = tmp;
					type = 2;
				}
				
				tmp = res_menu.getInt("LocalID");
				if(tmp != 0) {
					submenu_id = tmp;
					type = 3;
				}
			} else {
				System.out.println("Menu ID not found!");
				return;
			}
			if(type == -1) {
				System.out.println("Menu is not a regular, special, nor local. Please contact your administrator");;
				return;
			}
			
			
			String update_query;
			if(type == 1) {
				update_query = "UPDATE MsRegularMenu SET Price = ? WHERE RegularID = ?";
			} else if(type == 2) {
				update_query = "UPDATE MsSpecialMenu SET Price = ? WHERE SpecialID = ?";
			} else {
				update_query = "UPDATE MsLocalMenu SET Price = ? WHERE LocalID = ?";
			}
			PreparedStatement update_menu = sql_connection.prepareStatement(update_query);
			update_menu.setDouble(1,  new_price);
			update_menu.setInt(2, submenu_id);
			
			int check = update_menu.executeUpdate();
			if(check == 0) {
				throw new Exception("Update failed");
			}
		} catch (Exception e) {
			System.out.println("PreparedStatement Failed");
			e.printStackTrace();
		}
	}
	
	public void deleteMenu(int menu_id) {
		if(!menuInRestaurant(menu_id)) {
			System.out.println("Menu does not exist in this restaurant");
			return;
		}
		
		if(menuOrdered(menu_id)) {
			System.out.println("Can't delete menu since some customers have ordered this menu before");
			return;
		}
		
		try {
			PreparedStatement select_menu = sql_connection.prepareStatement("SELECT RegularID, SpecialID, LocalID FROM MsMenu WHERE MenuID = ?");
			select_menu.setInt(1, menu_id);
			
			ResultSet res_menu =  select_menu.executeQuery();
			
			int type = -1; // 1 -> regular, 2 -> special, 3 -> local
			int submenu_id = -1, tmp;
			
			// Check menu count
			if (res_menu.next()) {    
				// Check for each regular, special, and local id (only one is not null)
				
				tmp = res_menu.getInt("RegularID");
				if(tmp != 0) {
					submenu_id = tmp;
					type = 1;
				}
				
				tmp = res_menu.getInt("SpecialID");
				if(tmp != 0) {
					submenu_id = tmp;
					type = 2;
				}
				
				tmp = res_menu.getInt("LocalID");
				if(tmp != 0) {
					submenu_id = tmp;
					type = 3;
				}
			} else {
				System.out.println("Menu ID not found!");
				return;
			}
			if(type == -1) {
				throw new Exception("Menu is not a regular, special, nor local. Please contact your administrator");
			}
			
			String delete_query;
			if(type == 1) {
				delete_query = "DELETE FROM MsRegularMenu WHERE RegularID = ?";
			} else if(type == 2) {
				delete_query = "DELETE FROM MsSpecialMenu WHERE SpecialID = ?";
			} else {
				delete_query = "DELETE FROM MsLocalMenu WHERE LocalID = ?";
			}
			PreparedStatement delete_menu = sql_connection.prepareStatement(delete_query);
			delete_menu.setInt(1, submenu_id);
			
			int check = delete_menu.executeUpdate();
			if(check == 0) {
				throw new Exception("Update failed");
			}
			
		} catch (Exception e) {
			System.out.println("PreparedStatement Failed");
			e.printStackTrace();
		}
	}
	
	public void viewTable() {
		try {
			PreparedStatement select_table = sql_connection.prepareStatement("SELECT TableID FROM RestaurantTableMap"
					+ "WHERE RestaurantID = ?");
			select_table.setInt(1, this.id);
			
			ResultSet res_table = select_table.executeQuery();
			
			System.out.println("Table List");
			System.out.println("==============================================");
			System.out.println();
			while(res_table.next()) {
				Table curr_table = Table.createFromID(res_table.getInt("TableID"), sql_connection); 
				curr_table.print();
				System.out.println("==============================================");
			}
			System.out.println();
			
		} catch (Exception e) {
			System.out.println("PreparedStatement Failed");
			e.printStackTrace();
		}
	}
	
	public void viewOrder() {
		try {
			PreparedStatement select_table = sql_connection.prepareStatement("SELECT OrderID FROM RestaurantOrderMap"
					+ "WHERE RestaurantID = ?");
			select_table.setInt(1, this.id);
			
			ResultSet res_order = select_table.executeQuery();
			
			System.out.println("Order List");
			System.out.println("==============================================");
			System.out.println();
			while(res_order.next()) {
				Order curr_order = Order.createFromID(res_order.getInt("OrderID"), sql_connection);
				curr_order.print();
				System.out.println("==============================================");
			}
			System.out.println();
		} catch (Exception e) {
			System.out.println("PreparedStatement Failed");
			e.printStackTrace();
		}
	}
	
	public Integer createOrder(Order new_order) {
		
		try {
			// Insert to MsOrder Table
			PreparedStatement insert_order = sql_connection.prepareStatement("INSERT INTO MsOrder (CustomerName, Persons, Status)"
					+ "VALUES (?, ?, ?)");
			insert_order.setString(1, new_order.customer_name);
			insert_order.setInt(2, new_order.persons);
			insert_order.setInt(3, new_order.status);
			
			// This variable check if row inserted correctly
			int check;
			
			check = insert_order.executeUpdate();
			if(check == 0) {
				throw new Exception("Order table Insert Failed");
			}
			
			// Get newly inserted order id
			PreparedStatement select_order = sql_connection.prepareStatement("SELECT MAX(OrderID) FROM MsOrder");
			
			ResultSet res_order = select_order.executeQuery();
			
			int order_id = -1;
			if(res_order.next()) {
				order_id = res_order.getInt(1);
			} else {
				
				throw new Exception("Failed to get new order ID");
			}
			
			new_order.id = order_id;
			
			// Insert to RestaurantOrderMap
			PreparedStatement insert_rob = sql_connection.prepareStatement("INSERT INTO RestaurantOrderMap (RestaurantID, OrderID)"
					+ "VALUES (?, ?)");
			insert_rob.setInt(1, this.id);
			insert_rob.setInt(2, new_order.id);
			
			check = insert_rob.executeUpdate();
			if(check == 0) {
				throw new Exception("Restaurant-Order Map Table Insert Failed");
			}
			
			new_order.setOrderStatus(0, sql_connection);
			
			return order_id;
		} catch (Exception e) {
			System.out.println("PreparedStatement Failed");
			System.out.println(e.getMessage());
		}
		
		return -1;
	}

}
