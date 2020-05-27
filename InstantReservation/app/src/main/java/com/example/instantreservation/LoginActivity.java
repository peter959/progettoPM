package com.example.instantreservation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button btnLogIn;
    private FirebaseAuth mAuth;

    EditText ETemail, ETpassword;
    String email, password;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        btnLogIn = findViewById(R.id.btnLogIn);
        ETemail = findViewById(R.id.edit_text_email);
        ETpassword = findViewById(R.id.edit_text_password);


        //System.out.println("aaaaaaaaaaaaaaa" + email + " " + password);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = ETemail.getText().toString();
                password = ETpassword.getText().toString();
                //Log.d("LOG IN", "<"+email + " " + password+">");

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
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
                                    // If sign in fails, display a message to the user.
                                    Log.w("LOG IN", "signInWithEmail:failure", task.getException());
                                    Log.d("LOG IN", "<"+email + " " + password+">");
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
}
