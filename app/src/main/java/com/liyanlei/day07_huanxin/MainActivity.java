package com.liyanlei.day07_huanxin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

//李艳雷  1812B
public class MainActivity extends AppCompatActivity {

    /**
     * 点击登录
     */
    private TextView mTvUser;
    private RecyclerView mRv;
    private boolean isLogined;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        mTvUser = (TextView) findViewById(R.id.tv_user);
        mRv = (RecyclerView) findViewById(R.id.rv);

        mTvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Login();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        menu.add(1,1,1,"登录");
        menu.add(1,2,1,"退出");
        menu.add(1,3,1,"群聊");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()){
            case 1:
                Login();
                break;
            case 2:
               loginOut();
                break;
            case 3:
                group();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void group() {
        if (isLogined) {

        } else {
            Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginOut() {
        if (isLogined){
            EMClient.getInstance().logout(true, new EMCallBack() {
                @Override
                public void onSuccess() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
                            mTvUser.setText("点击登录");
                            SharedPreferencesUtils.setParam(MainActivity.this,Constants.NAME,"");

                        }
                    });
                    isLogined = false;  

                }

                @Override
                public void onError(final int i, final String s) {

                }

                @Override
                public void onProgress(final int i, final String s) {

                }
            });
        }else {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();

        }

    }

    private void Login() {
        if (!isLogined) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "已经登录", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void data(String name){
        isLogined =true;
        mTvUser.setText("当前用户为："+name);
    }
}
