package com.example.client_contacts.services;

import com.example.client_contacts.models.PersonModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
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
}

