package utils;

import cn.bmob.v3.BmobUser;

/**
 * Created by HAOTIAN on 2016/8/2.
 */
public class MyUser extends BmobUser {

     private String realname;
     private String address;
     private String studentId;

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return "MyUser{" +
                "realname='" + realname + '\'' +
                ", address='" + address + '\'' +
                ", studentId='" + studentId + '\'' +
                '}';
    }
}
