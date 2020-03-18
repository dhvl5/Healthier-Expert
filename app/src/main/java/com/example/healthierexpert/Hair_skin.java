package com.example.healthierexpert;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Hair_skin extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] hs = {"Hair","Skin"};
    Spinner s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hair_skin);

        s = findViewById(R.id.hairskin);
        s.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, hs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
