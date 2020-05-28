package com.example.instantreservation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

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

    View btnLogIn;
    ProgressButton progressButton;
    private FirebaseAuth mAuth;

    EditText ETemail, ETpassword;
    String email, password;

    AwesomeValidation awesomeValidation;

   /* @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    } */

    private void updateUI(FirebaseUser currentUser) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle("");



        btnLogIn = findViewById(R.id.btn_sign_in);
        progressButton = new ProgressButton(LoginActivity.this, btnLogIn, "LOG IN");


        ETemail = findViewById(R.id.edit_text_email);
        ETpassword = findViewById(R.id.edit_text_password);

        //initialize Validation Style
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        //Add Validation for mail
        //awesomeValidation.addValidation(this, R.id.edit_text_email, RegexTemplate.NOT_EMPTY,R.string.invalid_email);


        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressButton.buttonActivated();
                email = ETemail.getText().toString();
                password = ETpassword.getText().toString();

                if (!validateEmail() | !validatePassword()) {
                    progressButton.buttonFinished("Something went wrong :(");
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressButton.buttonFinished("DONE");
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("LOG IN", "signInWithEmail:success");
                                    Toast.makeText(LoginActivity.this, "Authentication Success.",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    //updateUI(user);
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    progressButton.buttonFinished("Something went wrong :(");
                                    // If sign in fails, display a message to the user.
                                    Log.w("LOG IN", "signInWithEmail:failure", task.getException());
                                    Log.d("LOG IN", "<" + email + " " + password + ">");
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                    // ...
                                }

                                // ...
                            }
                        });
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

}


