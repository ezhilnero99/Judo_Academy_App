package com.example.judoacademy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.judoacademy.Adapter.DetailsAdapter;
import com.example.judoacademy.Models.Attendance;
import com.example.judoacademy.Models.Details;
import com.example.judoacademy.Utils.Comms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AttendanceDetailActivity extends AppCompatActivity {

    ImageView backIV;
    TextView dateTV;
    RecyclerView detailsRV;
    DetailsAdapter adapter;
    ArrayList<Details> detailsAL = new ArrayList<>();

    private static final String TAG = "AttendanceDetailTest";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference attendanceCollection = db.collection("attendance");
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_detail);
        
        initUI();

        Intent intent = getIntent();
        String date = intent.getStringExtra(Comms.date);
        String time = intent.getStringExtra(Comms.time);

        Log.i(TAG, "onCreate: "+date+"   "+time);
        dateTV.setText(date+ " "+time);

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        attendanceCollection.whereEqualTo(Comms.date,date).whereEqualTo(Comms.time,time).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<DocumentSnapshot> object = task.getResult().getDocuments();
                    Attendance temp = object.get(0).toObject(Attendance.class);
                    Log.i(TAG, "onComplete: "+ temp);
                    for (Map.Entry<String,String> entry : temp.getAttendance().entrySet())
                        detailsAL.add(new Details(entry.getKey(),entry.getValue()));
                    adapter = new DetailsAdapter(detailsAL,getApplicationContext());
                    detailsRV.setAdapter(adapter);
                }
                else{
                    Toast.makeText(AttendanceDetailActivity.this, "X ERROR X", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AttendanceDetailActivity.this, "X ERROR X", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onFailure: "+e.getMessage());
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initUI() {
        dateTV = findViewById(R.id.dateTV);
        backIV = findViewById(R.id.backIV);
        detailsRV = findViewById(R.id.detailsRV);
        detailsRV.setLayoutManager(new LinearLayoutManager(this));
    }
}