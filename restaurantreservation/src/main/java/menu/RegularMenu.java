package menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegularMenu extends Menu {

    // A regular menu, which is present in both main and local restaurants.

    public Integer regular_id;

    // Constructor to create a new regular menu.
    public RegularMenu(Integer id, String name, Double price, Integer regular_id) {
        super(id, name, price);
        this.regular_id = regular_id;
    }

    // Constructor to create a new regular menu without specifying the ID.
    public RegularMenu(String name, Double price) {
        super(null, name, price);
        this.regular_id = null;
    }

    // Method to create a regular menu based on the database.
    // If the ID does not exist in the database, returns NULL.
    public static RegularMenu createFromID(Integer common_id, Connection sql_connection) {
        try {
            Integer regular_id = getRegularID(common_id, sql_connection);

            PreparedStatement reg_pst = sql_connection.prepareStatement("select * from MsRegularMenu where RegularID = ?");
            reg_pst.setInt(1, regular_id);
            ResultSet reg_rs = reg_pst.executeQuery();

            String name = null;
            Double price = null;

            while (reg_rs.next()) {
                name = reg_rs.getString("Name");
                price = reg_rs.getDouble("Price");
            }

            if (name != null) {
                return new RegularMenu(common_id, name, price, regular_id);
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
    }

    // Gets the RegularID from the MenuID.
    // Returns NULL if the RegularID is not found (i.e. the MenuID is invalid or the menu is not a regular menu).
    public static Integer getRegularID(Integer common_id, Connection sql_connection) {
        try {
            PreparedStatement menu_pst = sql_connection.prepareStatement("select * from MsMenu where MenuID = ?");
            menu_pst.setInt(1, common_id);
            ResultSet menu_rs = menu_pst.executeQuery();

            Integer regular_id = null;

            while (menu_rs.next()) {
                regular_id = menu_rs.getInt("RegularID");
                if (regular_id != 0) {
                    return regular_id;
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
