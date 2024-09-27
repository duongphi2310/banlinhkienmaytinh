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
import com.example.do_an_android.Adapter.LinhKienAdapter;
import com.example.do_an_android.Model.CategoryModel;
import com.example.do_an_android.Model.GridSpacingItemDecoration;
import com.example.do_an_android.Model.ProductModel;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.R;
import com.example.do_an_android._Fragment.SuaXoaLinhKienDialogFragment;
import com.example.do_an_android._Fragment.ThemLinhKienDialogFragment;
import com.example.do_an_android._Fragment.XoaLinhKienDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LinhKienActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, XoaLinhKienDialogFragment.ConfirmDeleteListener, ThemLinhKienDialogFragment.LoadDataListener {
    RecyclerView AllProductRecycler;
    public LinhKienAdapter productAdapter;
    public ArrayList<ProductModel> allProductModelList;
    Button btnThem;
    SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<CategoryModel> categoryList = new ArrayList<>();
    private String maLinhKien;
    private Long giaTien;

    @Override
    public void loadData() {
        loadDataAllProduct();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linhkien);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        setControl();
        setProductRecycler();
        loadDataAllProduct();
        loadTypeProducts();
        btnThem = findViewById(R.id.button_them_linh_kien);
        btnThem.setOnClickListener(v -> showThemLinhKienDialog());

        productAdapter.setOnItemClickListener(new LinhKienAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ProductModel productModel) {
            }
            @Override
            public void onItemLongClick(ProductModel productModel) {
                maLinhKien = productModel.getCode();
                String tenLinhKien = productModel.getName();
                String currentImagePath = productModel.getImage();
                String maLoaiLinhKien = productModel.getType_code();
                giaTien = productModel.getPrice();
                Integer soLuong = productModel.getQuantity();
                Long giaTienKhuyenMai = productModel.getPrice_discounted();
                String moTa = productModel.getDescription();
                Toast.makeText(getApplicationContext(), "Mã linh kiện: " + maLinhKien + "Giá tiền: " + giaTien + "Mô tả: " + moTa, Toast.LENGTH_SHORT).show();
                showSuaXoaLinhKienDialog(maLinhKien, tenLinhKien, giaTien, soLuong, giaTienKhuyenMai, moTa, maLoaiLinhKien, currentImagePath);
            }
        });
    }

    private void loadTypeProducts() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlTypeProduct, response -> {
            categoryList.clear();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    categoryList.add(new CategoryModel(jsonObject.getString("code"), jsonObject.getString("name"), jsonObject.getString("image")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            productAdapter.setCategoryList(categoryList);
        }, error -> {
        });
        queue.add(jsonArrayRequest);
    }

    @Override
    public void onRefresh() {
        allProductModelList.clear();
        loadDataAllProduct();
    }

    private void loadDataAllProduct() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlLinhKien, response -> {
            allProductModelList.clear();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    allProductModelList.add(new ProductModel(jsonObject.getString("code"), jsonObject.getString("name"), jsonObject.getLong("price"), jsonObject.getInt("quantity"),
                            jsonObject.getLong("price_discounted"), jsonObject.getString("description"), jsonObject.getString("image"),
                            jsonObject.getString("type_code")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            productAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }, error -> {
        });
        queue.add(jsonArrayRequest);
    }

    private void setControl() {
        AllProductRecycler = findViewById(R.id.recycler_linh_kien);
    }

    private void setProductRecycler() {
        allProductModelList = new ArrayList<>();
        AllProductRecycler.setLayoutManager(new GridLayoutManager(this, 1));
        AllProductRecycler.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(16), true));
        AllProductRecycler.setItemAnimator(new DefaultItemAnimator());
        productAdapter = new LinhKienAdapter(this,R.layout.item_linhkien,allProductModelList);
        AllProductRecycler.setAdapter(productAdapter);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_them_linh_kien) {
            showThemLinhKienDialog();
        }
    }

    private void showThemLinhKienDialog() {
        Context context = LinhKienActivity.this;
        ThemLinhKienDialogFragment dialogFragment = new ThemLinhKienDialogFragment(context);
        dialogFragment.setLoadDataListener(this);
        dialogFragment.show(getSupportFragmentManager(), "them_linh_kien_dialog");
    }

    private void showSuaXoaLinhKienDialog(String maLinhKien, String tenLinhKien, Long giaTien, Integer soLuong, Long giaTienKhuyenMai, String moTa, String maLoaiLinhKien, String currentImagePath) {
        Context context = LinhKienActivity.this;
        SuaXoaLinhKienDialogFragment dialogFragment = new SuaXoaLinhKienDialogFragment(maLinhKien, tenLinhKien, giaTien, soLuong, giaTienKhuyenMai, moTa, maLoaiLinhKien, currentImagePath);
        dialogFragment.show(getSupportFragmentManager(), "sua_xoa_linh_kien_dialog");
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onConfirmDelete() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlXoaLinhKien, response -> {
            if (response.equals("1")) {
                Toast.makeText(getApplicationContext(), "XÓA LINH KIỆN THÀNH CÔNG", Toast.LENGTH_SHORT).show();
                loadDataAllProduct();
            } else {
                Toast.makeText(getApplicationContext(), "XÓA LINH KIỆN THẤT BẠI", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(getApplicationContext(), "XÓA LINH KIỆN THẤT BẠI", Toast.LENGTH_SHORT).show()) {
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
