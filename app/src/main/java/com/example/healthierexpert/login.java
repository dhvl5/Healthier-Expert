package com.example.healthierexpert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class login extends AppCompatActivity
{
    Button b1;
    EditText email, password;
    String url = "https://creartproducts.com/student_api/healthierexpert/user_login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        b1 = findViewById(R.id.signin);
        email = findViewById(R.id.enter_your_email);
        password = findViewById(R.id.password);

        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
    }

    private void Login()
    {
        final String email1 = email.getText().toString();
        final String password1 = password.getText().toString();

        if (TextUtils.isEmpty(email1) || !Patterns.EMAIL_ADDRESS.matcher(email1).matches())
        {
            email.setError("Enter Valid Email Address");
            email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password1))
        {
            password.setError("Enter Valid Password");
            password.requestFocus();
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
                    if (error.equals("0")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        Intent intent10 = new Intent(login.this, Activity.class);
                        startActivity(intent10);
                        finish();
                    } else {
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
        }) {
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", email1);
                params.put("password", password1);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void signUp(View v)
    {
        Intent i = new Intent(login.this,signup.class);
        startActivity(i);
    }
    public void fpass(View v)
    {
        Intent i = new Intent(login.this,forgotpass.class);
        startActivity(i);
    }
}
