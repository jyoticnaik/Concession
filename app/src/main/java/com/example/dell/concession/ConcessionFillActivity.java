package com.example.dell.concession;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class ConcessionFillActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner pass_interval;
    EditText destination,source;
    TextView startDate,endDate,conDate;
    Button next;
    private DatePickerDialog.OnDateSetListener dateSetListener1,dateSetListener2,dateSetListener3;

    private FirebaseFirestore con_db;
    private CollectionReference db_concessionDetails;

    private DatabaseHelper myDB = new DatabaseHelper(ConcessionFillActivity.this);
    String uid_string;

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
        startDate=findViewById(R.id.startDate_textview);
        endDate=findViewById(R.id.endDate_textview);
        conDate=findViewById(R.id.conDate_textview);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(ConcessionFillActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,dateSetListener1,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListener1=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;//Reason: January starts with zero
                String date=dayOfMonth+"/"+month+"/"+year;
                startDate.setText(date);
            }
        };

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(ConcessionFillActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,dateSetListener2,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListener2=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;//Reason: January starts with zero
                String date=dayOfMonth+"/"+month+"/"+year;
                endDate.setText(date);
            }
        };
        conDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(ConcessionFillActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,dateSetListener3,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListener3=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;//Reason: January starts with zero
                String date=dayOfMonth+"/"+month+"/"+year;
                conDate.setText(date);
            }
        };
        next.setOnClickListener(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    private Boolean hasValidationErrors(String val_source,String val_startdate,String val_enddate,String val_condate){

        if(val_source.isEmpty()){
            source.setError("Source Required");
            source.requestFocus();
            return true;
        }
        if (val_startdate.isEmpty() || val_startdate.startsWith("P")){
            startDate.setError("Previous pass start date required.");
            Toast.makeText(this, "Previous pass start date required.", Toast.LENGTH_SHORT).show();
            startDate.requestFocus();
            return true;
        }
        if(val_enddate.isEmpty() || val_enddate.startsWith("P")){
            endDate.setError("Previous pass end date required.");
            Toast.makeText(this, "Previous pass end date required.", Toast.LENGTH_SHORT).show();
            endDate.requestFocus();
            return true;
        }
        if(val_condate.isEmpty() || val_condate.startsWith("C")){
            conDate.setError("Concession Date Required.");
            Toast.makeText(this, "Concession date is required.", Toast.LENGTH_SHORT).show();
            conDate.requestFocus();
            return true;
        }
        return false;
    }

    public void saveData(){
        String passInterval_string = pass_interval.getSelectedItem().toString();
        String destination_string = destination.getText().toString();
        String source_string = source.getText().toString();
        String ppsd_string = startDate.getText().toString();
        String pped_string = endDate.getText().toString();
        String condate_string = conDate.getText().toString();

        ConcessionDetails cd=new ConcessionDetails(passInterval_string,destination_string,source_string,ppsd_string,pped_string,condate_string);
        
        Cursor uid_cursor = myDB.getUID();
        
        if(uid_cursor.getCount() == 0){
            Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
        }
        while (uid_cursor.moveToNext()){
            uid_string = uid_cursor.getString(0);
        }

        DocumentReference db_uid = con_db.collection("Students").document(uid_string);

        db_concessionDetails=db_uid.collection("ConcessionDetails");
        DocumentReference db_condetail_nextlevel=db_concessionDetails.document("Details");
        db_condetail_nextlevel.set(cd).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("ConcessionFillActivity","Success!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ConcessionFillActivity","Not Successful.");
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v==next){
            if(!hasValidationErrors(source.getText().toString(),startDate.getText().toString(),endDate.getText().toString(),conDate.getText().toString())) {
                saveData();
                finish();
                startActivity(new Intent(this, ConfirmActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        }
    }
}
