package com.example.do_an_android.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
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

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.CategoryViewHolder> {
    Context context;
    int layout;
    ArrayList<OrderModel> orderList;
    private OnItemClickListener mListener;
    Dialog dialog;
    RecyclerView recycleviewOrderDetail;
    Button btnCancelOrderDetail;
    ArrayList<OrderDetailModel> orderDetailModelArrayList;
    OrderDetailAdapter orderDetailAdapter;

    public DonHangAdapter(Context context, int layout, ArrayList<OrderModel> orderList) {
        this.context = context;
        this.layout = layout;
        this.orderList = orderList;
    }

    public interface OnItemClickListener {
        void onItemLongClick(OrderModel orderModel);
    }
    public void setOnItemClickListener(DonHangAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_donhang, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        OrderModel orderModel = orderList.get(position);
        holder.mahoadon.setText(orderModel.getCode());
        holder.username.setText(orderModel.getUsername());
        holder.ngaylap.setText(orderModel.getCreateDate());
        holder.tongtien.setText(Support.ConvertMoney(orderModel.getTotal()));

        holder.itemView.setOnClickListener(view -> openDialogOrderDeatail(position));
        holder.itemView.setOnLongClickListener(v -> {
            if (mListener != null) {
                mListener.onItemLongClick(orderModel);
                return true;
            }
            return false;
        });

        if (orderModel.getStatus() == 3) {
            holder.itemView.setOnLongClickListener(view -> {
                openDialogChoXacNhan(position);
                return true;
            });
        } else if (orderModel.getStatus() == 4) {
            holder.itemView.setOnLongClickListener(view -> {
                openDialogDangGiao(position);
                return true;
            });
        }
    }

    private void openDialogChoXacNhan(int position) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_capnhattrangthai_choxacnhan);
        dialog.setCanceledOnTouchOutside(false);
        Button danggiaohang = dialog.findViewById(R.id.button_danggiaohang);
        Button huydonhang   = dialog.findViewById(R.id.button_huydonhang1);

        danggiaohang.setOnClickListener(view -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage("CẬP NHẬT TRẠNG THÁI ĐƠN HÀNG " + orderList.get(position).getCode() + " ?");
            alertDialogBuilder.setPositiveButton("CÓ", (dialogInterface, i) -> setDangGiaoHang(position));
            alertDialogBuilder.setNegativeButton("KHÔNG", (dialogInterface, i) -> dialogInterface.dismiss());
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

        huydonhang.setOnClickListener(view -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage("CẬP NHẬT TRẠNG THÁI ĐƠN HÀNG " + orderList.get(position).getCode() + " ?");
            alertDialogBuilder.setPositiveButton("CÓ", (dialogInterface, i) -> setHuyDonHang(position));
            alertDialogBuilder.setNegativeButton("KHÔNG", (dialogInterface, i) -> dialogInterface.dismiss());
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });
        dialog.show();
    }

    private void setDangGiaoHang(int position) {
        String urlDangDonHang = Server.urlChuyenTrangThaiDangGiaoHang + "?code_order=" + orderList.get(position).getCode();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, urlDangDonHang, null,
                response -> {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("ĐÃ CẬP NHẬT ĐƠN HÀNG " + orderList.get(position).getCode() + " SANG TRẠNG THÁI ĐANG GIAO HÀNG!");
                    alertDialogBuilder.setPositiveButton("OK", (dialogInterface, i) -> {
                        orderList.remove(position);
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

    private void setHuyDonHang(int position) {
        String urlHuyDonHang = Server.urlChuyenTrangThaiDaHuy + "?code_order=" + orderList.get(position).getCode();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, urlHuyDonHang, null,
                response -> {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("ĐÃ HỦY ĐƠN HÀNG " + orderList.get(position).getCode() + " !");
                    alertDialogBuilder.setPositiveButton("OK", (dialogInterface, i) -> {
                        updateStock(orderList.get(position).getCode());
                        orderList.remove(position);
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

    private void openDialogDangGiao(int position) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_capnhattrangthai_danggiao);
        dialog.setCanceledOnTouchOutside(false);

        Button dagiaohang = dialog.findViewById(R.id.button_dagiaohang);
        Button huydonhang = dialog.findViewById(R.id.button_huydonhang2);

        dagiaohang.setOnClickListener(view -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage("CẬP NHẬT TRẠNG THÁI ĐƠN HÀNG " + orderList.get(position).getCode() + " ?");
            alertDialogBuilder.setPositiveButton("CÓ", (dialogInterface, i) -> setDaGiaoHang(position));
            alertDialogBuilder.setNegativeButton("KHÔNG", (dialogInterface, i) -> dialogInterface.dismiss());
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

        huydonhang.setOnClickListener(view -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage("CẬP NHẬT TRẠNG THÁI ĐƠN HÀNG " + orderList.get(position).getCode() + " ?");
            alertDialogBuilder.setPositiveButton("CÓ", (dialogInterface, i) -> setHuyDonHang(position));
            alertDialogBuilder.setNegativeButton("KHÔNG", (dialogInterface, i) -> dialogInterface.dismiss());
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });
        dialog.show();
    }

    private void setDaGiaoHang(int position) {
        String urlDaGiaoHang = Server.urlChuyenTrangThaiDaGiaoHang + "?code_order=" + orderList.get(position).getCode();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, urlDaGiaoHang, null,
                response -> {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("ĐÃ CẬP NHẬT ĐƠN HÀNG " + orderList.get(position).getCode() + " SANG TRẠNG THÁI GIAO HÀNG THÀNH CÔNG!");
                    alertDialogBuilder.setPositiveButton("OK", (dialogInterface, i) -> {
                        orderList.remove(position);
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

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    private void openDialogOrderDeatail(int position) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_order_detail);
        dialog.setCanceledOnTouchOutside(false);

        TextView tenOrderDetail = dialog.findViewById(R.id.tenOrderDetail);
        TextView sodienthoaiOrderDetail = dialog.findViewById(R.id.sodienthoaiOrderDetail);
        TextView diachiOrderDetail = dialog.findViewById(R.id.diachiOrderDetail);
        fetchOrderDetails(orderList.get(position).getCode(), tenOrderDetail, sodienthoaiOrderDetail, diachiOrderDetail);

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

    private void setControl() {
        recycleviewOrderDetail = dialog.findViewById(R.id.recycleviewOrderDetail);
        btnCancelOrderDetail = dialog.findViewById(R.id.btnCancelOrderDetail);
    }

    private void setAdapterOrderDetail() {
        orderDetailModelArrayList = new ArrayList<>();
        orderDetailAdapter = new OrderDetailAdapter(context, R.layout.item_order_detail, orderDetailModelArrayList);
        recycleviewOrderDetail.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), false));
        recycleviewOrderDetail.setAdapter(orderDetailAdapter);
        recycleviewOrderDetail.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
    }

    private void getDataOrderDetail(int position) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlGetListOrderDetailByCode + "?code_order=" + orderList.get(position).getCode(), response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject object=response.getJSONObject(i);
                    orderDetailModelArrayList.add(new OrderDetailModel(object.getString("code_order"),object.getString("name_product"),object.getLong("price"),object.getInt("quantity"),object.getLong("total")));
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

    private int dpToPx(int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView mahoadon, username, ngaylap, tongtien;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            mahoadon = itemView.findViewById(R.id.mahoadon);
            username = itemView.findViewById(R.id.username);
            ngaylap = itemView.findViewById(R.id.ngaylap);
            tongtien = itemView.findViewById(R.id.tongtien);
        }
    }
}