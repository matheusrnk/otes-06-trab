package com.example.client_contacts.views.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client_contacts.R;
import com.example.client_contacts.views.listeners.ContactAddedListener;
import com.example.client_contacts.views.listeners.ContactListListener;
import com.example.client_contacts.models.ContactModel;
import com.example.client_contacts.models.PersonModel;
import com.example.client_contacts.services.NetworkService;
import com.example.client_contacts.services.services_callbacks.UpdatedPersonCallback;
import com.example.client_contacts.views.ContactAdapter;
import com.example.client_contacts.views.ContactViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements ContactAddedListener {

    private ContactAdapter contactAdapter;

    private final ContactViewModel contactViewModel;

    private final NetworkService networkService;

    private PersonModel loggedPerson;

    public HomeActivity(){
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
        setContentView(R.layout.activity_home);

        retrieveLoggedPerson();
        getContactList(loggedPerson.getId());

        Button btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(v -> finish());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(this::guideBottomButtonActions);

        contactViewModel.setContactAddedListener(this);

    }

    private boolean guideBottomButtonActions(MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        if(itemId == R.id.nav_profile){
            goToProfileActivity(loggedPerson);
            return true;
        } else if(itemId == R.id.nav_add_contact){
            goToAddContactActivity(loggedPerson);
            return true;
        } else if(itemId == R.id.nav_search_contact){
            goToSearchContactActivity(loggedPerson);
            return true;
        }
        return false;
    }

    private void getContactList(Long personId) {
        networkService.getContactListForPerson(personId, new ContactListListener() {
            @Override
            public void onContactListReceived(List<ContactModel> contactList) {
                displayContactList(contactList);
            }

            @Override
            public void onContactListError(String errorMessage) {
                Log.e("Error", errorMessage);
            }
        });
    }

    private void displayContactList(List<ContactModel> contactList) {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewContacts);

        contactAdapter = new ContactAdapter(contactList, this);

        recyclerView.setAdapter(contactAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void goToProfileActivity(PersonModel personLogged){
        networkService.personById(personLogged.getId(),
                new UpdatedPersonCallback(this, ProfileActivity.class));
    }

    private void goToAddContactActivity(PersonModel personLogged){
        networkService.personById(personLogged.getId(),
                new UpdatedPersonCallback(this, AddContactActivity.class));
    }


    private void goToSearchContactActivity(PersonModel personLogged){
        networkService.personById(personLogged.getId(),
                new UpdatedPersonCallback(this, SearchContactActivity.class));
    }

    @Override
    public void onContactAdded() {
        contactViewModel.isContactAdded().observe(this, isAdded -> {
            if (isAdded) {
                networkService.getContactListForPerson(loggedPerson.getId(), new ContactListListener() {
                    @Override
                    public void onContactListReceived(List<ContactModel> contactList) {
                        contactAdapter.updateContactList(contactList);
                    }

                    @Override
                    public void onContactListError(String errorMessage) {

                    }
                });
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        networkService.getContactListForPerson(loggedPerson.getId(), new ContactListListener() {
            @Override
            public void onContactListReceived(List<ContactModel> contactList) {
                contactAdapter.updateContactList(contactList);
            }

            @Override
            public void onContactListError(String errorMessage) {

            }
        });
    }

}

