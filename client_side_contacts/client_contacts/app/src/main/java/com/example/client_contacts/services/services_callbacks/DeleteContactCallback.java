package com.example.client_contacts.services.services_callbacks;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.client_contacts.models.ContactModel;
import com.example.client_contacts.views.ContactAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteContactCallback implements Callback<Void> {

    private final List<ContactModel> contactModelList;
    private final ContactAdapter contactAdapter;
    private int position;

    public DeleteContactCallback(List<ContactModel> contactModelList, ContactAdapter contactAdapter,
                                 int position){
        this.contactModelList = contactModelList;
        this.contactAdapter = contactAdapter;
        this.position = position;
    }

    @Override
    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
        if(response.isSuccessful()){
            contactModelList.remove(position);
            contactAdapter.notifyItemRemoved(position);
            contactAdapter.showDeletionFeedback();
            Log.i("Success", "Contact Deleted");
            return;
        }
        Log.i("Failed", "Contact Not Deleted!");
    }

    @Override
    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
        Log.e("Failed", "Contact Not Deleted! " + t.getMessage());
    }
}
