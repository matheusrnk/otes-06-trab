package com.example.client_contacts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.client_contacts.models.PersonModel;
import com.example.client_contacts.services.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextPhoneNumber;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button registerButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        NetworkService networkService = new NetworkService();

        editTextName = findViewById(R.id.editTextNameRegister);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumberRegister);
        editTextEmail = findViewById(R.id.editTextEmailRegister);
        editTextPassword = findViewById(R.id.editTextPasswordRegister);
        registerButton = findViewById(R.id.buttonRegister);
        backButton = findViewById(R.id.buttonBackToLoginRegister);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUserMethod(networkService);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }

    private void registerUserMethod(NetworkService networkService){
        String name = editTextName.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        PersonModel personModel = new PersonModel(name, phoneNumber, email, password);

        networkService.registerUser(personModel, new Callback<PersonModel>() {
            @Override
            public void onResponse(@NonNull Call<PersonModel> call, @NonNull Response<PersonModel> response) {
                if(response.isSuccessful()){
                    PersonModel personModelDeserialized = response.body();
                    //do something
                    int statusCode = response.code();
                    Log.e("Request Failed", "Successful response: " + response.code());
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

}
