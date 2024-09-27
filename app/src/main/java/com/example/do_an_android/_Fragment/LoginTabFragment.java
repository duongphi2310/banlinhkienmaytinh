package com.example.do_an_android._Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.do_an_android.Activity.MainActivity;
import com.example.do_an_android.Activity.TrangQuanLy;
import com.example.do_an_android.Activity.VerifyActivity;
import com.example.do_an_android.Model.SendEmail;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.Model.Support;
import com.example.do_an_android.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.HashMap;
import java.util.Map;

public class LoginTabFragment extends Fragment implements View.OnClickListener {
    Dialog dialog;
    TextView tvForgotPassword;
    Context context;
    EditText edtIssuedPassEmail;
    TextInputEditText username_login, password_login;
    TextInputLayout layout_username_login, layout_password_login;
    CheckBox ckbRemember;
    Button btnLogin, btnConfirmIssuingPass, btnCancelIssuingPass;
    SharedPreferences sharedPreferencesRemember;
    SharedPreferences sharedPreferencesUser;
    Boolean checkPayOrder = false;
    private static String NEWISSUEDPASSWORD;

    public LoginTabFragment(Context context, Boolean checkPayOrder) {
        this.context = context;
        this.checkPayOrder = checkPayOrder;
    }

    public LoginTabFragment(Context context) {
        this.context = context;
    }

    public LoginTabFragment() {
    }

    private boolean validateEditText(EditText txt, String description) {
        if (txt.getText().toString().trim().length() == 0) {
            Toast.makeText(context, description, Toast.LENGTH_SHORT).show();
            txt.requestFocus();
            return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl(view);
        checkRemember();
        sharedPreferencesUser = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        btnLogin.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
    }

    private void checkRemember() {
        sharedPreferencesRemember = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        boolean isRemember = sharedPreferencesRemember.getBoolean("isRemember", false);
        if (isRemember) {
            username_login.setText(sharedPreferencesRemember.getString("username", ""));
            password_login.setText(sharedPreferencesRemember.getString("password", ""));
        }
        ckbRemember.setChecked(isRemember);
        if (!isRemember) {
            clearRememberedData();
        }
    }

    private void setControl(View view) {
        username_login = (TextInputEditText) view.findViewById(R.id.username_login);
        password_login = (TextInputEditText) view.findViewById(R.id.password_login);
        layout_username_login = (TextInputLayout) view.findViewById(R.id.layout_username_login);
        layout_password_login = (TextInputLayout) view.findViewById(R.id.layout_password_login);
        ckbRemember = view.findViewById(R.id.ckbRemember);
        btnLogin = view.findViewById(R.id.btnLogin);
        tvForgotPassword = view.findViewById(R.id.tvForgotPassword);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnLogin:
                if (username_login.getText().toString().equals("")) {
                    layout_username_login.setErrorEnabled(true);
                    layout_username_login.setError("Tên đăng nhập không được để trống.");
                } else {
                    layout_username_login.setErrorEnabled(false);
                    layout_username_login.setError(null);
                }
                if (password_login.getText().toString().equals("")) {
                    layout_password_login.setErrorEnabled(true);
                    layout_password_login.setError("Mật khẩu không được để trống.");
                } else {
                    layout_password_login.setErrorEnabled(false);
                    layout_password_login.setError(null);
                }
                if (!username_login.getText().toString().isEmpty()
                        && !password_login.getText().toString().isEmpty()) {
                            // check login
                    checkAccountStatus();
                }
                break;
            case R.id.tvForgotPassword:
                openDialogReissuePassword();
                break;
            case R.id.btnCancelIssuingPass:
                dialog.dismiss();
                break;
            case R.id.btnConfirmIssuingPass:
                if (validateEditText(edtIssuedPassEmail, "Email không được để trống."))
                    break;
                else
                    reissuePassword();
                break;
        }
    }

