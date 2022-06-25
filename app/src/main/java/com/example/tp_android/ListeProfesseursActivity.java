package com.example.tp_android;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.app.ProgressDialog;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tp_android.adapetrs.ProfAdapter;
import com.example.tp_android.databinding.ActivityMainBinding;
import com.example.tp_android.model.Professeur;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedList;

public class ListeProfesseursActivity extends AppCompatActivity {

    ListView list_prof;
    LinkedList<Professeur> profs;
    FirebaseFirestore db;




    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_liste_professeurs);


     list_prof=(ListView) findViewById(R.id.list_view);
     db=FirebaseFirestore.getInstance();



        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (currentUser == null) {
            // No user is signed in
            System.out.println("Makayn walo");
        } else {
            // User logged in
            System.out.println(currentUser+"HANAMONTANA");
        }


        profs=new LinkedList<Professeur>();
        getallProfesseurs();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_prof);
        String emailCurrentUser=  FirebaseAuth.getInstance().getCurrentUser().getEmail();

        if(emailCurrentUser.equals("admin@gmail.com")) {
            fab.setVisibility(View.VISIBLE);
        }

        else{
            fab.setVisibility(View.GONE);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ListeProfesseursActivity.this, AddProfActivity.class);
                startActivity(intent);
            }
        });


    }
    void getallProfesseurs(){

        showProgressDialog();
        //test
        Task<QuerySnapshot> snapshot = db.collection("professeur").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull  Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        System.out.println(document.getId()+"55EWAAA");
                    }
                }
            }
        });

        //end test
        db.collection("professeur")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getId());
                                System.out.println(document.getString("nom"));
                                System.out.println(document.getString("prenom"));
                                System.out.println(document.getString("tel"));
                                System.out.println(document.getString("photo"));
                                System.out.println(document.getString("departement"));
                                Professeur p = new Professeur(
                                        document.getString("nom"),
                                        document.getString("prenom"),
                                        document.getString("tel"),
                                        document.getString("photo"),
                                        document.getString("departement"),
                                        document.getId(),
                                        document.getString("email"),
                                        document.getString("mdp"));
                                profs.add(p);
                            }
                            System.out.println(profs);
                        } else {
                            System.out.println("Erreur");
                        }

                        hideProgressDialog();

//                        ArrayList<String> professeurs = new ArrayList<String>();
//                        for (Professeur prof: profs){
//                            professeurs.add(prof.getNom());
//                            professeurs.add(prof.getDepartement());
//                            professeurs.add(prof.getPrenom());
//                            professeurs.add(prof.getTel());
//                            professeurs.add(prof.getPhoto());
//                        }

                        ListView profView= findViewById(R.id.list_view);
                        profView.setAdapter(new ProfAdapter(ListeProfesseursActivity.this,profs));

                        profView.setClickable(true);




                       profView.setOnItemClickListener(

                               (parent,view,position,id)->{

                                   Professeur p= (Professeur) profView.getItemAtPosition(position);
                                   String nom=p.getNom();
                                   String prenom=p.getPrenom();
                                   String tel=p.getTel();
                                   String image=p.getPhoto();
                                   String departement=p.getDepartement();
                                   String email=p.getEmail();
                                   String mdp= p.getMdp();
//                                   Toast.makeText(getApplicationContext(),"string to show",Toast.LENGTH_LONG).show();
                                   Intent intent = new Intent(getApplicationContext(), professeurDetails.class);
                                   intent.putExtra("nom",nom);
                                   intent.putExtra("prenom",prenom);
                                   intent.putExtra("tel",tel);
                                   intent.putExtra("image",image);
                                   intent.putExtra("departement",departement);
                                   intent.putExtra("email",email);
                                   intent.putExtra("mdp",mdp);
                                   startActivity(intent);
                               }
                       );

                       profView.setOnItemLongClickListener(

                               (parent,view,position,id)->{

                           AlertDialog.Builder builder=new AlertDialog.Builder(ListeProfesseursActivity.this).setTitle("Attention");
                           String[] options ={"Update","Delete"};



                           builder.setItems(options, new DialogInterface.OnClickListener() {
                               Professeur p= (Professeur) profView.getItemAtPosition(position);
                               String nom=p.getNom();
                               String prenom=p.getPrenom();
                               String tel=p.getTel();
                               String image=p.getPhoto();
                               String departement=p.getDepartement();
                               //get id
                               String id = p.getIdProf();
                               @Override
                               public void onClick(DialogInterface dialogInterface, int i) {
                                   if(i==0){
                                        //update is clicked
                                       Intent intent = new Intent(getApplicationContext(), UpdateProfActivity.class);
                                       intent.putExtra("nom",nom);
                                       intent.putExtra("prenom",prenom);
                                       intent.putExtra("tel",tel);
                                       intent.putExtra("image",image);
                                       intent.putExtra("departement",departement);

                                       intent.putExtra("idUser",id);
                                       startActivity(intent);

                                   }else{
                                       //delete is clicked
                                        db.collection("professeur").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getApplicationContext(),"Deleted succefully !!",Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(ListeProfesseursActivity.this, ListeProfesseursActivity.class));
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull  Exception e) {
                                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        });
                                   }
                               }
                           });
                                   AlertDialog alert = builder.create();
                                   alert.show();
                                   return true;
                       }
                       );


                    }
                });
    }
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


}




