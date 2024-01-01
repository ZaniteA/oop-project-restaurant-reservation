package menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SpecialMenu extends Menu {

    // A special menu, which is only present in main restaurants.

    public String lore;
    public Integer special_id;

    // Constructor to create a new special menu.
    public SpecialMenu(Integer id, String name, Double price, String lore, Integer special_id) {
        super(id, name, price);
        this.lore = lore;
        this.special_id = special_id;
    }

    // Constructor to create a new special menu without specifying the ID.
    public SpecialMenu(String name, Double price, String lore) {
        super(null, name, price);
        this.lore = lore;
        this.special_id = null;
    }

    // Method to create a special menu based on the database.
    // If the ID does not exist in the database, returns NULL.
    public static SpecialMenu createFromID(Integer common_id, Connection sql_connection) {
        try {
            Integer special_id = getSpecialID(common_id, sql_connection);

            PreparedStatement spec_pst = sql_connection
                    .prepareStatement("select * from MsSpecialMenu where SpecialID = ?");
            spec_pst.setInt(1, special_id);
            ResultSet spec_rs = spec_pst.executeQuery();

            String name = null;
            Double price = null;
            String lore = null;

            while (spec_rs.next()) {
                name = spec_rs.getString("Name");
                price = spec_rs.getDouble("Price");
                lore = spec_rs.getString("Lore");
            }

            if (name != null) {
                return new SpecialMenu(common_id, name, price, lore, special_id);
            }

        } catch (Exception e) {
            System.out.println("PreparedStatement failure");
            e.printStackTrace();
        }

        // If the ID is invalid, or an exception happened
        return null;
    }

    // Prints the menu for viewing.
    public void print() {
        System.out.printf("ID       : %d\n", super.id);
        System.out.printf("Name     : %s\n", super.name);
        System.out.printf("Price    : Rp%.0f\n", super.price);
        System.out.printf("Narration: %s\n", this.lore);
    }

    // Gets the SpecialID from the MenuID.
    // Returns NULL if the SpecialID is not found (i.e. the MenuID is invalid or the
    // menu is not a special menu).
    public static Integer getSpecialID(Integer common_id, Connection sql_connection) {
        try {
            PreparedStatement menu_pst = sql_connection.prepareStatement("select * from MsMenu where MenuID = ?");
            menu_pst.setInt(1, common_id);
            ResultSet menu_rs = menu_pst.executeQuery();

            Integer special_id = null;

            while (menu_rs.next()) {
                special_id = menu_rs.getInt("SpecialID");
                if (special_id != 0) {
                    return special_id;
                }
            }

        } catch (Exception e) {
            System.out.println("PreparedStatement failure");
            e.printStackTrace();
        }

        // If no non-NULL values were encountered
        return null;
    }

}
