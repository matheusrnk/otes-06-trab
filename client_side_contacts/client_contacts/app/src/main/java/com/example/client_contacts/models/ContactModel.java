package com.example.client_contacts.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

public class ContactModel implements Serializable {

    private Long id;
    private String contactName;
    private String phoneNumber;
    private String email;
    private byte[] photo;
    private PersonModel person;

    public ContactModel(){

    }

    public ContactModel(String contactName, String phoneNumber, String email) {
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactName() {
        return contactName;
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

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public Bitmap getPhoto() {
        return bytes2Bitmap(photo);
    }

    public void setPerson(PersonModel person) {
        this.person = person;
    }

    public PersonModel getPerson() {
        return person;
    }

    private static Bitmap bytes2Bitmap(byte[] b) {
        if (b == null) {
            return null;
        }
        if (b.length != 0) {
            InputStream is = new ByteArrayInputStream(b);
            return BitmapFactory.decodeStream(is);
        } else {
            return null;
        }
    }
}
