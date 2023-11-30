package com.example.client_contacts.services;

import com.example.client_contacts.views.listeners.ContactListListener;
import com.example.client_contacts.models.ContactModel;
import com.example.client_contacts.models.PersonModel;
import com.example.client_contacts.services.services_callbacks.ContactsListCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    private final ApiService apiService;
    private static NetworkService instance = null;

    private NetworkService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static NetworkService getInstance(){
        if(instance == null){
            instance = new NetworkService();
        }
        return instance;
    }

    public void registerPerson(PersonModel personModel, Callback<PersonModel> callback) {
        Call<PersonModel> call = apiService.registerPerson(personModel);
        call.enqueue(callback);
    }

    public void loginUser(String phoneNumber, String password, Callback<PersonModel> callback) {
        Call<PersonModel> call = apiService.loginPerson(phoneNumber, password);
        call.enqueue(callback);
    }

    public void personById(Long id, Callback<PersonModel> callback) {
        Call<PersonModel> call = apiService.getPersonById(id);
        call.enqueue(callback);
    }

    public void addContactToPerson(Long id, ContactModel contactModel, Callback<ContactModel> callback){
        Call<ContactModel> call = apiService.addContactToPerson(id, contactModel);
        call.enqueue(callback);
    }

    public void deleteContact(Long id, Callback<Void> callback){
        Call<Void> call = apiService.deleteContact(id);
        call.enqueue(callback);
    }

    public void getContactListForPerson(Long personId, final ContactListListener listener) {
        Call<PersonModel> call = apiService.getPersonById(personId);
        call.enqueue(new ContactsListCallback(listener));
    }
}

