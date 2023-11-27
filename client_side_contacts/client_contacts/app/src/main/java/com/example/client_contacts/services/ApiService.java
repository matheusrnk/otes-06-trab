package com.example.client_contacts.services;

import com.example.client_contacts.models.PersonModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("persons")
    Call<PersonModel> registerPerson(@Body PersonModel personModel);

    @POST("persons/login")
    Call<PersonModel> loginPerson(@Query("phoneNumber") String phoneNumber,
                                  @Query("password") String password);
}
