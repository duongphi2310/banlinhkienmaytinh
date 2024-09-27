package com.example.do_an_android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.do_an_android.Model.CategoryModel;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LoaiLinhKienAdapter extends RecyclerView.Adapter<LoaiLinhKienAdapter.CategoryViewHolder> {
    Context context;
    int layout;
    ArrayList<CategoryModel> categoryList;
    private OnItemClickListener mListener;

    public LoaiLinhKienAdapter(Context context, int layout, ArrayList<CategoryModel> categoryList) {
        this.context = context;
        this.layout = layout;
        this.categoryList = categoryList;
    }

    public interface OnItemClickListener {
        void onItemLongClick(CategoryModel categoryModel);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_loailinhkien, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel categoryModel = categoryList.get(position);
        Picasso.get().load(Server.urlImage + categoryModel.getImage()).into(holder.categoryImage);
        holder.categoryName.setText(categoryModel.getName());
        holder.categoryCode.setText(categoryModel.getCode());
        holder.itemView.setOnLongClickListener(v -> {
            if (mListener != null) {
                mListener.onItemLongClick(categoryModel);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage;
        TextView categoryName, categoryCode;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryCode = itemView.findViewById(R.id.categoryCode);
        }
    }
}
