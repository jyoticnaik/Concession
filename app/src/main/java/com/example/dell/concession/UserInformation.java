package com.example.dell.concession;

public class UserInformation {

    public String uid,name,gender,yob;

    public UserInformation() {
    }

    public UserInformation(String uid, String name, String gender, String yob) {
        this.uid = uid;
        this.name = name;
        this.gender = gender;
        this.yob = yob;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getYob() {
        return yob;
    }
}
