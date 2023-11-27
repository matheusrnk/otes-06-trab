package com.example.client_contacts.models;

import java.util.List;

import lombok.Data;

@Data
public class PersonModel {

    public PersonModel(String name, String phoneNumber, String email, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private String password;
    private List<ContactModel> contactsModelList;

}
