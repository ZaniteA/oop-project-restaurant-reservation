# Final Project: Restaurant Reservation

**LA01 OOP – Group 5**

-   Albert Yulius Ramahalim - 2602078726
-   Christoffer Edbert Karuniawan - 2602082944
-   Kenzie Raditya Tirtarahardja - 2602153581

---

A GitHub repository containing the source files for the final project of the Object Oriented Programming class.

## Database Design

**PICs:** Christoffer, Albert

The database is located in `/RestoManagement.sql`. An entity relationship diagram has been included in `/erd.png`.

The database needs to be imported into a local MySQL server under the name `restaurantreservation`, i.e. through the URL `localhost:3306/restaurantreservation` for the program to work correctly.

## Java source files

This project uses the [MySQL Connector](https://dev.mysql.com/downloads/connector/j/) as the driver for Java Database Connectivity (JDBC) to work with the SQL database.

All source files are in the directory `/restaurantreservation/src/main/java`. The files are separated into a number of folders based on the packages.

### `employee_account` package

**PIC:** Albert

Contains the class `EmployeeAccount`, which is the class that implements the behind-the-scenes methods for the user to interact with the system.

### `main` package

**PIC:** Kenzie

Contains the class `Main`, which handles all the program menus the user goes through during execution of the program.

### `menu` package

**PIC:** Christoffer

Contains the abstract class `Menu` and its subclasses `LocalMenu`, `RegularMenu`, and `SpecialMenu`, which represent the menu objects in the restaurant that can be fetched from the SQL database.

### `order` package

**PICs:** Christoffer, Albert

Contains the class `Order`, which represent the order objects in the restaurant that can be fetched from the SQL database. This class also makes it possible to manipulate the statuses of orders.

### `restaurant` package

**PIC:** Kenzie

Contains the abstract class `Restaurant` and its subclasses `LocalRestaurant` and `MainRestaurant`, which represent the restaurant objects and support operations that is associated with a particular restaurant and its items, i.e. menus and orders.

### `table` package

**PIC:** Albert

Contains the class `Table`, which represent the table objects in the restaurant that can be fetched from the SQL database.
