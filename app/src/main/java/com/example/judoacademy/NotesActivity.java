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
import android.widget.TextView;

import com.example.judoacademy.Models.Notes;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class NotesActivity extends AppCompatActivity {

    ImageView backIV,addIV;
    RecyclerView notesRV;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference notesCollection = db.collection("notes");

    FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        initUI();

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotesActivity.this, AddNotesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        
        setupFireStore();

    }

    private void initUI() {
        backIV = findViewById(R.id.backIV);
        addIV = findViewById(R.id.addIV);
        notesRV = findViewById(R.id.notesRV);
        notesRV.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //function to setup Adapter
    void setupFireStore() {
        //Query to get the desired data
        FirestoreRecyclerOptions<Notes> options = new FirestoreRecyclerOptions.Builder<Notes>().setQuery(notesCollection, new SnapshotParser<Notes>() {
            @NonNull
            @Override
            public Notes parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                final Notes post = (Notes) snapshot.toObject(Notes.class);
                return post;
            }
        }).build();

        adapter = new FirestoreRecyclerAdapter<Notes, NotesActivity.FeedViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final NotesActivity.FeedViewHolder holder, int i, @NonNull final Notes post) {
                holder.titleTV.setText(post.getTitle());
                holder.contentTV.setText(post.getContent());
            }

            @Override
            public NotesActivity.FeedViewHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_note, group, false);
                return new NotesActivity.FeedViewHolder(view);
            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }
        };

        notesRV.setAdapter(adapter);
    }

    /*********************************************************/

    public class FeedViewHolder extends RecyclerView.ViewHolder {

        TextView titleTV, contentTV;


        public FeedViewHolder(View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.titleTV);
            contentTV = itemView.findViewById(R.id.contentTV);

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