package order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Order {

    // An order, which contains data about the customer, number of persons, tables,
    // and menu items ordered.

    public Integer id;
    public String customer_name;
    public Integer persons;
    public Integer status;
    public ArrayList<Integer> table_id;
    public ArrayList<Integer> menu_id;

    // Constant for status names in English.
    public final String[] status_name = { "in reserve", "in order", "finalized" };

    // Constructor to create a new order.
    Order(Integer id, String customer_name, Integer persons, Integer status, ArrayList<Integer> table_id,
            ArrayList<Integer> menu_id) {
        this.id = id;
        this.customer_name = customer_name;
        this.persons = persons;
        this.status = status;
        this.table_id = table_id;
        this.menu_id = menu_id;
    }

    // Constructor to create a new order based on a new reservation.
    Order(String customer_name, Integer persons, ArrayList<Integer> table_id) {
        this.customer_name = customer_name;
        this.persons = persons;
        this.table_id = table_id;

        this.id = null;
        this.status = 0;
        this.menu_id = new ArrayList<Integer>();
    }

    // Method to create an order based on the database.
    // If the ID does not exist in the database, returns NULL.
    public static Order createFromID(Integer id, Connection sql_connection) {
        try {
            PreparedStatement order_pst = sql_connection.prepareStatement("select * from MsOrder where OrderID = ?");
            order_pst.setInt(1, id);
            ResultSet order_rs = order_pst.executeQuery();

            String customer_name = null;
            Integer persons = null;
            Integer status = null;

            while (order_rs.next()) {
                customer_name = order_rs.getString("CustomerName");
                persons = order_rs.getInt("Persons");
                status = order_rs.getInt("Status");
            }

            if (customer_name != null) {
                ArrayList<Integer> table_id = new ArrayList<Integer>();
                PreparedStatement table_pst = sql_connection
                        .prepareStatement("select * from OrderTableMap where OrderId = ?");
                table_pst.setInt(1, id);
                ResultSet table_rs = table_pst.executeQuery();

                while (table_rs.next()) {
                    table_id.add(table_rs.getInt("TableID"));
                }

                ArrayList<Integer> menu_id = new ArrayList<Integer>();
                PreparedStatement menu_pst = sql_connection
                        .prepareStatement("select * from OrderMenuMap where OrderId = ?");
                menu_pst.setInt(1, id);
                ResultSet menu_rs = menu_pst.executeQuery();

                while (menu_rs.next()) {
                    menu_id.add(menu_rs.getInt("TableID"));
                }

                return new Order(id, customer_name, persons, status, table_id, menu_id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // If the ID is invalid, or an exception happened
        return null;
    }

    // Verifies if the tables selected for the order is valid and enough for the
    // number of persons.
    public Boolean verifyTables(Connection sql_connection) {
        int total_cap = 0;
        for (int t : table_id) {
            try {
                PreparedStatement table_pst = sql_connection
                        .prepareStatement("select * from MsTable where TableID = ?");
                table_pst.setInt(1, t);
                ResultSet table_rs = table_pst.executeQuery();

                int cap = 0;
                boolean taken = false;
                while (table_rs.next()) {
                    cap = table_rs.getInt("Capacity");
                    taken = table_rs.getBoolean("Taken");
                }

                if (cap == 0) {
                    System.out.printf("Table with ID %d not found\n", t);
                    return false;
                } else if (taken) {
                    System.out.printf("Table with ID %d is already taken", t);
                    return false;
                }

                total_cap += cap;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (total_cap < persons) {
            System.out.println("Total capacity of tables is not enough to accommodate all persons");
            return false;
        } else {
            return true;
        }
    }

    // Prints the order for viewing.
    public void print() {
        System.out.printf("ID           : %d\n", this.id);
        System.out.printf("Customer name: %s\n", this.customer_name);
        System.out.printf("Persons      : %d\n", this.persons);
        System.out.printf("Status       : %s\n", status_name[this.status]);
    }

    // Updates the status of the order.
    public void setOrderStatus(Integer new_status, Connection sql_connection) {
        status = new_status;

        try {
            PreparedStatement status_pst = sql_connection
                    .prepareStatement("update MsOrder set Status = ? where OrderID = ?");
            status_pst.setInt(1, new_status);
            status_pst.setInt(2, id);

            int check = status_pst.executeUpdate();
            if (check == 0) {
                throw new Exception("Update failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * public void setTable(Integer newStatus) {
     * ;
     * }
     * 
     * public void addMenu(Integer menuId) {
     * ;
     * }
     * 
     * public void printBill() {
     * ;
     * }
     */

}
