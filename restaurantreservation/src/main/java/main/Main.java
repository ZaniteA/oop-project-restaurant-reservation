package main;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	
	static int sect;
	static Scanner userInput = new Scanner(System.in);
	static Employee emp = EmployeeAccount.getInstance();
	
	// Section 1
	public static void homeMenu() {
		System.out.println("Hello " , emp.name, , ", what would you like to do?");
		System.out.println("1.  View Order");
		System.out.println("2.  Add Reservasion");
		System.out.println("3.  Add Menu to an Order");
		System.out.println("4.  Finalize Order");
		System.out.println("5.  View Menu");
		System.out.println("6.  Insert Menu");
		System.out.println("7.  Update Menu");
		System.out.println("8.  Delete Menu");
		System.out.println("9.  Logout");
		System.out.println("10. View Table");
		System.out.println("0. Exit");
		System.out.print("> ");
		
		sect = userInput.nextInt();
		userInput.nextLine();
		
		// Add 2 to section since section 1 and 2 is reserved for home menu and login, except for exit
		if(sect > 0) {
			sect += 2;
		}
		
		return;
	}
	
	// Section 2
	public static void login() {
		System.out.println("Enter employee ID:");
		System.out.print("> ");
		
		int empId = userInput.nextInt();
		userInput.nextLine();
		
		emp.login(empId);
		
		sect = 1;
		
		return;
	}
	
	// Section 3
	public static void viewOrder() {
		emp.viewOrder();
		
		sect = 1;
		
		return;
	}
	
	// Section 4
	public static void addReservation() {
		// Get Customer Name
		System.out.println("Enter customer name:");
		System.out.print("> ");
		String name = userInput.nextLine();
		
		// Get Table List
		int number_of_table;
		ArrayList<Integer> table_id;
		
		System.out.println("How many table?");
		System.out.print("> ");
		number_of_table = userInput.nextInt();
		userInput.nextLine();
		
		while(number_of_table <= 0) { // Invalid number of table 
			System.out.println("Invalid input, please enter the correct number of table");
			number_of_table = userInput.nextInt();
			userInput.nextLine();
		}
		
		int tmp;
		for(int i = 0; i < number_of_table; i++) {
			System.out.print("Enter " + i + "-th table ID: ");
			tmp = userInput.nextInt();
			userInput.nextLine();
			table_id.add(tmp);
		}
		
		// Get number of person
		System.out.println("How many customers for this reservation?");
		System.out.print("> ");
		int number_of_person = userInput.nextInt();
		userInput.nextLine();
		
		while(number_of_person <= 0) { // Invalid number of table 
			System.out.println("Invalid input, please enter the correct number of person");
			number_of_person = userInput.nextInt();
			userInput.nextLine();
		}
		
		emp.addOrderInReseve(name, table_id, number_of_person);
		
		sect = 1;
		
		return;
	}
	
	// Section 5
	public static void addOrder() {
		// Get Order ID
		System.out.println("Enter order ID:");
		System.out.print("> ");
		int order_id = userInput.nextInt();
		userInput.nextLine();
		
		// Get Menu List
		int number_of_menu;
		ArrayList<Integer> menu_id;
		
		System.out.println("How many menu?");
		System.out.print("> ");
		number_of_menu = userInput.nextInt();
		userInput.nextLine();
		
		while(number_of_menu <= 0) { // Invalid number of table 
			System.out.println("Invalid input, please enter the correct number of menu");
			number_of_menu = userInput.nextInt();
			userInput.nextLine();
		}
		
		int tmp;
		for(int i = 0; i < number_of_menu; i++) {
			System.out.print("Enter " + i + "-th menu ID: ");
			tmp = userInput.nextInt();
			userInput.nextLine();
			menu_id.add(tmp);
		}
		
		emp.addMenuToOrder(order_id, menu_id);

		sect = 1;
			
		return;
	}
	
	// Section 6
	public static void finalizeOrder() {
		// Get Order ID
		System.out.println("Enter order ID:");
		System.out.print("> ");
		int order_id = userInput.nextInt();
		userInput.nextLine();
		
		emp.finalizeOrder(order_id);
		
		sect = 1;
		
		return;
	}
	
	// Section 7
	public static void viewMenu() {
		emp.viewMenu();
		
		sect = 1;
		
		return;
	}
	
	// Section 8
	public static void insertMenu() {
		System.out.println("Which type of menu do you want to insert?");
		System.out.println("1. Reguler");
		System.out.println("2. Special");
		System.out.println("3. Local Special");
		System.out.print("> ");
		
		int type = userInput.nextInt();
		userInput.nextLine();
		
		while(type <= 0 || type > 3) {
			System.out.println("Incorrect type! Try again!!");
			System.out.println("Which type of menu do you want to insert?");
			System.out.println("1. Reguler");
			System.out.println("2. Special");
			System.out.println("3. Local Special");
			
			type = userInput.nextInt();
			userInput.nextLine();
		}
		
		
		if(type == 1) {
			// Get Name
			System.out.println("Enter menu name:");
			System.out.print("> ");
			String name = userInput.nextLine();
			
			// Get Price
			System.out.println("Enter menu price:");
			System.out.print("> ");
			double price = userInput.nextDouble();
			
			emp.insertMenu(name, price);
		} else if(type == 2) {
			// Get Name
			System.out.println("Enter menu name:");
			System.out.print("> ");
			String name = userInput.nextLine();
			
			// Get Price
			System.out.println("Enter menu price:");
			System.out.print("> ");
			double price = userInput.nextDouble();
			
			// Get Naration
			System.out.println("Enter a naration for this menu:")
			System.out.print("> ");
			String lore = userInput.nextLine();
			
			emp.insertMenu(name, price, lore);
		} else {
			// Get Name
			System.out.println("Enter menu name:");
			System.out.print("> ");
			String name = userInput.nextLine();
			
			// Get Price
			System.out.println("Enter menu price:");
			System.out.print("> ");
			double price = userInput.nextDouble();
			
			// Get Naration
			System.out.println("Enter a naration for this menu:");
			System.out.print("> ");
			String lore = userInput.nextLine();
			
			// Get Location
			System.out.println("Enter menu's origin location:");
			System.out.print("> ");
			String location = userInput.nextLine();
			
			emp.insertMenu(name, price, lore, location);
		}
		
		sect = 1;
		
		return;
	}
	
	// Section 9
	public static void updateMenu() {
		// Get Menu ID
		System.out.println("Enter menu ID:");
		System.out.print("> ");
		int menu_id = userInput.nextInt();
		userInput.nextLine();
		
		System.out.println("Which one would you like to update?");
		System.out.println("1. Name");
		System.out.println("2. Price");
		System.out.print("> ");
		
		int type = userInput.nextInt();
		userInput.nextLine();
		
		while(type <= 0 || type > 2) {
			System.out.println();
			System.out.println("Invalid input! please try again!!");
			System.out.println("Which one would you like to update?");
			System.out.println("1. Name");
			System.out.println("2. Price");
			System.out.print("> ");
			type = userInput.nextInt();
			userInput.nextLine();
		}
		
		if(type == 1) {
			System.out.println("Enter new menu name:");
			System.out.print("> ");
			String new_name = userInput.nextLine();
			
			emp.updateMenu(menu_id, new_name);
		} else {
			System.out.println("Enter new menu price:");
			System.out.print("> ");
			double new_price = userInput.nextDouble();
			
			emp.updateMenu(menu_id, new_price);
		}
		
		sect = 1;
		
		return;
	}
	
	// Section 10
	public static void deleteMenu() {
		// Get Menu ID
		System.out.println("Enter menu ID:");
		System.out.print("> ");
		int menu_id = userInput.nextInt();
		userInput.nextLine();
		
		emp.deleteMenu(menu_id);
		
		sect = 1;
		return;
	}
	
	// Section 11
	public static void viewTable() {
		emp.viewTable();
		
		sect = 1;
		
		return;
	}
	
	// Section 12
	public static void logout() {
		System.out.println("See you again, " , emp.name , "!");
		
		emp.logout();
		sect = 2;
		
		return;
	}
	
    public static void main(String[] args) {
        sect = 2;
        while(true) {
        	System.out.println();
        	System.out.println();
        	if(sect == 2) {
        		login();
        	} else if (sect == 1) {
        		homeMenu();
        	} else if(sect == 3) {
        		viewOrder();
        	} else if(sect == 4) {
        		addReservation();
        	} else if(sect == 5) {
        		addOrder();
        	} else if(sect == 6) {
        		finalizeOrder();
        	} else if(sect == 7) {
        		viewMenu();
        	} else if(sect == 8) {
        		insertMenu();
        	} else if(sect == 9) {
        		updateMenu();
        	} else if(sect == 10) {
        		deleteMenu();
        	} else if(sect == 11) {
        		viewTable();
        	} else if(sect == 12) {
        		logout();
        	} else if(sect == 0) {
        		break;
        	} else {
        		System.out.println("Invalid section, please try again");
        		
        		if(emp.checkLoggedIn()) {
        			sect = 1;
        		} else {
        			sect = 2;
        		}
        	}
        }
        
        System.out.println("Thank you for your service");
        
    }
}