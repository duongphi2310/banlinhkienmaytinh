package com.example.do_an_android.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.do_an_android.Model.CartModel;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.Model.Support;
import com.example.do_an_android.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ThanhToanActivity extends AppCompatActivity {
    EditText name_order, street, sodienthoai;
    Spinner province, district, ward;
    ArrayAdapter<CharSequence> provinceAdapter, districtAdapter, wardAdapter;
    TextView sumProductConfirm, totalConfirm,unitMoneyPayOrderTotal;
    Button btnConfirmOrder;
    SharedPreferences sharedPreferencesUser;
    ArrayList<CartModel> lstCart;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order);
        setControl();
        getDataCustomer();
        ArrayList<CartModel> selectedItems = (ArrayList<CartModel>) getIntent().getSerializableExtra("selectedItems");
        lstCart = selectedItems != null ? selectedItems : new ArrayList<>();

        setDataCart();
        setClick();
        setupSpinners();
    }

    private void setupSpinners() {
        provinceAdapter = ArrayAdapter.createFromResource(this, R.array.provinces_array, android.R.layout.simple_spinner_item);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province.setAdapter(provinceAdapter);

        province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateDistrictSpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateWardSpinner(province.getSelectedItemPosition(), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void updateDistrictSpinner(int provincePosition) {
        int districtArrayId;

        switch (provincePosition) {
            case 0:
                districtArrayId = R.array.districts_tinh_1_array;
                break;
            case 1:
                districtArrayId = R.array.districts_tinh_2_array;
                break;
            // Thêm các trường hợp khác nếu có nhiều tỉnh
            default:
                districtArrayId = R.array.districts_tinh_1_array;
                break;
        }

        districtAdapter = ArrayAdapter.createFromResource(this, districtArrayId, android.R.layout.simple_spinner_item);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        district.setAdapter(districtAdapter);
        ward.setAdapter(null);
    }

    private void updateWardSpinner(int provincePosition, int districtPosition) {
        int wardArrayId = 0;

        if (provincePosition == 1) { // Assuming province 2 has wards
            switch (districtPosition) {
                case 0:
                    wardArrayId = R.array.wards_huyen_3_array;
                    break;
                case 1:
                    wardArrayId = R.array.wards_huyen_4_array;
                    break;
                default:
                    wardArrayId = 0; // No wards available
            }
        }

        if (wardArrayId != 0) {
            wardAdapter = ArrayAdapter.createFromResource(this, wardArrayId, android.R.layout.simple_spinner_item);
            wardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ward.setAdapter(wardAdapter);
        } else {
            ward.setAdapter(null); // No wards available
        }
    }

    private void setDataCart() {
        int sum = 0;
        long total = 0;
        for (CartModel cartModel : lstCart) {
            sum += cartModel.getQuantity();
            if (cartModel.getProductModel().getPrice_discounted() > 0)
                total += cartModel.getQuantity() * cartModel.getProductModel().getPrice_discounted();
            else
                total += cartModel.getQuantity() * cartModel.getProductModel().getPrice();
        }
        sumProductConfirm.setText(sum + "");
        totalConfirm.setText(Support.ConvertMoney(total));
    }

    private void getDataCustomer() {
        sharedPreferencesUser = getSharedPreferences("user", Context.MODE_PRIVATE);
        username = sharedPreferencesUser.getString("username", "");
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Server.urlGetCustomerByUsername + "?username=" + username, null, response -> {

        }, error -> {
        });
        queue.add(jsonObjectRequest);
    }

    private boolean validateInput() {
        if (name_order.getText().toString().trim().isEmpty()) {
            showAlert("Vui lòng nhập tên người nhận hàng.");
            return false;
        }
        if (sodienthoai.getText().toString().trim().isEmpty()) {
            showAlert("Vui lòng nhập số điện thoại.");
            return false;
        }

        if (street.getText().toString().trim().isEmpty()) {
            showAlert("Vui lòng nhập tên đường.");
            return false;
        }
        return true;
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }


    private void setClick() {
        btnConfirmOrder.setOnClickListener(view -> {
            if (validateInput()) {
                insertOrder();
            }
        });
    }

    private void updateOrder(String code_order) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlUpdateOrder, response -> {
        }, error -> {
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                long total = 0;
                for (CartModel cartModel : lstCart)
                    if (cartModel.getProductModel().getPrice_discounted() > 0)
                        total += cartModel.getQuantity() * cartModel.getProductModel().getPrice_discounted();
                    else
                        total += cartModel.getQuantity() * cartModel.getProductModel().getPrice();
                params.put("total", total + "");
                params.put("code_order", code_order);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void OrderDetail(String code_order) {
        for (CartModel cartModel : lstCart) {
            insertOrderDetail(code_order, cartModel);
        }
    }

    private void insertOrderDetail(String code_order, CartModel cartModel) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlInsertOrderDetail, response -> {
        }, error -> {
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                long price=0;
                if(cartModel.getProductModel().getPrice_discounted()>0)
                    price=cartModel.getProductModel().getPrice_discounted();
                else
                    price=cartModel.getProductModel().getPrice();
                params.put("code_order", code_order);
                params.put("code_product", cartModel.getProductModel().getCode());
                params.put("price", price + "");
                params.put("quantity", cartModel.getQuantity() + "");
                params.put("total", price * cartModel.getQuantity() + "");
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ĐẶT HÀNG THÀNH CÔNG")
                .setMessage("CẢM ƠN BẠN ĐÃ MUA HÀNG, ĐƠN HÀNG CỦA BẠN ĐÃ ĐƯỢC ĐẶT THÀNH CÔNG!")
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    dialogInterface.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    private void deleteProductsFromCart(ArrayList<CartModel> selectedItems) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlXoaLinhKienSauKhiMuaHang, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                String message = jsonObject.getString("message");
                if (success) {
                } else {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                ArrayList<String> productCodes = new ArrayList<>();
                for (CartModel item : selectedItems) {
                    productCodes.add(item.getProductModel().getCode());
                }
                params.put("productCodes", new Gson().toJson(productCodes));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private String getFullAddress() {
        String streetName = street.getText().toString().trim();
        String wardName = ward.getSelectedItem().toString();
        String districtName = district.getSelectedItem().toString();
        String provinceName = province.getSelectedItem().toString();

        return streetName + ", " + wardName + ", " + districtName + ", " + provinceName;
    }


    private void insertOrder() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlInsertOrder, response -> {
            String code_order = response.toString();
            if (!code_order.equals("0")) {
                OrderDetail(code_order);
                updateOrder(code_order);
                deleteProductsFromCart(lstCart);
                showSuccessDialog();
                getSharedPreferences("cart", Context.MODE_PRIVATE).edit().clear().commit();
            } else {
                Toast.makeText(getApplicationContext(), "Đặt hàng thất bại.", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(getApplicationContext(), "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show()) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("ten", name_order.getText().toString().trim());
                params.put("sodienthoai", sodienthoai.getText().toString().trim());
                params.put("diachi", getFullAddress());
                params.put("selectedItems", new Gson().toJson(lstCart));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void setControl() {
        name_order = findViewById(R.id.name_order);
        sodienthoai = findViewById(R.id.sdt_order);
        province = findViewById(R.id.province_spinner);
        district = findViewById(R.id.district_spinner);
        ward     = findViewById(R.id.ward_spinner);
        street   = findViewById(R.id.street_name);
        sumProductConfirm = findViewById(R.id.sumProductConfirm);
        totalConfirm = findViewById(R.id.totalConfirm);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);
        unitMoneyPayOrderTotal = findViewById(R.id.unitMoneyPayOrderTotal);
        unitMoneyPayOrderTotal.setPaintFlags(unitMoneyPayOrderTotal.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
    }
}