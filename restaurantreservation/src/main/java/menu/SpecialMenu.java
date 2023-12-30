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
    public static SpecialMenu createFromID(Integer id, Connection sql_connection) {
        try {
            PreparedStatement menu_pst = sql_connection.prepareStatement("select * from MsMenu where MenuID = ?");
            menu_pst.setInt(1, id);
            ResultSet menu_rs = menu_pst.executeQuery();

            String name = null;
            Double price = null;
            String lore = null;
            Integer special_id = null;

            while (menu_rs.next()) {
                special_id = menu_rs.getInt("SpecialID");

                PreparedStatement reg_pst = sql_connection
                        .prepareStatement("select * from MsSpecialMenu where SpecialID = ?");
                reg_pst.setInt(1, special_id);
                ResultSet reg_rs = reg_pst.executeQuery();

                while (reg_rs.next()) {
                    name = reg_rs.getString("Name");
                    price = reg_rs.getDouble("Price");
                    lore = reg_rs.getString("Lore");
                }
            }

            if (name != null) {
                return new SpecialMenu(id, name, price, lore, special_id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // If the ID is invalid, or an exception happened
        return null;
    }

    // Prints the menu for viewing.
    public void print() {
        System.out.printf("ID       : %d\n", super.id);
        System.out.printf("Name     : %s\n", super.name);
        System.out.printf("Price    : Rp%.0lf\n", super.price);
        System.out.printf("Narration: %s\n", this.lore);
    }

}
