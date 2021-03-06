package com.example.dell.concession;

import android.R.layout;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

import static android.R.layout.simple_spinner_dropdown_item;

public class FormActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText uid,name,gender,year_of_birth,address,pincode,email_id;
    private String uid_string,name_string,gender_string,yob_string,address_string,pincode_string;
    private Button save_detail;
    private Spinner courseS,yearS;


    //For dateofbirth
    private TextView birthdate;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore con_db;
    private CollectionReference db_studentDetails;

    ArrayAdapter<String> semAdapter1;

    private DatabaseHelper myDB = new DatabaseHelper(FormActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()==null){
            //Checking if user has registered. If not then the scanning page will not be displayed.
            finish();
            Toast.makeText(this, "Please Login First!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginPage.class));

        }

        uid=findViewById(R.id.uid_edittext);
        name=findViewById(R.id.name_edittext);
        gender=findViewById(R.id.gender_edittext);
        year_of_birth=findViewById(R.id.yob_edittext);
        birthdate=findViewById(R.id.dob_textview);
        birthdate_Code();
        address=findViewById(R.id.address_edittext);
        pincode=findViewById(R.id.pincode_edittext);

        email_id=findViewById(R.id.email_edittext);
        email_id.setText(firebaseAuth.getCurrentUser().getEmail());


        save_detail=findViewById(R.id.save_details);

        courseS=findViewById(R.id.course_spinner);
        yearS=findViewById(R.id.sem_spinner);
        ArrayAdapter<String> courseAdapter=new ArrayAdapter<>(this,
                simple_spinner_dropdown_item, getResources().getStringArray(R.array.course_list));

        ArrayAdapter<String> semAdapter=new ArrayAdapter<>(this,
                simple_spinner_dropdown_item, getResources().getStringArray(R.array.sem_list));
        courseS.setAdapter(courseAdapter);
        yearS.setAdapter(semAdapter);

        semAdapter1=new ArrayAdapter<>(this,
                simple_spinner_dropdown_item, getResources().getStringArray(R.array.sem_list1));

        courseS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sel_course = parent.getItemAtPosition(position).toString();
                if(sel_course.equals("MSC")){
                    yearS.setAdapter(semAdapter1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        courseS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String sel_course = parent.getSelectedItem().toString();
//                if(sel_course.equals("MSC")){
//                    yearS.setAdapter(semAdapter1);
//                }
//            }
//        });
        //Filling scanned data into fields.
        autoFillEditText();

        save_detail.setOnClickListener(this);
    }

    public void birthdate_Code(){
        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(FormActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener,
                        Integer.parseInt(yob_string),month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String date=dayOfMonth+"/"+month+"/"+year;
                birthdate.setText(date);
            }
        };
    }

    public void autoFillEditText(){
        Cursor res = myDB.getAllData();
        if(res.getCount() == 0){
            Toast.makeText(this, "Aadhaar Card not yet scanned.", Toast.LENGTH_SHORT).show();
            uid.setText(R.string.NA);
            name.setText(R.string.NA);
            gender.setText(R.string.NA);
            year_of_birth.setText(R.string.NA);
        }
        else {
            while (res.moveToNext()) {
                Log.d("FormActivity", " " + res.getString(0));
                uid_string = res.getString(0);
                name_string = res.getString(1);
                gender_string = res.getString(2);
                yob_string = res.getString(3);
                address_string = res.getString(4);
                pincode_string = res.getString(5);
            }

            Log.d("FormActivity"," "+uid_string+" "+name_string+" "+gender_string+" "+yob_string);
            uid.setText(uid_string);
            name.setText(name_string);
            gender.setText(gender_string);
            year_of_birth.setText(yob_string);
            address.setText(address_string);
            pincode.setText(pincode_string);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    private Boolean hasValidationErrors(String dob,String addr){
        if(dob.isEmpty() || dob.startsWith("D")){
            birthdate.setError("Date of Birth Required");
            Toast.makeText(this, "Date of Birth Required", Toast.LENGTH_SHORT).show();
            birthdate.requestFocus();
            return true;
        }
        if(addr.isEmpty()){
            address.setError("Address Required");
            address.requestFocus();
            return true;
        }

        return false;
    }

    public void saveProfile(){
        con_db= FirebaseFirestore.getInstance();
        db_studentDetails=con_db.collection("Students");
        DocumentReference db_uid=db_studentDetails.document(uid.getText().toString());

        String birthday_string=birthdate.getText().toString();
        String address_string=address.getText().toString();
        String course_string=courseS.getSelectedItem().toString();
        String sem_string=yearS.getSelectedItem().toString();
        String email=email_id.getText().toString();


        final StudentDetails sd=new StudentDetails(name_string,gender_string,birthday_string,address_string,pincode_string,course_string,sem_string,email);

        db_uid.set(sd).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(FormActivity.this, "Details saved successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                        MainActivity.test = true;
                        startActivity(new Intent(FormActivity.this,MainActivity.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FormActivity","Error while saving data: ",e);
                Toast.makeText(FormActivity.this, "Saving Unsuccessfull! Error: "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v==save_detail){
            if(!hasValidationErrors(birthdate.getText().toString(),address.getText().toString())) {
                saveProfile();
            }
        }
    }
}
