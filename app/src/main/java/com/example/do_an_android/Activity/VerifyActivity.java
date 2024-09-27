package com.example.do_an_android.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.do_an_android.Model.SendEmail;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.R;
import java.util.HashMap;
import java.util.Map;

public class VerifyActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnNext, btnVerifyEmail;
    SharedPreferences sharedPreferencesUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        sharedPreferencesUser = getSharedPreferences("user", Context.MODE_PRIVATE);
        setControl();
        btnNext.setOnClickListener(this);
        btnVerifyEmail.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnNext) {
            String username = sharedPreferencesUser.getString("username", "");
            if (!username.equals("")) {
                RequestQueue queue = Volley.newRequestQueue(VerifyActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlLogin, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("LOG LOGIN RESPONSE", response); // Log response for debugging
                        if (response.equals("1")) {
                            Toast.makeText(VerifyActivity.this, ".", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(VerifyActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            sharedPreferencesUser.edit().clear().commit();
                            Toast.makeText(VerifyActivity.this, "Xin hãy xác nhận tài khoản trước khi tiến hành mua hàng.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(VerifyActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VerifyActivity.this, "Lỗi kết nối: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("LOG LOGIN ERROR", error.getMessage(), error); // Log error for debugging
                    }
                }) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", username);
                        return params;
                    }
                };
                queue.add(stringRequest);
            }
        }

        if (id == R.id.btnVerifyEmail) {
            String username = sharedPreferencesUser.getString("username", "");
            Log.d("LOG username", username);
            if (!username.equals("")) {
                RequestQueue queue = Volley.newRequestQueue(VerifyActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlGetEmailCustomer, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("LOG GET EMAIL RESPONSE", response); // Log response for debugging
                        sendVerificationEmail(response);
                        Toast.makeText(VerifyActivity.this, "Email xác nhận đã được gửi đến cho bạn, vui lòng kiểm tra hộp thư của bạn.", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VerifyActivity.this, "Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng của bạn.", Toast.LENGTH_SHORT).show();
                        Log.e("LOG GET EMAIL ERROR", error.getMessage(), error); // Log error for debugging
                    }
                }) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", username);
                        return params;
                    }
                };
                queue.add(stringRequest);
            } else {
                Toast.makeText(VerifyActivity.this, "Có lỗi không xác đinh xảy ra.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendVerificationEmail(String recieverEmail) {
        String subject = "VERIFY ACCOUNT.";
        String text = "Hello " + recieverEmail + ",<br><br>"
                + "<span style='font-size: 16px;'>Thank you for your registration. To activate your account, please click this link:</span><br>"
                + "<a href='" + Server.urlBase + "activateAccount.php?email=" + recieverEmail + "' style='font-size: 16px;'>Active account</a><br><br>"
                + "<span style='font-size: 16px;'>Best regards,</span><br>";
        SendEmail email = new SendEmail();
        email.sendEmail(recieverEmail, subject, text);
    }

    private void setControl() {
        btnNext = findViewById(R.id.btnNext);
        btnVerifyEmail = findViewById(R.id.btnVerifyEmail);
    }
}