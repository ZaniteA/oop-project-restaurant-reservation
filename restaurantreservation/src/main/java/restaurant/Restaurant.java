package restaurant;

import java.sql.*;

import menu.LocalMenu;
import menu.RegularMenu;
import menu.SpecialMenu;
import order.Order;

public abstract class Restaurant {
	int id;
	Connection conn; 

	public Restaurant(int id, Connection conn) {
		// Create Connection
		this.id = id;
		this.conn = conn;
	}
	
	private Boolean menuInRestaurant(int menu_id) {
		try {
			PreparedStatement select_menu = conn.prepareStatement("SELECT COUNT(*) FROM RestaurantMenuMap WHERE RestaurantID = ? AND MenuID = ?");
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
			System.out.println(e.getMessage());
		}
		
		
		return false;
	}
	
	private Boolean orderInRestaurant(int order_id) {
		try {
			PreparedStatement select_order = conn.prepareStatement("SELECT COUNT(*) FROM RestaurantOrderMap WHERE RestaurantID = ? AND OrderID = ?");
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
	
	private Boolean menuOrdered(int menu_id) {
		try {
			PreparedStatement select_menu = conn.prepareStatement("SELECT COUNT(*) FROM OrderMenuMap WHERE MenuID = ?");
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
			System.out.println(e.getMessage());
		}
		
		
		return false;
	}
	
	public abstract void viewMenu();
	
	public abstract Integer insertMenu(RegularMenu new_menu);
	public abstract Integer insertMenu(SpecialMenu new_menu);
	public abstract Integer insertMenu(LocalMenu new_menu);
	
	public void updateMenu(int menu_id, String new_name) {
		if(menuOrdered(menu_id)) {
			System.out.println("Can't update menu since some customers have ordered this menu before");
			return;
		}
		
		try {
			PreparedStatement select_menu = conn.prepareStatement("SELECT RegularID, SpecialID, LocalID FROM MsMenu WHERE MenuID = ?");
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
			
			if(type == 1) {
				PreparedStatement update_menu = conn.prepareStatement("UPDATE MsRegularMenu SET Name = ? WHERE RegularID = ?");
				update_menu.setString(1,  new_name);
				update_menu.setInt(2, submenu_id);
			} else if(type == 2) {
				PreparedStatement update_menu = conn.prepareStatement("UPDATE MsSpecialMenu SET Name = ? WHERE SpecialID = ?");
				update_menu.setString(1,  new_name);
				update_menu.setInt(2, submenu_id);
			} else {
				PreparedStatement update_menu = conn.prepareStatement("UPDATE MsLocalMenu SET Name = ? WHERE LocalID = ?");
				update_menu.setString(1,  new_name);
				update_menu.setInt(2, submenu_id);
			}
		} catch (Exception e) {
			System.out.println("PreparedStatement Failed");
			System.out.println(e.getMessage());
		}
	}
	
	public void updateMenu(int menu_id, Double new_price) {
		if(menuOrdered(menu_id)) {
			System.out.println("Can't update menu since some customers have ordered this menu before");
			return;
		}
		
		try {
			PreparedStatement select_menu = conn.prepareStatement("SELECT RegularID, SpecialID, LocalID FROM MsMenu WHERE MenuID = ?");
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
			
			if(type == 1) {
				PreparedStatement update_menu = conn.prepareStatement("UPDATE MsRegularMenu SET Price = ? WHERE RegularID = ?");
				update_menu.setDouble(1,  new_price);
				update_menu.setInt(2, submenu_id);
			} else if(type == 2) {
				PreparedStatement update_menu = conn.prepareStatement("UPDATE MsSpecialMenu SET Price = ? WHERE SpecialID = ?");
				update_menu.setDouble(1,  new_price);
				update_menu.setInt(2, submenu_id);
			} else {
				PreparedStatement update_menu = conn.prepareStatement("UPDATE MsLocalMenu SET Price = ? WHERE LocalID = ?");
				update_menu.setDouble(1,  new_price);
				update_menu.setInt(2, submenu_id);
			}
		} catch (Exception e) {
			System.out.println("PreparedStatement Failed");
			System.out.println(e.getMessage());
		}
	}
	
	public void deleteMenu(int menu_id) {
		if(menuOrdered(menu_id)) {
			System.out.println("Can't delete menu since some customers have ordered this menu before");
			return;
		}
	}
	
	public void viewTable() {
		
	}
	
	public void viewOrder() {
		
	}
	
	public Integer createOrder(Order new_order) {
		
		return -1;
	}

}
