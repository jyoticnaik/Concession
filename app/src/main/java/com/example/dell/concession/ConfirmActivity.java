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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ConfirmActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView name,gender,dob,address,pincode,course,year,emailid,passint,dest,source,ppsd,pped,cond;
    private String name_string,gender_string,dob_string,address_string,pincode_string,course_string,year_string,
            emailid_string,passint_string,dest_string,source_string,ppsd_string,pped_string,cond_string;

    private Button cancle,confirm;

    private FirebaseFirestore con_db;
    private DocumentReference db_concessionDetails;
    private DocumentReference db_uid;
    private CollectionReference confirm_db;

    private DatabaseHelper myDB = new DatabaseHelper(ConfirmActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        con_db=FirebaseFirestore.getInstance();

        name=findViewById(R.id.name_textview);
        gender=findViewById(R.id.gender_textview);
        dob=findViewById(R.id.birthdate_textview);
        address=findViewById(R.id.address_textview);
        pincode=findViewById(R.id.pincode_textview);
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
        String uid_string = myDB.getUID();
        db_uid = con_db.collection("Students").document(uid_string);
        db_concessionDetails=db_uid.collection("ConcessionDetails").document("Details");

        db_uid.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            name_string=documentSnapshot.getString("name");
                            gender_string=documentSnapshot.getString("gender");
                            dob_string=documentSnapshot.getString("date_of_birth");
                            address_string=documentSnapshot.getString("address");
                            pincode_string=documentSnapshot.getString("pincode");
                            course_string=documentSnapshot.getString("course");
                            year_string=documentSnapshot.getString("year");
                            emailid_string=documentSnapshot.getString("email");
                        }
                        else {
                            Toast.makeText(ConfirmActivity.this, "Details Does Not Exists!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ConfirmActivity","Error: ",e);
                Toast.makeText(ConfirmActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        db_concessionDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    passint_string = documentSnapshot.getString("pass_interval");
                    dest_string = documentSnapshot.getString("destination");
                    source_string = documentSnapshot.getString("source");
                    ppsd_string = documentSnapshot.getString("pass_startDate");
                    pped_string = documentSnapshot.getString("pass_endDate");
                    cond_string = documentSnapshot.getString("con_date");
                }
                else {
                    Toast.makeText(ConfirmActivity.this, "Details Does Not Exists!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ConfirmActivity","Error: ",e);
                Toast.makeText(ConfirmActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        name.setText(name_string);
        gender.setText(gender_string);
        dob.setText(dob_string);
        address.setText(address_string);
        pincode.setText(pincode_string);
        course.setText(course_string);
        year.setText(year_string);
        emailid.setText(emailid_string);
        passint.setText(passint_string);
        dest.setText(dest_string);
        source.setText(source_string);
        ppsd.setText(ppsd_string);
        pped.setText(pped_string);
        cond.setText(cond_string);
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
