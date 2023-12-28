package restaurantreservation.src.main.java.menu;

public class LocalMenu extends Menu {
    String lore;
    String location;

    public void print() {
        System.out.println("ID: " + super.id + " Name: " + super.name + " Price: " + super.price + " Lore: " + this.lore + " Location: " + this.location);
    }

    LocalMenu(Integer id, String name, Double price, String lore, String location) {
        super(id, name, price);
        this.lore = lore;
        this.location = location;
    }
}
