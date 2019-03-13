package com.permission.interfaces;

import java.util.List;

/**
 * @author: wu.xu
 * @data: 2019/3/11/011.
 * <p>
 * 毫无BUG
 */

public interface WxPermissionResultListener {
  void onResult(int requestCode,boolean allGranted,List<String> permission);

}
