package com.ray.project.services;

import android.accessibilityservice.AccessibilityService;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 免密码安装应用
 * 使用说明：
 * 1.accessibility_config中android:packageNames逗号前面换成应用的包名
 * 2.启动服务startActivity(new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS));
 * 3.设置-》更多设置-》辅助功能-》辅助功能快捷方式-》快捷方式服务中选择应用
 */
public class InstallerHelperService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode == null) return;

        if (event.getPackageName().equals("com.vivo.secime.service")) {
            // ViVo账号密码
            String password = "Vivo123456";
            if (!TextUtils.isEmpty(password)) {
                fillPassword(rootNode, password);
            }
        }
        findAndClickView(rootNode);
    }

    private void fillPassword(AccessibilityNodeInfo rootNode, String password) {
        AccessibilityNodeInfo editText = rootNode.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
        if (editText == null) return;

        if (editText.getPackageName().equals("com.bbk.account")
                && editText.getClassName().equals("android.widget.EditText")) {
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo
                    .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, password);
            editText.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        }
    }

    /**
     * 查找按钮并点击
     */
    private void findAndClickView(AccessibilityNodeInfo rootNode) {
        List<AccessibilityNodeInfo> nodeInfoList = new ArrayList<>();
        nodeInfoList.addAll(rootNode.findAccessibilityNodeInfosByText("确定"));
        nodeInfoList.addAll(rootNode.findAccessibilityNodeInfosByText("继续安装"));
        nodeInfoList.addAll(rootNode.findAccessibilityNodeInfosByText("安装"));
        nodeInfoList.addAll(rootNode.findAccessibilityNodeInfosByText("打开"));

        for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    @Override
    public void onInterrupt() {

    }
}
