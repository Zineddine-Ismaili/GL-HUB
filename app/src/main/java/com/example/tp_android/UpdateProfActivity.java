package com.example.tp_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class UpdateProfActivity extends AppCompatActivity {

    EditText name_prof;
    EditText prenom_prof;
    EditText tel_prof;
    EditText departement_prof;
    ImageView profile_prof;
    Button update_prof;
    Button back;

    String nomProf, prenomProf, departementProf, telProf, imageProf, idProf;

    FirebaseFirestore db;

    StorageReference storageRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_prof);

        name_prof = (EditText) findViewById(R.id.name_prof);
        profile_prof = (ImageView) findViewById(R.id.profile_image);
        prenom_prof = (EditText) findViewById(R.id.prof_prenom);
        tel_prof =(EditText) findViewById(R.id.prof_phone);
        departement_prof =(EditText) findViewById(R.id.prof_dept);
        update_prof = (Button) findViewById(R.id.update_prof);
        back = (Button) findViewById(R.id.Back_menu);

        back.setOnClickListener(view ->{
            startActivity(new Intent(UpdateProfActivity.this, ListeProfesseursActivity.class));
        });


        //update code
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            //update
            nomProf=bundle.getString("nom");
            prenomProf=bundle.getString("prenom");
            departementProf=bundle.getString("departement");
            telProf=bundle.getString("tel");
            imageProf= bundle.getString("image");
            idProf=bundle.getString("idUser");

            Uri imageUrl= Uri.parse(imageProf);


            storageRef = FirebaseStorage.getInstance().getReference("photo/professeur/"+imageUrl);
            try {
                File locationFile = File.createTempFile(imageProf,"jpeg");
                storageRef.getFile(locationFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    profile_prof.setImageURI(imageUrl);
                        //start
                        Bitmap bitmap = BitmapFactory.decodeFile(locationFile.getAbsolutePath());
                        profile_prof.setImageBitmap(bitmap);
                        //end
                        Toast.makeText(getApplicationContext(),"profile prof",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Probleme to take the pic",Toast.LENGTH_LONG).show();
                    }
                });
            }catch (IOException e){
                e.printStackTrace();
            }


            name_prof.setText(nomProf);
            prenom_prof.setText(prenomProf);
            tel_prof.setText(telProf);
            departement_prof.setText(departementProf);

        }

        db = FirebaseFirestore.getInstance();
        //click button to update data
        update_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =getIntent().getExtras();
                if(bundle!=null){

                    String nomPrefesseur =name_prof.getText().toString().trim();
                    String prenomProfesseur =prenom_prof.getText().toString().trim();
                    String departementProfesseur= departement_prof.getText().toString().trim();
                    String telProfesseur = tel_prof.getText().toString().trim();
                    updateProf(nomPrefesseur,prenomProfesseur,departementProfesseur,imageProf,telProfesseur,idProf);
                    startActivity(new Intent(UpdateProfActivity.this, ListeProfesseursActivity.class));


                }

            }
        });

    }

    private void updateProf(String nomP, String prenomP, String departementP,String imageP ,String telP,String idProf){
        db.collection("professeur")
                .document(idProf).update("departement",departementP,"nom",nomP,"photo",imageP,"prenom",prenomP,"tel",telP)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                         //called when updated succefully
                        Toast.makeText(getApplicationContext(),"Updated succefully..",Toast.LENGTH_LONG).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //there is an error on update
                Toast.makeText(getApplicationContext(),"Update Failed..",Toast.LENGTH_LONG).show();
            }
        });
    }
}