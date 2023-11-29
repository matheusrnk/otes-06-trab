package com.example.client_contacts.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client_contacts.R;
import com.example.client_contacts.interfaces.ContactDeletedListener;
import com.example.client_contacts.models.ContactModel;
import com.example.client_contacts.models.PersonModel;
import com.example.client_contacts.services.NetworkService;
import com.example.client_contacts.views.ContactAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchContactActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSearchResults;
    private ContactAdapter contactAdapter;
    private List<ContactModel> searchResults;
    private ImageButton backButton;
    private BottomNavigationView bottomNavigationView;
    private ActivityResultLauncher<Intent> launchAddContactActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchcontact);

        backButton = findViewById(R.id.backButtonToolbarSearchContact);
        bottomNavigationView = findViewById(R.id.bottomNavigationViewSearchContact);

        recyclerViewSearchResults = findViewById(R.id.recyclerViewSearchResults);
        recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(this));
        searchResults = new ArrayList<>();

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return false;
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                }
                return false;
            }
        });

    }

    private void performSearch(String query) {
        searchResults.clear();
        searchResults.addAll(getSearchResultsFromAPI(query));

        contactAdapter = new ContactAdapter(searchResults, this);
        recyclerViewSearchResults.setAdapter(contactAdapter);
    }

    private List<ContactModel> getSearchResultsFromAPI(String query) {
        NetworkService networkService = new NetworkService();
        if (getIntent().hasExtra("loggedPerson")) {
            PersonModel loggedPerson = (PersonModel) getIntent().getSerializableExtra("loggedPerson");

            assert loggedPerson != null;
            List<ContactModel> contactModels = loggedPerson.getContactsModelList();
            return filterContacts(query, contactModels);
        }
        return new ArrayList<>();
    }

    private List<ContactModel> filterContacts(String query, List<ContactModel> contactModels) {
        return contactModels.stream().filter(contactModel -> contactModel.getContactName().toLowerCase(Locale.getDefault())
                .contains(query.toLowerCase(Locale.getDefault()))).collect(Collectors.toList());
    }

    private void goToProfileActivity(PersonModel personLogged){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("loggedPerson", personLogged);
        startActivity(intent);
        finish();
    }

    private void goToAddContactActivity(PersonModel personLogged){
        Intent intent = new Intent(this, AddContactActivity.class);
        intent.putExtra("loggedPerson", personLogged);

        startActivity(intent);
        finish();
    }

}

