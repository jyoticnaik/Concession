package com.example.dell.concession;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;

public class FormActivity extends AppCompatActivity {

    private EditText uid,name,gender,year_of_birth,address;
    private String uid_string,name_string,gender_string,yob_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        uid=findViewById(R.id.uid_edittext);
        name=findViewById(R.id.name_edittext);
        gender=findViewById(R.id.gender_edittext);
        year_of_birth=findViewById(R.id.yob_edittext);
        address=findViewById(R.id.address_edittext);

        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;
        uid_string = bundle.getString("u_id");
        name_string=bundle.getString("name");
        gender_string=bundle.getString("gender");
        yob_string=bundle.getString("yob");

        if(TextUtils.isEmpty(uid_string) || TextUtils.isEmpty(name_string) || TextUtils.isEmpty(gender_string) || TextUtils.isEmpty(yob_string)){
            Toast.makeText(this, "Aadhaar Card not yet scanned.", Toast.LENGTH_SHORT).show();
        }
        else {
            uid.setText(uid_string);
            name.setText(name_string);
            gender.setText(gender_string);
            year_of_birth.setText(yob_string);
        }
    }
}
