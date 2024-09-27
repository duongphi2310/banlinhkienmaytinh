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
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.do_an_android.Activity.LinhKienActivity;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.R;
import com.example.do_an_android.Retrofit2.APIUtils;
import com.example.do_an_android.Retrofit2.DataClient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class SuaLinhKienDialogFragment extends DialogFragment {
    ImageView imageSuaLinhKien;
    EditText edtMaLinhKien, edtTenLinhKien, edtGiaTien, edtGiaTienKhuyenMai, edtSoLuong, edtMoTa;
    Button btnDongY, btnHuyBo;
    Context context;
    String realPath = "";
    String image = "";
    private static final int REQUEST_CODE_IMAGE_SUALINHKIEN = 1122;
    String maLinhKien, tenLinhKien, giaTien, soLuong, giaTienKhuyenMai;
    String moTa, maLoaiLinhKien, currentImagePath;
    Spinner dropdownLoaiLinhKien;

    private void loadDataFromAPI() {
        String url = Server.urlTypeProduct;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            ArrayList<String> categoryNames = new ArrayList<>();
            String selectedCategoryName = "";
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);
                    String name = jsonObject.getString("name");
                    String code = jsonObject.getString("code");
                    categoryNames.add(name);
                    if (code.equals(maLoaiLinhKien)) {
                        selectedCategoryName = name;
                    }
                }
                if (!TextUtils.isEmpty(selectedCategoryName)) {
                    categoryNames.remove(selectedCategoryName);
                    categoryNames.add(0, selectedCategoryName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dropdownLoaiLinhKien.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> error.printStackTrace());
        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);
    }

    public SuaLinhKienDialogFragment(Context context, String maLinhKien, String tenLinhKien, Long giaTien, Integer soLuong,
                                     Long giaTienKhuyenMai, String moTa, String maLoaiLinhKien, String currentImagePath) {
        this.context = context;
        this.maLinhKien = maLinhKien;
        this.tenLinhKien = tenLinhKien;
        this.giaTien = String.valueOf(giaTien);
        this.soLuong = String.valueOf(soLuong);
        this.giaTienKhuyenMai = String.valueOf(giaTienKhuyenMai);
        this.moTa = moTa;
        this.maLoaiLinhKien = maLoaiLinhKien;
        this.currentImagePath = currentImagePath;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_sualinhkien, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl(view);
        loadDataFromAPI();
        edtTenLinhKien.requestFocus();
        edtMaLinhKien.setText(maLinhKien);
        edtTenLinhKien.setText(tenLinhKien);
        edtGiaTien.setText(giaTien);
        edtSoLuong.setText(soLuong);
        edtGiaTienKhuyenMai.setText(giaTienKhuyenMai);
        edtMoTa.setText(moTa);
        if (!TextUtils.isEmpty(currentImagePath)) {
            Picasso.get().load(urlImage + currentImagePath).into(imageSuaLinhKien);
        }
        btnDongY.setOnClickListener(v -> {
            if (!(validateEditText(edtTenLinhKien, "Tên loại linh kiện không được rỗng."))) {
                Sua();
            }
        });
        btnHuyBo.setOnClickListener(v -> dismiss());
        imageSuaLinhKien.setOnClickListener(v -> {
            if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 76);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_IMAGE_SUALINHKIEN);
            }
        });

        edtGiaTien.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().matches("\\d*")) {
                    edtGiaTien.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edtGiaTienKhuyenMai.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().matches("\\d*")) {
                    edtGiaTienKhuyenMai.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edtSoLuong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().matches("\\d*")) {
                    edtSoLuong.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
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
        dropdownLoaiLinhKien = view.findViewById(R.id.dropdown_loailinhkien);
        edtTenLinhKien = view.findViewById(R.id.edtSuaLinhKien);
        btnHuyBo = view.findViewById(R.id.btnHuyBo);
        btnDongY = view.findViewById(R.id.btnDongY);
        edtGiaTien = view.findViewById(R.id.edtGiaTien);
        edtGiaTienKhuyenMai = view.findViewById(R.id.edtGiaTienKhuyenMai);
        edtSoLuong = view.findViewById(R.id.edtSoLuong);
        edtMoTa = view.findViewById(R.id.edtMoTa);
        imageSuaLinhKien = view.findViewById(R.id.imageSuaLinhKien);
        edtMaLinhKien = view.findViewById(R.id.edtMaLinhKien);
        edtGiaTien.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtGiaTienKhuyenMai.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtSoLuong.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 76 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_IMAGE_SUALINHKIEN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_IMAGE_SUALINHKIEN && resultCode == ((Activity) context).RESULT_OK && data != null) {
            Uri uri = data.getData();
            realPath = getRealPathFromURI(uri);
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageSuaLinhKien.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void _Clear() {
        edtTenLinhKien.setText("");
        edtTenLinhKien.requestFocus();
        edtGiaTien.setText("");
        edtGiaTienKhuyenMai.setText("");
        edtSoLuong.setText("");
        edtMoTa.setText("");
        imageSuaLinhKien.setImageResource(R.drawable.no_image);
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
                        suaLinhKien();
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    suaLinhKien();
                }
            });
        } else {
            image = currentImagePath;
            suaLinhKien();
        }
    }

    private String findMaLoaiLinhKienFromJSON(JSONArray jsonArray, String tenLoaiLinhKien) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String code = jsonObject.getString("code");
                if (tenLoaiLinhKien.equals(name)) {
                    return code;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void suaLinhKien() {
        String tenLoaiLinhKien = dropdownLoaiLinhKien.getSelectedItem().toString();
        String url = Server.urlTypeProduct;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            String maLoaiLinhKien = findMaLoaiLinhKienFromJSON(response, tenLoaiLinhKien);
            if (!maLoaiLinhKien.isEmpty()) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlSuaLinhKien, response1 -> {
                    if (response1.equals("1")) {
                        Toast.makeText(context, "SỬA THÀNH CÔNG.", Toast.LENGTH_SHORT).show();
                        _Clear();
                        dismiss();
                        if (context instanceof LinhKienActivity) {
                            ((LinhKienActivity) context).loadData();
                        }
                    } else {
                        Toast.makeText(context, "SỬA THẤT BẠI.", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
                }) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("maLinhKien", edtMaLinhKien.getText().toString());
                        params.put("tenLinhKien", edtTenLinhKien.getText().toString());
                        params.put("giaTien", edtGiaTien.getText().toString());
                        params.put("giaTienKhuyenMai", edtGiaTienKhuyenMai.getText().toString());
                        params.put("soLuong", edtSoLuong.getText().toString());
                        params.put("moTa", edtMoTa.getText().toString());
                        params.put("image", image);
                        params.put("maLoaiLinhKien", maLoaiLinhKien);
                        image = "";
                        return params;
                    }
                };
                Volley.newRequestQueue(context).add(stringRequest);
            } else {
            }
        }, error -> {
        });
        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);
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
        dialog.setTitle("Sửa linh kiện");
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
