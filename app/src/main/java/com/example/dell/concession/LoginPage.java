package com.example.dell.concession;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity implements View.OnClickListener {

    private TextView emailid;
    private TextView password;
    private TextView forgetpassword;
    private Button signin;
    private TextView sigup;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        //getActionBar().hide();

        firebaseAuth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);

        emailid=findViewById(R.id.emailidtxt);
        password=findViewById(R.id.passwordtxt);
        forgetpassword=findViewById(R.id.fpasstxt);
        signin=findViewById(R.id.signinbtn);
        sigup=findViewById(R.id.signuptxt);

        signin.setOnClickListener(this);
        sigup.setOnClickListener(this);
        forgetpassword.setOnClickListener(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    private void userLogin(){
        String email=emailid.getText().toString().trim();
        String pass=password.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your email id.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Please enter password.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Logging User... please wait");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                finish();
                startActivity(new Intent(LoginPage.this,MainActivity.class));
            }
        });
    }

    private void forgetpasswordHandler(){
        String email=emailid.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your email id.", Toast.LENGTH_SHORT).show();
        }
        else {
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginPage.this, "Please Check your email.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String msg=task.getException().getLocalizedMessage();
                        Toast.makeText(LoginPage.this, "Error occure: "+msg, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {

        if(v==signin){
            userLogin();
        }
        if(v==sigup){
            startActivity(new Intent(this,RegisterPage.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        if (v==forgetpassword){
            forgetpasswordHandler();
        }
    }
}
