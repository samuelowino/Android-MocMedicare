package com.manage.hospital.hmapp.model;

public class Doctor {

    private String uuid;
    private String firstName;
    private String lastName;
    private String license;
    private String dob;
    private String gender;
    private String email;
    private String contact;
    private String address;
    private String specialization;
    private String clincalAddress;

    public Doctor() {
    }

    public Doctor(String uuid, String firstName, String lastName, String license, String dob, String gender, String email, String contact, String address, String specialization, String clincalAddress) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.license = license;
        this.dob = dob;
        this.gender = gender;
        this.email = email;
        this.contact = contact;
        this.address = address;
        this.specialization = specialization;
        this.clincalAddress = clincalAddress;
    }

    public String getClincalAddress() {
        return clincalAddress;
    }

    public void setClincalAddress(String clincalAddress) {
        this.clincalAddress = clincalAddress;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public String toString() {
        return "Doctor{" +
                "uuid='" + uuid + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", license='" + license + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", contact='" + contact + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
