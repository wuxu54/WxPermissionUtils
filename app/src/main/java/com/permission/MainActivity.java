package com.permission;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.permission.constant.Constant;
import com.permission.dialog.TipDialog;
import com.permission.interfaces.WxPermissionListener;
import com.permission.interfaces.WxPermissionResultListener;
import com.permission.interfaces.WxPermissionTipViewListener;
import com.permission.manager.WxPermissionManager;
import com.permission.utils.ScreenUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements WxPermissionListener {

  String[] permissions = new String[]{
          Manifest.permission.ACCESS_COARSE_LOCATION,
          Manifest.permission.ACCESS_FINE_LOCATION,
          Manifest.permission.RECORD_AUDIO
  };

  String[] permissions2 = new String[]{
          Manifest.permission.RECORD_AUDIO
  };

  WxPermissionManager wxPermissionManager;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    View viewById = findViewById(R.id.text);
    viewById.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        wxPermissionManager.request(permissions2, 111);
      }
    });

    wxPermissionManager = WxPermissionManager.instanceBuilder()
            .setPermissionListener(this)
            .setPermissionTipViewListener(new WxPermissionTipViewListener() {
              @Override
              public View reminderView(String permissionKey) {
                View view = View.inflate(activity(), R.layout.dialog, null);
                view.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    TipDialog.newInstance().hideDialog();
                    wxPermissionManager.request();
                  }
                });
                return view;
              }

              @Override
              public View tipSettingView(String permissionKey) {
                View view = View.inflate(activity(), R.layout.dialog1, null);
                view.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    Toast.makeText(activity(), "tt", Toast.LENGTH_SHORT).show();
                  }
                });
                return view;
              }

              @Override
              public Bundle reminderViewBundle(String permissionKey) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.DIALOG_CANCEL,false);
                bundle.putString(Constant.VIEW_HEIGHT, String.valueOf(ScreenUtils.getScreenWidth(activity())* 0.7f));
                bundle.putString(Constant.VIEW_WIDTH, String.valueOf(ScreenUtils.getScreenWidth(activity())* 0.7f));
                return bundle;
              }

              @Override
              public Bundle tipSettingViewBundle(String permissionKey) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.DIALOG_CANCEL,false);
                bundle.putString(Constant.VIEW_HEIGHT, String.valueOf(ScreenUtils.getScreenWidth(activity())* 0.7f));
                bundle.putString(Constant.VIEW_WIDTH, String.valueOf(ScreenUtils.getScreenWidth(activity())* 0.7f));
                return bundle;
              }
            })
            .setPermissionResultListener(new WxPermissionResultListener() {
              @Override
              public void onResult(int requestCode, boolean allGranted, List<String> permission) {
                Toast.makeText(getApplicationContext(), "requestCode---" + requestCode, Toast.LENGTH_SHORT).show();
                if (!allGranted) {
                  wxPermissionManager.showSettingTipView(permission);
                } else {
                  //授权成功
                  Toast.makeText(getApplicationContext(), "qu---", Toast.LENGTH_SHORT).show();
                }
              }
            })
            .build();
  }

  @Override
  public String[] permissions() {
    return permissions;
  }


  @Override
  public Activity activity() {
    return this;
  }

  @Override
  public int requestCode() {
    return 123;
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    wxPermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
}
