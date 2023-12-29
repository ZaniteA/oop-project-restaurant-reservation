package menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LocalMenu extends Menu {

    // A local menu, which is only present in local restaurants.

    String lore;
    String location;
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
    // If the ID does not exist in the database, all fields are filled with NULL.
    public static LocalMenu createFromID(Integer id, Connection sql_connection) {
        try {
            PreparedStatement menu_pst = sql_connection.prepareStatement("select * from MsMenu where MenuID = ?");
            menu_pst.setInt(1, id);
            ResultSet menu_rs = menu_pst.executeQuery();

            String name = null;
            Double price = null;
            String lore = null;
            String location = null;
            Integer local_id = null;

            while (menu_rs.next()) {
                local_id = menu_rs.getInt("LocalID");

                PreparedStatement reg_pst = sql_connection
                        .prepareStatement("select * from MsSpecialMenu where LocalID = ?");
                reg_pst.setInt(1, local_id);
                ResultSet reg_rs = reg_pst.executeQuery();

                while (reg_rs.next()) {
                    name = reg_rs.getString("Name");
                    price = reg_rs.getDouble("Price");
                    lore = reg_rs.getString("Lore");
                    location = reg_rs.getString("Location");
                }
            }

            if (name != null) {
                return new LocalMenu(id, name, price, lore, location, local_id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // If the ID is invalid, or an exception happened
        return new LocalMenu(null, null, null, null);
    }

    // Prints the menu for viewing.
    public void print() {
        System.out.printf("ID       : %d\n", super.id);
        System.out.printf("Name     : %s\n", super.name);
        System.out.printf("Price    : %.0f\n", super.price);
        System.out.printf("Narration: %s\n", this.lore);
        System.out.printf("Origin   : %s\n", this.location);
    }

}
