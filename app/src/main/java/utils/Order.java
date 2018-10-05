package utils;

import cn.bmob.v3.BmobObject;

/**
 * Created by HAOTIAN on 2016/8/7.
 */
public class Order extends BmobObject {

    private String image;
    private String price;

    public Boolean getIschecked() {
        return ischecked;
    }

    public void setIschecked(Boolean ischecked) {
        this.ischecked = ischecked;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private Boolean ischecked;

}
