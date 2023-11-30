package com.example.client_contacts.views.listeners;

import com.example.client_contacts.models.ContactModel;

import java.util.List;

public interface ContactListListener {
    void onContactListReceived(List<ContactModel> contactList);
    void onContactListError(String errorMessage);
}
