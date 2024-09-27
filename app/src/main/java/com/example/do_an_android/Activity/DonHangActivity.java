package com.example.do_an_android.Activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.tabs.TabLayout;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.do_an_android.Adapter.DonHangAdapter;
import com.example.do_an_android.Model.GridSpacingItemDecoration;
import com.example.do_an_android.Model.OrderModel;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class DonHangActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView AllOrderRecycler;
    public DonHangAdapter donHangAdapter;
    public ArrayList<OrderModel> allOrderModelList;
    SwipeRefreshLayout swipeRefreshLayout;
    private String mahoadon;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donhang);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        setupTabLayout();
        setControl();
        setOrderRecycler();
        loadDataAllOrder();

        donHangAdapter.setOnItemClickListener(orderModel -> {
            mahoadon = orderModel.getCode();
            Toast.makeText(getApplicationContext(), "Mã hoá đơn: " + mahoadon, Toast.LENGTH_SHORT).show();
        });
    }

    private void setupTabLayout() {
        tabLayout = findViewById(R.id.tabLayout1);
        tabLayout.addTab(tabLayout.newTab().setText("TẤT CẢ"));
        tabLayout.addTab(tabLayout.newTab().setText("CHỜ XÁC NHẬN"));
        tabLayout.addTab(tabLayout.newTab().setText("ĐANG GIAO"));
        tabLayout.addTab(tabLayout.newTab().setText("ĐÃ GIAO"));
        tabLayout.addTab(tabLayout.newTab().setText("ĐÃ HỦY"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                loadDataForTab(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void loadDataForTab(int position) {
        switch (position) {
            case 0:
                loadDataAllOrder();
                break;
            case 1:
                getHoaDonChoXacNhan();
                break;
            case 2:
                getHoaDonDangGiao();
                break;
            case 3:
                getHoaDonDaGiao();
                break;
            case 4:
                getHoaDonDaHuy();
                break;
        }
    }

    @Override
    public void onRefresh() {
        int currentTabPosition = tabLayout.getSelectedTabPosition();
        loadDataForTab(currentTabPosition);
    }

    private void loadDataAllOrder() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlDonHang, response -> {
            allOrderModelList.clear();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    allOrderModelList.add(new OrderModel(jsonObject.getString("code"), jsonObject.getString("username"), jsonObject.getLong("total"), jsonObject.getString("create_date"), jsonObject.getInt("status")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            donHangAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }, error -> {
        });
        queue.add(jsonArrayRequest);
    }

    private void getHoaDonDangGiao() {
        allOrderModelList.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlGetHoaDonDangGiao, response -> {
            allOrderModelList.clear();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    allOrderModelList.add(new OrderModel(jsonObject.getString("code"), jsonObject.getString("username"), jsonObject.getLong("total"), jsonObject.getString("create_date"), jsonObject.getInt("status")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            donHangAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }, error -> {
        });
        queue.add(jsonArrayRequest);
    }

    private void getHoaDonDaGiao() {
        allOrderModelList.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlGetHoaDonDaGiao, response -> {
            allOrderModelList.clear();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    allOrderModelList.add(new OrderModel(jsonObject.getString("code"), jsonObject.getString("username"), jsonObject.getLong("total"), jsonObject.getString("create_date"), jsonObject.getInt("status")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            donHangAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }, error -> {
        });
        queue.add(jsonArrayRequest);
    }

    private void getHoaDonChoXacNhan() {
        allOrderModelList.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlGetHoaDonChoXacNhan, response -> {
            allOrderModelList.clear();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    allOrderModelList.add(new OrderModel(jsonObject.getString("code"), jsonObject.getString("username"), jsonObject.getLong("total"), jsonObject.getString("create_date"), jsonObject.getInt("status")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            donHangAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }, error -> {
        });
        queue.add(jsonArrayRequest);
    }

    private void getHoaDonDaHuy() {
        allOrderModelList.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlGetHoaDonDaHuy, response -> {
            allOrderModelList.clear();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    allOrderModelList.add(new OrderModel(jsonObject.getString("code"), jsonObject.getString("username"), jsonObject.getLong("total"), jsonObject.getString("create_date"), jsonObject.getInt("status")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            donHangAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }, error -> {
        });
        queue.add(jsonArrayRequest);
    }

    private void setControl() {
        AllOrderRecycler = findViewById(R.id.recycler_donhang);
    }

    private void setOrderRecycler() {
        allOrderModelList = new ArrayList<>();
        AllOrderRecycler.setLayoutManager(new GridLayoutManager(this, 1));
        AllOrderRecycler.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(16), true));
        AllOrderRecycler.setItemAnimator(new DefaultItemAnimator());
        donHangAdapter = new DonHangAdapter(this, R.layout.item_donhang, allOrderModelList);
        AllOrderRecycler.setAdapter(donHangAdapter);
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}