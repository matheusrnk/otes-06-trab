package com.example.client_contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.example.client_contacts.models.ContactModel;
import com.example.client_contacts.models.PersonModel;
import com.example.client_contacts.services.NetworkService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddContactActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editTextContactName, editTextPhoneNumber, editTextEmail;
    private ImageView imagePhoto;
    private Button buttonAddContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontact);

        toolbar = findViewById(R.id.toolbarAddContact);
        setSupportActionBar(toolbar);

        editTextContactName = findViewById(R.id.editTextContactName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        imagePhoto = findViewById(R.id.imagePhoto);
        buttonAddContact = findViewById(R.id.buttonAddContact);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactName = editTextContactName.getText().toString().trim();
                String phoneNumber = editTextPhoneNumber.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();

                if (getIntent().hasExtra("loggedPerson")) {
                    PersonModel loggedPerson = (PersonModel) getIntent().getSerializableExtra("loggedPerson");

                    ContactModel contactModel = new ContactModel(contactName, phoneNumber, email);

                    assert loggedPerson != null;
                    addContactToPerson(loggedPerson.getId(), contactModel);

                    finish();
                }

            }
        });
    }

    private void addContactToPerson(Long id, ContactModel contactModel) {
        NetworkService networkService = new NetworkService();

        networkService.addContactToPerson(id, contactModel, new Callback<ContactModel>() {
            @Override
            public void onResponse(@NonNull Call<ContactModel> call, @NonNull Response<ContactModel> response) {
                if(response.isSuccessful()){
                    Log.i("Success!", "Contact Sent!");
                    return;
                }
                Log.e("Did not succeeded!", "Contact Not Sent!");
            }

            @Override
            public void onFailure(@NonNull Call<ContactModel> call, @NonNull Throwable t) {
                Log.e("Did not succeeded!", "Contact Not Sent!");
            }
        });
    }
}

