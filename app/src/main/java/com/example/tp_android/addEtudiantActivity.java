package com.example.tp_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class addEtudiantActivity extends AppCompatActivity {

    com.google.android.material.textfield.TextInputEditText nom_etudiant;
    com.google.android.material.textfield.TextInputEditText prenom_etudiant;
    com.google.android.material.textfield.TextInputEditText tel_etudiant;
    Button add_etudiant;
    FirebaseFirestore db;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_etudiant);

        nom_etudiant = findViewById(R.id.nom_etudiant);
        prenom_etudiant = findViewById(R.id.prenom_etudiant);
        tel_etudiant = findViewById(R.id.tel_etudiant);
        db = FirebaseFirestore.getInstance();
        add_etudiant = findViewById(R.id.add_etudiant);

        add_etudiant.setOnClickListener(view -> {
            createEtudiant();
        });
    }

    private void createEtudiant() {
        Map<String, Object> etudiant = new HashMap<>();

        String nom = nom_etudiant.getText().toString();
        String prenom = prenom_etudiant.getText().toString();
        String tel = tel_etudiant.getText().toString();
        etudiant.put("nom", nom);
        etudiant.put("prenom", prenom);
        etudiant.put("tel", tel);

        String emailCurrentUser=  FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String mdpCu= "123456";

        db.collection("etudiant")
                .add(etudiant)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        System.out.println("DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(addEtudiantActivity.this, "User added successfully", Toast.LENGTH_SHORT).show();
                        mAuth.signInWithEmailAndPassword(emailCurrentUser,mdpCu).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    startActivity(new Intent(addEtudiantActivity.this, MainActivity.class));
                                }else{
                                    Toast.makeText(addEtudiantActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        startActivity(new Intent(addEtudiantActivity.this, ListEtudiantActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Erreur" + e);
                    }
                });



    }




}