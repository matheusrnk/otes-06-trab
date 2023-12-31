package com.example.client_contacts.views.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.client_contacts.R;
import com.example.client_contacts.models.PersonModel;
import com.example.client_contacts.services.NetworkService;

import com.example.client_contacts.services.services_callbacks.RegisterCallback;
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
    @Pattern(regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "Password must contain at least 8 characters, including letters and numbers!")
    private EditText editTextPassword;
    private TextView errorTextView;

    private Validator validator;

    private final NetworkService networkService;

    public RegisterActivity(){
        networkService = NetworkService.getInstance();
    }

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
        Button registerButton = findViewById(R.id.buttonRegister);
        Button backButton = findViewById(R.id.buttonBackToLoginRegister);

        registerButton.setOnClickListener(v -> validator.validate());

        backButton.setOnClickListener(v -> finish());
    }

    @Override
    public void onValidationSucceeded() {
        String name = editTextName.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        PersonModel personModel = new PersonModel(name, phoneNumber, email, password);

        networkService.registerPerson(personModel, new RegisterCallback(errorTextView, this));
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
