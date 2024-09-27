package com.example.do_an_android._Fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class XoaLoaiLinhKienDialogFragment extends DialogFragment {
    String maLinhKien, tenLoaiLinhKien;
    String currentImagePath;
    Context context;
    private ConfirmDeleteListener mListener;

    public XoaLoaiLinhKienDialogFragment(Context context, String maLinhKien, String tenLoaiLinhKien, String currentImagePath) {
        this.context = context;
        this.maLinhKien = maLinhKien;
        this.tenLoaiLinhKien = tenLoaiLinhKien;
        this.currentImagePath = currentImagePath;
    }

    public interface ConfirmDeleteListener {
        void onConfirmDelete();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (ConfirmDeleteListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement ConfirmDeleteListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Bạn có muốn xóa loại linh kiện này không?")
                .setPositiveButton("CÓ", (dialog, id) -> mListener.onConfirmDelete())
                .setNegativeButton("KHÔNG", (dialog, id) -> dialog.dismiss());
        return builder.create();
    }
}