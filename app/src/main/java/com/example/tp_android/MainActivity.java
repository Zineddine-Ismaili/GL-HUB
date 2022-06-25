package com.example.tp_android;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

//    Button btnLogOut;
    androidx.cardview.widget.CardView btnLogOut;
    androidx.cardview.widget.CardView btnListProf;
    androidx.cardview.widget.CardView btnListEtd;
    androidx.cardview.widget.CardView btnPdf;
//    Button btnListProf;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogOut = findViewById(R.id.btnLogout);
        btnListProf = findViewById(R.id.btnListProf);
        btnListEtd=findViewById(R.id.btnListEtd);
        btnPdf=findViewById(R.id.pdf);
        mAuth = FirebaseAuth.getInstance();

        btnLogOut.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });
        btnListProf.setOnClickListener(view ->{
            startActivity(new Intent(MainActivity.this, ListeProfesseursActivity.class));
        });
        btnListEtd.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ListEtudiantActivity.class));
        });

        btnPdf.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AddPdfActivity.class));
        });

        //get current user
//        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }
}
