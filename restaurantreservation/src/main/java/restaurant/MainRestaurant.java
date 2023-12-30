package restaurant;

import java.sql.*;
import java.util.ArrayList;

import menu.LocalMenu;
import menu.RegularMenu;
import menu.SpecialMenu;

public class MainRestaurant extends Restaurant{

	public MainRestaurant(int id, Connection sql_connection) {
		super(id, sql_connection);
	}
	
	public void viewMenu() {
		try {
			// View Regular
			PreparedStatement select_menu = sql_connection.prepareStatement("SELECT MenuID, RegularID, SpecialID FROM MsMenu"
					+ "WHERE RestaurantID = ?");
			select_menu.setInt(1, this.id);
			
			ResultSet result_menu = select_menu.executeQuery();
			
			ArrayList<Integer> regular_id = new ArrayList<Integer>();
			ArrayList<Integer> special_id = new ArrayList<Integer>();
			
			int curr_id, curr_reg, curr_special;
			
			while(result_menu.next()) {
				curr_id = result_menu.getInt("Menu_ID");
				curr_reg = result_menu.getInt("RegularID");
				curr_special = result_menu.getInt("SpecialID");
				
				if(curr_reg != 0) {
					regular_id.add(curr_id);
				} else if(curr_special != 0){
					special_id.add(curr_id);
				} else {
					throw new Exception("Non main menu detected on main restaurant");
				}
			}
			
			// Print Regular Menu
			System.out.println("Regular Menu");
			System.out.println("==============================================");
			System.out.println();
			
			for(Integer i: regular_id) {
				RegularMenu curr_menu = RegularMenu.createFromID(i, sql_connection);
				curr_menu.print();
				System.out.println("==============================================");
			}
			System.out.println();
			
			
			// Print Special Menu
			System.out.println("Special Menu");
			System.out.println("==============================================");
			System.out.println();
			
			for(Integer i: special_id) {
				SpecialMenu curr_menu = SpecialMenu.createFromID(i, sql_connection);
				curr_menu.print();
				System.out.println("==============================================");
			}
			System.out.println();
		} catch (Exception e) {
			System.out.println("PreparedStatement Failed");
			System.out.println(e.getMessage());
		}
	}	
	
	public Integer insertMenu(SpecialMenu new_menu) {
		try {
			PreparedStatement insert_special = sql_connection.prepareStatement("INSERT INTO MsSpecialMenu (Name, Price, Lore)"
					+ "VALUES (?, ?)");
			insert_special.setString(1, new_menu.name);
			insert_special.setDouble(2, new_menu.price);
			insert_special.setString(4, new_menu.lore);
			
			// This variable check if row inserted correctly
			int check;
			
			check = insert_special.executeUpdate();
			if(check == 0) {
				throw new Exception("Special Menu Table Insert Failed");
			}
			
			// Get newly inserted special id
			PreparedStatement select_special = sql_connection.prepareStatement("SELECT MAX(SpecialID)FROM MsSpecialMenu");
			
			ResultSet res_special = select_special.executeQuery();
			
			int special_id = -1;
			if(res_special.next()) {
				special_id = res_special.getInt(1);
			} else {
				throw new Exception("Failed to get new special menu ID");
			}
			
			PreparedStatement insert_menu = sql_connection.prepareStatement("INSERT INTO MsMenu (RegularID, SpecialID, LocalID, RestaurantID)"
					+ "VALUES (NULL, NULL, ?, ?");
			insert_menu.setInt(1, special_id);
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

	public Integer insertMenu(LocalMenu new_menu) {
		System.out.println("Inserting local menu to your restaurant is not allowed!");
		return -1;
	}

}
