package com.example.do_an_android._Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView; // DONE
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.do_an_android.Activity.VerifyActivity;
import com.example.do_an_android.Model.SendEmail;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.Model.Support;
import com.example.do_an_android.R;
import com.example.do_an_android.Retrofit2.APIUtils;
import com.example.do_an_android.Retrofit2.DataClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class SignupTabFragment extends Fragment implements View.OnClickListener {
    TextInputLayout layout_hoten, layout_username_sigup, layout_password_signup, layout_password_confirm_signup, layout_email_signup;
    TextInputEditText hoten, username_sigup, password_signup, password_confirm_signup, email_signup;
    Button btnSignup;
    Context context;
    ImageView imageSignup; // DONE
    String realPath = "";
    String image=""; // DONE
    private static final int REQUEST_CODE_IMAGE_SIGNUP = 764; // DONE

    public SignupTabFragment(Context context) {
        this.context = context;
    }
    public SignupTabFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl(view);
        btnSignup.setOnClickListener(this);
    }

    private void setControl(View view) {
        hoten = view.findViewById(R.id.hoten);
        username_sigup = view.findViewById(R.id.username_sigup);
        password_signup = view.findViewById(R.id.password_signup);
        password_confirm_signup = view.findViewById(R.id.password_confirm_signup);
        email_signup = view.findViewById(R.id.email_signup);
        layout_hoten = view.findViewById(R.id.layout_hoten);
        layout_username_sigup = view.findViewById(R.id.layout_username_sigup);
        layout_password_signup = view.findViewById(R.id.layout_password_signup);
        layout_email_signup = view.findViewById(R.id.layout_email_signup);
        layout_password_confirm_signup = view.findViewById(R.id.layout_password_confirm_signup);
        btnSignup = view.findViewById(R.id.btnSignup);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnSignup) {
            boolean valid = true;

            if (hoten.getText().toString().equals("")) {
                layout_hoten.setErrorEnabled(true);
                layout_hoten.setError("Họ và tên không được để trống.");
                valid = false;
            } else {
                layout_hoten.setErrorEnabled(false);
                layout_hoten.setError(null);
            }

            String username = username_sigup.getText().toString();
            if (username.equals("")) {
                layout_username_sigup.setErrorEnabled(true);
                layout_username_sigup.setError("Tên đăng nhập không được để trống.");
                valid = false;
            } else if (username.length() > 10) {
                layout_username_sigup.setErrorEnabled(true);
                layout_username_sigup.setError("Tên đăng nhập không được vượt quá 10 ký tự.");
                valid = false;
            } else {
                layout_username_sigup.setErrorEnabled(false);
                layout_username_sigup.setError(null);
            }

            if (email_signup.getText().toString().equals("")) {
                layout_email_signup.setErrorEnabled(true);
                layout_email_signup.setError("Email không được để trống.");
                valid = false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email_signup.getText().toString()).matches()) {
                layout_email_signup.setErrorEnabled(true);
                layout_email_signup.setError("Email không đúng định dạng.");
                valid = false;
            } else {
                layout_email_signup.setErrorEnabled(false);
                layout_email_signup.setError(null);
            }

            if (password_signup.getText().toString().equals("")) {
                layout_password_signup.setErrorEnabled(true);
                layout_password_signup.setError("Mật khẩu không được để trống.");
                valid = false;
            } else if (!password_signup.getText().toString().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$")) {
                layout_password_signup.setErrorEnabled(true);
                layout_password_signup.setError("Mật khẩu phải có ít nhất 1 ký hiệu đặc biệt, chữ hoa, chữ thường và số.");
                valid = false;
            } else {
                layout_password_signup.setErrorEnabled(false);
                layout_password_signup.setError(null);
            }

            if (password_confirm_signup.getText().toString().equals("")) {
                layout_password_confirm_signup.setErrorEnabled(true);
                layout_password_confirm_signup.setError("VUI LÒNG NHẬP LẠI XÁC NHẬN MẬT KHẨU.");
                valid = false;
            } else if (!password_signup.getText().toString().equals(password_confirm_signup.getText().toString())) {
                password_confirm_signup.requestFocus();
                layout_password_confirm_signup.setErrorEnabled(true);
                layout_password_confirm_signup.setError("NHẬP LẠI MẬT KHẨU KHÔNG KHỚP !");
                valid = false;
            } else {
                layout_password_confirm_signup.setErrorEnabled(false);
                layout_password_confirm_signup.setError(null);
            }

            if (valid) {
                Sigup();
                sendVerificationEmail();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==111 &&grantResults[0]== PackageManager.PERMISSION_GRANTED)
        {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_IMAGE_SIGNUP);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_IMAGE_SIGNUP && resultCode == ((Activity) context).RESULT_OK && data != null) {
            Uri uri = data.getData();
            realPath = getRealPathFromURI(uri);
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageSignup.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void _Clear() {
        hoten.setText("");
        username_sigup.setText("");
        password_signup.setText("");
        email_signup.setText("");
        password_confirm_signup.setText("");
        hoten.requestFocus();
    }

    private void Sigup() {
        File file = new File(realPath);
        String file_path = file.getAbsolutePath();
        try {
            String[] arrayNameFile = file_path.split("\\.");
            file_path = arrayNameFile[0] + System.currentTimeMillis() + "." + arrayNameFile[1];
        }
        catch (Exception ignored)
        {
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", file_path, requestBody);
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<String> callback = dataClient.UploadImage(body);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if(response!=null)
                {
                    image =response.body();
                    insertCustomer();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                insertCustomer();

            }
        });
    }

    private void insertCustomer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlSignup, response -> {
            switch (response) {
                case "-1":
                    Toast.makeText(context, "TÀI KHOẢN ĐÃ TỒN TẠI.", Toast.LENGTH_SHORT).show();
                    break;
                case "-2":
                    Toast.makeText(context, "Email đã được đăng ký.", Toast.LENGTH_SHORT).show();
                    break;
                case "0":
                    Toast.makeText(context, "Có lỗi xảy ra khi thêm tài khoản.", Toast.LENGTH_SHORT).show();
                    break;
                case "1":
                    SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username_sigup.getText().toString());
                    editor.apply();

                    _Clear();
                    Log.d("LOG SIGNUP RESPONSE", response);
                    showAlertDialog();
                    break;
                default:
                    Log.d("UNKNOWN RESPONSE", "Unknown response: " + response);
                    Toast.makeText(context, "Unknown response: " + response, Toast.LENGTH_SHORT).show();
                    break;
            }
        }, error -> {
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username_sigup.getText().toString());
                params.put("name", hoten.getText().toString());
                params.put("password", Support.EndcodeMD5(password_signup.getText().toString()));
                params.put("address", "");
                params.put("phone", "" );
                params.put("image", "");
                params.put("email", email_signup.getText().toString());
                params.put("status", "0");
                params.put("idquyen", "2");
                Log.d("LOG PARAM", params.toString());
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Đăng kí thành công");
        builder.setMessage("Vui lòng kiểm tra email để xác nhận tài khoản.");
        builder.setPositiveButton("OK", (dialog, which) -> {
            Intent intent = new Intent(context, VerifyActivity.class); // Change this to your verification activity class
            context.startActivity(intent);
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> {
            dialog.dismiss(); // Dismiss the dialog
        });
        builder.show();
    }

    private void sendVerificationEmail() {
        String subject = "VERIFY ACCOUNT.";
        String text = "Hello " + email_signup.getText().toString() + ",<br><br>"
                + "<span style='font-size: 16px;'>Thank you for your registration. To activate your account, please click this link:</span><br>"
                + "<a href='" + Server.urlBase + "activateAccount.php?email=" + email_signup.getText().toString() + "' style='font-size: 16px;'>Active account</a><br><br>"
                + "<span style='font-size: 16px;'>Best regards,</span><br>";
        SendEmail email = new SendEmail();
        email.sendEmail(email_signup.getText().toString(), subject, text);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }
}