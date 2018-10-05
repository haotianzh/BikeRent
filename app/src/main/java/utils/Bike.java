package utils;

import cn.bmob.v3.BmobObject;

/**
 * Created by HAOTIAN on 2016/8/4.
 */
public class Bike extends BmobObject {
    private String name;
    private String owner;
    private String desc;
    private String price;
    private String image;
    private String type;
    private boolean isrent;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean setIsrent() {
        return isrent;
    }

    public void setIsrent(boolean isrent) {
        this.isrent = isrent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
