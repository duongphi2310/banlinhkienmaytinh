package com.example.do_an_android._Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.do_an_android.Adapter.OrderofCustomerAdapter;
import com.example.do_an_android.Model.GridSpacingItemDecoration;
import com.example.do_an_android.Model.OrderModel;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.R;
import com.google.android.material.tabs.TabLayout;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class OrderOfCustomerFragment extends Fragment {
    Context context;
    RecyclerView recycleviewOrderOfCustomer;
    ArrayList<OrderModel> orderModelArrayList;
    OrderofCustomerAdapter orderofCustomerAdapter;
    String username;
    TabLayout tabLayout;
    SharedPreferences sharedPreferencesUser;
    TextView titleOrderOfCustomer;

    public OrderOfCustomerFragment(Context context) {
        this.context = context;
    }
    public OrderOfCustomerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_of_customer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl(view);
        setupTabLayout();
        getCustomer();
        setAdapterRecycleview();
        getDataOrderOfCustomer();
    }

    private void setupTabLayout() {
        tabLayout = getView().findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("TẤT CẢ"));
        tabLayout.addTab(tabLayout.newTab().setText("CHỜ XÁC NHẬN"));
        tabLayout.addTab(tabLayout.newTab().setText("ĐANG GIAO"));
        tabLayout.addTab(tabLayout.newTab().setText("ĐÃ GIAO"));
        tabLayout.addTab(tabLayout.newTab().setText("ĐÃ HỦY"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadDataForTab(tab.getPosition());
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
                getDataOrderOfCustomer();
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

    private void getCustomer() {
        sharedPreferencesUser = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        username = sharedPreferencesUser.getString("username", "");
        titleOrderOfCustomer.setText("DANH SÁCH ĐƠN HÀNG CỦA " + username);
    }

    private void getDataOrderOfCustomer() {
        orderModelArrayList.clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlGetListOrderOfCustomer + "?username=" + username, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject object = response.getJSONObject(i);
                    orderModelArrayList.add(new OrderModel(object.getString("code"), object.getString("username"), object.getLong("total"), object.getString("create_date"), object.getInt("status")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            orderofCustomerAdapter.notifyDataSetChanged();
        }, error -> {
        });
        Volley.newRequestQueue(context).add(jsonArrayRequest);
    }

    private void getHoaDonChoXacNhan() {
        orderModelArrayList.clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlGetHoaDonChoXacNhanUsername + "?username=" + username, response -> {
            orderModelArrayList.clear();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject object = response.getJSONObject(i);
                    orderModelArrayList.add(new OrderModel(object.getString("code"), object.getString("username"), object.getLong("total"), object.getString("create_date"), object.getInt("status")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            orderofCustomerAdapter.notifyDataSetChanged();
        }, error -> {
        });
        Volley.newRequestQueue(context).add(jsonArrayRequest);
    }

    private void getHoaDonDangGiao() {
        orderModelArrayList.clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.UrlGetHoaDonDangGiaoUsername + "?username=" + username, response -> {
            orderModelArrayList.clear();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject object = response.getJSONObject(i);
                    orderModelArrayList.add(new OrderModel(object.getString("code"), object.getString("username"), object.getLong("total"), object.getString("create_date"), object.getInt("status")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            orderofCustomerAdapter.notifyDataSetChanged();
        }, error -> {
        });
        Volley.newRequestQueue(context).add(jsonArrayRequest);
    }

    private void getHoaDonDaGiao() {
        orderModelArrayList.clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.UrlGetHoaDonDaGiaoUsername + "?username=" + username, response -> {
            orderModelArrayList.clear();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject object = response.getJSONObject(i);
                    orderModelArrayList.add(new OrderModel(object.getString("code"), object.getString("username"), object.getLong("total"), object.getString("create_date"), object.getInt("status")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            orderofCustomerAdapter.notifyDataSetChanged();
        }, error -> {
        });
        Volley.newRequestQueue(context).add(jsonArrayRequest);
    }

    private void getHoaDonDaHuy() {
        orderModelArrayList.clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.UrlGetHoaDonDaHuyUsername + "?username=" + username, response -> {
            orderModelArrayList.clear();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject object = response.getJSONObject(i);
                    orderModelArrayList.add(new OrderModel(object.getString("code"), object.getString("username"), object.getLong("total"), object.getString("create_date"), object.getInt("status")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            orderofCustomerAdapter.notifyDataSetChanged();
        }, error -> {
        });
        Volley.newRequestQueue(context).add(jsonArrayRequest);
    }

    private void setAdapterRecycleview() {
        orderModelArrayList = new ArrayList<>();
        orderofCustomerAdapter = new OrderofCustomerAdapter(context, R.layout.item_order_of_customer, orderModelArrayList);
        recycleviewOrderOfCustomer.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(8), true));
        recycleviewOrderOfCustomer.setAdapter(orderofCustomerAdapter);
        recycleviewOrderOfCustomer.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
    }

    private void setControl(View view) {
        recycleviewOrderOfCustomer = view.findViewById(R.id.recycleviewOrderOfCustomer);
        titleOrderOfCustomer = view.findViewById(R.id.titleOrderOfCustomer);
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}