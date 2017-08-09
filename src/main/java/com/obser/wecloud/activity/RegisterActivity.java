package com.obser.wecloud.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.obser.wecloud.R;
import com.obser.wecloud.User;
import com.obser.wecloud.utils.NToast;
import com.obser.wecloud.utils.NetUtils;
import com.obser.wecloud.view.ClearWriteEditText;
import com.obser.wecloud.view.LoadDialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REGISTER_BACK = 1001;
    private ImageView mImgBackground;
    private ClearWriteEditText mAccountEdit, mPasswordEdit, mRealNameEdit;
    private Button mConfirm;
    private String mAccount, mPassword, mRealName;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        mContext = this;
        mAccountEdit = (ClearWriteEditText) findViewById(R.id.reg_account);
        mPasswordEdit = (ClearWriteEditText) findViewById(R.id.reg_password);
        mRealNameEdit = (ClearWriteEditText) findViewById(R.id.reg_realName);
        mConfirm = (Button) findViewById(R.id.reg_button);
        mConfirm.setOnClickListener(this);

        TextView goLogin = (TextView) findViewById(R.id.reg_login);
        TextView goForget = (TextView) findViewById(R.id.reg_forget);
        goLogin.setOnClickListener(this);
        goForget.setOnClickListener(this);

        mImgBackground = (ImageView) findViewById(R.id.rg_img_backgroud);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.translate_anim);
                mImgBackground.startAnimation(animation);
            }
        }, 200);

        addEditTextListener();

    }

    private void addEditTextListener() {
        mAccountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() == 11 && isBright) {
//                    if (AMUtils.isMobile(s.toString().trim())) {
//                        mAccount = s.toString().trim();
//                        request(CHECK_PHONE, true);
//                        AMUtils.onInactive(mContext, mAccountEdit);
//                    } else {
//                        Toast.makeText(mContext, R.string.Illegal_phone_number, Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    mGetCode.setClickable(false);
//                    mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 5) {
                    mConfirm.setClickable(true);
                    mConfirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_blue));
                } else {
                    mConfirm.setClickable(false);
                    mConfirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mRealNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.reg_forget:
//                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
            case R.id.reg_button:
                mAccount = mAccountEdit.getText().toString().trim();
                mPassword = mPasswordEdit.getText().toString().trim();
                mRealName = mRealNameEdit.getText().toString().trim();
                if (TextUtils.isEmpty(mAccount)) {
                    NToast.shortToast(mContext, getString(R.string.phone_number_is_null));
                    mAccountEdit.setShakeAnimation();
                    return;
                }
                if (TextUtils.isEmpty(mPassword)) {
                    NToast.shortToast(mContext, getString(R.string.password_is_null));
                    mPasswordEdit.setShakeAnimation();
                    return;
                }
                if (TextUtils.isEmpty(mRealName)) {
                    NToast.shortToast(mContext, getString(R.string.realname_is_null));
                    mRealNameEdit.setShakeAnimation();
                    return;
                }
                if (mPassword.contains(" ")) {
                    NToast.shortToast(mContext, getString(R.string.password_cannot_contain_spaces));
                    mPasswordEdit.setShakeAnimation();
                    return;
                }
                LoadDialog.show(mContext);
                register();
                break;
        }
    }

    private void register() {
        User user=new User();
        user.setUsername(mAccount);
        user.setUserpwd(mPassword);
        user.setRealname(mRealName);
        Map<String, String> params = new HashMap<>();
        params.put("username", user.getUsername());
        params.put("userpwd", user.getUserpwd());
        params.put("realname", user.getRealname());
        NetUtils.doPost(NetUtils.REGISTER_URL, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, "注册失败！");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, R.string.register_success);
                        Intent data = new Intent();
                        data.putExtra("account", mAccount);
                        data.putExtra("password", mPassword);
                        setResult(REGISTER_BACK, data);
                        RegisterActivity.this.finish();
                    }
                });
            }
        });
    }

}
