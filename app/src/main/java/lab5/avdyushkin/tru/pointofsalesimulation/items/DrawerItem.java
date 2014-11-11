package lab5.avdyushkin.tru.pointofsalesimulation.items;

public class DrawerItem {
    private int imageId; // reference to drawable
    private String name;

    public DrawerItem(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setName(String name) {
        this.name = name;
    }

}
