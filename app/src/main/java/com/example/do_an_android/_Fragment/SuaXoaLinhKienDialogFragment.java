package com.example.do_an_android._Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.do_an_android.R;

public class SuaXoaLinhKienDialogFragment extends DialogFragment {
    Context context;
    String maLinhKien, tenLinhKien;
    String moTa, maLoaiLinhKien, currentImagePath;
    Integer soLuong;
    Long giaTien, giaTienKhuyenMai;

    public SuaXoaLinhKienDialogFragment(String maLinhKien, String tenLinhKien, Long giaTien, Integer soLuong,
                                        Long giaTienKhuyenMai, String moTa, String maLoaiLinhKien, String currentImagePath) {
        this.maLinhKien = maLinhKien;
        this.tenLinhKien = tenLinhKien;
        this.giaTien = giaTien;
        this.soLuong = soLuong;
        this.giaTienKhuyenMai = giaTienKhuyenMai;
        this.moTa = moTa;
        this.maLoaiLinhKien = maLoaiLinhKien;
        this.currentImagePath = currentImagePath;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_suaxoalinhkien, container, false);
        Button suaButton = view.findViewById(R.id.button_sua_linh_kien);
        Button xoaButton = view.findViewById(R.id.button_xoa_linh_kien);
        suaButton.setOnClickListener(v -> showSuaLinhKienDialog(maLinhKien, tenLinhKien, giaTien, soLuong, giaTienKhuyenMai, moTa, maLoaiLinhKien, currentImagePath));
        xoaButton.setOnClickListener(view1 -> showXoaLinhKienDialog(maLinhKien, tenLinhKien, giaTien, soLuong, giaTienKhuyenMai, moTa, maLoaiLinhKien, currentImagePath));
        return view;
    }

    private void showSuaLinhKienDialog(String maLinhKien, String tenLinhKien, Long giaTien, Integer soLuong,
                                       Long giaTienKhuyenMai, String moTa, String maLoaiLinhKien, String currentImagePath) {
        SuaLinhKienDialogFragment suaDialogFragment = new SuaLinhKienDialogFragment(context, maLinhKien, tenLinhKien, giaTien, soLuong, giaTienKhuyenMai, moTa, maLoaiLinhKien, currentImagePath);
        suaDialogFragment.show(getFragmentManager(), "sua_linh_kien_dialog");
    }

    private void showXoaLinhKienDialog(String maLinhKien, String tenLinhKien, Long giaTien, Integer soLuong,
                                       Long giaTienKhuyenMai, String moTa, String maLoaiLinhKien, String currentImagePath) {
        XoaLinhKienDialogFragment xoaDialogFragment = new XoaLinhKienDialogFragment(context, maLinhKien, tenLinhKien, giaTien, soLuong, giaTienKhuyenMai, moTa, maLoaiLinhKien, currentImagePath);
        xoaDialogFragment.show(getFragmentManager(), "xoa_linh_kien_dialog");
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        getDialog().getWindow().setLayout((int) (width * 0.4), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

}
