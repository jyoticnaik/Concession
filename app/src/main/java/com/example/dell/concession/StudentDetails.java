package com.example.dell.concession;

import com.google.firebase.firestore.Exclude;

public class StudentDetails {

    private String name,gender,date_of_birth,address,pincode,course,year,email;

    public StudentDetails(){ }

    public StudentDetails(String name, String gender, String date_of_birth, String address,String pincode, String course, String year,String email) {
        this.name = name;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.address = address;
        this.pincode = pincode;
        this.course = course;
        this.year = year;
        this.email=email;
    }

    public String getPincode() {
        return pincode;
    }

    public String getEmail() {
        return email;
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

}
