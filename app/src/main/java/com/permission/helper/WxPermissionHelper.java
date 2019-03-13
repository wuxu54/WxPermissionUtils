package com.permission.helper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.permission.constant.Constant;
import com.permission.dialog.TipDialog;

/**
 * @author: wu.xu
 * @data: 2019/3/11/011.
 * <p>
 * 毫无BUG
 */

public class WxPermissionHelper {
  private static WxPermissionHelper wxPermissionHelper;

  private WxPermissionHelper() {
  }

  private synchronized static WxPermissionHelper createWxPermissionHelper() {
    return new WxPermissionHelper();
  }

  public synchronized static WxPermissionHelper getInstance() {
    if (wxPermissionHelper == null) {
      wxPermissionHelper = createWxPermissionHelper();
    }
    return wxPermissionHelper;
  }

  public void showTipView(View view, final Activity activity, Bundle bundle) {
    if (view == null || activity == null || bundle == null) return;
    TipDialog.newInstance()
            .setActivity(activity)
            .showTipDialog(view,
                    bundle.getBoolean(Constant.DIALOG_CANCEL, true),
                    bundle.getInt(Constant.VIEW_WIDTH, 500),
                    bundle.getInt(Constant.VIEW_HEIGHT, 500)
            );
  }

}
