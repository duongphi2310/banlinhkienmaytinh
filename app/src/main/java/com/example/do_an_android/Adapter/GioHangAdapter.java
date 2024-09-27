package com.example.do_an_android.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.do_an_android.Model.CartModel;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.Model.Support;
import com.example.do_an_android.R;
import com.example.do_an_android._Fragment.GioHangFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.CartViewHolder> {
    Context context;
    int layout;
    ArrayList<CartModel> listCart;

    public GioHangAdapter(Context context, int layout, ArrayList<CartModel> listCart) {
        this.context = context;
        this.layout = layout;
        this.listCart = listCart;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CartModel cart = listCart.get(position);

        String hinh1 = cart.getProductModel().getImage();
        long price = 0;
        if (cart.getProductModel().getPrice_discounted() > 0)
            price = cart.getProductModel().getPrice_discounted();
        else
            price = cart.getProductModel().getPrice();
        holder.quantity.setText(String.valueOf(cart.getQuantity()));
        holder.subtotal.setText(Support.ConvertMoney(cart.getQuantity() * price));
        holder.price.setText(Support.ConvertMoney(price));
        holder.name.setText(cart.getProductModel().getName());
        holder.checkbox.setChecked(cart.isChecked());

        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cart.setChecked(isChecked);
            updateTotalQuantityAndPrice();
        });

        Picasso.get().load(Server.urlImage + hinh1).into(holder.image);

        long finalPrice = price;

        holder.quantity.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                return true;
            if (i == KeyEvent.KEYCODE_ENTER) {
                if (holder.quantity.getText().toString().isEmpty()) {
                    return false;
                }
                int quantity = Integer.parseInt(holder.quantity.getText().toString());
                if (quantity <= 0) {
                    confirmDetele(position);
                    return false;
                } else {
                    int quantityRemain = cart.getQuantityRemain() + cart.getQuantity();
                    if (quantity > quantityRemain) {
                        Toast.makeText(context, "Không đủ hàng.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    holder.subtotal.setText(Support.ConvertMoney(quantity * finalPrice));
                    listCart.get(position).setQuantity(quantity);
                    listCart.get(position).setQuantityRemain(quantityRemain - quantity);
                    GioHangFragment.updateCart(listCart);
                }
            }
            return false;
        });

        holder.itemView.setOnLongClickListener(view -> {
            confirmDetele(position);
            return true;
        });

        holder.tangSoLuong.setOnClickListener(v -> {
            String maLinhKien = cart.getProductModel().getCode();
            String url = Server.urlGetSoLuongCon + "?maLinhKien=" + maLinhKien;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int soLuongCon = jsonObject.getInt("SOLUONG");
                            int currentQuantity = cart.getQuantity() + 1;

                            // Kiểm tra nếu số lượng hiện tại lớn hơn số lượng còn
                            if (currentQuantity > soLuongCon) {
                                Toast.makeText(context, "Không đủ hàng.", Toast.LENGTH_SHORT).show();
                            } else {
                                holder.quantity.setText(String.valueOf(currentQuantity));
                                tangSoLuong(maLinhKien, currentQuantity, position);
                            }
                        } catch (JSONException ignored) {
                        }
                    },
                    error -> Toast.makeText(context, "Lỗi khi lấy số lượng còn từ máy chủ", Toast.LENGTH_SHORT).show());
            Volley.newRequestQueue(context).add(stringRequest);
        });

        holder.giamSoLuong.setOnClickListener(v -> {
            int currentQuantity = cart.getQuantity() - 1;
            holder.quantity.setText(String.valueOf(currentQuantity));
            if (currentQuantity == 0) {
                confirmDetele(position);
            } else {
                giamSoLuong(cart.getProductModel().getCode(), currentQuantity, position);
            }
        });

        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cart.setChecked(isChecked);
            updateTotalQuantityAndPrice();
        });
    }

    private void tangSoLuong(String maLinhKien, int quantity, int position) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlTangSoLuongGioHang, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                String message = jsonObject.getString("message");
                if (success) {
                    listCart.get(position).setQuantity(quantity);
                    notifyDataSetChanged();
                    GioHangFragment.updateCart(listCart);
                    updateTotalQuantityAndPrice();
                } else {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("maLinhKien", maLinhKien);
                params.put("soLuongHienTai", String.valueOf(quantity - 1));
                params.put("soLuongMoi", String.valueOf(quantity));
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }

    private void giamSoLuong(String maLinhKien, int quantity, int position) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlGiamSoLuongGioHang, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                String message = jsonObject.getString("message");
                if (success) {
                    listCart.get(position).setQuantity(quantity);
                    notifyDataSetChanged();
                    GioHangFragment.updateCart(listCart);
                    updateTotalQuantityAndPrice();
                } else {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("maLinhKien", maLinhKien);
                params.put("soLuongHienTai", String.valueOf(quantity + 1));
                params.put("soLuongMoi", String.valueOf(quantity));
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }

    private void confirmDetele(int position) {
        CartModel cartModel = listCart.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("THÔNG BÁO!");
        builder.setMessage("XÓA SẢN PHẨM " + cartModel.getProductModel().getName() + " KHỎI GIỎ HÀNG?");
        builder.setIcon(R.drawable.icon);
        builder.setPositiveButton("CÓ", (dialogInterface, i) -> {
            String maLinhKien = cartModel.getProductModel().getCode();
            deleteCartItemFromServer(maLinhKien, position);
        });
        builder.setNegativeButton("KHÔNG", (dialogInterface, i) -> {
            listCart.get(position).setQuantity(1);
            updateTotalQuantityAndPrice();
            notifyDataSetChanged();
        });
        builder.show();
    }

    private void deleteCartItemFromServer(String maLinhKien, int position) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlXoaKhoiGioHang, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                String message = jsonObject.getString("message");
                if (success) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    listCart.remove(position);
                    GioHangFragment.updateCart(listCart);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(context, "XÓA SẢN PHẨM THẤT BẠI", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("maLinhKien", maLinhKien);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return listCart.size();
    }

    public void updateTotalQuantityAndPrice() {
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
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, price, subtotal, unitMoneyItemCartPrice, unitMoneyItemCartSubtotal;
        EditText quantity;
        Button tangSoLuong, giamSoLuong;
        CheckBox checkbox;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.item_checkbox);
            tangSoLuong = itemView.findViewById(R.id.tangSoLuong);
            giamSoLuong = itemView.findViewById(R.id.giamSoLuong);
            image = itemView.findViewById(R.id.image_detail_cart);
            name = itemView.findViewById(R.id.name_detail_cart);
            price = itemView.findViewById(R.id.price_deatail_cart);
            subtotal = itemView.findViewById(R.id.subtotal_detail_cart);
            quantity = itemView.findViewById(R.id.quantity_detail_cart);
            unitMoneyItemCartPrice = itemView.findViewById(R.id.unitMoneyItemCartPrice);
            unitMoneyItemCartSubtotal = itemView.findViewById(R.id.unitMoneyItemCartSubtotal);
            unitMoneyItemCartPrice.setPaintFlags(unitMoneyItemCartPrice.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            unitMoneyItemCartSubtotal.setPaintFlags(unitMoneyItemCartSubtotal.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }
}