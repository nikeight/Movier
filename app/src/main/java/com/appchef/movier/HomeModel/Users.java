package com.appchef.movier.HomeModel;

public class Users {

    String fullName, username, age, gender, imageUrl, contactNumber,uniqueID;

    public Users() {
        // Empty Const. to prevent from crashing.
    }

    public Users(String fullName, String username, String age, String gender, String imageUrl, String contactNumber, String uniqueID) {
        this.fullName = fullName;
        this.username = username;
        this.age = age;
        this.gender = gender;
        this.imageUrl = imageUrl;
        this.contactNumber = contactNumber;
        this.uniqueID = uniqueID;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
