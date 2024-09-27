package com.example.do_an_android._Fragment;

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
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.R;
import com.example.do_an_android.Retrofit2.APIUtils;
import com.example.do_an_android.Retrofit2.DataClient;

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

public class ThemLoaiLinhKienDialogFragment extends DialogFragment {
    ImageView imageThemLoaiLinhKien;
    EditText edtThemLoaiLinhKien;
    Button btnDongY, btnHuyBo;
    Context context;
    String realPath = "";
    String image = "";
    private static final int REQUEST_CODE_IMAGE_THEMLOAILINHKIEN = 888;
    private LoadDataListener loadDataListener;
    public interface LoadDataListener {
        void loadData();
    }

    public void setLoadDataListener(LoadDataListener listener) {
        this.loadDataListener = listener;
    }

    public ThemLoaiLinhKienDialogFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_themloailinhkien, container, false);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl(view);
        edtThemLoaiLinhKien.requestFocus();
        btnDongY.setOnClickListener(v -> {
            if (!(validateEditText(edtThemLoaiLinhKien, "Tên loại linh kiện không được rỗng."))) {
                Them();
            }
        });

        btnHuyBo.setOnClickListener(v -> dismiss());

        imageThemLoaiLinhKien.setOnClickListener(v -> {
            if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 222);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_IMAGE_THEMLOAILINHKIEN);
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
        edtThemLoaiLinhKien = view.findViewById(R.id.edtThemLoaiLinhKien);
        btnHuyBo = view.findViewById(R.id.btnHuyBo);
        btnDongY = view.findViewById(R.id.btnDongY);
        imageThemLoaiLinhKien = view.findViewById(R.id.imageThemLoaiLinhKien);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 222 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_IMAGE_THEMLOAILINHKIEN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_IMAGE_THEMLOAILINHKIEN && resultCode == ((Activity) context).RESULT_OK && data != null) {
            Uri uri = data.getData();
            realPath = getRealPathFromURI(uri);
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageThemLoaiLinhKien.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void _Clear() {
        edtThemLoaiLinhKien.setText("");
        edtThemLoaiLinhKien.requestFocus();
        imageThemLoaiLinhKien.setImageResource(R.drawable.no_image);
    }

    private void Them() {
        File file = new File(realPath);
        String file_path = file.getAbsolutePath();
        try {
            String[] arrayNameFile = file_path.split("\\.");
            file_path = arrayNameFile[0] + System.currentTimeMillis() + "." + arrayNameFile[1];
        } catch (Exception ex) {
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", file_path, requestBody);
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<String> callback = dataClient.UploadImage(body);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if(response!=null) {
                    image = response.body();
                    themLoaiLinhKien();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                themLoaiLinhKien();
            }
        });
    }

    private void themLoaiLinhKien() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlThemLoaiLinhKien, response -> {
            if (response.equals("1")) {
                Toast.makeText(context, "THÊM THÀNH CÔNG.", Toast.LENGTH_SHORT).show();
                _Clear();
                dismiss();
                loadDataListener.loadData();
            }
        }, error -> {
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("tenLinhKien", edtThemLoaiLinhKien.getText().toString());;
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
        dialog.setTitle("Thêm loại linh kiện");
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        getDialog().getWindow().setLayout((int) (width * 0.9), ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}