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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.instantreservationbusiness.ProgressButton;
import com.example.instantreservationbusiness.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    View btnLogIn;
    ProgressButton progressButton;
    EditText ETemail, ETpassword;
    String email, password;
    TextView txtRegister;

    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        //initialize toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle("");

        // initialize views
        btnLogIn = findViewById(R.id.btn_sign_in);
        progressButton = new ProgressButton(LoginActivity.this, btnLogIn, "LOG IN");
        ETemail = findViewById(R.id.edit_text_email);
        ETpassword = findViewById(R.id.edit_text_password);
        txtRegister = findViewById(R.id.txtRegister);

        //initialize Validation Style
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Login Button
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
                                    // if the user is successfully connected, check if it is a business user
                                    firebaseUser = mAuth.getCurrentUser();

                                    DatabaseReference CheckBusinessUserReference = FirebaseDatabase.getInstance().getReference().child("business");
                                    CheckBusinessUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (!dataSnapshot.hasChild(firebaseUser.getUid())) {
                                                // if it is not a business user, show wronAccountActivity
                                                Intent intent = new Intent(getApplicationContext(), WrongAccountActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else {
                                                // if the user is successfully connected, save his data in shared preferences
                                                mDatabase = FirebaseDatabase.getInstance().getReference().child("business").child(firebaseUser.getUid());
                                                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        String name =  dataSnapshot.child("business_name").getValue().toString();
                                                        String phone =  dataSnapshot.child("business_phone").getValue().toString();
                                                        String city =  dataSnapshot.child("business_city").getValue().toString();

                                                        //SHARED PREFERENCES
                                                        SharedPreferences sharedPreferences = getSharedPreferences("BusinessInfo", Context.MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.putString("businessUID", firebaseUser.getUid());
                                                        editor.putString("businessName", name);
                                                        editor.putString("businessCity", city);
                                                        editor.putString("businessEmail", email);
                                                        editor.putString("businessPhone", phone);
                                                        editor.commit();
                                                        System.out.println("-------Logged: email: " + email + ", name: " + name + ", phone:" + phone + ", UID: " + firebaseUser.getUid());

                                                        progressButton.buttonFinished("DONE");
                                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                } else {
                                    // show errors
                                    progressButton.buttonFinished("Something went wrong :(");
                                    // If sign in fails, display a message to the user.
                                    Log.w("LOG IN", "signInWithEmail:failure", task.getException());
                                    Log.d("LOG IN", "<" + email + " " + password + ">");
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });
    }

    //tab bar options
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

}


