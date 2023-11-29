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
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.client_contacts.R;
import com.example.client_contacts.models.ContactModel;
import com.example.client_contacts.models.PersonModel;
import com.example.client_contacts.services.NetworkService;
import com.example.client_contacts.views.ContactViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddContactActivity extends AppCompatActivity implements Validator.ValidationListener {

    private Toolbar toolbar;
    private ImageView imagePhoto;

    @NotEmpty(message = "Name is required")
    private EditText editTextContactName;

    @NotEmpty(message = "Phone number is required")
    @Pattern(regex = "^[0-9]{5}-[0-9]{4}$", message = "Invalid phone number format. Use XXXXX-XXXX format")
    private EditText editTextPhoneNumber;

    @NotEmpty(message = "Email is required")
    @Pattern(regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email address!")
    private EditText editTextEmail;
    private TextView errorTextView;
    private BottomNavigationView bottomNavigationView;
    private Button buttonAddContact;

    private Validator validator;

    private ContactViewModel contactViewModel = ContactViewModel.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontact);

        toolbar = findViewById(R.id.toolbarAddContact);

        validator = new Validator(this);
        validator.setValidationListener(this);

        editTextContactName = findViewById(R.id.editTextContactName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        imagePhoto = findViewById(R.id.imagePhoto);
        errorTextView = findViewById(R.id.textViewErrorAddContact);
        buttonAddContact = findViewById(R.id.buttonAddContact);
        bottomNavigationView = findViewById(R.id.bottomNavigationAddContact);
        ImageButton backButton = findViewById(R.id.backButtonToolbarAddContact);

        backButton.setOnClickListener(v -> onSupportNavigateUp());

        buttonAddContact.setOnClickListener(v -> validator.validate());

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
                    errorTextView.setVisibility(View.GONE);
                    contactViewModel.setContactAdded(true);
                    finish();
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
        finish();
    }

    private void goToSearchContactActivity(PersonModel personLogged){
        Intent intent = new Intent(this, SearchContactActivity.class);
        intent.putExtra("loggedPerson", personLogged);
        startActivity(intent);
        finish();
    }

    @Override
    public void onValidationSucceeded() {
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

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            }
        }
    }
}

