package com.example.tp_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateEtudiantActivity extends AppCompatActivity {

    EditText name_etd;
    EditText etd_prenom;
    EditText etd_phone;
    TextView update_etd;
    Button back;

    String nomEtd, prenomEtd, telEtd, idEtd;

    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_etudiant);
        name_etd = findViewById(R.id.name_etd);
        etd_prenom = findViewById(R.id.etd_prenom);
        etd_phone = findViewById(R.id.etd_phone);
        update_etd = findViewById(R.id.update_etd);
        back = (Button) findViewById(R.id.Back_list);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateEtudiantActivity.this, ListEtudiantActivity.class));
            }
        });




        //update code
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //update
            nomEtd = bundle.getString("nom");
            prenomEtd = bundle.getString("prenom");
            telEtd = bundle.getString("tel");
            idEtd = bundle.getString("idUser");

            name_etd.setText(nomEtd);
            etd_prenom.setText(prenomEtd);
            etd_phone.setText(telEtd);

        }

        db = FirebaseFirestore.getInstance();
        //click button to update data
        update_etd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =getIntent().getExtras();
                if(bundle!=null){

                    String nomEtd =name_etd.getText().toString().trim();
                    String prenomEtd =etd_prenom.getText().toString().trim();
                    String telEtd = etd_phone.getText().toString().trim();
                    update_etd(nomEtd,prenomEtd,telEtd,idEtd);
                    startActivity(new Intent(UpdateEtudiantActivity.this, ListEtudiantActivity.class));

                }

            }
        });


    }

    private void update_etd(String nomEtd, String prenomEtd, String telEtd, String idEtd){
        db.collection("etudiant")
                .document(idEtd).update("nom",nomEtd,"prenom",prenomEtd,"tel",telEtd)
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