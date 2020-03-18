package com.example.healthierexpert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class forgotpass  extends AppCompatActivity {

    EditText email;
    String url1 = "https://creartproducts.com/student_api/healthierexpert/send_otp.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        Button b1 = (Button)findViewById(R.id.send);
        email=findViewById(R.id.emailtv);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit_url_forget_password();
            }
        });
    }
    private void submit_url_forget_password() {
        String email_for_otp = email.getText().toString();
        if (TextUtils.isEmpty(email_for_otp))
        {
            email.setError("Enter Email Id");
            email.requestFocus();
            return;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url1, response -> {
            try
            {
                JSONObject jsonObject = new JSONObject(response);
                String error = jsonObject.getString("error");
                String otp = jsonObject.getString("otp");
                String message = jsonObject.getString("msg");
                if (error.equals("0")){
                    //Toast.makeText(getApplicationContext(), otp, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(forgotpass.this,otpsend.class );
                    intent.putExtra("email",email_for_otp);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", email_for_otp);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
