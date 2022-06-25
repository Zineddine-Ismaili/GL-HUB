package com.example.tp_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;

public class EtudiantDetailsActivity extends AppCompatActivity {
    private static final int REQUEST_CALL=1;
    private static final int REQUEST_SMS=2;

    TextView name_etudiant;
    TextView prenom_etd;
    TextView etudiant_phone;
    TextView backMenu;
    TextView backButton;
    ImageView call;
    String messageSent;
    ImageView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etudiant_details);

        call=findViewById(R.id.call);
        backMenu=findViewById(R.id.Go_back_menu);
        name_etudiant=findViewById(R.id.name_etudiant);
        prenom_etd=findViewById(R.id.prenom_etd);
        etudiant_phone= findViewById(R.id.etudiant_phone);
        backButton=findViewById(R.id.Go_back);
        message=findViewById(R.id.message);


        backButton.setOnClickListener(view ->{
            startActivity(new Intent(EtudiantDetailsActivity.this, ListEtudiantActivity.class));
        });

        backMenu.setOnClickListener(view ->{
            startActivity(new Intent(EtudiantDetailsActivity.this, MainActivity.class));
        });

        Intent intent = getIntent();


        String nom = intent.getStringExtra("nom");
        name_etudiant.setText(nom);


        String prenom = intent.getStringExtra("prenom");
        prenom_etd.setText(prenom);

        String tel = intent.getStringExtra("tel");
        etudiant_phone.setText(tel);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall();
            }
        });

        final EditText edittext = new EditText(EtudiantDetailsActivity.this );

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EtudiantDetailsActivity.this).setTitle("Message");
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
                                Intent myIntent = new Intent(view.getContext(), ListEtudiantActivity.class);
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

    }
    private void makePhoneCall(){
        if(ContextCompat.checkSelfPermission(EtudiantDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EtudiantDetailsActivity.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
        }else{
            String dial= "tel: "+ etudiant_phone.getText().toString();
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    private void sendPhoneSms(){
        if(ContextCompat.checkSelfPermission(EtudiantDetailsActivity.this,Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EtudiantDetailsActivity.this,new String[]{Manifest.permission.SEND_SMS},REQUEST_SMS);
        }else {
            SmsManager smsManager =SmsManager.getDefault();
            smsManager.sendTextMessage(etudiant_phone.getText().toString(),null,messageSent,null,null);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else if(requestCode ==REQUEST_SMS){
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    sendPhoneSms();
                }
            }else {
                System.out.println("no");
            }
        }
    }


}