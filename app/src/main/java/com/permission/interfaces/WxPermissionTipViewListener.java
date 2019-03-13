package com.permission.interfaces;

import android.os.Bundle;
import android.view.View;

/**
 * @author: wu.xu
 * @data: 2019/3/12/012.
 * <p>
 * 毫无BUG
 */

public interface WxPermissionTipViewListener {
  View reminderView(String permissionKey);

  View tipSettingView(String permissionKey);

  Bundle reminderViewBundle(String permissionKey);

  Bundle tipSettingViewBundle(String permissionKey);
}
