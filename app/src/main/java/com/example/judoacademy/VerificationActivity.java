package com.example.judoacademy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.judoacademy.Models.Attendance;
import com.example.judoacademy.Utils.Comms;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class VerificationActivity extends AppCompatActivity {

    ImageView backIV;
    RecyclerView dateRV;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference attendanceCollection = db.collection("attendance");
    FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        initUI();
        setupFireStore();
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initUI() {
        backIV = findViewById(R.id.backIV);
        dateRV = findViewById(R.id.dateRV);
        dateRV.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //function to setup Adapter
    void setupFireStore() {
        //Query to get the desired data
        FirestoreRecyclerOptions<Attendance> options = new FirestoreRecyclerOptions.Builder<Attendance>().setQuery(attendanceCollection, new SnapshotParser<Attendance>() {
            @NonNull
            @Override
            public Attendance parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                final Attendance post = (Attendance) snapshot.toObject(Attendance.class);
                return post;
            }
        }).build();

        adapter = new FirestoreRecyclerAdapter<Attendance, VerificationActivity.DateViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final VerificationActivity.DateViewHolder holder, int i, @NonNull final Attendance post) {
               holder.dateTV.setText(post.getDate()+", "+post.getDayofweek());
               holder.timeTV.setText((post.getTime()));
               holder.dateRL.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                        Intent intent = new Intent(VerificationActivity.this,AttendanceDetailActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Comms.date,post.getDate());
                        intent.putExtra(Comms.time,post.getTime());
                        startActivity(intent);
                   }
               });

            }

            @Override
            public VerificationActivity.DateViewHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_date, group, false);
                return new DateViewHolder(view);
            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }
        };

        dateRV.setAdapter(adapter);
    }

    /*********************************************************/

    public class DateViewHolder extends RecyclerView.ViewHolder {

        TextView dateTV, timeTV;
        RelativeLayout dateRL;


        public DateViewHolder(View itemView) {
            super(itemView);
            dateTV = itemView.findViewById(R.id.dateTV);
            timeTV = itemView.findViewById(R.id.timeTV);
            dateRL = itemView.findViewById(R.id.dateRL);

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