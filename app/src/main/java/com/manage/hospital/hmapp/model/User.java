package com.manage.hospital.hmapp.model;

public class User {

    private String userEmail;
    private String userPassword;
    private String userUuid;
    private String role;
    private String photoUri;

    private String firstName;
    private String lastName;
    private String uuid;

    public User() {
    }

    public User(String userEmail, String userPassword, String userUuid, String role) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userUuid = userUuid;
        this.role = role;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "User{" +
                "userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userUuid='" + userUuid + '\'' +
                ", role='" + role + '\'' +
                ", photoUri='" + photoUri + '\'' +
                '}';
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String toString) {

    }
}
