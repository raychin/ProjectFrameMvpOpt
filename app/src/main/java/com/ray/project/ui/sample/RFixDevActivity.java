package com.ray.project.ui.sample;

import android.os.Bundle;

import com.tencent.rfix.lib.RFix;
import com.tencent.rfix.lib.dev.AbsRFixDevActivity;

/**
 * @Description: shiply热修复集成测试页面
 * @Author: ray
 * @Date: 26/6/2024
 */
public class RFixDevActivity extends AbsRFixDevActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 主动拉取补丁
//        RFix manager = RFix.getInstance();
//        manager.requestConfig();

    }
}
