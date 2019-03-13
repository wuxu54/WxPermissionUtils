package com.permission.interfaces;

import android.app.Activity;

/**
 * @author: wu.xu
 * @data: 2019/3/11/011.
 * <p>
 * 毫无BUG
 */

public interface WxPermissionListener {

  String[] permissions();

  Activity activity();

  int requestCode();

}
