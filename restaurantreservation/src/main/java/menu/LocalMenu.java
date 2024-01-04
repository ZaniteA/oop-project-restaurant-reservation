package menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LocalMenu extends Menu {

    // A local menu, which is only present in local restaurants.

    public String lore;
    public String location;
    public Integer local_id;

    // Constructor to create a new local menu.
    public LocalMenu(Integer id, String name, Double price, String lore, String location, Integer local_id) {
        super(id, name, price);
        this.lore = lore;
        this.location = location;
        this.local_id = local_id;
    }

    // Constructor to create a new local menu without specifying the ID.
    public LocalMenu(String name, Double price, String lore, String location) {
        super(null, name, price);
        this.lore = lore;
        this.location = location;
        this.local_id = null;
    }

    // Method to create a local menu based on the database.
    // If the ID does not exist in the database, returns NULL.
    public static LocalMenu createFromID(Integer common_id, Connection sql_connection) {
        try {
            Integer local_id = getLocalID(common_id, sql_connection);

            PreparedStatement loc_pst = sql_connection
                    .prepareStatement("select * from MsLocalMenu where LocalID = ?");
            loc_pst.setInt(1, local_id);
            ResultSet loc_rs = loc_pst.executeQuery();

            String name = null;
            Double price = null;
            String lore = null;
            String location = null;

            while (loc_rs.next()) {
                name = loc_rs.getString("Name");
                price = loc_rs.getDouble("Price");
                lore = loc_rs.getString("Lore");
                location = loc_rs.getString("Location");
            }

            if (name != null) {
                return new LocalMenu(common_id, name, price, lore, location, local_id);
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
        System.out.printf("Origin   : %s\n", this.location);
    }

    // Gets the LocalID from the MenuID.
    // Returns NULL if the LocalID is not found (i.e. the MenuID is invalid or the
    // menu is not a local menu).
    public static Integer getLocalID(Integer common_id, Connection sql_connection) {
        try {
            PreparedStatement menu_pst = sql_connection.prepareStatement("select * from MsMenu where MenuID = ?");
            menu_pst.setInt(1, common_id);
            ResultSet menu_rs = menu_pst.executeQuery();

            Integer local_id = null;

            while (menu_rs.next()) {
                local_id = menu_rs.getInt("LocalID");
                if (local_id != 0) {
                    return local_id;
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