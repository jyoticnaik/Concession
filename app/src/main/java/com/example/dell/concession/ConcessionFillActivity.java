package com.example.dell.concession;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class ConcessionFillActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner pass_interval;
    EditText destination,source,startDate,endDate,conDate;
    Button next;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private FirebaseFirestore con_db;
    private CollectionReference db_concessionDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concession_fill);

        con_db=FirebaseFirestore.getInstance();

        destination=findViewById(R.id.dest_edittext);
        destination.setText(R.string.clgdest);
        source=findViewById(R.id.source_edittext);
        next=findViewById(R.id.next);

        //Code for dropdown list for pass interval
        pass_interval=findViewById(R.id.passint_spinner);
        ArrayAdapter<String> passint_adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.passint_list));
        pass_interval.setAdapter(passint_adapter);

        //Calender using field
        startDate=findViewById(R.id.startdate_edittext);
        endDate=findViewById(R.id.enddate_edittext);
        conDate=findViewById(R.id.condate_edittext);

        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        conDate.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    private Boolean hasValidationErrors(String val_source,String val_startdate,String val_enddate,String val_condate){

        if(val_source.isEmpty()){
            source.setError("Source Required");
            source.requestFocus();
            return true;
        }
        if (val_startdate.isEmpty()){
            startDate.setError("Previous pass start date required.");
            startDate.requestFocus();
            return true;
        }
        if(val_enddate.isEmpty()){
            endDate.setError("Previous pass end date required.");
            endDate.requestFocus();
            return true;
        }
        if(val_condate.isEmpty()){
            conDate.setError("Concession Date Required.");
            conDate.requestFocus();
            return true;
        }
        return false;
    }

    public void dateCode(){
        Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog=new DatePickerDialog(ConcessionFillActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener,
                year,month,day);

        try {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
        catch (NullPointerException ne)
        {
            Log.d("FormActivity","setBackgroundDrawable may have produced null pointer exception");
        }
    }


    public void saveData(){
        String passInterval_string=pass_interval.getSelectedItem().toString();
        String destination_string=destination.getText().toString();
        String source_string=source.getText().toString();
        String ppsd_string=startDate.getText().toString();
        String pped_string=endDate.getText().toString();
        String condate_string=conDate.getText().toString();

        ConcessionDetails cd=new ConcessionDetails(passInterval_string,destination_string,source_string,ppsd_string,pped_string,condate_string);
        String student_id=new StudentDetails().getId();

        db_concessionDetails=con_db.collection("Students").document(student_id).collection("ConcessionDetails");
        db_concessionDetails.add(cd)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("ConcessionFillActivity","Concession details added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ConcessionFillActivity","Error: "+e.getMessage());
                    }
                });
    }


    @Override
    public void onClick(View v) {
        if(v==startDate){
            dateCode();
            dateSetListener=new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String date=dayOfMonth+"/"+month+"/"+year;
                    startDate.setText(date);
                }
            };
        }
        if(v==endDate){
            dateCode();
            dateSetListener=new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String date=dayOfMonth+"/"+month+"/"+year;
                    endDate.setText(date);
                }
            };
        }
        if(v==conDate){
            dateCode();
            dateSetListener=new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String date=dayOfMonth+"/"+month+"/"+year;
                    conDate.setText(date);
                }
            };
        }

        if (v==next){
            if(!hasValidationErrors(source.getText().toString(),startDate.getText().toString(),endDate.getText().toString(),conDate.getText().toString())) {
                saveData();
                startActivity(new Intent(this, ConfirmActivity.class));
            }
        }
    }
}
