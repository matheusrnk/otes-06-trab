package com.example.client_contacts.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.client_contacts.R;
import com.example.client_contacts.models.PersonModel;
import com.example.client_contacts.services.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.List;


public class RegisterActivity extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty(message = "Name is required")
    private EditText editTextName;

    @NotEmpty(message = "Phone number is required")
    @Pattern(regex = "^[0-9]{5}-[0-9]{4}$", message = "Invalid phone number format. Use XXXXX-XXXX format")
    private EditText editTextPhoneNumber;

    @NotEmpty(message = "Email is required")
    @Pattern(regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email address!")
    private EditText editTextEmail;

    @NotEmpty(message = "Password is required")
    @Pattern(regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "Password must contain at least 8 characters, including letters and numbers!")
    private EditText editTextPassword;
    private TextView errorTextView;

    private Button registerButton;
    private Button backButton;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        validator = new Validator(this);
        validator.setValidationListener(this);

        editTextName = findViewById(R.id.editTextNameRegister);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumberRegister);
        editTextEmail = findViewById(R.id.editTextEmailRegister);
        editTextPassword = findViewById(R.id.editTextPasswordRegister);
        errorTextView = findViewById(R.id.textViewErrorRegister);
        registerButton = findViewById(R.id.buttonRegister);
        backButton = findViewById(R.id.buttonBackToLoginRegister);

        registerButton.setOnClickListener(v -> validator.validate());

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

                    int statusCode = response.code();
                    Log.e("Request Succeed", "Successful response: " + response.code());
                    errorTextView.setVisibility(View.GONE);
                    finish();
                } else {
                    int statusCode = response.code();
                    Log.e("Request Failed", "Unsuccessful response: " + response.code());
                    errorTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<PersonModel> call, @NonNull Throwable t) {
                Log.e("Request Failed", "Error: " + t.getMessage());
                errorTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        NetworkService networkService = new NetworkService();
        registerUserMethod(networkService);
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
        editTextName.setText("");
        editTextPhoneNumber.setText("");
        editTextEmail.setText("");
        editTextPassword.setText("");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        resetFields();
    }
}
