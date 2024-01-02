package restaurant;

import java.sql.*;
import java.util.ArrayList;

import menu.LocalMenu;
import menu.RegularMenu;
import menu.SpecialMenu;

public class LocalRestaurant extends Restaurant {

	// A local restaurant.
	// This class has its specific implementations of viewMenu(),
	// insertMenu(SpecialMenu), and insertMenu(LocalMenu).

	// Constructor.
	public LocalRestaurant(int id, Connection sql_connection) {
		super(id, sql_connection);
	}

	// Views all menu in the restaurant.
	public void viewMenu() {
		try {
			// Filter the menu items into regular and local
			PreparedStatement menu_pst = sql_connection
					.prepareStatement("select * from MsMenu where RestaurantID = ?");
			menu_pst.setInt(1, this.id);
			ResultSet menu_rs = menu_pst.executeQuery();

			ArrayList<Integer> regular_id = new ArrayList<Integer>();
			ArrayList<Integer> local_id = new ArrayList<Integer>();
			int curr_id = 0, curr_reg = 0, curr_local = 0;

			while (menu_rs.next()) {
				curr_id = menu_rs.getInt("MenuID");
				curr_reg = menu_rs.getInt("RegularID");
				curr_local = menu_rs.getInt("LocalID");

				if (curr_reg != 0) {
					regular_id.add(curr_id);
				} else if (curr_local != 0) {
					local_id.add(curr_id);
				} else {
					throw new Exception("Special menu detected on local restaurant");
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

			// Print local menu
			System.out.println("Local Menu");
			System.out.println("==================================================");
			for (Integer i : local_id) {
				LocalMenu curr_menu = LocalMenu.createFromID(i, sql_connection);
				curr_menu.print();
				System.out.println("==================================================");
			}
			System.out.println();

		} catch (Exception e) {
			System.out.println("PreparedStatement failure");
			System.out.println(e.getMessage());
		}
	}

	// Prevents the user from inserting a special menu to a local restaurant.
	public Integer insertMenu(SpecialMenu new_menu) {
		System.out.println("Inserting a special menu to a local restaurant is not allowed!");
		return -1;
	}

	// Inserts a local menu to a restaurant.
	// Returns the newly inserted menu's ID, or -1 if the insertion was not
	// successful.
	public Integer insertMenu(LocalMenu new_menu) {
		try {
			PreparedStatement insert_local = sql_connection
					.prepareStatement("insert into MsLocalMenu (Name, Price, Location, Lore) values (?, ?, ?, ?)");
			insert_local.setString(1, new_menu.name);
			insert_local.setDouble(2, new_menu.price);
			insert_local.setString(3, new_menu.location);
			insert_local.setString(4, new_menu.lore);

			// This variable checks if the row was inserted correctly
			int check = insert_local.executeUpdate();
			if (check == 0) {
				throw new Exception("MsLocalMenu table insertion failed");
			}

			// Get newly inserted LocalID
			PreparedStatement select_local = sql_connection.prepareStatement("select max(LocalID) from MsLocalMenu");
			ResultSet res_local = select_local.executeQuery();

			int local_id = -1;
			if (res_local.next()) {
				local_id = res_local.getInt(1);
			} else {
				throw new Exception("Failed to get new local menu ID");
			}

			// Insert the LocalID into MsMenu
			PreparedStatement insert_menu = sql_connection.prepareStatement(
					"insert into MsMenu (RegularID, SpecialID, LocalID, RestaurantID) values (NULL, NULL, ?, ?)");
			insert_menu.setInt(1, local_id);
			insert_menu.setInt(2, this.id);

			check = insert_menu.executeUpdate();
			if (check == 0) {
				throw new Exception("MsMenu table insertion failed");
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

		// If the insertion failed
		return -1;
	}

}
