package com.example.client_contacts.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.client_contacts.R;
import com.example.client_contacts.models.PersonModel;
import com.example.client_contacts.services.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextPhoneNumber;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumberLogin);
        editTextPassword = findViewById(R.id.editTextPasswordLogin);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String phoneNumber = editTextPhoneNumber.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if(validateInputs(phoneNumber, password)){
                    authenticateUser(phoneNumber, password);
                }
            }
        });

        buttonRegister = findViewById(R.id.buttonRegisterLogin);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                goToRegisterActivity();
            }
        });
    }

    private boolean validateInputs(String phoneNumber, String password){
        return true;
    }

    private void authenticateUser(String phoneNumber, String password){
        NetworkService networkService = new NetworkService();

        networkService.loginUser(phoneNumber, password, new Callback<PersonModel>() {
            @Override
            public void onResponse(@NonNull Call<PersonModel> call, @NonNull Response<PersonModel> response) {
                if(response.isSuccessful()){
                    PersonModel personModelDeserialized = response.body();

                    int statusCode = response.code();
                    Log.i("Request Complete", "Successful response: " + response.code());
                    assert personModelDeserialized != null;
                    goToHomeActivity(personModelDeserialized);
                } else {
                    int statusCode = response.code();
                    Log.e("Request Failed", "Unsuccessful response: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PersonModel> call, @NonNull Throwable t) {
                Log.e("Request Failed", "Error: " + t.getMessage());
            }
        });

    }

    private void goToRegisterActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void goToHomeActivity(PersonModel personLogged){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("loggedPerson", personLogged);
        startActivity(intent);
    }

}
