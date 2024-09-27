package com.example.do_an_android._Fragment;

import static com.example.do_an_android.Model.Server.urlImage;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.do_an_android.Activity.LoaiLinhKienActivity;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.R;
import com.example.do_an_android.Retrofit2.APIUtils;
import com.example.do_an_android.Retrofit2.DataClient;
import com.squareup.picasso.Picasso;

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

public class SuaLoaiLinhKienDialogFragment extends DialogFragment {
    ImageView imageSuaLoaiLinhKien; // DONE
    EditText edtSuaLoaiLinhKien, edtMaLinhKien;
    Button btnDongY, btnHuyBo;
    Context context;
    String realPath = "";
    String image = ""; // DONE
    private static final int REQUEST_CODE_IMAGE_SUALOAILINHKIEN = 777; // DONE
    String maLinhKien, tenLoaiLinhKien;
    String currentImagePath;

    public SuaLoaiLinhKienDialogFragment(Context context, String maLinhKien, String tenLoaiLinhKien, String currentImagePath) {
        this.context = context;
        this.maLinhKien = maLinhKien;
        this.tenLoaiLinhKien = tenLoaiLinhKien;
        this.currentImagePath = currentImagePath;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_sualoailinhkien, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl(view);
        edtSuaLoaiLinhKien.requestFocus();
        edtMaLinhKien.setText(maLinhKien);
        edtSuaLoaiLinhKien.setText(tenLoaiLinhKien);
        if (!TextUtils.isEmpty(currentImagePath)) {
            Picasso.get().load(urlImage + currentImagePath).into(imageSuaLoaiLinhKien);
        }
        btnDongY.setOnClickListener(v -> {
            if (!(validateEditText(edtSuaLoaiLinhKien, "Tên loại linh kiện không được rỗng."))) {
                Sua();
            }
        });
        btnHuyBo.setOnClickListener(v -> dismiss());
        imageSuaLoaiLinhKien.setOnClickListener(v -> {
            if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 701);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_IMAGE_SUALOAILINHKIEN);
            }
        });
    }

    private boolean validateEditText(EditText txt, String description) {
        if (txt.getText().toString().trim().length() == 0) {
            Toast.makeText(context, description, Toast.LENGTH_SHORT).show();
            txt.requestFocus();
            return true;
        }
        return false;
    }

    private void setControl(View view) {
        edtSuaLoaiLinhKien = view.findViewById(R.id.edtSuaLoaiLinhKien);
        btnHuyBo = view.findViewById(R.id.btnHuyBo);
        btnDongY = view.findViewById(R.id.btnDongY);
        edtMaLinhKien = view.findViewById(R.id.edtMaLinhKien);
        edtSuaLoaiLinhKien = view.findViewById((R.id.edtSuaLoaiLinhKien));
        imageSuaLoaiLinhKien = view.findViewById(R.id.imageSuaLoaiLinhKien);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 701 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_IMAGE_SUALOAILINHKIEN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_IMAGE_SUALOAILINHKIEN && resultCode == ((Activity) context).RESULT_OK && data != null) {
            Uri uri = data.getData();
            realPath = getRealPathFromURI(uri);
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageSuaLoaiLinhKien.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void _Clear() {
        edtSuaLoaiLinhKien.setText("");
        edtSuaLoaiLinhKien.requestFocus();
        imageSuaLoaiLinhKien.setImageResource(R.drawable.no_image);
    }

    private void Sua() {
        if (!TextUtils.isEmpty(realPath)) {
            File file = new File(realPath);
            String file_path = file.getAbsolutePath();
            try {
                String[] arrayNameFile = file_path.split("\\.");
                file_path = arrayNameFile[0] + System.currentTimeMillis() + "." + arrayNameFile[1];
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", file_path, requestBody);
            DataClient dataClient = APIUtils.getData();
            retrofit2.Call<String> callback = dataClient.UploadImage(body);
            callback.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    if (response != null) {
                        image = response.body();
                        suaLoaiLinhKien();
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    suaLoaiLinhKien();
                }
            });
        } else {
            image = currentImagePath;
            suaLoaiLinhKien();
        }
    }


    private void suaLoaiLinhKien() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlSuaLoaiLinhKien, response -> {
            if (response.equals("1")) {
                Toast.makeText(context, "SỬA THÀNH CÔNG.", Toast.LENGTH_SHORT).show();
                _Clear();
                dismiss();
                if (context instanceof LoaiLinhKienActivity) {
                    ((LoaiLinhKienActivity) context).loadData();
                }
            }
        }, error -> {
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("maLinhKien", maLinhKien);
                params.put("tenLinhKien", edtSuaLoaiLinhKien.getText().toString());;
                params.put("image", image);
                image = "";
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Sửa loại linh kiện");
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        getDialog().getWindow().setLayout((int) (width * 0.9), ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
