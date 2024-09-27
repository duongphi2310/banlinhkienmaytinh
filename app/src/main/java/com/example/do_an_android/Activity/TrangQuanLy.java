package com.example.do_an_android.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.do_an_android.R;

public class TrangQuanLy extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage);

        Button buttonLoaiLinhKien = findViewById(R.id.button_loailinhkien);
        buttonLoaiLinhKien.setOnClickListener(v -> {
            Intent intent = new Intent(TrangQuanLy.this, LoaiLinhKienActivity.class);
            startActivity(intent);
        });

        Button buttonSanPham = findViewById(R.id.button_sanpham);
        buttonSanPham.setOnClickListener(view -> {
            Intent intent = new Intent(TrangQuanLy.this, LinhKienActivity.class);
            startActivity(intent);
        });

        Button buttonNguoiDung = findViewById(R.id.button_nguoidung);
        buttonNguoiDung.setOnClickListener(view -> {
            Intent intent = new Intent(TrangQuanLy.this, NguoiDungActivity.class);
            startActivity(intent);
        });

        Button buttonDonHang = findViewById(R.id.button_donhang);
        buttonDonHang.setOnClickListener(view -> {
            Intent intent = new Intent(TrangQuanLy.this, DonHangActivity.class);
            startActivity(intent);
        });

        Button buttonThongKe = findViewById(R.id.button_thongke);
        buttonThongKe.setOnClickListener(view -> {
            Intent intent = new Intent(TrangQuanLy.this, ThongKeActivity.class);
            startActivity(intent);
        });
    }
}
