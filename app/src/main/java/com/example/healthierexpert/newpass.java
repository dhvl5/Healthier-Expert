package com.example.healthierexpert;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class newpass extends AppCompatActivity {

    Button submit;
    String email;
    String url1 = "https://creartproducts.com/student_api/healthierexpert/password_update.php";
    EditText newPassword,confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pass);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        newPassword = findViewById(R.id.newpwd);
        confirmPassword = findViewById(R.id.conpwd);

        submit = findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNewPassword();
            }
        });
    }
    private void saveNewPassword() {
        String newPassword1 = newPassword.getText().toString();
        String confirmPassword1 = confirmPassword.getText().toString();
        if (TextUtils.isEmpty(newPassword1)) {
            newPassword.setError("Enter New Password");
            newPassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(confirmPassword1)) {
            confirmPassword.setError("Enter Confirm Password");
            confirmPassword.requestFocus();
            return;
        }
        if (!newPassword1.equals(confirmPassword1))
        {
            confirmPassword.setError("Confirm Password not match.");
            confirmPassword.requestFocus();
        }
        else
        {
            String finalPassword = confirmPassword1;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url1, response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("msg");
                    if (error.equals("0")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(newpass.this, login.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("new_password", finalPassword);
                    return params;
                }
            };
            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        }
    }
}
