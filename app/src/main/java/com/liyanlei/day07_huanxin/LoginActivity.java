package com.liyanlei.day07_huanxin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import org.greenrobot.eventbus.EventBus;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 输入账号
     */
    private EditText mName;
    /**
     * 输入密码
     */
    private EditText mPwd;
    /**
     * 登录
     */
    private Button mBtnLogin;
    /**
     * 注册
     */
    private Button mBtnRegister;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        progressDialog = new ProgressDialog(this);
        mName = (EditText) findViewById(R.id.name);
        mPwd = (EditText) findViewById(R.id.pwd);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mBtnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                register();
                break;
        }
    }

    private void register() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void login() {
        String name = mName.getText().toString().trim();
        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "输入账号", Toast.LENGTH_SHORT).show();
            return;
        }
        String pwd = mPwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)){
            Toast.makeText(this, "输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        EMClient.getInstance().login(name, pwd, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main", "登录成功: ");

                SharedPreferencesUtils.setParam(LoginActivity.this,Constants.NAME,name);

                EventBus.getDefault().post(name);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
                finish();
            }

            @Override
            public void onError(final int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.setProgress(i);
                    }
                });
            }

            @Override
            public void onProgress(final int i, final String s) {
                Log.d("main", "登录失败: ");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }
}
