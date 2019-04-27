package com.manage.hospital.hmapp.model;

public class User {

    private String userEmail;
    private String userPassword;
    private String userUuid;
    private boolean isPatient;
    private String photoUri;

    public User() {
    }

    public User(String userEmail, String userPassword, String userUuid, boolean isPatient) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userUuid = userUuid;
        this.isPatient = isPatient;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public boolean isPatient() {
        return isPatient;
    }

    public void setPatient(boolean patient) {
        isPatient = patient;
    }

    @Override
    public String toString() {
        return "User{" +
                "userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userUuid='" + userUuid + '\'' +
                ", isPatient=" + isPatient +
                '}';
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String toString) {

    }
}
