package com.chr.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class BirthPickerActivity extends AppCompatActivity {

    DatePicker datepicker;
    Button btn_save;
    String yy, mm, dd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birth_picker);
        btn_save = findViewById(R.id.btn_save);
        datepicker = findViewById(R.id.datepicker);

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