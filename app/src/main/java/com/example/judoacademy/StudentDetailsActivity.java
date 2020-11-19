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
    EditText nameET, fatherET, ageET, weightET, heightET;
    Spinner genderSP;
    Button submitBT;
    ImageView backIV;
    ArrayAdapter<String> adapter;
    ArrayList<String> genderAL = new ArrayList<>();
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
                String gender = genderSP.getSelectedItem().toString();
                if(name.isEmpty()
                        || age.isEmpty()
                        || father.isEmpty()
                        || weight.isEmpty()
                        || height.isEmpty()
                        || gender.isEmpty()
                        || gender.equals("Pick A Gender:")){
                    Toast.makeText(StudentDetailsActivity.this, "X EMPTY FIELD X", Toast.LENGTH_LONG).show();
                }
                else{
                    Map<String,String> tempObject= new HashMap<>();
                    tempObject.put(Comms.name,name);
                    tempObject.put(Comms.father,father);
                    tempObject.put(Comms.age,age);
                    tempObject.put(Comms.gender,gender);
                    tempObject.put(Comms.weight,weight);
                    tempObject.put(Comms.height,height);

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
        genderSP = findViewById(R.id.genderSP);
        submitBT = findViewById(R.id.submitBT);

        genderAL.add("Pick A Gender:");
        genderAL.add("male");
        genderAL.add("female");

        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, genderAL);

        genderSP.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}