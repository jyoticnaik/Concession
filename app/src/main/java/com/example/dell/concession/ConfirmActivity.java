package com.example.dell.concession;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class ConfirmActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView name,gender,dob,address,pincode,course,year,emailid,passint,dest,source,ppsd,pped,cond;
        private String uid_string,name_string,gender_string,dob_string,address_string,pincode_string,course_string,year_string,
            emailid_string,passint_string,dest_string,source_string,ppsd_string,pped_string,cond_string;

    private Button cancle,confirm;

    private FirebaseFirestore con_db;
    private DocumentReference db_concessionDetails;
    private DocumentReference db_uid;
    private CollectionReference confirm_db;

    private DatabaseHelper myDB = new DatabaseHelper(ConfirmActivity.this);
    private DetailsDB detailsDB = new DetailsDB(ConfirmActivity.this);

    @Override
    protected void onStart() {
        super.onStart();

        db_uid = con_db.collection("Students").document(uid_string);
        db_uid.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                name_string = documentSnapshot.getString("name");
                gender_string = documentSnapshot.getString("gender");
                dob_string = documentSnapshot.getString("date_of_birth");
                address_string = documentSnapshot.getString("address");
                pincode_string = documentSnapshot.getString("pincode");
                course_string = documentSnapshot.getString("course");
                year_string = documentSnapshot.getString("year");
                emailid_string = documentSnapshot.getString("email");

                name.setText(name_string);
                gender.setText(gender_string);
                dob.setText(dob_string);
                address.setText(address_string);
                pincode.setText(pincode_string);
                course.setText(course_string);
                year.setText(year_string);
                emailid.setText(emailid_string);

                if(documentSnapshot.getMetadata().hasPendingWrites()){
                    name.setText("Loading");
                }
            }

        });

        db_concessionDetails.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                passint_string = documentSnapshot.getString("pass_interval");
                dest_string = documentSnapshot.getString("destination");
                source_string = documentSnapshot.getString("source");
                ppsd_string = documentSnapshot.getString("pass_startDate");
                pped_string = documentSnapshot.getString("pass_endDate");
                cond_string = documentSnapshot.getString("con_date");

                passint.setText(passint_string);
                dest.setText(dest_string);
                source.setText(source_string);
                ppsd.setText(ppsd_string);
                pped.setText(pped_string);
                cond.setText(cond_string);

                if(documentSnapshot.getMetadata().hasPendingWrites()){
                    passint.setText("Loading");
                }
            }
        });

    }

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
        Cursor res = myDB.getAllData();
        if(res.getCount() == 0){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
        else {
            while (res.moveToNext()) {
                uid_string = res.getString(0);
            }
        }

        db_uid = con_db.collection("Students").document(uid_string);
        db_concessionDetails=db_uid.collection("ConcessionDetails").document("Details");

        db_uid.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();

                    if(documentSnapshot.exists() && documentSnapshot != null) {
                        name_string = documentSnapshot.getString("name");
                        gender_string = documentSnapshot.getString("gender");
                        dob_string = documentSnapshot.getString("date_of_birth");
                        address_string = documentSnapshot.getString("address");
                        pincode_string = documentSnapshot.getString("pincode");
                        course_string = documentSnapshot.getString("course");
                        year_string = documentSnapshot.getString("year");
                        emailid_string = documentSnapshot.getString("email");

                        name.setText(name_string);
                        gender.setText(gender_string);
                        dob.setText(dob_string);
                        address.setText(address_string);
                        pincode.setText(pincode_string);
                        course.setText(course_string);
                        year.setText(year_string);
                        emailid.setText(emailid_string);
                    }
                }
                else {
                    Log.d("ConfirmActivity","Error: "+task.getException().getMessage());
                }
            }
        });

        db_concessionDetails.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if(documentSnapshot.exists() && documentSnapshot != null) {
                        passint_string = documentSnapshot.getString("pass_interval");
                        dest_string = documentSnapshot.getString("destination");
                        source_string = documentSnapshot.getString("source");
                        ppsd_string = documentSnapshot.getString("pass_startDate");
                        pped_string = documentSnapshot.getString("pass_endDate");
                        cond_string = documentSnapshot.getString("con_date");

                        passint.setText(passint_string);
                        dest.setText(dest_string);
                        source.setText(source_string);
                        ppsd.setText(ppsd_string);
                        pped.setText(pped_string);
                        cond.setText(cond_string);
                    }
                } else {
                    Log.d("ConfirmActivity","Error: "+task.getException().getMessage());
                }
            }
        });

        /*db_uid.get()
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


                            boolean insertionSucessful=detailsDB.insertData1(name_string,gender_string,dob_string,address_string,pincode_string,course_string,year_string,emailid_string);
                            if(insertionSucessful)
                                Log.d("ConfrimActivity()","INSERTION SUCCESSFUL");
                            else
                                Log.d("ConfrimActivity()","INSERTION UN-SUCCESSFUL");


                            name.setText(name_string);
                            gender.setText(gender_string);
                            dob.setText(dob_string);
                            address.setText(address_string);
                            pincode.setText(pincode_string);
                            course.setText(course_string);
                            year.setText(year_string);
                            emailid.setText(emailid_string);
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
        });*/

        /*db_concessionDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    passint_string = documentSnapshot.getString("pass_interval");
                    dest_string = documentSnapshot.getString("destination");
                    source_string = documentSnapshot.getString("source");
                    ppsd_string = documentSnapshot.getString("pass_startDate");
                    pped_string = documentSnapshot.getString("pass_endDate");
                    cond_string = documentSnapshot.getString("con_date");

                    boolean insertionSucessful=detailsDB.insertData2(passint_string,dest_string,source_string,ppsd_string,pped_string,cond_string);
                    if(insertionSucessful)
                        Log.d("ConfrimActivity()","INSERTION SUCCESSFUL");
                    else
                        Log.d("ConfrimActivity()","INSERTION UN-SUCCESSFUL");

                    showData();

                    passint.setText(passint_string);
                    dest.setText(dest_string);
                    source.setText(source_string);
                    ppsd.setText(ppsd_string);
                    pped.setText(pped_string);
                    cond.setText(cond_string);
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
        });*/
        //TODO: Nothing is displayed. Try OFFLINE option.
    }

    /*public void showData(){
        Cursor details_cursor1 = detailsDB.getAllData1();
        Cursor details_cursor2 = detailsDB.getAllData2();

        if(details_cursor1.getCount() == 0){
            Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
        }
        while (details_cursor1.moveToNext()){
            name.setText(details_cursor1.getString(0));
            gender.setText(details_cursor1.getString(1));
            dob.setText(details_cursor1.getString(2));
            address.setText(details_cursor1.getString(3));
            pincode.setText(details_cursor1.getString(4));
            course.setText(details_cursor1.getString(5));
            year.setText(details_cursor1.getString(6));
            emailid.setText(details_cursor1.getString(7));
        }

        if(details_cursor2.getCount() == 0){
            Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
        }
        while (details_cursor2.moveToNext()){
            passint.setText(details_cursor2.getString(0));
            dest.setText(details_cursor2.getString(1));
            source.setText(details_cursor2.getString(2));
            ppsd.setText(details_cursor2.getString(3));
            pped.setText(details_cursor2.getString(4));
            cond.setText(details_cursor2.getString(5));
        }
    }*/

    @Override
    public void onClick(View v) {
        if(v==cancle){
            finish();
            startActivity(new Intent(this,MainActivity.class));
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
