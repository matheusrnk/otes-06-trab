package com.example.client_contacts.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client_contacts.R;
import com.example.client_contacts.models.ContactModel;
import com.example.client_contacts.models.PersonModel;
import com.example.client_contacts.services.NetworkService;
import com.example.client_contacts.views.ContactAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private List<ContactModel> contactList;

    private Button btnLogout;
    private BottomNavigationView bottomNavigationView;
    private ActivityResultLauncher<Intent> launchAddContactActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (getIntent().hasExtra("loggedPerson")) {
            PersonModel loggedPerson = (PersonModel) getIntent().getSerializableExtra("loggedPerson");

            if (loggedPerson != null) {
                getContactList(loggedPerson.getId());
            }
        }

        btnLogout = findViewById(R.id.btnLogout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
            } else if(itemId == R.id.nav_add_contact){
                if (getIntent().hasExtra("loggedPerson")) {
                    PersonModel loggedPerson = (PersonModel) getIntent().getSerializableExtra("loggedPerson");
                    goToAddContactActivity(loggedPerson);
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

        launchAddContactActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            Long id = result.getData().getLongExtra("addedContactToPersonId", -1);

                            NetworkService networkService = new NetworkService();

                           networkService.getContactListForPerson(id, new NetworkService.ContactListListener() {
                               @Override
                               public void onContactListReceived(List<ContactModel> contactList) {
                                   contactAdapter.updateContactList(contactList);
                               }

                               @Override
                               public void onContactListError(String errorMessage) {

                               }
                           });
                        }
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        Log.e("Failed!", "Activity Canceled");
                    }
                });

    }

    private void getContactList(Long personId) {
        NetworkService networkService = new NetworkService();

        networkService.getContactListForPerson(personId, new NetworkService.ContactListListener() {
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
        recyclerView = findViewById(R.id.recyclerViewContacts);

        contactAdapter = new ContactAdapter(contactList, this);
        recyclerView.setAdapter(contactAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void goToProfileActivity(PersonModel personLogged){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("loggedPerson", personLogged);
        startActivity(intent);
    }

    private void goToAddContactActivity(PersonModel personLogged){
        Intent intent = new Intent(this, AddContactActivity.class);
        intent.putExtra("loggedPerson", personLogged);

        launchAddContactActivity.launch(intent);
    }


    private void goToSearchContactActivity(PersonModel personLogged){
        Intent intent = new Intent(this, SearchContactActivity.class);
        intent.putExtra("loggedPerson", personLogged);
        startActivity(intent);
    }

}

