package com.example.client_contacts.models;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class PersonModel implements Serializable {

    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private String password;
    private List<ContactModel> contacts;

    public PersonModel() {

    }

    public PersonModel(String name, String phoneNumber, String email, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setContactsModelList(List<ContactModel> contacts) {
        this.contacts = contacts;
    }

    public List<ContactModel> getContactsModelList() {
        return contacts;
    }
}
