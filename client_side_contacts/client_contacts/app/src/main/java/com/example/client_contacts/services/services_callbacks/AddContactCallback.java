package com.example.client_contacts.services.services_callbacks;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.client_contacts.views.activities.AddContactActivity;
import com.example.client_contacts.models.ContactModel;
import com.example.client_contacts.views.ContactViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddContactCallback implements Callback<ContactModel> {

    private final TextView errorTextView;
    private final AddContactActivity addContactActivity;
    private final ContactViewModel contactViewModel;

    public AddContactCallback(TextView errorTextView, AddContactActivity addContactActivity,
                              ContactViewModel contactViewModel){
        this.errorTextView = errorTextView;
        this.addContactActivity = addContactActivity;
        this.contactViewModel = contactViewModel;
    }

    @Override
    public void onResponse(@NonNull Call<ContactModel> call, @NonNull Response<ContactModel> response) {
        if(response.isSuccessful()){
            Log.i("Success!", "Contact Sent!");
            errorTextView.setVisibility(View.GONE);
            contactViewModel.setContactAdded(true);
            addContactActivity.finish();
            return;
        }
        Log.e("Did not succeeded!", "Contact Not Sent!");
        errorTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailure(@NonNull Call<ContactModel> call, @NonNull Throwable t) {
        Log.e("Did not succeeded!", "Contact Not Sent!");
        errorTextView.setVisibility(View.VISIBLE);
    }

}
