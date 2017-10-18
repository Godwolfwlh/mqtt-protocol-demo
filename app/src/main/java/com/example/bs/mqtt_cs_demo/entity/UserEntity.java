package com.example.bs.mqtt_cs_demo.entity;

/**
 * Created by bs on 2017/8/30.
 */

public class UserEntity {

    //设为单列类
    private UserEntity(){}

    public static UserEntity getInstence(){ return UserEntityTon.sUserEntity;}

    /**
     * 静态内部类
     * **/
    private static class UserEntityTon{
        private static final UserEntity sUserEntity = new UserEntity();
    }

    public String id = new String();
    public String username = new String();
    public String password = new String();
    public String phone = new String();
    public String icon = new String();
    public String nickname = new String();
    public String sex = new String();
    public String age = new String();
    public String profession = new String();
    public String company = new String();
    public String address = new String();
    public String email = new String();

    @Override
    public String toString() {
        return "UserEntity{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", icon='" + icon + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", profession='" + profession + '\'' +
                ", company='" + company + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
