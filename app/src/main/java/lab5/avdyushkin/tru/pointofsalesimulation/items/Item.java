/*
*   Holds an item that is in the choice list(displayed on the left side)
* */

package lab5.avdyushkin.tru.pointofsalesimulation.items;

public class Item {
    private String path; // image path
    private double price;
    private int imageId; // reference to drawable
    private long columnID; // database reference
    private String name;

    public Item(String name, double price, int imageId) {
        this.name = name;
        this.price = price;
        this.imageId = imageId;
    }

    public Item(String name, double price, String path) {
        this.name = name;
        this.price = price;
        this.path = path;
        this.imageId = 0;
    }

    public Item(String name, double price, String path, long columnID) {
        this.name = name;
        this.price = price;
        this.path = path;
        this.imageId = 0;
        this.columnID = columnID;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getColumnID() {
        return columnID;
    }

    public void setColumnID(long columnID) {
        this.columnID = columnID;
    }

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
