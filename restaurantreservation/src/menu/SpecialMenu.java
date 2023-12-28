package restaurantreservation.src.menu;

public class SpecialMenu extends Menu {
    String lore;

    public void print() {
        System.out.println("ID: " + super.id + " Name: " + super.name + " Price: " + super.price + " Lore: " + this.lore);
    }

    SpecialMenu(Integer id, String name, Double price, String lore) {
        super(id, name, price);
        this.lore = lore;
    }
}
