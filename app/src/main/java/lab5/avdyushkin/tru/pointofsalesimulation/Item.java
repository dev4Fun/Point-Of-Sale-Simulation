package lab5.avdyushkin.tru.pointofsalesimulation;

public class Item {
    public Item(String name, double price, int imageId) {
        this.name = name;
        this.price = price;
        this.imageId = imageId;
    }

    private String name;
    private double price;
    private int imageId;

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getImageId() {
        return imageId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
