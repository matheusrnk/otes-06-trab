package com.example.client_contacts.services.services_callbacks;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.client_contacts.models.PersonModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatedPersonCallback implements Callback<PersonModel> {

    private final Activity activity;
    private final Class<?> cls;

    public UpdatedPersonCallback(Activity activity, Class<?> cls){
        this.activity = activity;
        this.cls = cls;
    }

    @Override
    public void onResponse(@NonNull Call<PersonModel> call, @NonNull Response<PersonModel> response) {
        if(response.isSuccessful()){
            PersonModel updatedPerson = response.body();
            Intent intent = new Intent(activity, cls);
            intent.putExtra("loggedPerson", updatedPerson);
            activity.startActivity(intent);
        }
    }

    @Override
    public void onFailure(@NonNull Call<PersonModel> call, @NonNull Throwable t) {

    }
}
