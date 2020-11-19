package com.example.judoacademy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.judoacademy.Utils.Comms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddNotesActivity extends AppCompatActivity {

    ImageView backIV;
    EditText titleET, contentET;
    Button submitBT;
    private static final String TAG = "AddNotesActivity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference notesCollection = db.collection("notes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
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
                String title = titleET.getText().toString();
                String content = contentET.getText().toString();

                if(title.isEmpty() || content.isEmpty()){
                    Toast.makeText(AddNotesActivity.this, "X EMPTY FILEDS X", Toast.LENGTH_SHORT).show();
                }
                else{
                    Map<String,String> object = new HashMap<>();
                    object.put(Comms.title,title);
                    object.put(Comms.content,content);
                    notesCollection.add(object).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AddNotesActivity.this, "! SUCCESS !", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(AddNotesActivity.this, "X ERROR X", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddNotesActivity.this, "X ERROR X", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "onFailure: "+e.getMessage());
                        }
                    });
                }

            }
        });
    }

    private void initUI() {
        backIV = findViewById(R.id.backIV);
        titleET = findViewById(R.id.titleET);
        contentET = findViewById(R.id.contentET);
        submitBT = findViewById(R.id.submitBT);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}