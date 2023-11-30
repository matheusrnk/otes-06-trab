package com.example.client_contacts.services.services_callbacks;

import androidx.annotation.NonNull;

import com.example.client_contacts.views.listeners.ContactListListener;
import com.example.client_contacts.models.ContactModel;
import com.example.client_contacts.models.PersonModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactsListCallback implements Callback<PersonModel> {

    private final ContactListListener contactListListener;

    public ContactsListCallback(ContactListListener contactListListener){
        this.contactListListener = contactListListener;
    }

    @Override
    public void onResponse(@NonNull Call<PersonModel> call, @NonNull Response<PersonModel> response) {
        if (response.isSuccessful()) {
            PersonModel personModel = response.body();
            if (personModel != null) {
                List<ContactModel> contactList = personModel.getContactsModelList();
                contactListListener.onContactListReceived(contactList);
            } else {
                contactListListener.onContactListError("PersonModel is null");
            }
        } else {
            contactListListener.onContactListError("Unsuccessful response: " + response.code());
        }
    }

    @Override
    public void onFailure(@NonNull Call<PersonModel> call, @NonNull Throwable t) {
        contactListListener.onContactListError("Failed to fetch data: " + t.getMessage());
    }
}
