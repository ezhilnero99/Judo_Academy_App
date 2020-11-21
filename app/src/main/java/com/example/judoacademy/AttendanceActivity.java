package com.example.judoacademy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.judoacademy.Models.Students;
import com.example.judoacademy.Utils.Comms;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AttendanceActivity extends AppCompatActivity {

    ImageView backIV;
    TextView dateTV;
    Button submitBT;
    RecyclerView attendanceRV;
    int students_list_size = 0;
    private static final String TAG = "attendanceTest";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference studentsCollection = db.collection("students");
    CollectionReference attendanceCollection = db.collection("attendance");

    FirestoreRecyclerAdapter adapter;
    Map<String, String> attendanceHM = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        initUI();
        String dateStamp = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        String timeStamp = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
        String dayOfWeek = new SimpleDateFormat("EE").format(Calendar.getInstance().getTime());
        dateTV.setText(dateStamp);

        submitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentsCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            students_list_size = task.getResult().toObjects(Students.class).size();
                            if (attendanceHM.size() == 0) {
                                Toast.makeText(AttendanceActivity.this, "X EMPTY ATTENDANCE X", Toast.LENGTH_LONG).show();
                            } else if (attendanceHM.size() < students_list_size) {
                                Toast.makeText(AttendanceActivity.this, "X MISSING ATTENDANCE X", Toast.LENGTH_LONG).show();
                            } else {
                                Map<String, Object> uploadObject = new HashMap<>();
                                uploadObject.put(Comms.date, dateStamp);
                                uploadObject.put(Comms.time, timeStamp);
                                uploadObject.put(Comms.dayofweek, dayOfWeek);
                                uploadObject.put(Comms.attendance, attendanceHM);

                                attendanceCollection.add(uploadObject).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AttendanceActivity.this, "! SUCCESS !", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(AttendanceActivity.this, "X FAILED X", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AttendanceActivity.this, "X FAILED X", Toast.LENGTH_LONG).show();
                                        Log.i(TAG, "onComplete: " + e.getMessage());

                                    }
                                });
                            }
                        } else {
                            Toast.makeText(AttendanceActivity.this, "X ERROR X", Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AttendanceActivity.this, "X ERROR X", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onFailure: " + e.getMessage());
                    }
                });


            }
        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setupFireStore();

    }

    private void initUI() {
        backIV = findViewById(R.id.backIV);
        submitBT = findViewById(R.id.submitBT);
        dateTV = findViewById(R.id.dateTV);
        attendanceRV = findViewById(R.id.attendanceRV);
        attendanceRV.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onBackPressed() {
        attendanceHM.clear();
        super.onBackPressed();
    }

    //function to setup Adapter
    void setupFireStore() {
        //Query to get the desired data
        FirestoreRecyclerOptions<Students> options = new FirestoreRecyclerOptions.Builder<Students>().setQuery(studentsCollection, new SnapshotParser<Students>() {
            @NonNull
            @Override
            public Students parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                final Students post = (Students) snapshot.toObject(Students.class);
                return post;
            }
        }).build();

        adapter = new FirestoreRecyclerAdapter<Students, FeedViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FeedViewHolder holder, int i, @NonNull final Students post) {
                holder.nameTV.setText(post.getName());
                holder.ageTV.setText(post.getAge());
                holder.idTV.setText(post.getId());
                holder.attendanceTV.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        String status = holder.attendanceTV.getText().toString();
                        if (status.equals("Present")) {
                            holder.attendanceTV.setText("Absent");
                            holder.attendanceTV.setTextColor(Color.RED);
                        } else {
                            holder.attendanceTV.setText("Present");
                            holder.attendanceTV.setTextColor(Color.parseColor("#00C146"));
                        }
                        String currentStatus = holder.attendanceTV.getText().toString();
                        if (attendanceHM.containsKey(post.getName())) {
                            attendanceHM.replace(post.getName(), currentStatus);
                        } else {
                            attendanceHM.put(post.getName(), currentStatus);
                        }
                    }
                });

            }

            @Override
            public FeedViewHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_attendance, group, false);
                return new FeedViewHolder(view);
            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }
        };

        attendanceRV.setAdapter(adapter);
    }

    /*********************************************************/

    public class FeedViewHolder extends RecyclerView.ViewHolder {

        TextView idTV, nameTV, ageTV, attendanceTV;


        public FeedViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);
            idTV = itemView.findViewById(R.id.idTV);
            ageTV = itemView.findViewById(R.id.ageTV);
            attendanceTV = itemView.findViewById(R.id.attendanceTV);

        }
    }

    /*********************************************************/
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}