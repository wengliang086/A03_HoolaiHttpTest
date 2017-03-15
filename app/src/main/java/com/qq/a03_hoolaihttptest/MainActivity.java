package com.qq.a03_hoolaihttptest;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qq.a03_hoolaihttptest.module.User;
import com.qq.a03_hoolaihttptest.service.hoolai.HoolaiResponse;
import com.qq.a03_hoolaihttptest.service.hoolai.HoolaiService;
import com.qq.a03_hoolaihttptest.service.hoolai.HoolaiServiceCreater;

import java.io.IOException;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private HoolaiService service = HoolaiServiceCreater.create();

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
        Call<HoolaiResponse<User>> call = service.trialLogin("1", 1, "456oooppp");
        call.enqueue(new Callback<HoolaiResponse<User>>() {
            @Override
            public void onResponse(Call<HoolaiResponse<User>> call, Response<HoolaiResponse<User>> response) {
                User user = response.body().getValue();
                Toast.makeText(MainActivity.this, new Gson().toJson(user), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<HoolaiResponse<User>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void trialLogin() {
        Call<HoolaiResponse<User>> call = service.trialLogin("1", 1, "456oooppp");
        try {
            Response<HoolaiResponse<User>> res = call.execute();
            User user = res.body().getValue();
            t(new Gson().toJson(user));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void t(String msg) {
        Looper.prepare();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    public void onRxjavaBtnClick(View view) {
        Observable<HoolaiResponse<User>> observable = service.rxJavaLogin("1", 1, "456oooppp");
        observable = observable.observeOn(AndroidSchedulers.mainThread())//指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。
                .subscribeOn(Schedulers.io());//指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。
        final int type = new Random().nextInt(3);//三种方式
        switch (type) {
            case 0:
                observable.subscribe(new Subscriber<HoolaiResponse<User>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(HoolaiResponse<User> userHoolaiResponse) {
                        User user = userHoolaiResponse.getValue();
                        Toast.makeText(MainActivity.this, "type=" + type + " " + new Gson().toJson(user), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case 1:
                observable.subscribe(new Observer<HoolaiResponse<User>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(HoolaiResponse<User> userHoolaiResponse) {
                        User user = userHoolaiResponse.getValue();
                        Toast.makeText(MainActivity.this, "type=" + type + " " + new Gson().toJson(user), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case 2:
                Action1<HoolaiResponse<User>> nextAction = new Action1<HoolaiResponse<User>>() {
                    @Override
                    public void call(HoolaiResponse<User> userHoolaiResponse) {
                        User user = userHoolaiResponse.getValue();
                        Toast.makeText(MainActivity.this, "type=" + type + " " + new Gson().toJson(user), Toast.LENGTH_SHORT).show();
                    }
                };
                Action1<Throwable> errorAction = new Action1<Throwable>() {
                    @Override
                    public void call(Throwable o) {
                        Toast.makeText(MainActivity.this, "type=" + type + " Error", Toast.LENGTH_SHORT).show();
                    }
                };
                Action0 complateAction = new Action0() {
                    @Override
                    public void call() {
                        Toast.makeText(MainActivity.this, "type=" + type + " Complate", Toast.LENGTH_SHORT).show();
                    }
                };
                observable.subscribe(nextAction, errorAction, complateAction);
                break;
        }
    }
}
