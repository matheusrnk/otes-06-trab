package com.example.client_contacts.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.client_contacts.R;
import com.example.client_contacts.models.PersonModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.MessageFormat;

public class ProfileActivity extends AppCompatActivity {

    private TextView textName, textPhoneNumber, textEmail;
    private PersonModel loggedPerson;

    private void retrieveLoggedPerson(){
        if (getIntent().hasExtra("loggedPerson")) {
            loggedPerson = (PersonModel) getIntent().getSerializableExtra("loggedPerson");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        retrieveLoggedPerson();

        textName = findViewById(R.id.textName);
        textPhoneNumber = findViewById(R.id.textPhoneNumber);
        textEmail = findViewById(R.id.textEmail);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationProfile);

        ImageButton backButton = findViewById(R.id.backButtonToolbar);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if(itemId == R.id.nav_add_contact){
                goToAddContactActivity(loggedPerson);
                return true;
            } else if(itemId == R.id.nav_search_contact){
                goToSearchContactActivity(loggedPerson);
                return true;
            }
            return false;
        });

        backButton.setOnClickListener(v -> onSupportNavigateUp());

        fetchAndDisplayProfileDetails();
    }

    private void fetchAndDisplayProfileDetails() {
        PersonModel loggedInPerson = loggedPerson;

        if (loggedInPerson != null) {
            textName.setText(MessageFormat.format("{0}{1}", getString(R.string.nameTextProfile), loggedInPerson.getName()));
            textPhoneNumber.setText(MessageFormat.format("{0}{1}", getString(R.string.phoneTextProfile), loggedInPerson.getPhoneNumber()));
            textEmail.setText(MessageFormat.format("{0}{1}", getString(R.string.emailTextProfile), loggedInPerson.getEmail()));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void goToAddContactActivity(PersonModel personLogged){
        Intent intent = new Intent(this, AddContactActivity.class);
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

