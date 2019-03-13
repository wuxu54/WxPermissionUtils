package com.permission.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.permission.R;


public class TipDialog {
  private static TipDialog tipDialog = null;

  private static Activity activity = null;

  private Dialog dialog;

  private TipDialog() {
  }

  private static synchronized TipDialog getDialog() {
    if (tipDialog == null) {
      tipDialog = new TipDialog();
    }
    return tipDialog;
  }

  public static TipDialog newInstance() {
    return getDialog();
  }

  public TipDialog setActivity(Activity activity) {
    this.activity = activity;
    return this;
  }

  public TipDialog showTipDialog(View view,boolean cancel,int width,int height) {
    final Dialog dialog = new Dialog(activity, R.style.dialog_activity);
    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
    dialog.setCancelable(cancel);
    dialog.addContentView(view, layoutParams);
    dialog.show();
    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
    lp.width = width; // 设置宽度
    lp.height =height;

    dialog.getWindow().
            setAttributes(lp);
    this.dialog = dialog;
    return this;
  }


  public void hideDialog() {
    if (dialog != null) {
      dialog.dismiss();
    }
  }

  public boolean isShowing() {
    if (dialog != null) {
      return dialog.isShowing();
    }
    return false;
  }

  public void showDialog() {
    if (dialog != null && !dialog.isShowing()) {
      try {
        dialog.show();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private DialogClickListener dialogClickListener = null;



  public interface DialogClickListener {
    void onClick();
  }

  public void setListener(DialogClickListener listener) {
    dialogClickListener = listener;
  }
}
