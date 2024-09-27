package com.example.do_an_android.Activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.do_an_android.Model.ProductModel;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.Model.Support;
import com.example.do_an_android.R;
import com.squareup.picasso.Picasso;

public class ChiTietSanPhamActivity extends AppCompatActivity {
    TextView tenLinhKien, giaTienKhuyenMai, giaGoc, soLuong, moTa, vndGiaGoc, vndGiaGiam;
    ImageView hinhAnhLinhKien;
    ProductModel productModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chitietsanpham);

        tenLinhKien = findViewById(R.id.tenLinhKien);
        giaTienKhuyenMai = findViewById(R.id.giaTienKhuyenMai);
        giaGoc = findViewById(R.id.giaGoc);
        soLuong = findViewById(R.id.edtSoLuong);
        moTa = findViewById(R.id.mota);
        hinhAnhLinhKien = findViewById(R.id.imageLinhKien);
        vndGiaGoc = findViewById(R.id.vndGiaGoc);
        vndGiaGiam = findViewById(R.id.vndGiaGiam);
        giaGoc.setPaintFlags(giaGoc.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        ProductModel productModel = (ProductModel) getIntent().getSerializableExtra("productModel");

        if (productModel != null) {
            tenLinhKien.setText(productModel.getName());
            giaTienKhuyenMai.setText(String.valueOf(productModel.getPrice_discounted()));
            giaGoc.setText(String.valueOf(productModel.getPrice()));
            soLuong.setText(String.valueOf(productModel.getQuantity()));
            moTa.setText(productModel.getDescription());
            Picasso.get().load(Server.urlImage + productModel.getImage()).into(hinhAnhLinhKien);
            if (productModel.getPrice_discounted() > 0) {
                giaTienKhuyenMai.setText(Support.ConvertMoney(productModel.getPrice_discounted()));
                giaGoc.setText(Support.ConvertMoney(productModel.getPrice()));
            } else {
                giaTienKhuyenMai.setText(Support.ConvertMoney(productModel.getPrice()));
                giaGoc.setText("");
                vndGiaGoc.setText("");
            }
        }
    }


}
