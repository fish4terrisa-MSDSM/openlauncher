package com.freebrio.interfaces;

public interface DialogListener {

    interface OnActionDialogListener {
        void onAdd(int type);
    }

    interface OnEditDialogListener {
        void onRename(String name);
    }
}