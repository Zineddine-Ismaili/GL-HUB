package com.example.tp_android;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddPdfActivity extends AppCompatActivity {

    Button btn_select_pdf;
    Button btn_add_pdf;
    FirebaseFirestore db;
    Uri urlPdf;
//    PDFView pdfView;
    StorageReference storageRef;
    String namePdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pdf);

        btn_select_pdf = findViewById(R.id.select_pdf);
        btn_add_pdf = findViewById(R.id.add_prof);
//        pdfView = findViewById(R.id.pdfView);

        btn_select_pdf.setOnClickListener(view -> {
            selectPDF();
        });
//        btn_add_pdf.setOnClickListener(view -> {
//            uploadPdf();
//        });
    }

   private void selectPDF(){
       Intent intent = new Intent();
       intent.setType("pdf/*");
       intent.setAction(intent.ACTION_GET_CONTENT);
       launcher.launch(intent);
    }
    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        urlPdf=data.getData();
                        System.out.println(urlPdf+"HEEERE");
//                        pdfView.fromUri(urlPdf);
                    }
                }
            });

    private void uploadPdf(){

        SimpleDateFormat formatter =new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.FRANCE);
        Date now = new Date();
        String fileName = formatter.format(now);

        storageRef = FirebaseStorage.getInstance().getReference("pdf/"+fileName);
        //"gs://iwimapp-9d944.appspot.com/photo/professeur/"+
        namePdf=fileName;
        storageRef.putFile(urlPdf).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                pdfView.fromUri(null);
                Toast.makeText(getApplicationContext(),"Succefully uploaded",Toast.LENGTH_LONG).show();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failed to upload",Toast.LENGTH_LONG).show();
            }
        });
    }
}