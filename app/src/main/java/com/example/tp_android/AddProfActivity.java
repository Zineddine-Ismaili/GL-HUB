package com.example.tp_android;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import static android.Manifest.permission.CAMERA;

public class AddProfActivity extends AppCompatActivity {

    EditText nomProf;
    EditText prenomProf;
    EditText departementProf;
    ImageView imageProf;
    Button photoProf;
    EditText telProf;
    Button add;
    TextView Back_menu;
    FirebaseFirestore db;
    Uri urlImage;
    StorageReference storageRef;
    String nameImage;
    EditText emailProf;
    EditText mdpProf;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prof);

        nomProf = findViewById(R.id.nomProf);
        prenomProf = findViewById(R.id.prenomProf);
        departementProf = findViewById(R.id.departementProf);
        photoProf = findViewById(R.id.buttonPhoto);
        telProf = findViewById(R.id.telProf);
        add = findViewById(R.id.add);
        Back_menu = findViewById(R.id.Back_menu);
        imageProf =findViewById(R.id.prof_image);
        emailProf = findViewById(R.id.emailProf);
        mdpProf = findViewById(R.id.mdpProf);
        db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();



            photoProf.setOnClickListener(view -> {
                selectImage();
            });


            add.setOnClickListener(view -> {
                uploadImage();
                createProf();
            });


        Back_menu.setOnClickListener(view ->{
            startActivity(new Intent(AddProfActivity.this, MainActivity.class));
        });




    }


    private void uploadImage(){

        SimpleDateFormat formatter =new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.FRANCE);
        Date now = new Date();
        String fileName = formatter.format(now);

        storageRef = FirebaseStorage.getInstance().getReference("photo/professeur/"+fileName);
        //"gs://iwimapp-9d944.appspot.com/photo/professeur/"+
        nameImage=fileName;
        storageRef.putFile(urlImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageProf.setImageURI(null);
                Toast.makeText(getApplicationContext(),"Succefully uploaded",Toast.LENGTH_LONG).show();

            }

    }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failed to upload",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent);
//        startActivityForResult(intent,100);
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        urlImage=data.getData();
                        System.out.println(urlImage+"HEEERE");
                        imageProf.setImageURI(urlImage);
                    }
                }
            });





    private void createProf() {
        Map<String, Object> prof = new HashMap<>();

            String nom = nomProf.getText().toString();
            String prenom = prenomProf.getText().toString();
            String departement = departementProf.getText().toString();
            String tel = telProf.getText().toString();
            String email = emailProf.getText().toString();
            String mdp= mdpProf.getText().toString();
            prof.put("nom", nom);
            prof.put("prenom", prenom);
            prof.put("departement", departement);
            prof.put("tel", tel);
            prof.put("photo", nameImage);
            prof.put("email",email);
            prof.put("mdp",mdp);

        String emailCurrentUser=  FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String mdpCu= "123456";


            db.collection("professeur")
                    .add(prof)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            System.out.println("DocumentSnapshot added with ID: " + documentReference.getId());
                            //mAuth.createUserWithEmailAndPassword(email,mdp);

                          //test
                            mAuth.createUserWithEmailAndPassword(email,mdp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(AddProfActivity.this, "User added successfully", Toast.LENGTH_SHORT).show();
                                        mAuth.signInWithEmailAndPassword(emailCurrentUser,mdpCu).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()){
                                                    startActivity(new Intent(AddProfActivity.this, MainActivity.class));
                                                }else{
                                                    Toast.makeText(AddProfActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        startActivity(new Intent(AddProfActivity.this, ListeProfesseursActivity.class));
                                    }else{
                                        Toast.makeText(AddProfActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            //endtestetst


//                            startActivity(new Intent(AddProfActivity.this, ListeProfesseursActivity.class));
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