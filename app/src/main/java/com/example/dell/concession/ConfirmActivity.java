package com.example.dell.concession;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ConfirmActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView name,gender,dob,address,course,year,emailid,passint,dest,source,ppsd,pped,cond;
    private Button cancle,confirm;

    private FirebaseFirestore con_db;
    private CollectionReference confirm_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        con_db=FirebaseFirestore.getInstance();

        name=findViewById(R.id.name_textview);
        gender=findViewById(R.id.gender_textview);
        dob=findViewById(R.id.birthdate_textview);
        address=findViewById(R.id.address_textview);
        course=findViewById(R.id.course_textview);
        year=findViewById(R.id.year_textview);
        emailid=findViewById(R.id.email_textview);
        passint=findViewById(R.id.passint_textview);
        dest=findViewById(R.id.dest_textview);
        source=findViewById(R.id.source_textview);
        ppsd=findViewById(R.id.ppsd_textview);
        pped=findViewById(R.id.pped_textview);
        cond=findViewById(R.id.condate_textview);

        fillData();

        cancle=findViewById(R.id.cancle_button);
        confirm=findViewById(R.id.confirm_button);

        cancle.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    public void fillData(){
        StudentDetails sd=new StudentDetails();
        ConcessionDetails cd=new ConcessionDetails();

        name.setText(sd.getName());
        gender.setText(sd.getGender());
        dob.setText(sd.getDate_of_birth());
        address.setText(sd.getAddress());
        course.setText(sd.getCourse());
        year.setText(sd.getYear());
        emailid.setText(sd.getEmail());
        passint.setText(cd.getPass_interval());
        dest.setText(cd.getDestination());
        source.setText(cd.getSource());
        ppsd.setText(cd.getPass_startDate());
        pped.setText(cd.getPass_endDate());
        cond.setText(cd.getCon_date());
    }

    @Override
    public void onClick(View v) {
        if(v==cancle){
            finish();
            startActivity(new Intent(this,ConcessionFillActivity.class));
        }
        if(v==confirm) {
            Boolean c = Boolean.TRUE;
            Map<String,Boolean> cn=new HashMap<>();
            cn.put("Confirmation",c);

            confirm_db = con_db.collection("Students");
            confirm_db.add(cn).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(ConfirmActivity.this, "Concession Confirmed", Toast.LENGTH_SHORT).show();
                    Log.d("ConfirmationActivity","Concession Confirmed");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ConfirmActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                    Log.d("ConfirmationActivity","Error Occured: "+e.getMessage());
                }
            });
        }
    }
}
