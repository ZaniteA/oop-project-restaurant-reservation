package employee_account;

import java.sql.*;
import java.util.ArrayList;

import menu.LocalMenu;
import menu.RegularMenu;
import menu.SpecialMenu;
import order.Order;
import restaurant.LocalRestaurant;
import restaurant.MainRestaurant;
import restaurant.Restaurant;

public final class EmployeeAccount {

    // Class that enables the user to interact with the database as an employee.
    // Implemented using the Singleton principle.

    // Singleton instance
    private static final EmployeeAccount instance = new EmployeeAccount();

    private Integer id;
    private Integer restaurant_id;
    private Restaurant current_restaurant;
    private Connection sql_connection;
    private String name;

    // Hide the constructor, as the class is a singleton
    private EmployeeAccount() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.sql_connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantreservation",
                    "root", "");
            if (this.sql_connection == null) {
                throw new Exception("Failed to connect to MySQL server");
            }
        } catch (Exception e) {
            System.out.println("Failed to connect to MySQL server");
            System.out.println("The server needs to be hosted on `localhost:3306/restaurantreservation`");
            System.out.println("");
            System.out.println("Detailed information:");
            e.printStackTrace();

            System.exit(1);
        }
    }

    // Method to get the single instance of EmployeeAccount
    public static EmployeeAccount getInstance() {
        return instance;
    }

    // Private utility functions

    // Checks if the user is logged in.
    // If not, throws an error.
    private Boolean checkLoggedIn() {
        if (id == null) {
            try {
                throw new Exception("User is not logged in");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    // Returns the restaurant instance from its ID.
    private void getRestaurantInstance() {
        try {
            // Get the restaurant type and create the appropriate subclass
            PreparedStatement restaurant_pst = sql_connection
                    .prepareStatement("select * from MsRestaurant where RestaurantID = ?");
            restaurant_pst.setInt(1, restaurant_id);
            ResultSet restaurant_rs = restaurant_pst.executeQuery();

            while (restaurant_rs.next()) {
                if (restaurant_rs.getInt("MainID") != 0) { // If not null
                    MainRestaurant mr = new MainRestaurant(restaurant_rs.getInt("MainID"), sql_connection);
                    current_restaurant = mr;
                } else if (restaurant_rs.getInt("LocalID") != 0) { // If not null
                    LocalRestaurant lr = new LocalRestaurant(restaurant_rs.getInt("LocalID"), sql_connection);
                    current_restaurant = lr;
                }
            }

            // If no restaurant was found, the ID is invalid
            // (This should not happen in normal circumstances, which means something is
            // wrong in the database)
            if (current_restaurant == null) {
                throw new Exception("Restaurant with ID " + restaurant_id.toString() + " not found");
            }

        } catch (Exception e) {
            System.out.println("PreparedStatement failure");
            e.printStackTrace();
        }
    }

    // Logs the user in, setting the id, restaurant_id, and current_restaurant to
    // non-NULL values.
    public Boolean login(Integer employee_id) {
        try {
            // Get the restaurant ID from the employee ID
            PreparedStatement employee_pst = sql_connection
                    .prepareStatement("select * from MsEmployee where EmployeeID = ?");
            employee_pst.setInt(1, employee_id);
            ResultSet employee_rs = employee_pst.executeQuery();

            while (employee_rs.next()) {
                restaurant_id = employee_rs.getInt("RestaurantID");
                name = employee_rs.getString("Name");
            }

            // If no restaurant ID was found, the employee ID is invalid
            if (restaurant_id == null) {
                return false;
            } else {
                id = employee_id;
                getRestaurantInstance();
                return true;
            }

        } catch (Exception e) {
            System.out.println("PreparedStatement failure");
            e.printStackTrace();
        }
        return false;
    }

    // Logs the user out, setting id, restaurant_id, and current_restaurant to NULL.
    public void logout() {
        if (!checkLoggedIn())
            return;

        id = null;
        restaurant_id = null;
        current_restaurant = null;
        name = null;
    }

    // Gets the employee's name.
    // The name itself is private, to prevent tampering from outside sources.
    public String getName() {
        if (!checkLoggedIn())
            return null;

        return name;
    }

    // Views all menu in the restaurant the employee is working in.
    public void viewMenu() {
        if (!checkLoggedIn())
            return;

        current_restaurant.viewMenu();
    }

    // Several functions to insert a new menu.
    // The function insertMenu is overloaded depending on the type of menu to
    // insert.

    // Inserts a regular menu.
    // Returns the menu ID, or -1 if the insertion was not successful.
    public Integer insertMenu(String name, Double price) {
        if (!checkLoggedIn())
            return -1;

        RegularMenu current_menu = new RegularMenu(name, price);
        return current_restaurant.insertMenu(current_menu);
    }

    // Inserts a special menu.
    // Returns the menu ID, or -1 if the insertion was not successful.
    public Integer insertMenu(String name, Double price, String lore) {
        if (!checkLoggedIn())
            return -1;

        SpecialMenu current_menu = new SpecialMenu(name, price, lore);
        return current_restaurant.insertMenu(current_menu);
    }

    // Inserts a local menu.
    // Returns the menu ID, or -1 if the insertion was not successful.
    public Integer insertMenu(String name, Double price, String lore, String location) {
        if (!checkLoggedIn())
            return -1;

        LocalMenu current_menu = new LocalMenu(name, price, lore, location);
        return current_restaurant.insertMenu(current_menu);
    }

    // Several functions to update an existing menu.
    // The function updateMenu is overloaded depending on the data to update.

    // Updates the name of a menu. Returns TRUE if the menu is successfully updated.
    public Boolean updateMenu(Integer id, String new_name) {
        if (!checkLoggedIn())
            return false;

        return current_restaurant.updateMenu(id, new_name);
    }

    // Updates the price of a menu. Returns TRUE if the menu is successfully
    // updated.
    public Boolean updateMenu(Integer id, Double new_price) {
        if (!checkLoggedIn())
            return false;

        return current_restaurant.updateMenu(id, new_price);
    }

    // Deletes a menu. Returns TRUE if the menu is successfully deleted.
    public Boolean deleteMenu(Integer id) {
        if (!checkLoggedIn())
            return false;

        return current_restaurant.deleteMenu(id);
    }

    // Views the list of tables.
    public void viewTable() {
        if (!checkLoggedIn())
            return;

        current_restaurant.viewTable();
    }

    // Views the list of orders.
    public void viewOrder() {
        if (!checkLoggedIn())
            return;

        current_restaurant.viewOrder();
    }

    // Adds a new order and sets the status to `in reserve` (0).
    // Updates the table statuses accordingly.
    // Returns the ID of the newly created order, or -1 if the creation was not
    // successful.
    public Integer addOrderInReserve(String customer_name, ArrayList<Integer> table_id, Integer persons) {
        if (!checkLoggedIn())
            return -1;

        Order current_order = new Order(customer_name, persons, table_id);
        if (!current_order.verifyTables(current_restaurant, sql_connection)) {
            return -1;
        }
        current_order.id = current_restaurant.createOrder(current_order);

        if (current_order.id != -1) {
            current_order.confirmTables(sql_connection);
            current_order.setTableStatus(true, sql_connection);
        }

        return current_order.id;
    }

    // Adds a list of menu items to an order, and sets the status to `in order` (1).
    // Returns the number of successfully added menu items.
    public Integer addMenuToOrder(Integer order_id, ArrayList<Integer> menu_id) {
        if (!checkLoggedIn())
            return 0;

        // If the order ID is invalid (doesn't exist or not in the correct restaurant)
        if (!current_restaurant.orderInRestaurant(order_id)) {
            System.out.println("Invalid order ID for the current restaurant");
            return 0;
        }

        Order current_order = Order.createFromID(order_id, sql_connection);

        // If order has been finalized
        if (current_order.status == 2) {
            System.out.printf("Order with ID %d has already been finalized\n", order_id);
            return 0;
        }

        // Count successfully added menu items
        int success = 0;
        for (Integer m : menu_id) {
            if (!current_restaurant.menuInRestaurant(m)) {
                System.out.printf("Menu with ID %d does not exist in this restaurant\n", m);
                continue;
            }
            if (current_order.addMenu(m, sql_connection)) {
                success++;
            }
        }

        // If at least one menu item was added, change the status to `in order`
        if (success > 0) {
            current_order.setOrderStatus(1, sql_connection);
        }

        return success;
    }

    // Finalizes an order. Sets the status to `finalized` (2).
    // Also prints the bill and updates the table statuses accordingly.
    public void finalizeOrder(Integer order_id) {
        if (!checkLoggedIn())
            return;

        // If the order ID is invalid (doesn't exist or not in the correct restaurant)
        if (!current_restaurant.orderInRestaurant(order_id)) {
            System.out.println("Invalid order ID for the current restaurant");
            return;
        }

        Order current_order = Order.createFromID(order_id, sql_connection);
        if (current_order.status == 0) {
            System.out.println("No menu items ordered");
            return;
        } else if (current_order.status == 2) {
            System.out.println("Order has already been finalized");
            return;
        }

        current_order.setOrderStatus(2, sql_connection);
        current_order.printBill(sql_connection);
        current_order.setTableStatus(false, sql_connection);
    }

}