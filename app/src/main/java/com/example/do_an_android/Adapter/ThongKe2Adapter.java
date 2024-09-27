package com.example.do_an_android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.do_an_android.Model.ThongKe2Model;
import com.example.do_an_android.R;
import java.util.ArrayList;

public class ThongKe2Adapter extends RecyclerView.Adapter<ThongKe2Adapter.ThongKe2ViewHolder> {
    Context context;
    int layout;
    ArrayList<ThongKe2Model> thongKe2List;

    public ThongKe2Adapter(Context context, int layout, ArrayList<ThongKe2Model> thongKe2List) {
        this.context = context;
        this.layout = layout;
        this.thongKe2List = thongKe2List;
    }

    @NonNull
    @Override
    public ThongKe2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_thongke_loailinhkien2, parent, false);
        return new ThongKe2ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThongKe2ViewHolder holder, int position) {
        ThongKe2Model thongKe2Model = thongKe2List.get(position);
        holder.loailinhkien.setText(thongKe2Model.getTenllinhkien());
        holder.tongtien.setText(thongKe2Model.getTongtienban());
    }

    @Override
    public int getItemCount() {
        return thongKe2List.size();
    }

    public static class ThongKe2ViewHolder extends RecyclerView.ViewHolder {
        TextView loailinhkien, tongtien;
        public ThongKe2ViewHolder(@NonNull View itemView) {
            super(itemView);
            loailinhkien = itemView.findViewById(R.id.thongke2_loailinhkien);
            tongtien = itemView.findViewById(R.id.thongke2_tongtien);
        }
    }
}