package com.chr.travel.mpackage.operation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.chr.travel.R;


/* 출발.도착 날짜 Picker */

public class TripDatePickerActivity extends AppCompatActivity {

    DatePicker datepicker;
    Button btn_save;
    TextView txt_tripDate;
    String yy, mm, dd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_trip_date_picker);

        btn_save = findViewById(R.id.btn_save);
        datepicker = findViewById(R.id.datepicker);
        txt_tripDate = findViewById(R.id.txt_tripDate);

        Intent i = getIntent();

        int date = i.getIntExtra("date", 0);

        if(date == 1){
            txt_tripDate.setText("출발 날짜");
        }

        else if(date == 2){
            txt_tripDate.setText("도착 날짜");
        }

        datepicker.init(datepicker.getYear(), datepicker.getMonth(), datepicker.getDayOfMonth(),

                new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        yy=Integer.toString(year);
                        mm=Integer.toString(monthOfYear+1);
                        dd=Integer.toString(dayOfMonth);
                    }
                });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.putExtra("yy",yy);
                intent.putExtra("mm",mm);
                intent.putExtra("dd",dd);
                setResult(RESULT_OK, intent);

                finish();

            }
        });
    }
}