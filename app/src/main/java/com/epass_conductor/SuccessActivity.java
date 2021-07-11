package com.epass_conductor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SuccessActivity extends AppCompatActivity {

    TextView textView;

    EpassModel epassModel;
    String docID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        textView=findViewById(R.id.checkdetails);
        Intent intent=getIntent();
        epassModel= (EpassModel) intent.getSerializableExtra("epass");
        docID=intent.getStringExtra("docid");

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SuccessActivity.this,VerifyEpass.class);
                intent.putExtra("epass",epassModel);
                intent.putExtra("docid",docID);
                startActivity(intent);
            }
        });
    }
}