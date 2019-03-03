package com.example.dell.concession;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

public class ConcessionFillActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner pass_interval;
    EditText destination,source,startDate,endDate,conDate;
    Button next;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concession_fill);

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
            startActivity(new Intent(this,ConfirmActivity.class));
        }
    }
}
