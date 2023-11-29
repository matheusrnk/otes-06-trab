package com.example.client_contacts.views;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.client_contacts.interfaces.ContactAddedListener;

public class ContactViewModel extends ViewModel {

    private static ContactViewModel instance = null;

    private MutableLiveData<Boolean> contactAdded = new MutableLiveData<>();
    private ContactAddedListener contactAddedListener;

    private ContactViewModel(){}

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

