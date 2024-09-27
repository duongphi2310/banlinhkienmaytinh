package com.example.do_an_android.Activity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.do_an_android.Adapter.LoaiLinhKienAdapter;
import com.example.do_an_android.Model.CategoryModel;
import com.example.do_an_android.Model.GridSpacingItemDecoration;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.R;
import com.example.do_an_android._Fragment.SuaXoaLoaiLinhKienDialogFragment;
import com.example.do_an_android._Fragment.ThemLoaiLinhKienDialogFragment;
import com.example.do_an_android._Fragment.XoaLoaiLinhKienDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoaiLinhKienActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, XoaLoaiLinhKienDialogFragment.ConfirmDeleteListener, ThemLoaiLinhKienDialogFragment.LoadDataListener {
    RecyclerView AllCategoryRecycler;
    public LoaiLinhKienAdapter categoryAdapter;
    public ArrayList<CategoryModel> allCategoryModelList;
    Button themButton;
    SwipeRefreshLayout swipeRefreshLayout;
    private String maLinhKien;

    @Override
    public void loadData() {
        loadDataAllCategory();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loailinhkien);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        setControl();
        setCategoryRecycler();
        loadDataAllCategory();
        themButton = findViewById(R.id.button_them_loai_linh_kien);
        themButton.setOnClickListener(v -> showThemLoaiLinhKienDialog());
        categoryAdapter.setOnItemClickListener(categoryModel -> {
            maLinhKien = categoryModel.getCode();
            String tenLoaiLinhKien = categoryModel.getName();
            String currentImagePath = categoryModel.getImage();
            Toast.makeText(getApplicationContext(), "Mã linh kiện: " + maLinhKien, Toast.LENGTH_SHORT).show();
            showSuaXoaLoaiLinhKienDialog(maLinhKien, tenLoaiLinhKien, currentImagePath);
        });
    }

    @Override
    public void onRefresh() {
        allCategoryModelList.clear();
        loadDataAllCategory();
    }

    private void loadDataAllCategory() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlTypeProduct, response -> {
            allCategoryModelList.clear();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    allCategoryModelList.add(new CategoryModel(jsonObject.getString("code"), jsonObject.getString("name"), jsonObject.getString("image")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            categoryAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }, error -> {
        });
        queue.add(jsonArrayRequest);
    }

    private void setControl() {
        AllCategoryRecycler = findViewById(R.id.recycler_loai_linh_kien);
    }

    private void setCategoryRecycler() {
        allCategoryModelList = new ArrayList<>();
        AllCategoryRecycler.setLayoutManager(new GridLayoutManager(this, 1));
        AllCategoryRecycler.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(16), true));
        AllCategoryRecycler.setItemAnimator(new DefaultItemAnimator());
        categoryAdapter = new LoaiLinhKienAdapter(this,R.layout.item_loailinhkien,allCategoryModelList);
        AllCategoryRecycler.setAdapter(categoryAdapter);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_them_loai_linh_kien) {
            showThemLoaiLinhKienDialog();
        }
    }
    private void showThemLoaiLinhKienDialog() {
        Context context = LoaiLinhKienActivity.this;
        ThemLoaiLinhKienDialogFragment dialogFragment = new ThemLoaiLinhKienDialogFragment(context);
        dialogFragment.setLoadDataListener(this);
        dialogFragment.show(getSupportFragmentManager(), "them_loai_linh_kien_dialog");
    }

    private void showSuaXoaLoaiLinhKienDialog(String maLinhKien, String tenLoaiLinhKien, String currentImagePath) {
        Context context = LoaiLinhKienActivity.this;
        SuaXoaLoaiLinhKienDialogFragment dialogFragment = new SuaXoaLoaiLinhKienDialogFragment(maLinhKien, tenLoaiLinhKien, currentImagePath);
        dialogFragment.show(getSupportFragmentManager(), "sua_xoa_loai_linh_kien_dialog");
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onConfirmDelete() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlXoaLoaiLinhKien, response -> {
            if (response.equals("1")) {
                Toast.makeText(getApplicationContext(), "XÓA LOẠI LINH KIỆN THÀNH CÔNG", Toast.LENGTH_SHORT).show();
                loadDataAllCategory();
            } else {
                Toast.makeText(getApplicationContext(), "XÓA LOẠI LINH KIỆN THẤT BẠI", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(getApplicationContext(), "XÓA LOẠI LINH KIỆN THẤT BẠI", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("maLinhKien", maLinhKien);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
