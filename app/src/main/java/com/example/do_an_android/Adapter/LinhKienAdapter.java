package com.example.do_an_android.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.do_an_android.Activity.ChiTietSanPhamActivity;
import com.example.do_an_android.Model.CategoryModel;
import com.example.do_an_android.Model.ProductModel;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LinhKienAdapter extends RecyclerView.Adapter<LinhKienAdapter.ProductViewHolder> {
    Context context;
    int layout;
    ArrayList<ProductModel> productList;
    private OnItemClickListener mListener;
    private ArrayList<CategoryModel> categoryList = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setCategoryList(ArrayList<CategoryModel> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    public LinhKienAdapter(Context context, int layout, ArrayList<ProductModel> productList) {
        this.context = context;
        this.layout = layout;
        this.productList = productList;
    }

    public interface OnItemClickListener {
        void onItemClick(ProductModel productModel);

        void onItemLongClick(ProductModel productModel);
    }
    public void setOnItemClickListener(LinhKienAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_linhkien, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel productModel = productList.get(position);
        Picasso.get().load(Server.urlImage + productModel.getImage()).into(holder.productImage);
        holder.productName.setText(productModel.getName());
        holder.productCode.setText(productModel.getCode());
        holder.productTypeCode.setText(getTypeName(productModel.getType_code()));
        holder.itemView.setOnLongClickListener(v -> {
            if (mListener != null) {
                mListener.onItemLongClick(productModel);
                return true;
            }
            return false;
        });
        holder.chiTietButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChiTietSanPhamActivity.class);
            intent.putExtra("productModel", productModel);
            context.startActivity(intent);
        });
    }
    private String getTypeName(String typeCode) {
        for (CategoryModel category : categoryList) {
            if (category.getCode().equals(typeCode)) {
                return category.getName();
            }
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productCode;
        TextView productTypeCode;
        Button chiTietButton;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.hinhanhlinhkien);
            productName = itemView.findViewById(R.id.tenlinhkien);
            productCode = itemView.findViewById(R.id.malinhkien);
            productTypeCode = itemView.findViewById(R.id.loailinhkien);
            chiTietButton = itemView.findViewById(R.id.chiTietButton);
        }
    }
}