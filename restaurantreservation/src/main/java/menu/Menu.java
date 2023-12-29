package menu;

public abstract class Menu {

    // Abstract class of a menu.

    public Integer id;
    public String name;
    public Double price;

    public Menu(Integer id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    abstract public void print();

}
