package com.example.do_an_android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.do_an_android.Model.CustomerModel;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NguoiDungAdapter extends RecyclerView.Adapter<NguoiDungAdapter.NguoiDungViewHolder> {
    Context context;
    int layout;
    ArrayList<CustomerModel> customerList;
    private NguoiDungAdapter.LoadDataListener loadDataListener;
    public interface LoadDataListener {
        void loadData();
    }
    public void setLoadDataListener(NguoiDungAdapter.LoadDataListener listener) {
        this.loadDataListener = listener;
    }

    public NguoiDungAdapter(Context context, int layout, ArrayList<CustomerModel> customerList) {
        this.context = context;
        this.layout = layout;
        this.customerList = customerList;
    }

    @NonNull
    @Override
    public NguoiDungAdapter.NguoiDungViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_nguoidung, parent, false);
        return new NguoiDungAdapter.NguoiDungViewHolder(view);
    }

    private void KhoaTaiKhoan(String username, int status) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlKhoaTaiKhoan, response -> {
            Toast.makeText(context, "Cập nhật trạng thái thành công!", Toast.LENGTH_SHORT).show();
            loadDataListener.loadData();
        }, error -> Toast.makeText(context, "Lỗi khi cập nhật trạng thái!", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("status", String.valueOf(status));
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }

    private void MoKhoaTaiKhoan(String username, int status) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlMoKhoaTaiKhoan, response -> {
            Toast.makeText(context, "Cập nhật trạng thái thành công!", Toast.LENGTH_SHORT).show();
            loadDataListener.loadData();
        }, error -> Toast.makeText(context, "Lỗi khi cập nhật trạng thái!", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("status", String.valueOf(status));
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }

    @Override
    public void onBindViewHolder(@NonNull NguoiDungAdapter.NguoiDungViewHolder holder, int position) {
        CustomerModel customerModel = customerList.get(position);
        Picasso.get().load(Server.urlImage + customerModel.getImage()).into(holder.customerImage);
        holder.customerName.setText(customerModel.getName());
        holder.customerUsername.setText(customerModel.getUsername());
        holder.customerStatus.setText(String.valueOf(customerModel.getStatus()));

        if (customerModel.getUsername().equals("admin")) {
            holder.btnLockORUnlock.setVisibility(View.GONE);
        } else {
            holder.btnLockORUnlock.setVisibility(View.VISIBLE);
            if (customerModel.getStatus() == 1) {
                holder.btnLockORUnlock.setBackgroundResource(R.drawable.lock);
            } else if (customerModel.getStatus() == 0) {
                holder.btnLockORUnlock.setVisibility(View.GONE);
            } else {
                holder.btnLockORUnlock.setBackgroundResource(R.drawable.unlock);
            }
        }

        holder.btnLockORUnlock.setOnClickListener(v -> {
            int status = customerModel.getStatus();
            String lockOrUnlockMessage = (status == 1) ? "BẠN CÓ MUỐN KHÓA TÀI KHOẢN NÀY?" : "BẠN CÓ MUỐN MỞ KHÓA TÀI KHOẢN NÀY?";
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(lockOrUnlockMessage)
                    .setPositiveButton("CÓ", (dialog, which) -> {
                        if (status == 1) {
                            KhoaTaiKhoan(customerModel.getUsername(), -1);
                        }
                        if (status == -1) {
                            MoKhoaTaiKhoan(customerModel.getUsername(), 1);
                        }
                    })
                    .setNegativeButton("KHÔNG", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public static class NguoiDungViewHolder extends RecyclerView.ViewHolder {
        ImageView customerImage;
        TextView customerName, customerUsername, customerStatus;
        Button btnLockORUnlock;
        public NguoiDungViewHolder(@NonNull View itemView) {
            super(itemView);
            customerImage = itemView.findViewById(R.id.ivHinh);
            customerName = itemView.findViewById(R.id.Ten);
            customerUsername = itemView.findViewById(R.id.Username);
            customerStatus = itemView.findViewById(R.id.TrangThai);
            btnLockORUnlock = itemView.findViewById(R.id.btnLockORUnlock);
        }
    }
}