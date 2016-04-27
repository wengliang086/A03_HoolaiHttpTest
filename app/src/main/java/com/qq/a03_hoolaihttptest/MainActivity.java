package com.qq.a03_hoolaihttptest;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "aaaaa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                trialLogin();
//            }
//        }).start();
    }

    public void onBtnClick(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                trialLogin();
            }
        }).start();
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
