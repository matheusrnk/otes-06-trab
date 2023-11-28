package com.example.client_contacts.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.example.client_contacts.R;
import com.example.client_contacts.models.ContactModel;
import com.example.client_contacts.models.PersonModel;
import com.example.client_contacts.services.NetworkService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddContactActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editTextContactName, editTextPhoneNumber, editTextEmail;
    private ImageView imagePhoto;
    private BottomNavigationView bottomNavigationView;
    private Button buttonAddContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontact);

        toolbar = findViewById(R.id.toolbarAddContact);

        editTextContactName = findViewById(R.id.editTextContactName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        imagePhoto = findViewById(R.id.imagePhoto);
        buttonAddContact = findViewById(R.id.buttonAddContact);
        bottomNavigationView = findViewById(R.id.bottomNavigationAddContact);
        ImageButton backButton = findViewById(R.id.backButtonToolbarAddContact);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
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

                }

            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if(itemId == R.id.nav_profile){
                if (getIntent().hasExtra("loggedPerson")) {
                    PersonModel loggedPerson = (PersonModel) getIntent().getSerializableExtra("loggedPerson");
                    goToProfileActivity(loggedPerson);
                }
                return true;
            } else if(itemId == R.id.nav_search_contact){
                if (getIntent().hasExtra("loggedPerson")) {
                    PersonModel loggedPerson = (PersonModel) getIntent().getSerializableExtra("loggedPerson");
                    goToSearchContactActivity(loggedPerson);
                }
                return true;
            }
            return false;
        });
    }

    private void addContactToPerson(Long id, ContactModel contactModel) {
        NetworkService networkService = new NetworkService();

        networkService.addContactToPerson(id, contactModel, new Callback<ContactModel>() {
            @Override
            public void onResponse(@NonNull Call<ContactModel> call, @NonNull Response<ContactModel> response) {
                if(response.isSuccessful()){
                    Log.i("Success!", "Contact Sent!");
                    setResultAndFinish(id);
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

    private void setResultAndFinish(Long id) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("addedContactToPersonId", id);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void goToProfileActivity(PersonModel personLogged){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("loggedPerson", personLogged);
        startActivity(intent);
    }

    private void goToSearchContactActivity(PersonModel personLogged){
        Intent intent = new Intent(this, SearchContactActivity.class);
        intent.putExtra("loggedPerson", personLogged);
        startActivity(intent);
    }
}

