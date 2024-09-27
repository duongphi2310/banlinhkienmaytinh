package com.example.do_an_android.Adapter;

import com.example.do_an_android.Model.ThongKe3Model;
import com.example.do_an_android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ThongKe3Adapter extends RecyclerView.Adapter<ThongKe3Adapter.ThongKe3ViewHolder> {
    Context context;
    int layout;
    ArrayList<ThongKe3Model> thongKe3List;

    public ThongKe3Adapter(Context context, int layout, ArrayList<ThongKe3Model> thongKe3List) {
        this.context = context;
        this.layout = layout;
        this.thongKe3List = thongKe3List;
    }

    @NonNull
    @Override
    public ThongKe3ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_thongke_loailinhkien3, parent, false);
        return new ThongKe3Adapter.ThongKe3ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThongKe3Adapter.ThongKe3ViewHolder holder, int position) {
        ThongKe3Model thongKe3Model = thongKe3List.get(position);
        holder.loailinhkien.setText(thongKe3Model.getTenllinhkien());
        holder.soluong.setText(String.valueOf(thongKe3Model.getSoluongban()));
        holder.phantram.setText(String.valueOf(thongKe3Model.getPhantram()));
    }

    @Override
    public int getItemCount() {
        return thongKe3List.size();
    }

    public static class ThongKe3ViewHolder extends RecyclerView.ViewHolder {
        TextView loailinhkien, soluong, phantram;
        public ThongKe3ViewHolder(@NonNull View itemView) {
            super(itemView);
            loailinhkien = itemView.findViewById(R.id.thongke3_loailinhkien);
            soluong      = itemView.findViewById(R.id.thongke3_soluong);
            phantram     = itemView.findViewById(R.id.thongke3_phantram);
        }
    }
}