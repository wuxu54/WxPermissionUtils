package com.permission.manager;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.permission.helper.WxPermissionHelper;
import com.permission.interfaces.WxPermissionListener;
import com.permission.interfaces.WxPermissionResultListener;
import com.permission.interfaces.WxPermissionTipViewListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wu.xu
 * @data: 2019/3/6/006.
 * <p>
 * <p>
 * PermissionManager
 */

public class WxPermissionManager {
  private Activity activity;
  private String[] permissions;
  private int requestCode = 0x65;
  private WxPermissionHelper wxPermissionHelper;

  private WxPermissionResultListener wxResultListener;

  private WxPermissionTipViewListener wxTipViewListener;

  public static Builder instanceBuilder() {
    return new Builder();
  }


  private WxPermissionManager(Builder builder) {
    if (builder.wxPermissionListener == null) {
      throw new IllegalArgumentException(" WxPermissionListener should be implements ");
    }

    this.activity = builder.wxPermissionListener.activity();

    this.permissions = builder.wxPermissionListener.permissions();

    this.requestCode = builder.wxPermissionListener.requestCode();

    wxPermissionHelper = WxPermissionHelper.getInstance();

    wxResultListener = builder.resultListener;

    wxTipViewListener = builder.tipViewListener;

    View reminderView = wxTipViewListener.reminderView(null);
    Bundle bundle = wxTipViewListener.reminderViewBundle(null);

    if (reminderView == null) {
      request();
    } else {
      showTipView(reminderView, bundle);
    }
  }

  public void request() {
    request(permissions, requestCode);
  }

  /**
   * 暴露可单独请求数组
   * 筛选数组，找出需要申请的数组。如果都已申请完事，直接调用结果监听 result设置为true
   *
   * @param permissions
   * @param requestCode
   */
  public void request(String[] permissions, int requestCode) {
    if (permissions != null && permissions.length > 0) {

      boolean allGranted = true;

      List<String> allPermissions = new ArrayList<>();

      String[] unGrantedPermission = new String[permissions.length];

      int index = 0;

      for (int i = 0; i < permissions.length; i++) {
        String permission = permissions[i];
        allPermissions.add(permission);
        if (!permissionGranted(activity, permission)) {
          allGranted = false;
          unGrantedPermission[index++] = permission;
        }
      }


      if (allGranted) {
        if (wxResultListener != null) {
          wxResultListener.onResult(requestCode, true, allPermissions);
        }
      } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          activity.requestPermissions(unGrantedPermission, requestCode);
        }
      }
    }
  }

  public List<String> needless2Setting(String[] permissions) {
    if (permissions != null && permissions.length > 0) {
      List<String> needSettingPermissions = new ArrayList<>();
      for (int i = 0; i < permissions.length; i++) {
        String permission = permissions[i];
        if (!permissionGranted(activity, permission) && shouldShowRationale(activity, permission)) {
          needSettingPermissions.add(permission);
        }
      }
      return needSettingPermissions;
    }
    return null;
  }

  public List<String> need2Setting(String[] permissions) {
    if (permissions != null && permissions.length > 0) {
      List<String> needSettingPermissions = new ArrayList<>();
      for (int i = 0; i < permissions.length; i++) {
        String permission = permissions[i];
        if (!permissionGranted(activity, permission) && !shouldShowRationale(activity, permission)) {
          needSettingPermissions.add(permission);
        }
      }
      return needSettingPermissions;
    }
    return null;
  }


  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    boolean result = true;
    List<String> permission = new ArrayList<>();
    for (int i = 0; i < permissions.length; i++) {
      if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
        permission.add(permissions[i]);
        result = false;
      }
    }

    if (wxResultListener != null) {
      wxResultListener.onResult(requestCode, result, permission);
    }
  }

  public boolean permissionGranted(Activity activity, String permissions) {
    boolean result = false;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
      int RECORD_AUDIO = activity.checkSelfPermission(permissions);
      result = RECORD_AUDIO == PackageManager.PERMISSION_GRANTED;
    }
    return result;
  }

  public boolean shouldShowRationale(Activity activity, String permissions) {
    return ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions);
  }

  public void showTipView(View view, Bundle bundle) {
    if (view == null) return;
    wxPermissionHelper.showTipView(view, activity, bundle);
  }


  public void showSettingTipView(List<String> permission) {
    if (permission.size() > 0) {
      boolean toSettingDialog = false;//当没有权限且禁止申请权限时，提示跳转设置
      String p = "";
      for (String permissions : permission) {
        boolean a = permissionGranted(activity, permissions);
        boolean b = shouldShowRationale(activity, permissions);

        if (!a && !b) {
          toSettingDialog = true;
          p = permissions;
          break;
        }
      }

      if (toSettingDialog) {
        wxPermissionHelper.showTipView(wxTipViewListener.tipSettingView(p), activity, wxTipViewListener.tipSettingViewBundle(p));
      }
    }
  }

  public static final class Builder {
    WxPermissionListener wxPermissionListener;
    WxPermissionResultListener resultListener;
    WxPermissionTipViewListener tipViewListener;

    public Builder setPermissionListener(WxPermissionListener wxPermissionListener) {
      this.wxPermissionListener = wxPermissionListener;
      return this;
    }

    public Builder setPermissionResultListener(WxPermissionResultListener resultListener) {
      this.resultListener = resultListener;
      return this;
    }

    public Builder setPermissionTipViewListener(WxPermissionTipViewListener tipViewListener) {
      this.tipViewListener = tipViewListener;
      return this;
    }

    public WxPermissionManager build() {
      return new WxPermissionManager(this);
    }
  }
}
