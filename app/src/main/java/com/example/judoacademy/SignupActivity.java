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

import com.example.judoacademy.Models.Attendance;
import com.example.judoacademy.Models.UserDetails;
import com.example.judoacademy.Utils.Comms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class SignupActivity extends AppCompatActivity {

    EditText keyET, emailET, passwordET, passcheckET;
    Button submitBT;
    GifImageView progressIV;
    private static final String TAG = "SignupActivity";

    FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collref = db.collection("userdetails");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initUI();

        submitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comms.hideKeyboard(SignupActivity.this);
                if (emailET.getText().toString().isEmpty() || passwordET.getText().toString().isEmpty() || passcheckET.getText().toString().isEmpty() || keyET.getText().toString().isEmpty()) {
                    Toast.makeText(SignupActivity.this, "FAIL : Credentials empty!", Toast.LENGTH_LONG).show();
                } else if (!passwordET.getText().toString().trim().equals(passcheckET.getText().toString().trim())) {
                    Toast.makeText(SignupActivity.this, "FAIL : Password not SAME", Toast.LENGTH_LONG).show();
                }else if(passwordET.getText().toString().trim().length() < 8){
                    Toast.makeText(SignupActivity.this, "FAIL : Password too SMALL", Toast.LENGTH_LONG).show();
                }
                else {
                    createLogin(keyET.getText().toString().trim(), emailET.getText().toString().trim(), passwordET.getText().toString().trim());
                }
            }
        });
    }

    private void initUI() {
        keyET = findViewById(R.id.keyET);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        passcheckET = findViewById(R.id.passcheckET);
        submitBT = findViewById(R.id.submitBT);
        mAuth = FirebaseAuth.getInstance();
        progressIV = findViewById(R.id.progressIV);
        progressIV.setVisibility(View.GONE);
    }

    public void createLogin(final String secretKey, final String email, final String password) {
        progressIV.setVisibility(View.VISIBLE);
        collref.whereEqualTo(Comms.email, email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    List<DocumentSnapshot> object = task.getResult().getDocuments();
                    UserDetails tempObject = object.get(0).toObject(UserDetails.class);
                    if (tempObject.getKey()==null){
                        Toast.makeText(SignupActivity.this, "Admin Not Allowed. Only for Coach ", Toast.LENGTH_LONG).show();
                        progressIV.setVisibility(View.GONE);
                    }
                    else if (tempObject.getKey().equals(secretKey)) {
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressIV.setVisibility(View.GONE);
                                if(task.isSuccessful()){
                                    Toast.makeText(SignupActivity.this, "SUCCESS !!!", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(SignupActivity.this, "X ERROR X", Toast.LENGTH_LONG).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressIV.setVisibility(View.GONE);
                                Toast.makeText(SignupActivity.this, "X ERROR X", Toast.LENGTH_LONG).show();
                                Log.i(TAG, "onFailure: " + e.getMessage());
                            }
                        });
                    }
                } else {
                    progressIV.setVisibility(View.GONE);
                    Toast.makeText(SignupActivity.this, "X ERROR X", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressIV.setVisibility(View.GONE);
                Toast.makeText(SignupActivity.this, "X ERROR X", Toast.LENGTH_LONG).show();
                Log.i(TAG, "onFailure: " + e.getMessage());
            }
        });


    }
}