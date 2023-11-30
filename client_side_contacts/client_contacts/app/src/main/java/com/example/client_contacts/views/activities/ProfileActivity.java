package com.example.client_contacts.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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

        bottomNavigationView.setOnItemSelectedListener(this::guideBottomButtonActions);

        backButton.setOnClickListener(v -> finish());

        fetchAndDisplayProfileDetails();
    }

    private boolean guideBottomButtonActions(MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        if(itemId == R.id.nav_add_contact){
            goToAddContactActivity(loggedPerson);
            return true;
        } else if(itemId == R.id.nav_search_contact){
            goToSearchContactActivity(loggedPerson);
            return true;
        }
        return false;
    }

    private void fetchAndDisplayProfileDetails() {
        if (loggedPerson != null) {
            textName.setText(MessageFormat.format("{0}{1}", getString(R.string.nameTextProfile), loggedPerson.getName()));
            textPhoneNumber.setText(MessageFormat.format("{0}{1}", getString(R.string.phoneTextProfile), loggedPerson.getPhoneNumber()));
            textEmail.setText(MessageFormat.format("{0}{1}", getString(R.string.emailTextProfile), loggedPerson.getEmail()));
        }
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

