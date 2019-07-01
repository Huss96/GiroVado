package com.example.hussnain.girovado;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InsertActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.timeStay) Spinner timeStay;
    @BindView(R.id.dateButton) Button dateButton;
    @BindView(R.id.timeButton) Button timeButton;
    @BindView(R.id.date) TextView txtDate;
    @BindView(R.id.time) TextView txtTime;
    @BindView(R.id.leisureName) EditText name;
    @BindView(R.id.addressText) EditText address;
    @BindView(R.id.cityText) EditText city;
    @BindView(R.id.descriptionText) EditText description;
    @BindView(R.id.radioButton1) RadioButton radioPlace;
    @BindView(R.id.radioButton2) RadioButton radioActivity;
    @BindView(R.id.radioFamily) RadioButton radioFamily;
    @BindView(R.id.radioMusic) RadioButton radioMusic;
    @BindView(R.id.radioHistory) RadioButton radioHistory;
    @BindView(R.id.radioFriend) RadioButton radioFriend;
    @BindView(R.id.radioLove) RadioButton radioLove;
    @BindView(R.id.radioSport) RadioButton radioSport;
    @BindView(R.id.radioOther) RadioButton radioOther;
    @BindView(R.id.radioChildren) RadioButton radioChildren;
    @BindView(R.id.uncheck) Button uncheck;
    @BindView(R.id.next) Button next;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        ButterKnife.bind(this);

        //create the adpter for the Spinner
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.time,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeStay.setAdapter(adapter2);

        dateButton.setOnClickListener(this);
        timeButton.setOnClickListener(this);
        radioPlace.setOnClickListener(this);
        radioActivity.setOnClickListener(this);
        next.setOnClickListener(this);
        radioChildren.setOnClickListener(this);
        radioLove.setOnClickListener(this);
        radioFamily.setOnClickListener(this);
        radioFriend.setOnClickListener(this);
        radioMusic.setOnClickListener(this);
        radioSport.setOnClickListener(this);
        radioOther.setOnClickListener(this);
        radioHistory.setOnClickListener(this);
        uncheck.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        int mYear, mMonth, mDay, mHour, mMinute;
        if(v == uncheck){
            radioChildren.setChecked(false);
            radioLove.setChecked(false);
            radioFamily.setChecked(false);
            radioFriend.setChecked(false);
            radioMusic.setChecked(false);
            radioSport.setChecked(false);
            radioOther.setChecked(false);
            radioHistory.setChecked(false);
        }
        if (v == dateButton) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == timeButton) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            if(minute<9)
                                txtTime.setText(hourOfDay + ":0" + minute);
                            else
                                txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if(v == radioPlace){
            txtDate.setVisibility(View.INVISIBLE);
            txtTime.setVisibility(View.INVISIBLE);
            dateButton.setVisibility(View.INVISIBLE);timeButton.setVisibility(View.INVISIBLE);

        }
        if(v == radioActivity){
            txtDate.setVisibility(View.VISIBLE);
            txtTime.setVisibility(View.VISIBLE);
            dateButton.setVisibility(View.VISIBLE);
            timeButton.setVisibility(View.VISIBLE);

        }
        if(v == next){
            if(!radioLove.isChecked()&& !radioFriend.isChecked() && !radioFamily.isChecked() && !radioOther.isChecked() && !radioSport.isChecked() && !radioMusic.isChecked() && !radioHistory.isChecked() && !radioChildren.isChecked())
                alert();
            if(((!radioActivity.isChecked())&&(!radioPlace.isChecked()))||(radioActivity.isChecked()&&((txtTime.getText().toString().isEmpty())||(txtDate.getText().toString().isEmpty())))||((name.getText().toString().isEmpty())||(address.getText().toString().isEmpty())||(city.getText().toString().isEmpty())||(description.getText().toString().isEmpty()))){
                alert();
            }else {
                int place = 1;
                if(radioActivity.isChecked())
                    place = 0;
                ArrayList<String> type = new ArrayList<>();
                if(radioLove.isChecked())
                    type.add(radioLove.getText().toString());
                if(radioFamily.isChecked())
                    type.add(radioFamily.getText().toString());
                if(radioFriend.isChecked())
                    type.add(radioFriend.getText().toString());
                if(radioSport.isChecked())
                    type.add(radioSport.getText().toString());
                if(radioOther.isChecked())
                    type.add(radioOther.getText().toString());
                if(radioMusic.isChecked())
                    type.add(radioMusic.getText().toString());
                if(radioHistory.isChecked())
                    type.add(radioHistory.getText().toString());
                if(radioChildren.isChecked())
                    type.add(radioChildren.getText().toString());
                Leisure leisure = new Leisure(place,type,name.getText().toString(),address.getText().toString(),city.getText().toString(),description.getText().toString(),txtDate.getText().toString(),txtTime.getText().toString(),timeStay.getSelectedItem().toString());
                startActivity(new Intent(InsertActivity.this, PhotoActivity.class));
            }
        }
    }
    private void alert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("You have to to fill in all the fields");
        builder1.setCancelable(true);

        builder1.setNeutralButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
