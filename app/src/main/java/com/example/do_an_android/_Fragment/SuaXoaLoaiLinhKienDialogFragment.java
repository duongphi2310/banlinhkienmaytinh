package com.example.do_an_android._Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.do_an_android.Activity.LoaiLinhKienActivity;
import com.example.do_an_android.R;

public class SuaXoaLoaiLinhKienDialogFragment extends DialogFragment {
    Context context;
    String maLinhKien;
    String tenLoaiLinhKien;
    String currentImagePath;

    public SuaXoaLoaiLinhKienDialogFragment(String maLinhKien, String tenLoaiLinhKien, String currentImagePath) {
        this.maLinhKien = maLinhKien;
        this.tenLoaiLinhKien = tenLoaiLinhKien;
        this.currentImagePath = currentImagePath;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_suaxoaloailinhkien, container, false);
        Button suaButton = view.findViewById(R.id.button_sua_loai_linh_kien);
        Button xoaButton = view.findViewById(R.id.button_xoa_loai_linh_kien);
        suaButton.setOnClickListener(v -> showSuaLoaiLinhKienDialog(maLinhKien, tenLoaiLinhKien, currentImagePath));
        xoaButton.setOnClickListener(view1 -> showXoaLoaiLinhKienDialog(maLinhKien, tenLoaiLinhKien, currentImagePath));
        return view;
    }

    private void showSuaLoaiLinhKienDialog(String maLinhKien, String tenLoaiLinhKien, String currentImagePath) {
        SuaLoaiLinhKienDialogFragment suaDialogFragment = new SuaLoaiLinhKienDialogFragment(context, maLinhKien, tenLoaiLinhKien, currentImagePath);
        suaDialogFragment.show(getFragmentManager(), "sua_loai_linh_kien_dialog");
    }

    private void showXoaLoaiLinhKienDialog(String maLinhKien, String tenLoaiLinhKien, String currentImagePath) {
        XoaLoaiLinhKienDialogFragment xoaDialogFragment = new XoaLoaiLinhKienDialogFragment(context, maLinhKien, tenLoaiLinhKien, currentImagePath);
        xoaDialogFragment.show(getFragmentManager(), "xoa_loai_linh_kien_dialog");
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        getDialog().getWindow().setLayout((int) (width * 0.4), ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
