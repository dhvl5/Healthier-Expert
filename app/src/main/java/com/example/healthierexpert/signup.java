package com.example.healthierexpert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity
{
    Button b1;
    EditText name, email, mobile, password;
    RadioGroup radioGroup;
    RadioButton radioButton;
    String url = "https://creartproducts.com/student_api/healthierexpert/user_add.php";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        b1 = findViewById(R.id.cont);
        name = findViewById(R.id.fname);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.phoneno);
        password = findViewById(R.id.spassword);
        radioGroup = findViewById(R.id.rg1);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
    }

    private void Register()
    {
        final String name1 = name.getText().toString();
        final String email1 = email.getText().toString();
        final int radioButtonId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioButtonId);
        final String mobile1 = mobile.getText().toString();
        final String password1 = password.getText().toString();

        if (TextUtils.isEmpty(name1))
        {
            name.setError("Enter UserName");
            name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email1) || !Patterns.EMAIL_ADDRESS.matcher(email1).matches())
        {
            email.setError("Enter Valid Email-ID");
            email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mobile1) || TextUtils.getTrimmedLength(mobile1)!=10)
        {
            mobile.setError("Enter 10 Digit Mobile No..");
            mobile.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(name1))
        {
            name.setError("Enter UserName");
            name.requestFocus();
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("msg");
                    if (error.equals("0"))
                    {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(signup.this, login.class);
                        intent.putExtra("email", email1);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("name", name1);
                params.put("email", email1);
                params.put("mobile", mobile1);
                params.put("gender", radioButton.toString());
                params.put("password", password1);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
