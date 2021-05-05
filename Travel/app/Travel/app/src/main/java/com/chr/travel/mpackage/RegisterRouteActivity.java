package com.chr.travel.mpackage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chr.travel.R;

import java.util.List;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;

public class RegisterRouteActivity extends AppCompatActivity {

    TextView txt_date;

    String startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_register_route);

        txt_date = findViewById(R.id.txt_date);

        Intent i = getIntent();
        startDate = i.getStringExtra("startDate");
        endDate = i.getStringExtra("endDate");

        txt_date.setText(startDate + " " + endDate);


    }

}