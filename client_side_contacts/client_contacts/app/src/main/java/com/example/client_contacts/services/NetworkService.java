package com.example.client_contacts.services;

import androidx.annotation.NonNull;

import com.example.client_contacts.models.ContactModel;
import com.example.client_contacts.models.PersonModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    private final ApiService apiService;

    public NetworkService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public void registerUser(PersonModel personModel, Callback<PersonModel> callback) {
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

    public List<ContactModel> searchContacts(String query) {

        return new ArrayList<>();
    }

    public interface ContactListListener {
        void onContactListReceived(List<ContactModel> contactList);
        void onContactListError(String errorMessage);
    }

    public void getContactListForPerson(Long personId, final ContactListListener listener) {
        Call<PersonModel> call = apiService.getPersonById(personId);

        call.enqueue(new Callback<PersonModel>() {
            @Override
            public void onResponse(@NonNull Call<PersonModel> call, @NonNull Response<PersonModel> response) {
                if (response.isSuccessful()) {
                    PersonModel personModel = response.body();
                    if (personModel != null) {
                        List<ContactModel> contactList = personModel.getContactsModelList();
                        listener.onContactListReceived(contactList);
                    } else {
                        listener.onContactListError("PersonModel is null");
                    }
                } else {
                    listener.onContactListError("Unsuccessful response: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PersonModel> call, @NonNull Throwable t) {
                listener.onContactListError("Failed to fetch data: " + t.getMessage());
            }
        });
    }
}

