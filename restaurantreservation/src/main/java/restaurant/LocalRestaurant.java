package restaurant;

import java.sql.*;
import java.util.ArrayList;

import menu.LocalMenu;
import menu.RegularMenu;
import menu.SpecialMenu;

public class LocalRestaurant extends Restaurant{

	public LocalRestaurant(int id, Connection sql_connection) {
		super(id, sql_connection);
	}
	
	public void viewMenu() {
		try {
			// View Regular
			PreparedStatement select_menu = sql_connection.prepareStatement("SELECT MenuID, RegularID, LocalID FROM MsMenu"
					+ "WHERE RestaurantID = ?");
			select_menu.setInt(1, this.id);
			
			ResultSet result_menu = select_menu.executeQuery();
			
			ArrayList<Integer> regular_id = new ArrayList<Integer>();
			ArrayList<Integer> local_id = new ArrayList<Integer>();
			
			int curr_id, curr_reg, curr_local;
			
			while(result_menu.next()) {
				curr_id = result_menu.getInt("Menu_ID");
				curr_reg = result_menu.getInt("RegularID");
				curr_local = result_menu.getInt("LocalID");
				
				if(curr_reg != 0) {
					regular_id.add(curr_id);
				} else if(curr_local != 0){
					local_id.add(curr_id);
				} else {
					throw new Exception("Non local menu detected on local restaurant");
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
			
			// Print Local Menu
			System.out.println("Local Menu");
			System.out.println("==============================================");
			System.out.println();
			
			for(Integer i: local_id) {
				LocalMenu curr_menu = LocalMenu.createFromID(i, sql_connection);
				curr_menu.print();
				System.out.println("==============================================");
			}
			System.out.println();
			
			// View Local Special
		} catch (Exception e) {
			System.out.println("PreparedStatement Failed");
			System.out.println(e.getMessage());
		}
	}
	
	public Integer insertMenu(SpecialMenu new_menu) {
		System.out.println("Inserting special menu to your restaurant is not allowed!");
		return -1;
	}

	public Integer insertMenu(LocalMenu new_menu) {
		try {
			PreparedStatement insert_local = sql_connection.prepareStatement("INSERT INTO MsLocalMenu (Name, Price, Location, Lore)"
					+ "VALUES (?, ?)");
			insert_local.setString(1, new_menu.name);
			insert_local.setDouble(2, new_menu.price);
			insert_local.setString(3, new_menu.location);
			insert_local.setString(4, new_menu.lore);
			
			// This variable check if row inserted correctly
			int check;
			
			check = insert_local.executeUpdate();
			if(check == 0) {
				throw new Exception("Local Menu Table Insert Failed");
			}
			
			// Get newly inserted local id
			PreparedStatement select_local = sql_connection.prepareStatement("SELECT MAX(LocalID)FROM MsLocalMenu");
			
			ResultSet res_local = select_local.executeQuery();
			
			int local_id = -1;
			if(res_local.next()) {
				local_id = res_local.getInt(1);
			} else {
				throw new Exception("Failed to get new local menu ID");
			}
			
			PreparedStatement insert_menu = sql_connection.prepareStatement("INSERT INTO MsMenu (RegularID, SpecialID, LocalID, RestaurantID)"
					+ "VALUES (NULL, NULL, ?, ?");
			insert_menu.setInt(1, local_id);
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

}
