package com.example.client_contacts.services;

import com.example.client_contacts.models.ContactModel;
import com.example.client_contacts.models.PersonModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("persons")
    Call<PersonModel> registerPerson(@Body PersonModel personModel);

    @POST("persons/login")
    Call<PersonModel> loginPerson(@Query("phoneNumber") String phoneNumber,
                                  @Query("password") String password);

    @POST("contacts/persons/{id}/contacts")
    Call<ContactModel> addContactToPerson(@Path("id") Long id, @Body ContactModel contactModel);

    @GET("persons/{id}")
    Call<PersonModel> getPersonById(@Path("id") Long id);

    @DELETE("contacts/{id}")
    Call<Void> deleteContact(@Path("id") Long id);
}
