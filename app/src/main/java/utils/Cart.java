package utils;

import cn.bmob.v3.BmobObject;

/**
 * Created by HAOTIAN on 2016/8/7.
 */
public class Cart extends BmobObject {
    private String bikeid;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBikeid() {
        return bikeid;
    }

    public void setBikeid(String bikeid) {
        this.bikeid = bikeid;
    }
}
