package com.example.client_contacts.models;


import lombok.Data;

@Data
public class ContactModel {

    private Long id;
    private String contactName;
    private String phoneNumber;
    private String email;
    private byte[] photo;
    private PersonModel person;

}
