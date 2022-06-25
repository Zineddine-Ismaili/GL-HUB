package com.example.tp_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tp_android.adapetrs.EtudiantAdapetr;
import com.example.tp_android.adapetrs.ProfAdapter;
import com.example.tp_android.model.Etudiant;
import com.example.tp_android.model.Professeur;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedList;

public class ListEtudiantActivity extends AppCompatActivity {

    ListView list_etudiant;
    ArrayList<Etudiant> etudiants;
    FirebaseFirestore db;

    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_etudiant);


        list_etudiant= (ListView) findViewById(R.id.list_view2);
        db=FirebaseFirestore.getInstance();

       etudiants= new ArrayList<Etudiant>();
       getAllEtudiant();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_etudiant);
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
                Intent intent = new Intent(ListEtudiantActivity.this,addEtudiantActivity.class);
                startActivity(intent);
            }
        });

    }

    void getAllEtudiant(){
        showProgressDialog();

        db.collection("etudiant")
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
                                Etudiant etudiant = new Etudiant(
                                        document.getString("nom"),
                                        document.getString("prenom"),
                                        document.getString("tel"),
                                        document.getId());
                                System.out.println(etudiant);
                                etudiants.add(etudiant);
                            }

                            System.out.println(etudiants+" YARBEE");
                        } else {
                            System.out.println("Erreur");
                        }

                        hideProgressDialog();


                        ListView etudiantView= findViewById(R.id.list_view2);
                        etudiantView.setAdapter(new EtudiantAdapetr(ListEtudiantActivity.this,etudiants));

                        etudiantView.setClickable(true);




                        etudiantView.setOnItemClickListener(

                                (parent,view,position,id)->{

                                    Etudiant p= (Etudiant) etudiantView.getItemAtPosition(position);
                                    String nom=p.getNom();
                                    String prenom=p.getPrenom();
                                    String tel=p.getTel();
//                                  Toast.makeText(getApplicationContext(),"string to show",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), EtudiantDetailsActivity.class);
                                    intent.putExtra("nom",nom);
                                    intent.putExtra("prenom",prenom);
                                    intent.putExtra("tel",tel);
                                    startActivity(intent);
                                }
                        );

                        etudiantView.setOnItemLongClickListener(

                                (parent,view,position,id)->{

                                    AlertDialog.Builder builder=new AlertDialog.Builder(ListEtudiantActivity.this).setTitle("Attention");
                                    String[] options ={"Update","Delete"};



                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        Etudiant p= (Etudiant) etudiantView.getItemAtPosition(position);
                                        String nom=p.getNom();
                                        String prenom=p.getPrenom();
                                        String tel=p.getTel();
                                        //get id
                                        String id = p.getIdE();
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if(i==0){
                                                //update is clicked
                                                Intent intent = new Intent(getApplicationContext(), UpdateEtudiantActivity.class);
                                                intent.putExtra("nom",nom);
                                                intent.putExtra("prenom",prenom);
                                                intent.putExtra("tel",tel);

                                                intent.putExtra("idUser",id);
                                                startActivity(intent);

                                            }else{
                                                //delete is clicked
                                                db.collection("etudiant").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(getApplicationContext(),"Deleted succefully !!",Toast.LENGTH_LONG).show();
                                                        startActivity(new Intent(ListEtudiantActivity.this, ListEtudiantActivity.class));
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