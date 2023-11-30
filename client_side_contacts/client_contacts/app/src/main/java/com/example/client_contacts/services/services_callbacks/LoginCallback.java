package com.example.client_contacts.services.services_callbacks;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.client_contacts.views.activities.LoginActivity;
import com.example.client_contacts.models.PersonModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginCallback implements Callback<PersonModel> {

    private final TextView errorTextView;
    private final LoginActivity loginActivity;

    public LoginCallback(TextView errorTextView, LoginActivity activity){
        this.errorTextView = errorTextView;
        this.loginActivity = activity;
    }

    @Override
    public void onResponse(@NonNull Call<PersonModel> call, @NonNull Response<PersonModel> response) {
        if(response.isSuccessful()){
            PersonModel personModelDeserialized = response.body();

            Log.i("Request Complete", "Successful response: " + response.code());
            assert personModelDeserialized != null;
            errorTextView.setVisibility(View.GONE);
            loginActivity.goToHomeActivity(personModelDeserialized);
        } else {
            Log.e("Request Failed", "Unsuccessful response: " + response.code());
            loginActivity.resetFields();
            errorTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailure(@NonNull Call<PersonModel> call, @NonNull Throwable t) {
        Log.e("Request Failed", "Error: " + t.getMessage());
        loginActivity.resetFields();
        errorTextView.setVisibility(View.VISIBLE);
    }
}
