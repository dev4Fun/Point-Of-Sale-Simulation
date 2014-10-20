package lab5.avdyushkin.tru.pointofsalesimulation;

public class ItemInCard extends Item {
    private int quantity;

    public ItemInCard(String name, double price, int imageId, int quantity) {
        super(name, price, imageId);
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
