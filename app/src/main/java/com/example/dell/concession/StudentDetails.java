package com.example.dell.concession;

import com.google.firebase.firestore.Exclude;

public class StudentDetails {

    @Exclude private String id;

    private String uid,name,gender,date_of_birth,address,course,year,email;

    public StudentDetails(){ }

    public StudentDetails(String uid, String name, String gender, String date_of_birth, String address, String course, String year,String email) {
        this.uid = uid;
        this.name = name;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.address = address;
        this.course = course;
        this.year = year;
        this.email=email;
    }

    public String getEmail() {
        return email;
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

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public String getAddress() {
        return address;
    }

    public String getCourse() {
        return course;
    }

    public String getYear() {
        return year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
