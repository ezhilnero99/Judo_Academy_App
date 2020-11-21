package com.example.judoacademy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.judoacademy.Models.UserDetails;
import com.example.judoacademy.Utils.Comms;
import com.example.judoacademy.Utils.SharedPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class LoginActivity extends AppCompatActivity {

    EditText emailET, passET;
    Button loginBT, signupBT;
    GifImageView progressIV;
    private static final String TAG = "LoginActivity";

    FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collref = db.collection("userdetails");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();

        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comms.hideKeyboard(LoginActivity.this);
                if (emailET.getText().toString().isEmpty() || passET.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Credentials empty!", Toast.LENGTH_SHORT).show();
                } else {
                    loginCheck(emailET.getText().toString().trim(), passET.getText().toString().trim());
                }
            }
        });
        signupBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    private void initUI() {
        emailET = findViewById(R.id.emailET);
        passET = findViewById(R.id.passwordET);
        loginBT = findViewById(R.id.loginBT);
        signupBT = findViewById(R.id.signupBT);
        mAuth = FirebaseAuth.getInstance();
        progressIV = findViewById(R.id.progressIV);
        progressIV.setVisibility(View.GONE);
    }

    public void loginCheck(final String email, String password) {
        progressIV.setVisibility(View.VISIBLE);
        if (!email.isEmpty() && !password.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithEmail:success");
                                //refers to student details
                                collref.whereEqualTo("email", email).get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    progressIV.setVisibility(View.GONE);
                                                    if (task.getResult().isEmpty()) {
                                                        SharedPref.putBoolean(getApplicationContext(), "sp_loggedin", false);
                                                        Log.i(TAG, "onComplete: error fetching data || data empty for email");
                                                        Toast.makeText(LoginActivity.this, "FAILED : Contact Admin", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        List<DocumentSnapshot> object = task.getResult().getDocuments();
                                                        UserDetails userdetails = object.get(0).toObject(UserDetails.class);

                                                        Log.i(TAG, "onSuccess: ");

                                                        SharedPref.putString(getApplicationContext(), "sp_name", userdetails.getName());
                                                        SharedPref.putString(getApplicationContext(), "sp_category", userdetails.getCategory());
                                                        SharedPref.putString(getApplicationContext(), "sp_email", userdetails.getEmail());
                                                        SharedPref.putString(getApplicationContext(), "sp_clearance", userdetails.getClearance());
                                                        SharedPref.putBoolean(getApplicationContext(), "sp_loggedin", true);
                                                        if (userdetails.getClearance().equals("AD")) {
                                                            Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        } else if (userdetails.getClearance().equals("CO")) {
                                                            Intent intent = new Intent(getApplicationContext(), CoachActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        }

                                                    }
                                                } else {
                                                    Toast.makeText(LoginActivity.this, "X ERROR X", Toast.LENGTH_SHORT).show();
                                                    progressIV.setVisibility(View.GONE);
                                                }
                                            }
                                        });

                            } else {
                                progressIV.setVisibility(View.GONE);
                                SharedPref.putBoolean(getApplicationContext(), "sp_loggedin", false);
                                Toast.makeText(LoginActivity.this, "Credentials Wrong !", Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "signInWithEmail:failure", task.getException());
                            }

                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressIV.setVisibility(View.GONE);
                            SharedPref.putBoolean(getApplicationContext(), "sp_loggedin", false);
                            Toast.makeText(LoginActivity.this, "X ERROR X", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "error-1: " + e.toString());
                        }
                    });
        } else {
            progressIV.setVisibility(View.GONE);
            Toast.makeText(this, "Credentials Empty", Toast.LENGTH_SHORT).show();
            SharedPref.putBoolean(getApplicationContext(), "sp_loggedin", false);
        }
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(startMain);
        finishAffinity();
        finish();
    }

}