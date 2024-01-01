package restaurant;

import java.sql.*;
import java.util.ArrayList;

import menu.LocalMenu;
import menu.RegularMenu;
import menu.SpecialMenu;

public class MainRestaurant extends Restaurant {

	// A main restaurant.
	// This class has its specific implementations of viewMenu(),
	// insertMenu(SpecialMenu), and insertMenu(LocalMenu).

	// Constructor.
	public MainRestaurant(int id, Connection sql_connection) {
		super(id, sql_connection);
	}

	// Views all menu in the restaurant.
	public void viewMenu() {
		try {
			// Filter the menu items into regular and special
			PreparedStatement menu_pst = sql_connection
					.prepareStatement("select * from MsMenu where RestaurantID = ?");
			menu_pst.setInt(1, this.id);
			ResultSet menu_rs = menu_pst.executeQuery();

			ArrayList<Integer> regular_id = new ArrayList<Integer>();
			ArrayList<Integer> special_id = new ArrayList<Integer>();
			int curr_id = 0, curr_reg = 0, curr_special = 0;

			while (menu_rs.next()) {
				curr_id = menu_rs.getInt("MenuID");
				curr_reg = menu_rs.getInt("RegularID");
				curr_special = menu_rs.getInt("SpecialID");

				if (curr_reg != 0) {
					regular_id.add(curr_id);
				} else if (curr_special != 0) {
					special_id.add(curr_id);
				} else {
					throw new Exception("Local menu detected on main restaurant");
				}
			}

			// Print regular menu
			System.out.println("Regular Menu");
			System.out.println("==================================================");
			for (Integer i : regular_id) {
				RegularMenu curr_menu = RegularMenu.createFromID(i, sql_connection);
				curr_menu.print();
				System.out.println("==================================================");
			}
			System.out.println();

			// Print special menu
			System.out.println("Special Menu");
			System.out.println("==================================================");
			for (Integer i : special_id) {
				SpecialMenu curr_menu = SpecialMenu.createFromID(i, sql_connection);
				assert(curr_menu != null);
				curr_menu.print();
				System.out.println("==================================================");
			}
			System.out.println();

		} catch (Exception e) {
			System.out.println("PreparedStatement failure");
			System.out.println(e.getMessage());
		}
	}

	// Inserts a special menu to a restaurant.
	// Returns the newly inserted menu's ID, or -1 if the insertion was not
	// successful.
	public Integer insertMenu(SpecialMenu new_menu) {
		try {
			PreparedStatement insert_special = sql_connection
					.prepareStatement("insert into MsSpecialMenu (Name, Price, Lore) values (?, ?, ?)");
			insert_special.setString(1, new_menu.name);
			insert_special.setDouble(2, new_menu.price);
			insert_special.setString(3, new_menu.lore);

			// This variable checks if the row was inserted correctly
			int check = insert_special.executeUpdate();
			if (check == 0) {
				throw new Exception("MsSpecialMenu table insertion failed");
			}

			// Get newly inserted SpecialID
			PreparedStatement select_special = sql_connection
					.prepareStatement("select max(SpecialID) from MsSpecialMenu");
			ResultSet res_special = select_special.executeQuery();

			int special_id = -1;
			if (res_special.next()) {
				special_id = res_special.getInt(1);
			} else {
				throw new Exception("Failed to get new special menu ID");
			}

			// Insert the SpecialID into MsMenu
			PreparedStatement insert_menu = sql_connection.prepareStatement(
					"insert into MsMenu (RegularID, SpecialID, LocalID, RestaurantID) values (NULL, ?, NULL, ?)");
			insert_menu.setInt(1, special_id);
			insert_menu.setInt(2, this.id);

			check = insert_menu.executeUpdate();
			if (check == 0) {
				throw new Exception("Menu Table Insert Failed");
			}

			// Get newly inserted MenuID
			PreparedStatement select_menu = sql_connection.prepareStatement("select max(MenuID) from MsMenu");
			ResultSet res_menu = select_menu.executeQuery();

			int menu_id = -1;
			if (res_menu.next()) {
				menu_id = res_menu.getInt(1);
			} else {
				throw new Exception("Failed to get new menu ID");
			}

			return menu_id;

		} catch (Exception e) {
			System.out.println("PreparedStatement failure");
			e.printStackTrace();
		}

		return -1;
	}

	// Prevents the user from inserting a local menu to a main restaurant.
	public Integer insertMenu(LocalMenu new_menu) {
		System.out.println("Inserting a local menu to a main restaurant is not allowed!");
		return -1;
	}

}
