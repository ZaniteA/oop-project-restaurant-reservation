package restaurantreservation.src.order;

public class Order {
    Integer id;
    String customerName;
    Integer persons;
    Integer status;

    String[] statusName = new String[3];
    statusName = {"in reserve", "in order", "finalized"};

    Order(Integer id, String customerName, Integer persons, Integer status) {
        this.id = id;
        this.customerName = customerName;
        this.persons = persons;
        this.status = status;
    }

    public void print() {
        System.out.println("ID: " + this.id + " Customer Name: " + this.customerName + " persons: " + this.persons + " status: " + this.statusName[status]);
    }

    public void setOrderStatus(Integer newStatus) {
        status = newStatus;
    }

    public void setTable(Integer newStatus) {
        ;
    }

    public void addMenu(Integer menuId) {
        ;
    }

    public void printBill() {
        ;
    }
}
