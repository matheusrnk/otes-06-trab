package com.example.client_contacts.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client_contacts.R;
import com.example.client_contacts.interfaces.ContactAddedListener;
import com.example.client_contacts.interfaces.ContactDeletedListener;
import com.example.client_contacts.models.ContactModel;
import com.example.client_contacts.models.PersonModel;
import com.example.client_contacts.services.NetworkService;
import com.example.client_contacts.views.ContactAdapter;
import com.example.client_contacts.views.ContactViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements ContactAddedListener {

    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private List<ContactModel> contactList;

    private Button btnLogout;
    private BottomNavigationView bottomNavigationView;
    private ActivityResultLauncher<Intent> launchAddContactActivity;

    private ContactViewModel contactViewModel = ContactViewModel.getInstance();

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
                    assert loggedPerson != null;
                    goToProfileActivity(loggedPerson);
                }
                return true;
            } else if(itemId == R.id.nav_add_contact){
                if (getIntent().hasExtra("loggedPerson")) {
                    PersonModel loggedPerson = (PersonModel) getIntent().getSerializableExtra("loggedPerson");
                    assert loggedPerson != null;
                    goToAddContactActivity(loggedPerson);
                }
                return true;
            } else if(itemId == R.id.nav_search_contact){
                if (getIntent().hasExtra("loggedPerson")) {
                    PersonModel loggedPerson = (PersonModel) getIntent().getSerializableExtra("loggedPerson");
                    assert loggedPerson != null;
                    goToSearchContactActivity(loggedPerson);
                }
                return true;
            }
            return false;
        });

        contactViewModel.setContactAddedListener(this);

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
        NetworkService networkService = new NetworkService();
        networkService.personById(personLogged.getId(), new Callback<PersonModel>() {
            @Override
            public void onResponse(@NonNull Call<PersonModel> call, @NonNull Response<PersonModel> response) {
                if(response.isSuccessful()){
                    PersonModel updatedPerson = response.body();
                    Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                    intent.putExtra("loggedPerson", updatedPerson);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(@NonNull Call<PersonModel> call, @NonNull Throwable t) {

            }
        });
    }

    private void goToAddContactActivity(PersonModel personLogged){
        NetworkService networkService = new NetworkService();
        networkService.personById(personLogged.getId(), new Callback<PersonModel>() {
            @Override
            public void onResponse(@NonNull Call<PersonModel> call, @NonNull Response<PersonModel> response) {
                if(response.isSuccessful()){
                    PersonModel updatedPerson = response.body();
                    Intent intent = new Intent(HomeActivity.this, AddContactActivity.class);
                    intent.putExtra("loggedPerson", updatedPerson);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(@NonNull Call<PersonModel> call, @NonNull Throwable t) {

            }
        });
    }


    private void goToSearchContactActivity(PersonModel personLogged){
        NetworkService networkService = new NetworkService();
        networkService.personById(personLogged.getId(), new Callback<PersonModel>() {
            @Override
            public void onResponse(@NonNull Call<PersonModel> call, @NonNull Response<PersonModel> response) {
                if(response.isSuccessful()){
                    PersonModel updatedPerson = response.body();
                    Intent intent = new Intent(HomeActivity.this, SearchContactActivity.class);
                    intent.putExtra("loggedPerson", updatedPerson);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(@NonNull Call<PersonModel> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void onContactAdded() {
        contactViewModel.isContactAdded().observe(this, isAdded -> {
            if (isAdded) {
                if (getIntent().hasExtra("loggedPerson")) {
                    PersonModel loggedPerson = (PersonModel) getIntent().getSerializableExtra("loggedPerson");

                    NetworkService networkService = new NetworkService();

                    assert loggedPerson != null;
                    networkService.getContactListForPerson(loggedPerson.getId(), new NetworkService.ContactListListener() {
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
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Lifecycle", "onRestart() called");
        if (getIntent().hasExtra("loggedPerson")) {
            PersonModel loggedPerson = (PersonModel) getIntent().getSerializableExtra("loggedPerson");

            NetworkService networkService = new NetworkService();

            assert loggedPerson != null;
            networkService.getContactListForPerson(loggedPerson.getId(), new NetworkService.ContactListListener() {
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

}

