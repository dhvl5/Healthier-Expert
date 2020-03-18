package com.example.healthierexpert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class weight_o_meter extends AppCompatActivity {

    EditText age,weight,height;
    Double age1,weight1,height1,bmi_value;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_o_meter);

        age=findViewById(R.id.age);
        height=findViewById(R.id.height);
        weight=findViewById(R.id.weight);
        submit=findViewById(R.id.submit3);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bmiCount();
            }
        });
    }
    private void bmiCount() {
        age1=Double.parseDouble(age.getText().toString());
        weight1=Double.parseDouble(weight.getText().toString());
        height1=Double.parseDouble(height.getText().toString());
        bmi_value=weight1/(height1*height1);
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("bmi_value",String.format("%.2f",bmi_value));
        editor.apply();
        editor.commit();
        Intent intent=new Intent(weight_o_meter.this,Profile.class);
        //intent.putExtra("bmiValue",bmi_value);
        startActivity(intent);
        finish();

    }
    /*public class ShowPopUp extends Activity {

        PopupWindow popUp;
        LinearLayout layout;
        TextView tv;
        LayoutParams params;
        LinearLayout mainLayout;
        Button but;
        boolean click = true;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            popUp = new PopupWindow(this);
            layout = new LinearLayout(this);
            mainLayout = new LinearLayout(this);
            tv = new TextView(this);
            but = new Button(this);
            but.setText("Click Me");
            but.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
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
            params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            layout.setOrientation(LinearLayout.VERTICAL);
            tv.setText("Hi this is a sample text for popup window");
            layout.addView(tv, params);
            popUp.setContentView(layout);
            // popUp.showAtLocation(layout, Gravity.BOTTOM, 10, 10);
            mainLayout.addView(but, params);
            setContentView(mainLayout);
        }
    }*/
}
