package com.extremex.tablemanager.lib;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.extremex.tablemanager.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CustomDialogBuilder extends Dialog implements View.OnClickListener {

    Dialog dialog;
    int id;

    public CustomDialogBuilder(@NonNull Context context) {
        super(context);
    }

    public CustomDialogBuilder(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomDialogBuilder(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    public void dismissButton(View v){
        onClick(v);
        dismiss();
    }

    ////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MaterialButton dismissButton = (MaterialButton) findViewById(R.id.DialogDismissButton);
        MaterialButton confirmButton = (MaterialButton) findViewById(R.id.DialogConfirmButton);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, @Nullable Menu menu, int deviceId) {
        super.onProvideKeyboardShortcuts(data, menu, deviceId);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
    ////
}
