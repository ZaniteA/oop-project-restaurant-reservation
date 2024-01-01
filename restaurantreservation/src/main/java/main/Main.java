package main;

import java.util.ArrayList;
import java.util.Scanner;

import employee_account.EmployeeAccount;

public class Main {

    // Class that handles all the main menu functions, i.e. the interface through
    // which the user interacts with the system.

    static int sect;
    static Scanner user_input = new Scanner(System.in);
    static EmployeeAccount emp_acc = EmployeeAccount.getInstance();

    // Private utility functions

    // Reads an integer.
    // If lo is not null, check if the integer is not less than lo.
    // If hi is not null, check if the integer is not greater than hi.
    // In case of invalid input, keep going until user sends valid input.
    private static int readInteger(String prompt, String error_message, Integer lo, Integer hi) {
        while (true) {
            System.out.print(prompt);

            int ret = 0;
            boolean error = false;
            try {
                ret = user_input.nextInt();
            } catch (Exception e) {
                System.out.println(error_message);
                error = true;
            }
            user_input.nextLine();
            if (error)
                continue;

            if (lo != null) {
                if (ret < lo) {
                    System.out.println(error_message);
                    continue;
                }
            }

            if (hi != null) {
                if (ret > hi) {
                    System.out.println(error_message);
                    continue;
                }
            }

            return ret;
        }
    }

    // Reads a double.
    // If lo is not null, check if the integer is not less than lo.
    // If hi is not null, check if the integer is not greater than hi.
    // In case of invalid input, keep going until user sends valid input.
    private static double readDouble(String prompt, String error_message, Double lo, Double hi) {
        while (true) {
            System.out.print(prompt);

            double ret = 0.0;
            boolean error = false;
            try {
                ret = user_input.nextDouble();
            } catch (Exception e) {
                System.out.println(error_message);
                error = true;
            }
            user_input.nextLine();
            if (error)
                continue;

            if (lo != null) {
                if (ret < lo) {
                    System.out.println(error_message);
                    continue;
                }
            }

            if (hi != null) {
                if (ret > hi) {
                    System.out.println(error_message);
                    continue;
                }
            }

            return ret;
        }
    }

    // Gets the ordinal suffix of a number.
    private static String getOrdinalSuffix(int i) {
        if (i == 1)
            return "st";
        if (i == 2)
            return "nd";
        if (i == 3)
            return "rd";
        return "th";
    }

    // Section 1: Home menu
    private static void homeMenu() {
        System.out.printf("Hello %s, what would you like to do?\n", emp_acc.getName());
        System.out.println(" 1. View Order");
        System.out.println(" 2. Add Reservation");
        System.out.println(" 3. Add Menu to an Order");
        System.out.println(" 4. Finalize Order");
        System.out.println(" 5. View Menu");
        System.out.println(" 6. Insert Menu");
        System.out.println(" 7. Update Menu");
        System.out.println(" 8. Delete Menu");
        System.out.println(" 9. View Table");
        System.out.println("10. Logout");
        System.out.println(" 0. Exit");

        int resp = readInteger("> ", "Undefined menu section", 0, 10);

        // Add 2 to user input since section 1 and 2 is reserved for home menu and
        // login,
        // except for exit
        sect = resp;
        if (resp > 0) {
            sect += 2;
        }

        return;
    }

    // Section 2: Login
    private static void login() {
        System.out.println("Enter Employee ID:");
        int emp_id = readInteger("> ", "Invalid employee ID format", null, null);

        if (!emp_acc.login(emp_id)) {
            System.out.println("Login failed: invalid employee ID. Please try again");
            return;
        }

        // Return to home menu
        sect = 1;

        return;
    }

    // Section 3: View order
    private static void viewOrder() {
        emp_acc.viewOrder();

        // Return to home menu
        sect = 1;

        return;
    }

    // Section 4: Add reservation
    private static void addReservation() {
        // Get customer name
        System.out.println("Enter customer name:");
        System.out.print("> ");
        String name = user_input.nextLine();

        // Get table list
        System.out.println("How many tables?");
        int number_of_tables = readInteger("> ", "Invalid number of tables", 1, null);

        ArrayList<Integer> table_id = new ArrayList<Integer>();
        for (int i = 1; i <= number_of_tables; i++) {
            String prompt = String.format("Enter %d%s table ID: ", i, getOrdinalSuffix(i));
            table_id.add(readInteger(prompt, "Invalid table ID", 1, null));
        }

        // Get number of persons
        System.out.println("How many persons for this reservation?");
        int number_of_persons = readInteger("> ", "Invalid number of persons", 1, null);

        emp_acc.addOrderInReserve(name, table_id, number_of_persons);

        // Return to home menu
        sect = 1;

        return;
    }

