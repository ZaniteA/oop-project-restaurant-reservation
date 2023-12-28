package restaurantreservation.src.menu;

public abstract class Menu {
    Integer id;
    String name;
    Double price;

    abstract public void print();

    Menu(Integer id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
