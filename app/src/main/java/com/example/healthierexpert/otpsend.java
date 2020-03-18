package com.example.healthierexpert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.StringRequest;

public class otpsend extends AppCompatActivity {

    String otp,email;
    EditText otp_field;
    String url = "https://creartproducts.com/student_api/healthierexpert/check_otp.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_send);
        Button b1 = (Button)findViewById(R.id.submitbtn);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        otp_field=findViewById(R.id.otp_email);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify_email_otp_forget_password();
            }
        });
    }
    private void verify_email_otp_forget_password() {
        otp = otp_field.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("msg");
                    if (error.equals("0")){
                        Toast.makeText(getApplicationContext(),otp, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(otpsend.this,newpass.class);
                        intent.putExtra("email",email);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, error -> Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email",email);
                params.put("otp",otp);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
