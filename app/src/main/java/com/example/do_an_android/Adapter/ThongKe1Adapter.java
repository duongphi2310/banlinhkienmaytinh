package com.example.do_an_android.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.do_an_android.Model.ThongKe3Model;
import com.example.do_an_android.R;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ThongKe1Adapter extends RecyclerView.Adapter<ThongKe1Adapter.ViewHolder> {
    Context context;
    List<ThongKe3Model> thongKe3List;
    private OnItemClickListener mListener;
    HashMap<String, Integer> colorMap;

    public ThongKe1Adapter(Context context, List<ThongKe3Model> thongKe3List) {
        this.context = context;
        this.thongKe3List = thongKe3List;
        this.colorMap = new HashMap<>();
    }

    public interface OnItemClickListener {
        void onItemLongClick(ThongKe3Model thongKe3Model);
    }

    public void setOnItemClickListener(ThongKe1Adapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public void updateData(List<ThongKe3Model> newData) {
        thongKe3List.clear();
        thongKe3List.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thongke_loaikinhkien, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThongKe3Model thongKe3Model = thongKe3List.get(position);
        holder.tenloailinhkien.setText(thongKe3Model.getTenllinhkien());
        int color = colorMap.getOrDefault(thongKe3Model.getTenllinhkien(), getRandomColor());
        holder.colorIndicator.setBackgroundColor(color);
        holder.itemView.setOnLongClickListener(v -> {
            if (mListener != null) {
                mListener.onItemLongClick(thongKe3Model);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return thongKe3List.size();
    }

    private int getRandomColor() {
        Random random = new Random();
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    public void setColorMap(HashMap<String, Integer> colorMap) {
        this.colorMap = colorMap;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenloailinhkien;
        View colorIndicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenloailinhkien = itemView.findViewById(R.id.tenloailinhkien);
            colorIndicator = itemView.findViewById(R.id.colorIndicator);
        }
    }
}