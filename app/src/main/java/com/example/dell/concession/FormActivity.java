package com.example.dell.concession;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;

import java.util.Calendar;

public class FormActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText uid,name,gender,year_of_birth,address,email_id;
    private String uid_string,name_string,gender_string,yob_string;
    private Button save_detail;
    private Spinner courseS,yearS;

    //For dateofbirth
    private EditText birthdate;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        //for birthdate
        birthdate=findViewById(R.id.birthdate_edittext);

        uid=findViewById(R.id.uid_edittext);
        name=findViewById(R.id.name_edittext);
        gender=findViewById(R.id.gender_edittext);
        year_of_birth=findViewById(R.id.yob_edittext);
        address=findViewById(R.id.address_edittext);
        email_id=findViewById(R.id.email_edittext);
        save_detail=findViewById(R.id.save_details);


        courseS=findViewById(R.id.course_spinner);
        yearS=findViewById(R.id.year_spinner);


        ArrayAdapter<String> courseAdapter=new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.course_list));

        ArrayAdapter<String> yearAdapter=new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.year_list));

        courseS.setAdapter(courseAdapter);
        yearS.setAdapter(yearAdapter);

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

        birthdate.setOnClickListener(this);

        dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date=dayOfMonth+"/"+month+"/"+year;
                birthdate.setText(date);
            }
        };

        save_detail.setOnClickListener(this);
    }

    public void saveProfile(){
        //TODO:firebase saving
    }

    public void birthdateCode(){
        Calendar cal=Calendar.getInstance();
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog=new DatePickerDialog(FormActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener,
                Integer.parseInt(yob_string),month,day);


        try {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
        catch (NullPointerException ne)
        {
            Log.d("FormActivity","setBackgroundDrawable may have produced null pointer exception");
        }
    }

    @Override
    public void onClick(View v) {
        if(v==save_detail){
            saveProfile();
        }
        if(v==birthdate){
            birthdateCode();
        }
    }
}
