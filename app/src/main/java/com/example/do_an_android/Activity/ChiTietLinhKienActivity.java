package com.example.do_an_android.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.do_an_android.Adapter.ProductOfTypeAdapter;
import com.example.do_an_android.Model.CartModel;
import com.example.do_an_android.Model.GridSpacingItemDecoration;
import com.example.do_an_android.Model.ProductModel;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.Model.Support;
import com.example.do_an_android.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ChiTietLinhKienActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imageProductDetail;
    TextView nameProductDetail, priceProductDetail, priceDiscountProductDetail, descriptionProdcutDetail,titleSimilarProduct,unitPriceProductDetail,unitPriceDiscountProductDetail;
    EditText quantityProductDetail;
    Button btnBuyNow, btnAddCart, giamsoluong, tangsoluong;
    ProductModel productModel;
    SharedPreferences sharedPreferencesCart;
    ArrayList<CartModel> listCart = null;
    RecyclerView recycleviewSimilarProduct;
    ArrayList<ProductModel> lstSimilarProducts;
    ProductOfTypeAdapter similarProductAdapter;
    SharedPreferences sharedPreferencesUser;
    Context context;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        setControl();
        loadDataProduct();
        setClick();
        loadDataCart();
        setRecyclerAdapter();
        loadDataSimilarProduct();
        sharedPreferencesUser = getSharedPreferences("user", Context.MODE_PRIVATE);
        username = sharedPreferencesUser.getString("username", "");
    }

    private void loadDataCart() {
        sharedPreferencesCart = getSharedPreferences("cart", Context.MODE_PRIVATE);
        String cart = sharedPreferencesCart.getString("item_cart", "");
        CartModel[] cartModels = new Gson().fromJson(cart, CartModel[].class);
        if (cartModels != null)
            listCart = new ArrayList<CartModel>(Arrays.asList(cartModels));
        else
            listCart = new ArrayList<>();
    }

    private void setClick() {
        btnAddCart.setOnClickListener(this);
        btnBuyNow.setOnClickListener(this);
        giamsoluong.setOnClickListener(this);
        tangsoluong.setOnClickListener(this);
    }

    private void loadDataProduct() {
        productModel = (ProductModel) getIntent().getSerializableExtra("productDetail");
        Picasso.get().load(Server.urlImage + productModel.getImage()).into(imageProductDetail);
        nameProductDetail.setText(productModel.getName());
        descriptionProdcutDetail.setText(productModel.getDescription());
        if (productModel.getPrice_discounted() > 0) {
            priceDiscountProductDetail.setText(Support.ConvertMoney(productModel.getPrice_discounted()));
            priceProductDetail.setText(Support.ConvertMoney(productModel.getPrice()));
        } else {
            priceDiscountProductDetail.setText(Support.ConvertMoney(productModel.getPrice()));
            priceProductDetail.setText("");
            unitPriceProductDetail.setText("");
        }
    }

    private void setControl() {
        imageProductDetail = findViewById(R.id.imageProductDetail);
        nameProductDetail = findViewById(R.id.nameProductDetail);
        priceProductDetail = findViewById(R.id.priceProductDetail);
        priceDiscountProductDetail = findViewById(R.id.priceDiscountProductDetail);
        descriptionProdcutDetail = findViewById(R.id.descriptionProdcutDetail);
        quantityProductDetail = findViewById(R.id.quantityProductDetail);
        giamsoluong = findViewById(R.id.giamSoLuong);
        tangsoluong = findViewById(R.id.tangSoLuong);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        btnAddCart = findViewById(R.id.btnAddCart);
        recycleviewSimilarProduct = findViewById(R.id.recycleviewSimilarProduct);
        titleSimilarProduct = findViewById(R.id.titleSimilarProduct);
        unitPriceProductDetail = findViewById(R.id.unitPriceProductDetail);
        unitPriceDiscountProductDetail = findViewById(R.id.unitPriceDiscountProductDetail);
        priceProductDetail.setPaintFlags(priceProductDetail.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        unitPriceProductDetail.setPaintFlags(unitPriceProductDetail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        unitPriceDiscountProductDetail.setPaintFlags(unitPriceDiscountProductDetail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btnAddCart:
                if (!quantityProductDetail.getText().toString().equals(""))
                    AddCart();
                break;
            case R.id.btnBuyNow:
                if (quantityProductDetail.getText().toString().equals(""))
                    break;
                    int checkAddCart = AddCart();
                    if (checkAddCart == 1) {
                        Intent intent = new Intent(ChiTietLinhKienActivity.this, MainActivity.class);
                        intent.putExtra("checkBuyNow", true);
                        startActivity(intent);
                    }
                break;
            case R.id.giamSoLuong:
                decreaseQuantity();
                break;
            case R.id.tangSoLuong:
                increaseQuantity();
                break;
        }
    }

    private void decreaseQuantity() {
        int quantity = Integer.parseInt(quantityProductDetail.getText().toString());
        if (quantity > 1) {
            quantity--;
            quantityProductDetail.setText(String.valueOf(quantity));
        } else {
            Toast.makeText(this, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show();
        }
    }

    private void increaseQuantity() {
        int quantity = Integer.parseInt(quantityProductDetail.getText().toString());
        quantity++;
        quantityProductDetail.setText(String.valueOf(quantity));
    }

    private int AddCart() {
        String maLinhKien = productModel.getCode();
        int soLuong = Integer.parseInt(quantityProductDetail.getText().toString());
        if (soLuong == 0) {
            Toast.makeText(this, "Số lượng không hợp lệ.", Toast.LENGTH_SHORT).show();
            return 0;
        }
        if (soLuong > productModel.getQuantity()) {
            Toast.makeText(this, "Không đủ hàng.", Toast.LENGTH_SHORT).show();
            return -1;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlThemVaoGioHang, response -> {
            if (response.equals("1")) {
                Toast.makeText(ChiTietLinhKienActivity.this, "Đã thêm vào giỏ.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ChiTietLinhKienActivity.this, "Thêm giỏ thất bại.", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("maLinhKien", maLinhKien);
                params.put("username", username);
                params.put("soLuong", String.valueOf(soLuong));
                return params;
            }
        };
        queue.add(stringRequest);

        boolean check = false;
        int quantity = Integer.parseInt(quantityProductDetail.getText().toString());
        if (quantity == 0)
            return 0;
        for (CartModel item : listCart) {
            if (item.getProductModel().getCode().equals(productModel.getCode())) {
                check = true;
                CartModel cartModel = item;
                if (quantity > cartModel.getQuantityRemain()) {
                    Toast.makeText(this, "Không đủ hàng.", Toast.LENGTH_SHORT).show();
                    return -1;
                }
                listCart.remove(item);
                cartModel.setQuantityRemain(cartModel.getQuantityRemain() - quantity);
                cartModel.setQuantity(cartModel.getQuantity() + quantity);
                listCart.add(cartModel);
                break;
            }
        }
        if (!check) {
            if (quantity > productModel.getQuantity()) {
                Toast.makeText(this, "Không đủ hàng.", Toast.LENGTH_SHORT).show();
                return -1;
            }
            listCart.add(new CartModel(username, productModel, quantity, productModel.getQuantity() - quantity, productModel.getImage()));
        }
        Toast.makeText(this, "Thêm sản phẩm thành công.", Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editorCart = sharedPreferencesCart.edit();
        editorCart.putString("item_cart", new Gson().toJson(listCart));
        editorCart.commit();
        return 1;
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void setRecyclerAdapter() {
        lstSimilarProducts = new ArrayList<>();
        similarProductAdapter = new ProductOfTypeAdapter(this, R.layout.item_product_of_type, lstSimilarProducts);
        recycleviewSimilarProduct.setLayoutManager(new GridLayoutManager(this,2));
        recycleviewSimilarProduct.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), false));
        recycleviewSimilarProduct.setAdapter(similarProductAdapter);
    }

    private void loadDataSimilarProduct() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlProductsOfType + "?type_code=" + productModel.getType_code(), response -> {
            boolean check = false;
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    if (productModel.getCode().equals(jsonObject.getString("code")))
                        continue;
                    lstSimilarProducts.add(
                            new ProductModel(
                                    jsonObject.getString("code"),
                                    jsonObject.getString("name"),
                                    jsonObject.getLong("price"),
                                    jsonObject.getInt("quantity"),
                                    jsonObject.getLong("price_discounted"),
                                    jsonObject.getString("description"),
                                    jsonObject.getString("image"),
                                    jsonObject.getString("type_code")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (lstSimilarProducts.size() == 0)
                titleSimilarProduct.setText("");
            similarProductAdapter.notifyDataSetChanged();
        }, error -> {
        });
        queue.add(jsonArrayRequest);
    }
}