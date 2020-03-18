package com.example.healthierexpert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class Profile extends AppCompatActivity
{
    MaterialToolbar materialToolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    String bmi_final;
    TextView bmi_num;
    Button button;

    PopupWindow popUp;
    LinearLayout layout;
    TextView tv;
    ViewGroup.LayoutParams params;
    LinearLayout mainLayout;
    Button but;
    boolean click = true;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        //Intent intent_bmi = getIntent();
        //bmi_final=String.format("%.2f",intent_bmi.getDoubleExtra("bmiValue",1.0));
        SharedPreferences sharedPreferences1=getApplicationContext().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        bmi_final=sharedPreferences1.getString("bmi_value",null);
        bmi_num=findViewById(R.id.bmi_num);
        if (sharedPreferences1.contains("bmi_value")){
            bmi_num.setText(bmi_final);
        }
        else{
            bmi_num.setText("0.0");
        }

        button=findViewById(R.id.bmi_popup);

        /*popUp = new PopupWindow(this);
        layout = new LinearLayout(this);
        mainLayout = new LinearLayout(this);
        tv = new TextView(this);
        but = new Button(this);
        but.setText("Click Me");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (click) {
                    popUp.showAtLocation(layout, Gravity.BOTTOM, 10, 10);
                    popUp.update(50, 50, 300, 80);
                    click = false;
                } else {
                    popUp.dismiss();
                    click = true;
                }

            }
        });

        popUp.setContentView(layout);
        popUp.showAtLocation(layout, Gravity.BOTTOM, 10, 10);
        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        tv.setText("Hi this is a sample text for popup window");
        layout.addView(tv, params);

        mainLayout.addView(but, params);
        setContentView(mainLayout);*/



        materialToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(materialToolbar);

        drawerLayout = findViewById(R.id.custom_drawer_layout);
        navigationView = findViewById(R.id.custom_navigation_view);

        ActionBarDrawerToggle drawerToggle;
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,materialToolbar,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                int id = item.getItemId();

                switch(id)
                {
                    case R.id.nav_item_1:
                        Toast.makeText(getApplicationContext(), "clickednav1", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Profile.this, CalorieCounter.class));
                        finish();
                        break;
                    case R.id.nav_item_2:
                        startActivity(new Intent(Profile.this, weight_o_meter.class));
                        finish();
                        break;
                    case R.id.nav_item_3:
                        startActivity(new Intent(Profile.this,Hair_skin.class));
                        finish();
                        break;
                    case R.id.nav_item_4:
                        startActivity(new Intent(Profile.this,ChildCare.class));
                        finish();
                        break;
                    case R.id.nav_item_5:
                        startActivity(new Intent(Profile.this,FAQs.class));
                        finish();
                        break;
                    default:

                    break;
                }
                return true;
            }
        });
    }
}