    // Section 5: Add order
    private static void addOrder() {
        // Get order ID
        System.out.println("Enter order ID:");
        int order_id = readInteger("> ", "Invalid order ID", 1, null);

        // Get menu list
        System.out.println("How many menu items?");
        int number_of_menu = readInteger("> ", "Invalid number of menu items", 1, null);

        ArrayList<Integer> menu_id = new ArrayList<Integer>();
        for (int i = 1; i <= number_of_menu; i++) {
            String prompt = String.format("Enter %d%s menu ID: ", i, getOrdinalSuffix(i));
            menu_id.add(readInteger(prompt, "Invalid menu ID", 1, null));
        }

        emp_acc.addMenuToOrder(order_id, menu_id);

        // Return to home menu
        sect = 1;

        return;
    }

    // Section 6: Finalize order
    private static void finalizeOrder() {
        // Get order ID
        System.out.println("Enter order ID:");
        int order_id = readInteger("> ", "Invalid order ID", 1, null);

        emp_acc.finalizeOrder(order_id);

        // Return to home menu
        sect = 1;

        return;
    }

    // Section 7 : View menu
    private static void viewMenu() {
        emp_acc.viewMenu();

        sect = 1;

        return;
    }

    // Section 8: Insert menu
    private static void insertMenu() {
        System.out.println("Which type of menu do you want to insert?");
        System.out.println("1. Regular");
        System.out.println("2. Special");
        System.out.println("3. Local Special");
        int type = readInteger("> ", "Invalid type", 1, 3);

        if (type == 1) {
            // Get name
            System.out.println("Enter menu name:");
            System.out.print("> ");
            String name = user_input.nextLine();

            // Get price
            System.out.println("Enter menu price:");
            double price = readDouble("> ", "Invalid menu price", 0.0, null);

            emp_acc.insertMenu(name, price);

        } else if (type == 2) {
            // Get name
            System.out.println("Enter menu name:");
            System.out.print("> ");
            String name = user_input.nextLine();

            // Get price
            System.out.println("Enter menu price:");
            double price = readDouble("> ", "Invalid menu price", 0.0, null);

            // Get narration
            System.out.println("Enter narration for this menu:");
            System.out.print("> ");
            String lore = user_input.nextLine();

            emp_acc.insertMenu(name, price, lore);

        } else {
            // Get name
            System.out.println("Enter menu name:");
            System.out.print("> ");
            String name = user_input.nextLine();

            // Get price
            System.out.println("Enter menu price:");
            double price = readDouble("> ", "Invalid menu price", 0.0, null);

            // Get narration
            System.out.println("Enter narration for this menu:");
            System.out.print("> ");
            String lore = user_input.nextLine();

            // Get location
            System.out.println("Enter this menu's origin location:");
            System.out.print("> ");
            String location = user_input.nextLine();

            emp_acc.insertMenu(name, price, lore, location);
        }

        // Return to home menu
        sect = 1;

        return;
    }

    // Section 9: Update menu
    private static void updateMenu() {
        // Get menu ID
        System.out.println("Enter menu ID:");
        int menu_id = readInteger("> ", "Invalid menu ID", 1, null);

        System.out.println("Which data would you like to update?");
        System.out.println("1. Name");
        System.out.println("2. Price");

        int type = readInteger("> ", "Invalid input", 1, 2);

        if (type == 1) {
            System.out.println("Enter new menu name:");
            System.out.print("> ");
            String new_name = user_input.nextLine();

            emp_acc.updateMenu(menu_id, new_name);

        } else {
            System.out.println("Enter new menu price:");
            double new_price = readDouble("> ", "Invalid price", 0.0, null);

            emp_acc.updateMenu(menu_id, new_price);
        }

        // Return to home menu
        sect = 1;

        return;
    }

    // Section 10: Delete menu
    private static void deleteMenu() {
        // Get Menu ID
        System.out.println("Enter menu ID:");
        int menu_id = readInteger("> ", "Invalid menu ID", 1, null);

        emp_acc.deleteMenu(menu_id);

        // Return to home menu
        sect = 1;

        return;
    }

    // Section 11: View table
    private static void viewTable() {
        emp_acc.viewTable();

        // Return to home menu
        sect = 1;

        return;
    }

    // Section 12: Logout
    private static void logout() {
        System.out.printf("See you again, %s!\n", emp_acc.getName());

        emp_acc.logout();

        // Return to login page
        sect = 2;

        return;
    }

    public static void main(String[] args) {
        // Begin at login page
        sect = 2;

        while (true) {
            System.out.println();
            System.out.println();
            if (sect == 1) {
                homeMenu();
            } else if (sect == 2) {
                login();
            } else if (sect == 3) {
                viewOrder();
            } else if (sect == 4) {
                addReservation();
            } else if (sect == 5) {
                addOrder();
            } else if (sect == 6) {
                finalizeOrder();
            } else if (sect == 7) {
                viewMenu();
            } else if (sect == 8) {
                insertMenu();
            } else if (sect == 9) {
                updateMenu();
            } else if (sect == 10) {
                deleteMenu();
            } else if (sect == 11) {
                viewTable();
            } else if (sect == 12) {
                logout();
            } else if (sect == 0) {
                break;
            } else {
                // This part should be unreachable as input has been validated by readInteger()
                try {
                    throw new Exception("Invalid section");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Thank you for your service");
    }

}