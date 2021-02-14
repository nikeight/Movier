package com.appchef.movier.Model;

public class ModelUser {
    String uid, profileImage, userName;

    public ModelUser() {
    }

    public ModelUser(String uid, String profileImage, String userName) {
        this.uid = uid;
        this.profileImage = profileImage;
        this.userName = userName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
