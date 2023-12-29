package employee_account;

import java.sql.*;

// Class that enables the user to interact with the database as an employee.
// Implemented using the Singleton principle.

public final class EmployeeAccount {

    // Singleton instance
    private static EmployeeAccount instance;

    private static Integer id;
    private static Integer restaurant_id;
    private static Restaurant current_restaurant;
    private static Connection sql_connection;

    // Hide the constructor, as the class is a singleton
    private EmployeeAccount() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantreservation", "root",
                    "");
            if (conn == null) {
                throw new Exception("Failed to connect to MySQL server");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to get the single instance of EmployeeAccount
    public static EmployeeAccount getInstance() {
        if (instance == null) {
            instance = new EmployeeAccount();
        }
        return instance;
    }

    // Private utility functions

    // Checks if the user is logged in.
    // If not, throws an error.
    private static Boolean checkLoggedIn() {
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
    private static void getRestaurantInstance() {
        try {
            // Get the restaurant type and create the appropriate subclass
            PreparedStatement restaurant_pst = sql_connection
                    .prepareStatement("select * from MsRestaurant where RestaurantID = ?");
            restaurant_pst.setInt(1, restaurant_id);
            ResultSet restaurant_rs = restaurant_pst.executeQuery();

            while (restaurant_rs.next()) {
                if (restaurant_rs.getInt("MainID") != 0) { // If not null
                    MainRestaurant mr = new MainRestaurant(id, sql_connection);
                    current_restaurant = mr;
                } else if (restaurant_rs.getInt("LocalID") != 0) { // If not null
                    LocalRestaurant lr = new LocalRestaurant(id, sql_connection);
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
            e.printStackTrace();
        }
    }

    // Logs the user in, setting the id, restaurant_id, and current_restaurant to non-NULL values.
    public Boolean login(Integer employee_id) {
        try {
            // Get the restaurant ID from the employee ID
            PreparedStatement employee_pst = sql_connection
                    .prepareStatement("select * from MsEmployee where EmployeeID = ?");
            employee_pst.setInt(1, employee_id);
            ResultSet employee_rs = employee_pst.executeQuery();

            while (employee_rs.next()) {
                restaurant_id = employee_rs.getInt("RestaurantID");
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
            e.printStackTrace();
        }
    }

    // Logs the user out, setting id, restaurant_id, and current_restaurant to NULL.
    public void logout() {
        if (!checkLoggedIn()) return;

        id = null;
        restaurant_id = null;
        current_restaurant = null;
    }

    // Views all menu in the restaurant the employee is working in.
    public void viewMenu() {
        if (!checkLoggedIn()) return;

        current_restaurant.viewMenu();
    }

    // Several functions to insert a new menu.
    // The function insertMenu is overloaded depending on the type of menu to insert.

    // Inserts a regular menu, and returns the menu ID.
    public Integer insertMenu(String name, Double price) {
        if (!checkLoggedIn()) return;

        RegularMenu current_menu = new RegularMenu(name, price);
        return current_restaurant.insertMenu(current_menu);
    }

    // Inserts a special menu, and returns the menu ID.
    public Integer insertMenu(String name, Double price, String lore) {
        if (!checkLoggedIn()) return;

        SpecialMenu current_menu = new SpecialMenu(name, price, lore);
        return current_restaurant.insertMenu(current_menu);
    }

    // Inserts a local menu, and returns the menu ID.
    public Integer insertMenu(String name, Double price, String lore, String location) {
        if (!checkLoggedIn()) return;

        LocalMenu current_menu = new LocalMenu(name, price, lore, location);
        return current_restaurant.insertMenu(current_menu);
    }

    // Several functions to update an existing menu.
    // The function updateMenu is overloaded depending on the data to update.

    // Updates the name of a menu. Returns TRUE if the menu is successfully updated.
    public Boolean updateMenu(Integer id, String new_name) {
        if (!checkLoggedIn()) return;

        current_restaurant.updateMenu(id, new_name);
    }

    // Updates the price of a menu. Returns TRUE if the menu is successfully updated.
    public Boolean updateMenu(Integer id, Double new_price) {
        if (!checkLoggedIn()) return;

        current_restaurant.updateMenu(id, new_price);
    }

    // Deletes a menu. Returns TRUE if the menu is successfully deleted.
    public Boolean deleteMenu(Integer id) {
        if (!checkLoggedIn()) return;

        current_restaurant.deleteMenu(id);
    }

    // Views the list of tables.
    public void viewTable() {
        if (!checkLoggedIn()) return;

        current_restaurant.viewTable();
    }

    // Views the list of orders.
    public void viewOrder() {
        if (!checkLoggedIn()) return;

        current_restaurant.viewOrder();
    }

    // Adds a new order and sets the status to `in reserve` (0).
    // Updates the table statuses accordingly.
    // Returns the ID of the newly created order.
    public Integer addOrderInReserve(String customer_name, ArrayList<Integer> table_id, Integer persons) {
        if (!checkLoggedIn()) return;

        Order current_order = new Order(customer_name, table_id, persons);
        if (current_restaurant.createOrder(current_order)) {
            current_order.setTableStatus(true);
            return true;
        } else {
            return false;
        }
    }

    // Adds a list of menu items to an order, and sets the status to `in order` (1).
    // Returns the number of successfully added menu items.
    public Integer addMenuToOrder(Integer order_id, ArrayList<Integer> menu_id) {
        if (!checkLoggedIn()) return;

        // If the order ID is invalid (doesn't exist or not in the correct restaurant)
        if (!current_restaurant.orderInRestaurant(order_id)) {
            try {
                throw new Exception("Invalid order ID for the current restaurant");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        Order current_order = new Order(order_id, sql_connection);
        current_order.setOrderStatus(1);

        // Count successfully added menu items
        int success = 0;
        for (Integer m : menu_id) {
            if (current_order.addMenu(m)) {
                success++;
            }
        }
        return success;
    }

    // Finalizes an order. Sets the status to `finalized` (2).
    // Also prints the bill and updates the table statuses accordingly.
    public void finalizeOrder(Integer order_id) {
        if (!checkLoggedIn()) return;

        // If the order ID is invalid (doesn't exist or not in the correct restaurant)
        if (!current_restaurant.orderInRestaurant(order_id)) {
            try {
                throw new Exception("Invalid order ID for the current restaurant");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        Order current_order = new Order(order_id, sql_connection);
        current_order.setOrderStatus(2);
        current_order.printBill();
        current_order.setTableStatus(false);
    }
    
}
