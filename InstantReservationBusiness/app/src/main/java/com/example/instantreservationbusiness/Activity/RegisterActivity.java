package com.example.instantreservationbusiness.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.instantreservationbusiness.Business;
import com.example.instantreservationbusiness.ProgressButton;
import com.example.instantreservationbusiness.R;
import com.example.instantreservationbusiness.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {

    // pattern for password validation format
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    //Button btnSignUp;
    View btnSignUp;
    ProgressButton progressButton;
    
    EditText ETemail, ETpassword, ETname, ETphone, ETconfPass, ETcity;
    String email, password, name, phone, city;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //initialize toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle("");

        //initialize views
        btnSignUp = findViewById(R.id.btn_sign_up);
        progressButton = new ProgressButton(RegisterActivity.this, btnSignUp, "Get Started");
        ETname = findViewById(R.id.et_name);
        ETphone = findViewById(R.id.et_phone);
        ETemail = findViewById(R.id.et_email);
        ETpassword = findViewById(R.id.et_password);
        ETconfPass = findViewById(R.id.et_confirm_password);
        ETcity = findViewById(R.id.et_city);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressButton.buttonActivated();
                name = ETname.getText().toString();
                email = ETemail.getText().toString();
                city = ETcity.getText().toString();
                password = ETpassword.getText().toString();
                phone = ETphone.getText().toString();

                if (!validateEmail() | !validateUsername() | !validatePhone() | !validatePassword() | !confirmPassword()) {
                    progressButton.buttonFinished("Something went wrong :(");
                    return;
                }


                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    // Sign in success, save user info in firebase and in shared preferences
                                    Log.d("Register", "createUserWithEmail:success");

                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();

                                    Business business = new Business(name, email, phone, city);
                                    mDatabase.child("business").child(firebaseUser.getUid()).setValue(business);

                                    //SHARED PREFERENCES
                                    SharedPreferences sharedPreferences = getSharedPreferences("BusinessInfo", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("businessUID", firebaseUser.getUid());
                                    editor.putString("businessName", name);
                                    editor.putString("businessCity", city);
                                    editor.putString("businessEmail", email);
                                    editor.putString("businessPhone", phone);
                                    editor.commit();

                                    progressButton.buttonFinished("DONE");

                                    //navigate to MainActivity
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    progressButton.buttonFinished("Something went wrong :(");
                                    // If sign in fails, display a message to the user.
                                    Log.w("Register", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                }});

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Email validation
    private boolean validateEmail() {
        String emailInput = ETemail.getText().toString().trim();
        if (emailInput.isEmpty()) {
            ETemail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            ETemail.setError("Please enter a valid email address");
            return false;
        } else {
            ETemail.setError(null);
            return true;
        }
    }

    //Username validation
    private boolean validateUsername() {
        String usernameInput = ETname.getText().toString().trim();
        if (usernameInput.isEmpty()) {
            ETname.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 15) {
            ETname.setError("Username too long");
            return false;
        } else {
            ETname.setError(null);
            return true;
        }
    }

    //Phone validation
    private boolean validatePhone() {
        String phonenumber = ETphone.getText().toString().trim();
        if (phonenumber.isEmpty()) {
            ETphone.setError("Field can't be empty");
            return false;
        } else if (!Patterns.PHONE.matcher(phonenumber).matches()) {
            ETphone.setError("Username too long");
            return false;
        } else {
            ETphone.setError(null);
            return true;
        }
    }

    //Password validation
    private boolean validatePassword() {
        String passwordInput = ETpassword.getText().toString().trim();
        if (passwordInput.isEmpty()) {
            ETpassword.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            ETpassword.setError("Password too weak");
            return false;
        } else {
            ETpassword.setError(null);
            return true;
        }
    }

    //Confirm Password validation
    private boolean confirmPassword() {
        String passwordInput = ETpassword.getText().toString().trim();
        String confirmpasswordInput = ETconfPass.getText().toString().trim();

        if (confirmpasswordInput.isEmpty()) {
            ETconfPass.setError("Field can't be empty");
            return false;
        } else if(!passwordInput.equals(confirmpasswordInput)) {
            ETconfPass.setError("Passwords entered are not the same");
            return false;
        } else {
            ETconfPass.setError(null);
            return true;
        }

    }

}