package com.rawtalent.epass_user.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rawtalent.epass_user.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText email, password;
    Button loginButton;
    TextView createNewAccount, forgetPassword;
    TextView alertTV;

    AlertDialog.Builder builder;
    AlertDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = getBuilder().create();
        progressDialog.setCancelable(false);


        loginButton = findViewById(R.id.loginButton);
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);
        createNewAccount = findViewById(R.id.createNewaccount);
        forgetPassword = findViewById(R.id.forgotpassword);
        alertTV = findViewById(R.id.alertTV);

        loginButton.setOnClickListener(this);
        createNewAccount.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View v) {
        alertTV.setVisibility(View.GONE);
        if (v.getId() == R.id.loginButton) {

            String mEmail = email.getText().toString();
            String mPassword = password.getText().toString();

            if (mEmail.equals("") || mEmail.isEmpty()) {
                alertTV.setVisibility(View.VISIBLE);
                alertTV.setText("please enter email");
                return;
            }
            if (mPassword.equals("") || mPassword.isEmpty()) {
                alertTV.setText("please enter password");
                alertTV.setVisibility(View.VISIBLE);
                return;
            }
            progressDialog.show();
            loginWithCredentials(mEmail, mPassword);
        }
        if (v.getId() == R.id.createNewaccount) {
            Intent myIntent = new Intent(LoginActivity.this, CreateNewAccount.class);
            startActivity(myIntent);
        }
        if (v.getId() == R.id.forgotpassword) {
            PasswordResetDialog fileDialog = new PasswordResetDialog(LoginActivity.this);
            fileDialog.show(getSupportFragmentManager(), "Reset Password");

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);
        if (currentUser != null) {
            updateUI();
        }
    }

    public void loginWithCredentials(@NonNull String email, @NonNull String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressDialog.dismiss();
                updateUI();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public AlertDialog.Builder getBuilder() {
        if (builder == null) {
            builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("Checking details...");

            final ProgressBar progressBar = new ProgressBar(LoginActivity.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            progressBar.setLayoutParams(layoutParams);
            builder.setView(progressBar);
        }
        return builder;
    }

    public void updateUI() {
        Intent myIntent = new Intent(LoginActivity.this, RedirectActivity.class);
        startActivity(myIntent);
        LoginActivity.this.finish();
    }

}