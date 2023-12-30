package table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Table {

    // A table, which contains data about the capacity and status (taken or not).

    public Integer id;
    public Integer capacity;
    public Boolean taken;

    // Constant for mapping table capacities to table names.
    static Map<Integer, String> table_types = Stream.of(new Object[][] {
            { 2, "Romantic" },
            { 4, "General" },
            { 10, "Family" }
    }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (String) data[1]));

    // Constructor to create a new table.
    public Table(Integer id, Integer capacity, Boolean taken) {
        this.id = id;
        this.capacity = capacity;
        this.taken = taken;
    }

    // Constructor to create a new table without specifying its ID.
    public Table(Integer capacity, Boolean taken) {
        this.capacity = capacity;
        this.taken = taken;

        this.id = null;
    }

    // Method to create a table based on the database.
    // If the ID does not exist in the database, returns NULL.
    public static Table createFromID(Integer id, Connection sql_connection) {
        try {
            PreparedStatement table_pst = sql_connection.prepareStatement("select * from MsTable where TableID = ?");
            table_pst.setInt(1, id);
            ResultSet table_rs = table_pst.executeQuery();

            Integer capacity = 0;
            Boolean taken = false;

            while (table_rs.next()) {
                capacity = table_rs.getInt("Capacity");
                taken = table_rs.getBoolean("Taken");
            }

            if (capacity != 0) {
                return new Table(id, capacity, taken);
            }

        } catch (Exception e) {
            System.out.println("PreparedStatement failure");
            e.printStackTrace();
        }

        // If the ID is invalid, or an exception happened
        return null;
    }

    // Prints the table for display.
    public void print() {
        // Get the type of the table
        String type = table_types.getOrDefault(capacity, "Unknown");
        String taken_string = (taken ? "Yes" : "No");

        System.out.printf("ID      : %d\n", id);
        System.out.printf("Type    : %s\n", type);
        System.out.printf("Capacity: %d\n", capacity);
        System.out.printf("Taken   : %s\n", taken_string);
    }

}
