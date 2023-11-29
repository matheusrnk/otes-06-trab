package com.example.client_contacts.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.client_contacts.R;
import com.example.client_contacts.models.PersonModel;
import com.example.client_contacts.services.NetworkService;
import com.google.android.material.snackbar.Snackbar;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty(message = "Phone number is required")
    @Pattern(regex = "^[0-9]{5}-[0-9]{4}$", message = "Invalid phone number format. Use XXXXX-XXXX format")
    private EditText editTextPhoneNumber;

    @NotEmpty(message = "Password is required")
    private EditText editTextPassword;
    private TextView errorTextView;
    private Button buttonLogin;
    private Button buttonRegister;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        validator = new Validator(this);
        validator.setValidationListener(this);

        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumberLogin);
        editTextPassword = findViewById(R.id.editTextPasswordLogin);
        errorTextView = findViewById(R.id.textViewErrorLogin);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegisterLogin);

        buttonLogin.setOnClickListener(v -> validator.validate());

        buttonRegister.setOnClickListener(v -> goToRegisterActivity());
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
                    errorTextView.setVisibility(View.GONE);
                    goToHomeActivity(personModelDeserialized);
                } else {
                    int statusCode = response.code();
                    Log.e("Request Failed", "Unsuccessful response: " + response.code());
                    resetFields();
                    errorTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<PersonModel> call, @NonNull Throwable t) {
                Log.e("Request Failed", "Error: " + t.getMessage());
                resetFields();
                errorTextView.setVisibility(View.VISIBLE);
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

    @Override
    public void onValidationSucceeded() {
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        authenticateUser(phoneNumber, password);
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

    private void resetFields(){
        editTextPhoneNumber.setText("");
        editTextPassword.setText("");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        resetFields();
    }
}
