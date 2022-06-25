package com.example.tp_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class professeurDetails extends AppCompatActivity {
    private static final int REQUEST_CALL=1;
    private static final int REQUEST_SMS=2;

    TextView name_prof;
    TextView prenom_prof;
    TextView tel_prof;
    TextView departement_prof;
    TextView backMenu;
    ImageView profile_prof;
    TextView backButton;
    ImageView call;
    ImageView message;
    StorageReference storageRef;
    String messageSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professeur_details);

        backMenu=findViewById(R.id.Go_back_menu);
        name_prof = (TextView) findViewById(R.id.name_prof);
        profile_prof = (ImageView) findViewById(R.id.profile_image);
        prenom_prof = (TextView) findViewById(R.id.prof_prenom);
        tel_prof =(TextView) findViewById(R.id.prof_phone);
        departement_prof =(TextView) findViewById(R.id.prof_dept);
        backButton = findViewById(R.id.Go_back);
        call =findViewById(R.id.call);
        message=findViewById(R.id.message);

        backButton.setOnClickListener(view ->{
            startActivity(new Intent(professeurDetails.this, ListeProfesseursActivity.class));
        });

        backMenu.setOnClickListener(view ->{
            startActivity(new Intent(professeurDetails.this, MainActivity.class));
        });



        Intent intent = getIntent();




        String nom = intent.getStringExtra("nom");
        name_prof.setText(nom);


        String prenom = intent.getStringExtra("prenom");
        prenom_prof.setText(prenom);

        String tel = intent.getStringExtra("tel");
        tel_prof.setText(tel);

        String departement = intent.getStringExtra("departement");
        departement_prof.setText(departement);


        final EditText edittext = new EditText(professeurDetails.this );


        String photo = intent.getStringExtra("image");
        Uri imageUrl= Uri.parse(photo);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall();
            }
        });



        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(professeurDetails.this).setTitle("Message");
                builder.setTitle("MESSAGE");
                builder.setMessage("Enter Your Message");
                builder.setIcon(R.drawable.text_message);
                builder.setView(edittext);
                builder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                messageSent=edittext.getText().toString();
                                sendPhoneSms();
                                Intent myIntent = new Intent(view.getContext(), ListeProfesseursActivity.class);
                                startActivity(myIntent);
                            }
                        });
                // Setting Negative "NO" Button
                builder.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                dialog.cancel();
                            }
                        });

                // closed

                // Showing Alert Message
                builder.show();
            }


        });


        storageRef = FirebaseStorage.getInstance().getReference("photo/professeur/"+imageUrl);
        try {
            File locationFile = File.createTempFile(photo,"jpeg");
            storageRef.getFile(locationFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    profile_prof.setImageURI(imageUrl);
                    //start
                    Bitmap bitmap = BitmapFactory.decodeFile(locationFile.getAbsolutePath());
                    profile_prof.setImageBitmap(bitmap);
                    //end
                    Toast.makeText(getApplicationContext(),"profile prof image done",Toast.LENGTH_LONG).show();
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



        System.out.println(storageRef.getFile(imageUrl)+"HANA11");

        System.out.println(imageUrl+"wsslt");

    }

    private void makePhoneCall(){
        if(ContextCompat.checkSelfPermission(professeurDetails.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(professeurDetails.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
        }else{
            String dial= "tel: "+ tel_prof.getText().toString();
            startActivity(new Intent(Intent.ACTION_CALL,Uri.parse(dial)));
        }
    }

    private void sendPhoneSms(){
        if(ContextCompat.checkSelfPermission(professeurDetails.this,Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(professeurDetails.this,new String[]{Manifest.permission.SEND_SMS},REQUEST_SMS);
        }else {
            SmsManager smsManager =SmsManager.getDefault();
            smsManager.sendTextMessage(tel_prof.getText().toString(),null,messageSent,null,null);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else if(requestCode ==REQUEST_SMS){
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    sendPhoneSms();
                }
            }

            else {
                System.out.println("no");
            }
        }
    }






}