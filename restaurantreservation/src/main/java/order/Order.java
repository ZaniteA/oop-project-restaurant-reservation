package order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;

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
    public Order(Integer id, String customer_name, Integer persons, Integer status, ArrayList<Integer> table_id,
            ArrayList<Integer> menu_id) {
        this.id = id;
        this.customer_name = customer_name;
        this.persons = persons;
        this.status = status;
        this.table_id = table_id;
        this.menu_id = menu_id;
    }

    // Constructor to create a new order based on a new reservation.
    public Order(String customer_name, Integer persons, ArrayList<Integer> table_id) {
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
            System.out.println("PreparedStatement failure");
            e.printStackTrace();
        }

        // If the ID is invalid, or an exception happened
        return null;
    }

    // Verifies if the tables selected for the order is valid and enough for the
    // number of persons.
    public Boolean verifyTables(Connection sql_connection) {
        Integer total_cap = 0;
        for (Integer t : table_id) {
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
                System.out.println("PreparedStatement failure");
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
            System.out.println("PreparedStatement failure");
            e.printStackTrace();
        }
    }

    // Updates the status of the table.
    public void setTableStatus(Boolean new_status, Connection sql_connection) {
        try {
            // 1st query to get the tableID from OrderTableMap
            PreparedStatement table_pst = sql_connection
                    .prepareStatement("select * from OrderTableMap where OrderID = ?");
            table_pst.setInt(1, id);
            ResultSet table_rs = table_pst.executeQuery();

            while (table_rs.next()) {
                Integer curTableId = table_rs.getInt("TableID");

                // 2nd query to update the table on MsTable
                PreparedStatement status_pst = sql_connection
                        .prepareStatement("update MsTable set Taken = ? where TableID = ?");
                status_pst.setBoolean(1, new_status);
                status_pst.setInt(2, curTableId);

                int check = status_pst.executeUpdate();
                if (check == 0) {
                    throw new Exception("Update failed");
                }
            }

        } catch (Exception e) {
            System.out.println("PreparedStatement failure");
            e.printStackTrace();
        }
    }

    // Updates the menu of the order.
    // Returns TRUE if the menu was successfully updated.
    public Boolean addMenu(Integer new_menu_id, Connection sql_connection) {
        menu_id.add(new_menu_id);
        try {
            PreparedStatement status_pst = sql_connection
                    .prepareStatement("insert into OrderMenuMap values (?, ?)");
            status_pst.setInt(1, new_menu_id);
            status_pst.setInt(2, id);

            int check = status_pst.executeUpdate();
            if (check == 0) {
                throw new Exception("Insert failed");
            }

            return true;
        } catch (Exception e) {
            System.out.println("PreparedStatement failure");
            e.printStackTrace();
        }

        // If an exception happened
        return false;
    }

    // Prints the bill of the order.
    public void printBill(Connection sql_connection) {
        Double total_price = 0.0;
        ArrayList<String> menu_name_list = new ArrayList<String>();
        ArrayList<Double> price_list = new ArrayList<Double>();

        // Sort the menu_id to make it easier
        Collections.sort(menu_id);

        for (int i = 0; i < menu_id.size(); i++) {
            Integer t = menu_id.get(i);

            // This menu_id has been computed before so we can simply continue to avoid
            // double counting
            if (i > 0 && t == menu_id.get(i - 1)) {
                menu_name_list.add(menu_name_list.get(i - 1));
                price_list.add(price_list.get(i - 1));
                continue;
            }

            try {
                // Get cur_menu_id from the first query
                PreparedStatement table_pst = sql_connection
                        .prepareStatement("select * from OrderMenuTransaction where MenuID = ? AND OrderID = ?");
                table_pst.setInt(1, t);
                table_pst.setInt(2, id);
                ResultSet table_rs = table_pst.executeQuery();

                String menu_name = "";
                Double menu_price = 0.0;
                while (table_rs.next()) {
                    Integer cur_menu_id = table_rs.getInt("MenuID");

                    // Get cur_regular_id, cur_special_id, and cur_local_id from the second query
                    PreparedStatement table_pst_2 = sql_connection
                            .prepareStatement("select * from MsMenu where menuID = ?");
                    table_pst_2.setInt(1, cur_menu_id);
                    ResultSet table_rs_2 = table_pst_2.executeQuery();

                    Integer cur_regular_id = table_rs_2.getInt("RegularID");
                    Integer cur_special_id = table_rs_2.getInt("SpecialID");
                    Integer cur_local_id = table_rs_2.getInt("LocalID");

                    // Get menuName and menuPrice from the third query
                    while (table_rs_2.next()) {
                        PreparedStatement table_pst_3 = null;

                        if (cur_regular_id != 0) {
                            table_pst_3 = sql_connection
                                    .prepareStatement("select * from MsRegularMenu where RegularID = ?");
                            table_pst_3.setInt(1, cur_regular_id);

                        } else if (cur_special_id != 0) {
                            table_pst_3 = sql_connection
                                    .prepareStatement("select * from MsRegularMenu where SpecialID = ?");
                            table_pst_3.setInt(1, cur_special_id);

                        } else if (cur_local_id != 0) {
                            table_pst_3 = sql_connection
                                    .prepareStatement("select * from MsRegularMenu where LocalID = ?");
                            table_pst_3.setInt(1, cur_local_id);

                        } else {
                            throw new Exception("Something went wrong on getting list menu");
                        }

                        ResultSet table_rs_3 = table_pst_3.executeQuery();
                        while (table_rs_3.next()) {
                            menu_name = table_rs_3.getString("Name");
                            menu_price = table_rs_3.getDouble("Price");
                        }

                        menu_name_list.add(menu_name);
                        price_list.add(menu_price);
                    }
                }

            } catch (Exception e) {
                System.out.println("PreparedStatement failure");
                e.printStackTrace();
            }
        }

        // Print the lists and total price
        for (int i = 0; i < menu_name_list.size(); i++) {
            System.out.printf("%s: Rp %.0lf\n", menu_name_list.get(i), price_list.get(i));
            total_price += price_list.get(i);
        }
        System.out.printf("Total Price: Rp %.0lf\n", total_price);
    }
}
