package com.example.client_contacts.views;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.client_contacts.views.listeners.ContactAddedListener;

public class ContactViewModel extends ViewModel {

    private static ContactViewModel instance = null;

    private final MutableLiveData<Boolean> contactAdded;
    private ContactAddedListener contactAddedListener;

    private ContactViewModel(){
        contactAdded = new MutableLiveData<>();
    }

    public static ContactViewModel getInstance(){
        if(instance == null){
            instance = new ContactViewModel();
        }
        return instance;
    }

    public void setContactAdded(boolean value) {
        contactAdded.setValue(value);
        notifyContactAdded();
    }

    public LiveData<Boolean> isContactAdded() {
        return contactAdded;
    }

    public void setContactAddedListener(ContactAddedListener contactAddedListener) {
        this.contactAddedListener = contactAddedListener;
    }

    public void notifyContactAdded(){
        if(contactAddedListener != null){
            contactAddedListener.onContactAdded();
        }
    }
}

