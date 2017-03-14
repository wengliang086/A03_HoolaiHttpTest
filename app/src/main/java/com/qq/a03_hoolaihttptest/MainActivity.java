package com.qq.a03_hoolaihttptest;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qq.a03_hoolaihttptest.service.hoolai.BaseResponse;
import com.qq.a03_hoolaihttptest.service.hoolai.HoolaiService;
import com.qq.a03_hoolaihttptest.service.hoolai.HoolaiServiceCreater;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 同步请求
     *
     * @param v
     */
    public void onSyncBtnClick(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                trialLogin();
            }
        }).start();
    }

    /**
     * 异步请求
     *
     * @param view
     */
    public void onAsyncBtnClick(View view) {
        HoolaiService service = HoolaiServiceCreater.create();
        Call<BaseResponse> call = service.trialLogin("1", 1, "456oooppp");
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Toast.makeText(MainActivity.this, new Gson().toJson(response.body()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void trialLogin() {
        HoolaiService service = HoolaiServiceCreater.create();
        Call<BaseResponse> call = service.trialLogin("1", 1, "456oooppp");
        try {
            Response<BaseResponse> res = call.execute();
            BaseResponse result = res.body();
            t(new Gson().toJson(result));
            Log.e(TAG, result.getDesc());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void t(String msg) {
        Looper.prepare();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

}
