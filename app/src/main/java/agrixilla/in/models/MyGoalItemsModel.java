package agrixilla.in.models;

/**
 * Created by Ind3 on 04-05-18.
 */

public class MyGoalItemsModel {
    int id;
    int images;
    String name;

    public MyGoalItemsModel(int images, String name) {
        this.images = images;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImages() {
        return images;
    }

    public void setImages(int images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
