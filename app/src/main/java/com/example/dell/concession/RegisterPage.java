package com.example.dell.concession;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterPage extends AppCompatActivity implements View.OnClickListener {

    private TextView emailid,password,confirmpassword;
    private Button register;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore con_db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        getActionBar().hide();

        firebaseAuth=FirebaseAuth.getInstance();

        // Access a Cloud Firestore instance from your Activity
        con_db= FirebaseFirestore.getInstance();

        progressDialog=new ProgressDialog(this);

        emailid=findViewById(R.id.emailidtxt);
        password=findViewById(R.id.passwordtxt);
        confirmpassword=findViewById(R.id.cnfrmpasswordtxt);
        register=findViewById(R.id.registerbtn);

        register.setOnClickListener(this);
    }

    private void userRegister(){
        final String email=emailid.getText().toString().trim();
        String pass=password.getText().toString().trim();
        String cnfrmpass=confirmpassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your email id.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Please enter password.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(cnfrmpass)){
            Toast.makeText(this, "Please confrim your password.", Toast.LENGTH_SHORT).show();
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        //if validations are ok
        if(pass.equals(cnfrmpass)){
            firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(RegisterPage.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(RegisterPage.this,LoginPage.class));
                    }
                    else{
                        String msg=task.getException().getLocalizedMessage();
                        Toast.makeText(RegisterPage.this, "Registration Unsuccessful: "+msg+" Please try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }




    }


    @Override
    public void onClick(View v) {
        if (v==register){
            userRegister();
        }
    }
}
