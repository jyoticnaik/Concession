package com.example.dell.concession;

import android.annotation.SuppressLint;
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
    private DocumentReference confirm_db;

    private DatabaseHelper myDB = new DatabaseHelper(ConfirmActivity.this);

    @SuppressLint("ResourceAsColor")
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

        name.setTextColor(R.color.shadeMatching1);
        gender.setTextColor(R.color.shadeMatching1);
        dob.setTextColor(R.color.shadeMatching1);
        address.setTextColor(R.color.shadeMatching1);
        pincode.setTextColor(R.color.shadeMatching1);
        course.setTextColor(R.color.shadeMatching1);
        year.setTextColor(R.color.shadeMatching1);
        emailid.setTextColor(R.color.shadeMatching1);
        passint.setTextColor(R.color.shadeMatching1);
        dest.setTextColor(R.color.shadeMatching1);
        source.setTextColor(R.color.shadeMatching1);
        ppsd.setTextColor(R.color.shadeMatching1);
        pped.setTextColor(R.color.shadeMatching1);
        cond.setTextColor(R.color.shadeMatching1);

        cancle=findViewById(R.id.cancle_button);
        confirm=findViewById(R.id.confirm_button);

        cancle.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Cursor uid_cursor = myDB.getUID();

        if(uid_cursor.getCount() == 0){
            Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
        }
        while (uid_cursor.moveToNext()){
            uid_string = uid_cursor.getString(0);
        }

        try {
            db_uid = con_db.collection("Students").document(uid_string);
            db_concessionDetails = db_uid.collection("ConcessionDetails").document("Details");
        }catch (Exception e){
            Log.d("ComfirmationActivity",e.getMessage());
        }


        db_uid.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Toast.makeText(ConfirmActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    Log.d("ComfirmationActivity",e.toString());
                    return;
                }

                String source = documentSnapshot != null && documentSnapshot.getMetadata().hasPendingWrites()
                        ? "Local" : "Server";

                if (documentSnapshot!=null && documentSnapshot.exists()){
                    name.setText(documentSnapshot.getString("name"));
                    gender.setText(documentSnapshot.getString("gender"));
                    dob.setText(documentSnapshot.getString("date_of_birth"));
                    address.setText(documentSnapshot.getString("address"));
                    pincode.setText(documentSnapshot.getString("pincode"));
                    course.setText(documentSnapshot.getString("course"));
                    year.setText(documentSnapshot.getString("year"));
                    emailid.setText(documentSnapshot.getString("email"));

                    Log.d("ConfirmActivity",source + " data: " + documentSnapshot.getData());
                }else{
                    Toast.makeText(ConfirmActivity.this, "Document doesnot exists!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        db_concessionDetails.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Toast.makeText(ConfirmActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    Log.d("ComfirmationActivity",e.toString());
                    return;
                }

                if (documentSnapshot != null) {
                    if (documentSnapshot.exists()){
                        passint.setText(documentSnapshot.getString("pass_interval"));
                        dest.setText(documentSnapshot.getString("destination"));
                        source.setText(documentSnapshot.getString("source"));
                        ppsd.setText(documentSnapshot.getString("pass_startDate"));
                        pped.setText(documentSnapshot.getString("pass_endDate"));
                        cond.setText(documentSnapshot.getString("con_date"));
                    }else {
                        Toast.makeText(ConfirmActivity.this, "Document doesnot exists!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(ConfirmActivity.this, "Document doesnot exists!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v==cancle){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
        if(v==confirm) {

            Cursor uid_cursor = myDB.getUID();

            if(uid_cursor.getCount() == 0){
                Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
            }
            while (uid_cursor.moveToNext()){
                uid_string = uid_cursor.getString(0);
            }

            Boolean c = Boolean.TRUE;
            Map<String,Boolean> cn=new HashMap<>();
            cn.put("Confirmation",c);

            confirm_db = con_db.collection("Students").document(uid_string).collection("ConfirmationDetails").document("Confirmation");
            confirm_db.set(cn).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ConfirmActivity.this, "Concession Confirmed", Toast.LENGTH_SHORT).show();
                    Log.d("ConfirmationActivity","Concession Confirmed");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ConfirmActivity.this, "Concession Confirmed", Toast.LENGTH_SHORT).show();
                    Log.d("ConfirmationActivity","Concession Confirmed");
                }
            });

            finish();
            startActivity(new Intent(ConfirmActivity.this,MainActivity.class));
        }
    }
}
