package com.example.do_an_android.Activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.do_an_android.Adapter.LinhKienAdapter;
import com.example.do_an_android.Adapter.LoaiLinhKienAdapter;
import com.example.do_an_android.Adapter.NguoiDungAdapter;
import com.example.do_an_android.Model.CategoryModel;
import com.example.do_an_android.Model.CustomerModel;
import com.example.do_an_android.Model.GridSpacingItemDecoration;
import com.example.do_an_android.Model.ProductModel;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NguoiDungActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, NguoiDungAdapter.LoadDataListener {
    RecyclerView AllUserRecycler;
    public NguoiDungAdapter customerAdapter;
    public ArrayList<CustomerModel> allCustomerModelList;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void loadData() {
        allCustomerModelList.clear();
        loadDataAllCustomer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nguoidung);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        setControl();
        setCustomerRecycler();
        loadDataAllCustomer();
    }

    @Override
    public void onRefresh() {
        allCustomerModelList.clear();
        loadDataAllCustomer();
    }

    private void loadDataAllCustomer() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlNguoiDung, response -> {
            allCustomerModelList.clear();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    allCustomerModelList.add(new CustomerModel(jsonObject.getString("username"),
                            jsonObject.getString("name"), jsonObject.getString("password"),
                            jsonObject.getString("diachi"), jsonObject.getString("sodienthoai"),
                            jsonObject.getString("hinhanh"), jsonObject.getInt("idquyen"), jsonObject.getInt("trangthai")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            customerAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }, error -> {
        });
        queue.add(jsonArrayRequest);
    }

    private void setControl() {
        AllUserRecycler = findViewById(R.id.recycler_nguoi_dung);
    }

    private void setCustomerRecycler() {
        allCustomerModelList = new ArrayList<>();
        AllUserRecycler.setLayoutManager(new GridLayoutManager(this, 1));
        AllUserRecycler.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(16), true));
        AllUserRecycler.setItemAnimator(new DefaultItemAnimator());
        customerAdapter = new NguoiDungAdapter(this, R.layout.item_nguoidung, allCustomerModelList);
        customerAdapter.setLoadDataListener(this);
        AllUserRecycler.setAdapter(customerAdapter);
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
