package com.example.do_an_android._Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class XoaLinhKienDialogFragment extends DialogFragment {
    String maLinhKien, tenLinhKien, giaTien, soLuong, giaTienKhuyenMai;
    String moTa, maLoaiLinhKien, currentImagePath;
    Context context;
    private ConfirmDeleteListener mListener;

    public interface ConfirmDeleteListener {
        void onConfirmDelete();
    }

    public XoaLinhKienDialogFragment(Context context, String maLinhKien, String tenLinhKien, Long giaTien, Integer soLuong,
                                     Long giaTienKhuyenMai, String moTa, String maLoaiLinhKien, String currentImagePath) {
        this.context = context;
        this.maLinhKien = maLinhKien;
        this.tenLinhKien = tenLinhKien;
        this.giaTien = String.valueOf(giaTien);
        this.soLuong = String.valueOf(soLuong);
        this.giaTienKhuyenMai = String.valueOf(giaTienKhuyenMai);
        this.moTa = moTa;
        this.maLoaiLinhKien = maLoaiLinhKien;
        this.currentImagePath = currentImagePath;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (ConfirmDeleteListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ConfirmDeleteListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Bạn có muốn xóa linh kiện này không?")
                .setPositiveButton("CÓ", (dialog, id) -> mListener.onConfirmDelete())
                .setNegativeButton("KHÔNG", (dialog, id) -> dialog.dismiss());
        return builder.create();
    }
}
