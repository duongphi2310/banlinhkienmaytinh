package com.example.do_an_android._Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.do_an_android.Activity.MainActivity;
import com.example.do_an_android.Activity.ThanhToanActivity;
import com.example.do_an_android.Adapter.GioHangAdapter;
import com.example.do_an_android.Model.CartModel;
import com.example.do_an_android.Model.GridSpacingItemDecoration;
import com.example.do_an_android.Model.ProductModel;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.Model.Support;
import com.example.do_an_android.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class GioHangFragment extends Fragment implements View.OnClickListener {
    Context context;
    static ArrayList<CartModel> listCart = null;
    RecyclerView recyclerView;
    static TextView txttotalquantity_cart, txttotalpay_cart;
    Button btnThanhToan;
    TextView unitMoneyCartTotal;

    static SharedPreferences sharedPreferencesUser, sharedPreferencesCart;

    public GioHangFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.giohang, container, false);
    }

    public static void updateTotalQuantityAndPrice(int totalQuantity, long totalPrice) {
        txttotalquantity_cart.setText(String.valueOf(totalQuantity));
        txttotalpay_cart.setText(Support.ConvertMoney(totalPrice));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferencesUser = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        sharedPreferencesCart = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        String cart = sharedPreferencesCart.getString("item_cart", "");
        String username = sharedPreferencesUser.getString("username", "");
        fetchDataFromServer(username);

        CartModel[] cartModels = new Gson().fromJson(cart, CartModel[].class);
        if (cartModels != null)
            listCart = new ArrayList<CartModel>(Arrays.asList(cartModels));
        else
            listCart = new ArrayList<>();
        setControl(view);
        setData();
        btnThanhToan.setOnClickListener(this);
    }

    private void fetchDataFromServer(String username) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlGioHang, response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                listCart.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String maLinhKien = jsonObject.getString("maLinhKien");
                    int soLuong = jsonObject.getInt("soLuong");
                    String tenLinhKien = jsonObject.getString("tenLinhKien");
                    String hinhAnh = jsonObject.getString("hinhAnh");
                    long gia = jsonObject.getLong("gia");

                    CartModel cartModel = new CartModel(username, new ProductModel(maLinhKien, tenLinhKien, gia, 0, 0, "", hinhAnh, ""), soLuong, 0, hinhAnh);
                    listCart.add(cartModel);
                }
                setData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void setData() {
        int totalQuantity = 0;
        long totalPrice = 0;

        for (CartModel cart : listCart) {
            if (cart.isChecked()) {
                totalQuantity += cart.getQuantity();
                long price = cart.getProductModel().getPrice_discounted() > 0 ?
                            cart.getProductModel().getPrice_discounted() : cart.getProductModel().getPrice();
                totalPrice += cart.getQuantity() * price;
            }
        }

        GioHangFragment.updateTotalQuantityAndPrice(totalQuantity, totalPrice);
        GioHangAdapter gioHangAdapter = new GioHangAdapter(getContext(), R.layout.item_cart, listCart);
        recyclerView.setAdapter(gioHangAdapter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1,dpToPx(2),false));
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        gioHangAdapter.notifyDataSetChanged();
    }


    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    public static void updateCart(ArrayList<CartModel> cartList) {
        listCart = cartList;
        int sum = 0;
        int total = 0;
        for (CartModel item : listCart) {
            sum += item.getQuantity();
            if (item.getProductModel().getPrice_discounted() > 0)
                total += item.getQuantity() * item.getProductModel().getPrice_discounted();
            else
                total += item.getQuantity() * item.getProductModel().getPrice();
        }
        txttotalquantity_cart.setText(sum + "");
        txttotalpay_cart.setText(Support.ConvertMoney(total));
        SharedPreferences.Editor editorCart = sharedPreferencesCart.edit();
        editorCart.putString("item_cart", new Gson().toJson(listCart));
        editorCart.commit();
    }

    private void setControl(View view) {
        recyclerView = view.findViewById(R.id.recycleviewcart);
        txttotalquantity_cart = view.findViewById(R.id.txttotalquantity_cart);
        txttotalpay_cart = view.findViewById(R.id.txttotalpay_cart);
        btnThanhToan = view.findViewById(R.id.btnThanhToan);
        unitMoneyCartTotal = view.findViewById(R.id.unitMoneyCartTotal);
        unitMoneyCartTotal.setPaintFlags(unitMoneyCartTotal.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnThanhToan:
                if (listCart.size() == 0) {
                    Toast.makeText(context, "Giỏ hàng chưa có gì.", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<CartModel> selectedItems = new ArrayList<>();
                    for (CartModel cart : listCart) {
                        if (cart.isChecked()) {
                            selectedItems.add(cart);
                        }
                    }
                    if (selectedItems.size() == 0) {
                        Toast.makeText(context, "Chọn ít nhất một mặt hàng để thanh toán.", Toast.LENGTH_SHORT).show();
                    } else {
                        String username = sharedPreferencesUser.getString("username", "fail");
                        if (username.equals("fail")) {
                            Toast.makeText(context, "Chưa đăng nhập.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("checkPayCart", true);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, ThanhToanActivity.class);
                            intent.putExtra("selectedItems", selectedItems);
                            startActivity(intent);
                        }
                    }
                }
                break;
        }
    }

}