package com.example.judoacademy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.judoacademy.Utils.Comms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentDetailsActivity extends AppCompatActivity {
    EditText nameET, idET, fatherET, ageET, weightET, heightET, categoryET, numberET, emailET;
    EditText genderET;
    Button submitBT;
    ImageView backIV;
    private static final String TAG = "studentsTest";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference studentsCollection = db.collection("students");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        initUI();
        
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        submitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comms.hideKeyboard(StudentDetailsActivity.this);
                String name = nameET.getText().toString();
                String age = ageET.getText().toString();
                String father = fatherET.getText().toString();
                String weight = weightET.getText().toString();
                String height = heightET.getText().toString();
                String gender = genderET.getText().toString();
                String studentId = idET.getText().toString();
                String category = categoryET.getText().toString();
                String number = numberET.getText().toString();
                String email = emailET.getText().toString();
                if(name.isEmpty()
                        || age.isEmpty()
                        || father.isEmpty()
                        || weight.isEmpty()
                        || height.isEmpty()
                        || studentId.isEmpty()
                        || category.isEmpty()
                        || number.isEmpty()
                        || email.isEmpty()
                        || gender.isEmpty()
                        || gender.equals("Pick A Gender:")){
                    Toast.makeText(StudentDetailsActivity.this, "X EMPTY FIELD X", Toast.LENGTH_LONG).show();
                }else if(number.length()!=10){
                    Toast.makeText(StudentDetailsActivity.this, "Invalid Phone Number", Toast.LENGTH_LONG).show();
                }else if(!email.contains("@")) {
                    Toast.makeText(StudentDetailsActivity.this, "Invalid Email", Toast.LENGTH_LONG).show();
                }else{
                    Map<String,String> tempObject= new HashMap<>();
                    tempObject.put(Comms.name,name);
                    tempObject.put(Comms.father,father);
                    tempObject.put(Comms.age,age);
                    tempObject.put(Comms.gender,gender);
                    tempObject.put(Comms.weight,weight);
                    tempObject.put(Comms.height,height);
                    tempObject.put(Comms.studentId,studentId);
                    tempObject.put(Comms.category,category);
                    tempObject.put(Comms.number,number);
                    tempObject.put(Comms.email,email);

                    studentsCollection.add(tempObject).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(StudentDetailsActivity.this, "! SUCCESS !", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(StudentDetailsActivity.this, "X ERROR X", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(StudentDetailsActivity.this, "X ERROR X", Toast.LENGTH_LONG).show();
                            Log.i(TAG, "onFailure: " + e.getMessage());
                        }
                    });
                }
            }
        });
    }

    private void initUI() {
        backIV = findViewById(R.id.backIV);
        nameET = findViewById(R.id.nameET);
        fatherET = findViewById(R.id.fatherET);
        ageET = findViewById(R.id.ageET);
        weightET = findViewById(R.id.weightET);
        heightET = findViewById(R.id.heightET);
        idET = findViewById(R.id.idET);
        categoryET = findViewById(R.id.categoryET);
        numberET = findViewById(R.id.numberET);
        emailET = findViewById(R.id.emailET);
        genderET = findViewById(R.id.genderET);
        submitBT = findViewById(R.id.submitBT);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}