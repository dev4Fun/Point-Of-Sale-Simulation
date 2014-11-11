/*
*   Holds an item that is in the cart list(displayed on the right side)
*   extends an item by adding quantity
* */

package lab5.avdyushkin.tru.pointofsalesimulation.items;


public class ItemInCart extends Item {
    private int quantity;

    public ItemInCart(String name, double price, int imageId, int quantity) {
        super(name, price, imageId);
        this.quantity = quantity;
    }

    public ItemInCart(String name, double price, String path, int quantity) {
        super(name, price, path);
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
