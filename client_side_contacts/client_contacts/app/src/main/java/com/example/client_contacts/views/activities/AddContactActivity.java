package com.example.client_contacts.views.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.client_contacts.R;
import com.example.client_contacts.models.ContactModel;
import com.example.client_contacts.models.PersonModel;
import com.example.client_contacts.services.NetworkService;
import com.example.client_contacts.services.services_callbacks.AddContactCallback;
import com.example.client_contacts.views.ContactViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.List;

public class AddContactActivity extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty(message = "Name is required")
    private EditText editTextContactName;

    @NotEmpty(message = "Phone number is required")
    @Pattern(regex = "^[0-9]{5}-[0-9]{4}$", message = "Invalid phone number format. Use XXXXX-XXXX format")
    private EditText editTextPhoneNumber;

    @NotEmpty(message = "Email is required")
    @Pattern(regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email address!")
    private EditText editTextEmail;
    private TextView errorTextView;

    private Validator validator;

    private final ContactViewModel contactViewModel;
    private final NetworkService networkService;

    private PersonModel loggedPerson;

    public AddContactActivity(){
        contactViewModel = ContactViewModel.getInstance();
        networkService = NetworkService.getInstance();
    }

    private void retrieveLoggedPerson(){
        if (getIntent().hasExtra("loggedPerson")) {
            loggedPerson = (PersonModel) getIntent().getSerializableExtra("loggedPerson");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontact);

        retrieveLoggedPerson();

        validator = new Validator(this);
        validator.setValidationListener(this);

        editTextContactName = findViewById(R.id.editTextContactName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        errorTextView = findViewById(R.id.textViewErrorAddContact);
        Button buttonAddContact = findViewById(R.id.buttonAddContact);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationAddContact);
        ImageButton backButton = findViewById(R.id.backButtonToolbarAddContact);

        backButton.setOnClickListener(v -> finish());

        buttonAddContact.setOnClickListener(v -> validator.validate());

        bottomNavigationView.setOnItemSelectedListener(this::guideBottomButtonActions);
    }

    private boolean guideBottomButtonActions(MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        if(itemId == R.id.nav_profile){
            goToProfileActivity(loggedPerson);
            return true;
        } else if(itemId == R.id.nav_search_contact){
            goToSearchContactActivity(loggedPerson);
            return true;
        }
        return false;
    }

    private void addContactToPerson(Long id, ContactModel contactModel) {
        networkService.addContactToPerson(id, contactModel,
                new AddContactCallback(errorTextView, this, contactViewModel));
    }

    @Override
    public void onValidationSucceeded() {
        String contactName = editTextContactName.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        ContactModel contactModel = new ContactModel(contactName, phoneNumber, email);

        addContactToPerson(loggedPerson.getId(), contactModel);
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

}

