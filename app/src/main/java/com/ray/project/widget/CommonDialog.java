package com.ray.project.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ray.project.R;

/**
 * 自定义对话框
 * @author ray
 * @date 2018/07/09
 */
public class CommonDialog extends Dialog implements View.OnClickListener {

    // 标题、两个按钮
    public static final int DIALOG_TITLE = 0x1001;
    // 标题、内容、两个按钮
    public static final int DIALOG_TITLE_WITH_CONTENT = 0x1002;
    // 标题、单个按钮
    public static final int DIALOG_TITLE_BUTTON = 0x1003;
    // 标题、内容、单个按钮
    public static final int DIALOG_TITLE_BUTTON_CONTENT = 0x1004;
    // 圆形等待进度框
    public static final int DIALOG_PROGRESS = 0x1005;
    // 圆形带底部文子进度框
    public static final int DIALOG_PROGRESS_CONTENT = 0x1006;

    private TextView contentTxt;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;

    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;

    public CommonDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public CommonDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public CommonDialog(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
    }

    public CommonDialog(Context context, String content, OnCloseListener listener) {
        super(context, R.style.dialog);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
    }

    protected CommonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public CommonDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public CommonDialog setPositiveButton(String name){
        this.positiveName = name;
        return this;
    }

    public CommonDialog setNegativeButton(String name){
        this.negativeName = name;
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_commom);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){
        contentTxt = (TextView)findViewById(R.id.content);
        titleTxt = (TextView)findViewById(R.id.title);
        submitTxt = (TextView)findViewById(R.id.submit);
        cancelTxt = (TextView)findViewById(R.id.cancel);

        submitTxt.setOnClickListener(this);
        cancelTxt.setOnClickListener(this);

        contentTxt.setText(content);
        if(!TextUtils.isEmpty(positiveName)){
            submitTxt.setText(positiveName);
        }

        if(!TextUtils.isEmpty(negativeName)){
            cancelTxt.setText(negativeName);
        }

        if(!TextUtils.isEmpty(title)){
            titleTxt.setText(title);
        }
    }

    // 取消按钮id
    private static final int ID_CANCEL = 0x2001;
    // 确定按钮id
    private static final int ID_SUBMIT = 0x2002;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel:
                if(listener != null){
                    listener.onClick(this, false);
                }
                this.dismiss();
                break;
            case R.id.submit:
                if(listener != null){
                    listener.onClick(this, true);
                }
                break;
        }
    }

    public interface OnCloseListener{
        void onClick(Dialog dialog, boolean confirm);
    }
}
