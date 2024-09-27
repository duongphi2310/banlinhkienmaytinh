package com.example.do_an_android.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.do_an_android.Model.GridSpacingItemDecoration;
import com.example.do_an_android.Model.OrderDetailModel;
import com.example.do_an_android.Model.OrderModel;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.Model.Support;
import com.example.do_an_android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderofCustomerAdapter extends RecyclerView.Adapter<OrderofCustomerAdapter.ViewOrderOfCustomer> {
    Context context;
    int layout;
    ArrayList<OrderModel> orderModelArrayList;
    Dialog dialog;
    RecyclerView recycleviewOrderDetail;
    Button btnCancelOrderDetail;
    ArrayList<OrderDetailModel> orderDetailModelArrayList;
    OrderDetailAdapter orderDetailAdapter;

    public OrderofCustomerAdapter(Context context, int layout, ArrayList<OrderModel> orderModelArrayList) {
        this.context = context;
        this.layout = layout;
        this.orderModelArrayList = orderModelArrayList;
    }

    @NonNull
    @Override
    public ViewOrderOfCustomer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewOrderOfCustomer(LayoutInflater.from(context).inflate(layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewOrderOfCustomer holder, @SuppressLint("RecyclerView") int position) {
        OrderModel orderModel = orderModelArrayList.get(position);
        holder.codeOrderOfCustomer.setText(orderModel.getCode());
        holder.dateOrderOfCustomer.setText(orderModel.getCreateDate());
        holder.totalOrderOfCustomer.setText(Support.ConvertMoney(orderModel.getTotal()));

        if (orderModel.getStatus() == 3) {
            holder.itemView.setOnLongClickListener(view -> {
                openCancelOrderDialog(position);
                return true;
            });
        } else {
            holder.itemView.setOnLongClickListener(null);
        }
        holder.itemView.setOnClickListener(view -> openDialogOrderDeatail(position));
    }

    private void openDialogOrderDeatail(int position) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_order_detail);
        dialog.setCanceledOnTouchOutside(false);

        TextView titleOrderDetail = dialog.findViewById(R.id.titleOrderDetail);
        titleOrderDetail.setText("CHI TIẾT ĐƠN HÀNG " + orderModelArrayList.get(position).getCode());

        TextView tenOrderDetail = dialog.findViewById(R.id.tenOrderDetail);
        TextView sodienthoaiOrderDetail = dialog.findViewById(R.id.sodienthoaiOrderDetail);
        TextView diachiOrderDetail = dialog.findViewById(R.id.diachiOrderDetail);
        fetchOrderDetails(orderModelArrayList.get(position).getCode(), tenOrderDetail, sodienthoaiOrderDetail, diachiOrderDetail);
        setControl();
        setAdapterOrderDetail();
        getDataOrderDetail(position);
        setClick();
        dialog.show();
    }

    private void fetchOrderDetails(String code, TextView tenOrderDetail, TextView sodienthoaiOrderDetail, TextView diachiOrderDetail) {
        String url = Server.urlGetAddressOrder + "?mahd=" + code;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, response -> {
            try {
                JSONObject object = response.getJSONObject(0);
                Log.d("OrderDetails", object.toString()); // In dữ liệu JSON để kiểm tra
                tenOrderDetail.setText(object.getString("ten"));
                sodienthoaiOrderDetail.setText(object.getString("sodienthoai"));
                diachiOrderDetail.setText(object.getString("diachi"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            // Handle error
            Log.e("FetchOrderDetailsError", error.toString());
        });

        Volley.newRequestQueue(context).add(jsonArrayRequest);
    }



    private void openCancelOrderDialog(int position) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_huydonhang);
        dialog.setCanceledOnTouchOutside(false);
        Button confirmButton = dialog.findViewById(R.id.button_huydonhang);

        confirmButton.setOnClickListener(view -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage("BẠN CÓ CHẮC CHẮN MUỐN HỦY ĐƠN HÀNG " + orderModelArrayList.get(position).getCode() + " ?");
            alertDialogBuilder.setPositiveButton("CÓ", (dialogInterface, i) -> cancelOrder(position));
            alertDialogBuilder.setNegativeButton("KHÔNG", (dialogInterface, i) -> dialogInterface.dismiss());
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });
        dialog.show();
    }

    private void cancelOrder(int position) {
        String urlHuyDonHang = Server.urlHuyDonHang + "?code_order=" + orderModelArrayList.get(position).getCode() + "&username=" + orderModelArrayList.get(position).getUsername();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, urlHuyDonHang, null,
                response -> {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("ĐÃ HỦY ĐƠN HÀNG " + orderModelArrayList.get(position).getCode());
                    alertDialogBuilder.setPositiveButton("OK", (dialogInterface, i) -> {
                        updateStock(orderModelArrayList.get(position).getCode());
                        orderModelArrayList.remove(position);
                        notifyDataSetChanged();
                        dialogInterface.dismiss();
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setOnDismissListener(dialogInterface -> dialog.dismiss());
                    alertDialog.show();
                },
                error -> {
                });
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    private void updateStock(String codeOrder) {
        String urlUpdateStock = Server.urlUpdateInventory + "?code_order=" + codeOrder;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, urlUpdateStock, null, response -> {
                },
                error -> {
                });
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    private void getDataOrderDetail(int position) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlGetListOrderDetailByCode + "?code_order=" + orderModelArrayList.get(position).getCode(), response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject object = response.getJSONObject(i);
                    orderDetailModelArrayList.add(new OrderDetailModel(object.getString("code_order"), object.getString("name_product"), object.getLong("price"), object.getInt("quantity"), object.getLong("total")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            orderDetailAdapter.notifyDataSetChanged();
        }, error -> {

        });
        Volley.newRequestQueue(context).add(jsonArrayRequest);
    }

    private void setClick() {
        btnCancelOrderDetail.setOnClickListener(view -> dialog.dismiss());
    }

    private void setControl() {
        recycleviewOrderDetail = dialog.findViewById(R.id.recycleviewOrderDetail);
        btnCancelOrderDetail = dialog.findViewById(R.id.btnCancelOrderDetail);
    }

    private int dpToPx(int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void setAdapterOrderDetail() {
        orderDetailModelArrayList = new ArrayList<>();
        orderDetailAdapter = new OrderDetailAdapter(context, R.layout.item_order_detail, orderDetailModelArrayList);
        recycleviewOrderDetail.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(8), false));
        recycleviewOrderDetail.setAdapter(orderDetailAdapter);
        recycleviewOrderDetail.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
    }

    @Override
    public int getItemCount() {
        return orderModelArrayList.size();
    }

    public static class ViewOrderOfCustomer extends RecyclerView.ViewHolder {
        TextView codeOrderOfCustomer, dateOrderOfCustomer, totalOrderOfCustomer, unitTotalOrderOfCustomer;

        public ViewOrderOfCustomer(@NonNull View itemView) {
            super(itemView);
            codeOrderOfCustomer = itemView.findViewById(R.id.codeOrderOfCustomer);
            dateOrderOfCustomer = itemView.findViewById(R.id.dateOrderOfCustomer);
            totalOrderOfCustomer = itemView.findViewById(R.id.totalOrderOfCustomer);
            unitTotalOrderOfCustomer = itemView.findViewById(R.id.unitTotalOrderOfCustomer);
            unitTotalOrderOfCustomer.setPaintFlags(unitTotalOrderOfCustomer.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }
}