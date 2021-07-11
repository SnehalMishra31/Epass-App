package com.epass_conductor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity  {

    private Context mContext;
    TextView name, email, gender, college, roll, accountID;
    ImageView imageView;
    ProgressBar progressBar;

    Button approve,reject;

    ScrollView scrollView;
    AlertDialog.Builder builder;
    AlertDialog progressDialog;

    StudentData studentData;
    String docID;
    String tag;

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        mContext =ProfileActivity.this;


        progressDialog = getBuilder().create();
        progressDialog.setCancelable(false);

        name = findViewById(R.id.name);
        college = findViewById(R.id.college);
        gender = findViewById(R.id.gender);
        email = findViewById(R.id.email);
        roll = findViewById(R.id.roll);
        accountID = findViewById(R.id.accountID);
        imageView = findViewById(R.id.idproofimage);
        progressBar=findViewById(R.id.imageloader);


        approve=findViewById(R.id.approve1);
        reject=findViewById(R.id.reject);

        Intent intent=getIntent();
        studentData= (StudentData) intent.getSerializableExtra("studentdata");
        docID=intent.getStringExtra("docid");

        linearLayout=findViewById(R.id.approvalLayout);

        if(studentData.getIsVerified().equals("1")){
            linearLayout.setVisibility(View.GONE);
        }else{
            linearLayout.setVisibility(View.VISIBLE);
        }

        setDetails();






      //  getDetails();

    }


    public void setDetails(){

        name.setText(studentData.getFirstName()+" "+studentData.getMiddleName()+" "+studentData.getLastName());
        college.setText(studentData.getCollege());
        gender.setText(studentData.getGender());
        email.setText(studentData.getEmail());
        roll.setText(studentData.getRollNumber());
        accountID.setText(docID);

        loadImageFromDB(docID);

    }

/*

    public void getDetails() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(StudentDetails.STUDENT_COLLECTION).document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                scrollView.setVisibility(View.VISIBLE);
                name.setText("" + documentSnapshot.getString(StudentDetails.FIRST_NAME) + " " + documentSnapshot.getString(StudentDetails.MIDDLE_NAME) + " " +
                        documentSnapshot.getString(StudentDetails.LAST_NAME));
                email.setText("" + documentSnapshot.getString(StudentDetails.USER_EMAIL));
                college.setText("" + documentSnapshot.getString(StudentDetails.COLLEGE_NAME));
                roll.setText("" + documentSnapshot.getString(StudentDetails.COLLEGE_ID));
                accountID.setText("" + documentSnapshot.getId());

                gender.setText("" + documentSnapshot.getString("gender"));
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


 */
    public AlertDialog.Builder getBuilder() {
        if (builder == null) {
            builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setTitle("Updating status...");

            final ProgressBar progressBar = new ProgressBar(ProfileActivity.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            progressBar.setLayoutParams(layoutParams);
            builder.setView(progressBar);
        }
        return builder;
    }


    public void loadImageFromDB(String imageName){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("IdCards").child(imageName);
        storageReference.getBytes(1024 * 1024 * 5).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);
                progressBar.setVisibility(View.GONE);
            }
        });

    }





}