package com.example.client_contacts.services.services_callbacks;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.client_contacts.views.activities.RegisterActivity;
import com.example.client_contacts.models.PersonModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterCallback implements Callback<PersonModel> {

    private final TextView errorTextView;
    private final RegisterActivity registerActivity;

    public RegisterCallback(TextView errorTextView, RegisterActivity registerActivity){
        this.errorTextView = errorTextView;
        this.registerActivity = registerActivity;
    }

    @Override
    public void onResponse(@NonNull Call<PersonModel> call, @NonNull Response<PersonModel> response) {
        if(response.isSuccessful()){
            Log.e("Request Succeed", "Successful response: " + response.code());
            errorTextView.setVisibility(View.GONE);
            registerActivity.finish();
        } else {
            Log.e("Request Failed", "Unsuccessful response: " + response.code());
            errorTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailure(@NonNull Call<PersonModel> call, @NonNull Throwable t) {
        Log.e("Request Failed", "Error: " + t.getMessage());
        errorTextView.setVisibility(View.VISIBLE);
    }
}