    private String generateNewPassword() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder newPassword = new StringBuilder("@");
        for (int i = 0; i < 7; i++) {
            int index = (int) (Math.random() * chars.length());
            newPassword.append(chars.charAt(index));
        }
        return newPassword.toString();
    }

    public void sendEmailReissuedPassword() {
        String subject = "CẤP LẠI MẬT KHẨU CỦA ỨNG DỤNG BÁN LINH KIỆN ĐIỆN TỬ";
        String text = "Your New Password: " + NEWISSUEDPASSWORD;
        SendEmail email = new SendEmail();
        email.sendEmail(edtIssuedPassEmail.getText().toString(), subject, text);
    }

    private void reissuePassword() {
        NEWISSUEDPASSWORD = generateNewPassword();
        sendEmailReissuedPassword();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlReissuePassword, response -> {
            if (response.toString().equals("-1")) {
                Toast.makeText(context, "Có lỗi không xác định xảy ra.", Toast.LENGTH_SHORT).show();
            } else if (response.toString().equals("0")) {
                Toast.makeText(context, "Có lỗi xảy ra trong lúc cấp lại mật khẩu.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Mật khẩu đã được cấp lại. Xin hãy kiểm tra email.", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                sharedPreferencesUser.edit().clear().commit();
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("checkChangpass", true);
                startActivity(intent);
            }
        }, error -> {
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", edtIssuedPassEmail.getText().toString());
                params.put("pass_new", Support.EndcodeMD5(NEWISSUEDPASSWORD));
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }

    private void openDialogReissuePassword() {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.diaglog_forgot_pass);
        dialog.setCanceledOnTouchOutside(false);
        edtIssuedPassEmail = dialog.findViewById(R.id.edtIssuedPassEmail);
        btnConfirmIssuingPass = dialog.findViewById(R.id.btnConfirmIssuingPass);
        btnCancelIssuingPass = dialog.findViewById(R.id.btnCancelIssuingPass);
        btnConfirmIssuingPass.setOnClickListener(this);
        btnCancelIssuingPass.setOnClickListener(this);
        dialog.show();
    }

    private void showLockedAccountDialog() {
        if (getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("TÀI KHOẢN BỊ KHÓA");
            builder.setMessage("Tài khoản của bạn đã bị khóa, vui lòng liên hệ quản trị viên để biết thêm thông tin chi tiết.");
            builder.setPositiveButton("OK", (dialog, which) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void checkAccountStatus() {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlCheckAccountStatus, response -> {
            if (response.equals("not_exist")) {
                Toast.makeText(context, "Sai tài khoản hoặc mật khẩu.", Toast.LENGTH_SHORT).show();
            } else {
                String[] parts = response.split("-");
                String status = parts[0];
                String username = parts[1];
                int idquyen = Integer.parseInt(parts[2]); // Lấy idquyen từ server

                SharedPreferences.Editor editorRemember = sharedPreferencesRemember.edit();
                SharedPreferences.Editor editorUser = sharedPreferencesUser.edit();

                if (status.equals("inactive")) {
                    editorUser.putString("username", username_login.getText().toString());
                    editorUser.apply();
                    Intent intent = new Intent(getContext(), VerifyActivity.class);
                    startActivity(intent);

                } else if (status.equals("active")) {
                    // Đăng nhập thành công
                    editorUser.putString("username", username_login.getText().toString());
                    editorUser.apply();

                    // Kiểm tra idquyen và chuyển hướng phù hợp
                    if (idquyen == 1) {
                        Intent intent = new Intent(context, TrangQuanLy.class);
                        startActivity(intent);
                    } else {
                        if (ckbRemember.isChecked()) {
                            editorRemember.putString("username", username_login.getText().toString());
                            editorRemember.putString("password", password_login.getText().toString());
                        }
                        editorRemember.putBoolean("isRemember", ckbRemember.isChecked());
                        editorRemember.apply();
                        Toast.makeText(context, "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MainActivity.class);
                        if (checkPayOrder)
                            intent.putExtra("checkBuyNow", true);
                        startActivity(intent);
                    }
                } else if (status.equals("locked")) {
                    showLockedAccountDialog();
                } else {
                    Toast.makeText(context, "Lỗi không xác định. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> Toast.makeText(context, "Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng của bạn.", Toast.LENGTH_SHORT).show()) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username_login.getText().toString());
                params.put("password", Support.EndcodeMD5(password_login.getText().toString()));
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void clearRememberedData() {
        SharedPreferences.Editor editorRemember = sharedPreferencesRemember.edit();
        editorRemember.remove("username");
        editorRemember.remove("password");
        editorRemember.remove("isRemember");
        editorRemember.apply();
    }
}