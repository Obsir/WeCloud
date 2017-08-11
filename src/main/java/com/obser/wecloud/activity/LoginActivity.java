package com.obser.wecloud.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.obser.wecloud.R;
import com.obser.wecloud.app.WeCloudApplication;
import com.obser.wecloud.bean.LoginInfo;
import com.obser.wecloud.bean.User;
import com.obser.wecloud.utils.NToast;
import com.obser.wecloud.utils.NetUtils;
import com.obser.wecloud.utils.UDPUtils;
import com.obser.wecloud.view.ClearWriteEditText;
import com.obser.wecloud.view.LoadDialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private final static String TAG = "LoginActivity";

    private ImageView mImg_Background;
    private Context mContext;
    private ClearWriteEditText mAccountEdit, mPasswordEdit;
    private String accountString;
    private String passwordString;
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        gson = new Gson();
        mContext = this;
        mAccountEdit = (ClearWriteEditText) findViewById(R.id.de_login_account);
        mPasswordEdit = (ClearWriteEditText) findViewById(R.id.de_login_password);
        Button mConfirm = (Button) findViewById(R.id.de_login_sign);
        TextView mRegister = (TextView) findViewById(R.id.de_login_register);
        TextView forgetPassword = (TextView) findViewById(R.id.de_login_forgot);
        forgetPassword.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mImg_Background = (ImageView) findViewById(R.id.de_img_backgroud);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.translate_anim);
                mImg_Background.startAnimation(animation);
            }
        }, 200);
        mAccountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
//                    AMUtils.onInactive(mContext, mAccountEdit);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.de_login_sign:
                accountString = mAccountEdit.getText().toString().trim();
                passwordString = mPasswordEdit.getText().toString().trim();

                if (TextUtils.isEmpty(accountString)) {
                    NToast.shortToast(mContext, R.string.phone_number_is_null);
                    mAccountEdit.setShakeAnimation();
                    return;
                }

//                if (!AMUtils.isMobile(accountString)) {
//                    NToast.shortToast(mContext, R.string.Illegal_phone_number);
//                    mAccountEdit.setShakeAnimation();
//                    return;
//                }

                if (TextUtils.isEmpty(passwordString)) {
                    NToast.shortToast(mContext, R.string.password_is_null);
                    mPasswordEdit.setShakeAnimation();
                    return;
                }
                if (passwordString.contains(" ")) {
                    NToast.shortToast(mContext, R.string.password_cannot_contain_spaces);
                    mPasswordEdit.setShakeAnimation();
                    return;
                }
                LoadDialog.show(mContext);
                login();
                break;
            case R.id.de_login_register:
                startActivityForResult(new Intent(this, RegisterActivity.class), 1);
                break;
            case R.id.de_login_forgot:
//                startActivityForResult(new Intent(this, ForgetPasswordActivity.class), 2);
                break;
        }
    }

    private void login(){
        User user = new User();
        user.setUsername(accountString);
        user.setUserpwd(passwordString);
        user.setIp(UDPUtils.getIPAddress(this));
        user.setPort("7901");
        Log.e("ChatUser", user.getIp().toString());
        Log.e("ChatUser", user.toString());
        Map<String, String> params = new HashMap<>();
        params.put("username", user.getUsername());
        params.put("userpwd", user.getUserpwd());
        params.put("ip", user.getIp());
        params.put("port", user.getPort());

        NetUtils.doPost(NetUtils.LOGIN_URL, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       login_failure();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.d(TAG, response.body().string() + "");
                final String str = response.body().string();
                final LoginInfo loginInfo = gson.fromJson(str, LoginInfo.class);
                Log.d(TAG, loginInfo.getHead());
                Log.d(TAG, loginInfo.getBody().getUSER() + "");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(loginInfo.getHead().equals("SUCCESS")){
                            goToMain(loginInfo);
                        } else {
                            login_failure();
                        }
                    }
                });
//                if(response.body().string().equals("SUCCESS")){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    goToMain();
//                                }
//                            });
//                        }
//                    });
//                } else {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            login_failure();
//                        }
//                    });
//                }
            }
        });
    }

    /**
     * 登录失败
     */
    private void login_failure(){
        LoadDialog.dismiss(mContext);
        NToast.shortToast(mContext, "登录失败！");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && data != null) {
            String account = data.getStringExtra("account");
            String password = data.getStringExtra("password");
            mAccountEdit.setText(account);
            mPasswordEdit.setText(password);
        } else if (data != null && requestCode == 1) {
            String account = data.getStringExtra("account");
            String password = data.getStringExtra("password");
            if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {
                mAccountEdit.setText(account);
                mPasswordEdit.setText(password);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    private void goToMain(LoginInfo loginInfo) {
        WeCloudApplication application = (WeCloudApplication) getApplication();
        User user = loginInfo.getBody().getUSER();
        user.setId("0");
        application.setUser(user);
//        editor.putString("loginToken", loginToken);
//        editor.putString(SealConst.SEALTALK_LOGING_PHONE, accountString);
//        editor.putString(SealConst.SEALTALK_LOGING_PASSWORD, passwordString);
//        editor.apply();
        LoadDialog.dismiss(mContext);
        NToast.shortToast(mContext, R.string.login_success);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//        intent.putExtra("account", accountString);
        startActivity(intent);
        finish();
    }
}
