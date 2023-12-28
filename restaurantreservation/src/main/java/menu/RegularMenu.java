package restaurantreservation.src.main.java.menu;

public class RegularMenu extends Menu {
    public void print() {
        System.out.println("ID: " + super.id + " Name: " + super.name + " Price: " + super.price);
    }

    RegularMenu(Integer id, String name, Double price) {
        super(id, name, price);
    }
}
