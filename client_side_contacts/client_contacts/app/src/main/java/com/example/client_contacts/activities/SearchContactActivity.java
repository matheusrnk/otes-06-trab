package com.example.client_contacts.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client_contacts.R;
import com.example.client_contacts.models.ContactModel;
import com.example.client_contacts.models.PersonModel;
import com.example.client_contacts.views.ContactAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SearchContactActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSearchResults;
    private List<ContactModel> searchResults;

    private PersonModel loggedPerson;

    private void retrieveLoggedPerson(){
        if (getIntent().hasExtra("loggedPerson")) {
            loggedPerson = (PersonModel) getIntent().getSerializableExtra("loggedPerson");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchcontact);

        retrieveLoggedPerson();

        ImageButton backButton = findViewById(R.id.backButtonToolbarSearchContact);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationViewSearchContact);

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

        backButton.setOnClickListener(v -> finish());

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if(itemId == R.id.nav_profile){
                    goToProfileActivity(loggedPerson);
                    return true;
                } else if(itemId == R.id.nav_add_contact){
                    goToAddContactActivity(loggedPerson);
                    return true;
                }
                return false;
            }
        });

    }

    private void performSearch(String query) {
        searchResults.clear();
        searchResults.addAll(getSearchResultsFromAPI(query));

        ContactAdapter contactAdapter = new ContactAdapter(searchResults, this);
        recyclerViewSearchResults.setAdapter(contactAdapter);
    }

    private List<ContactModel> getSearchResultsFromAPI(String query) {
        List<ContactModel> contactModels = loggedPerson.getContactsModelList();
        if(contactModels == null){
            return new ArrayList<>();
        }
        return filterContacts(query, contactModels);
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

