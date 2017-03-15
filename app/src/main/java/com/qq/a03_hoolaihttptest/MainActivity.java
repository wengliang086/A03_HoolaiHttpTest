package com.qq.a03_hoolaihttptest;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qq.a03_hoolaihttptest.module.User;
import com.qq.a03_hoolaihttptest.service.flowable.ProgressSubscriber;
import com.qq.a03_hoolaihttptest.service.flowable.SubscriberOnNextListener;
import com.qq.a03_hoolaihttptest.service.hoolai.HoolaiResponse;
import com.qq.a03_hoolaihttptest.service.hoolai.HoolaiService;
import com.qq.a03_hoolaihttptest.service.hoolai.HoolaiServiceCreater;
import com.qq.a03_hoolaihttptest.service.observable.ObservableOnNextListener;
import com.qq.a03_hoolaihttptest.service.observable.ProgressObservable;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.Random;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 参照博客 http://gank.io/post/56e80c2c677659311bed9841
 */
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
        Flowable<HoolaiResponse<User>> observable = service.rxJavaLogin("1", 1, "456oooppp");
        observable = observable.observeOn(AndroidSchedulers.mainThread())//指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。
                .subscribeOn(Schedulers.io());//指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。
        int max = 5;
        int rType = new Random().nextInt(max);//多种方式
        int t = Integer.parseInt(((EditText) findViewById(R.id.id_et_type)).getText().toString());
        if (t >= 0 && t < 5) {
            rType = t;
        }
        final int type = rType;
        switch (type) {
            case 0://原始方式
                observable.subscribe(new Subscriber<HoolaiResponse<User>>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        //这一步是必须，我们通常可以在这里做一些初始化操作，调用request()方法表示初始化工作已经完成
                        //调用request()方法，会立即触发onNext()方法
                        //在onComplete()方法完成，才会再执行request()后边的代码
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "type=" + type + " Error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(HoolaiResponse<User> userHoolaiResponse) {
                        User user = userHoolaiResponse.getValue();
                        Toast.makeText(MainActivity.this, "type=" + type + " " + new Gson().toJson(user), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case 1://优化1：去掉包装层的方式（直接能拿到User对象）
                HoolaiServiceCreater.rxJavaLogin(new Subscriber<User>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(User user) {
                        Toast.makeText(MainActivity.this, "type=" + type + " " + new Gson().toJson(user), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(MainActivity.this, "type=" + type + " Error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                }, "1", 1, "456oooppp");
                break;
            case 2://优化2：在优化1的基础上，只关心onNext方法
                /*HoolaiServiceCreater.rxJavaLogin(new ProgressSubscriber<User>(this, new SubscriberOnNextListener<User>() {
                    @Override
                    public void onNext(User user) {
                        Toast.makeText(MainActivity.this, "type=" + type + " " + new Gson().toJson(user), Toast.LENGTH_SHORT).show();
                    }
                }), "1", 1, "456oooppp");*/
                //优化rxJavaLogin2方法内部
                HoolaiServiceCreater.rxJavaLogin2(new ProgressSubscriber<User>(this, new SubscriberOnNextListener<User>() {
                    @Override
                    public void onNext(User user) {
                        Toast.makeText(MainActivity.this, "type=" + type + " " + new Gson().toJson(user), Toast.LENGTH_SHORT).show();
                    }
                }), "1", 1, "456oooppp");
                /**
                 * Test取消Http请求
                 */
//                new ProgressObservable<Object>(this, null).onCancelProgress();
                break;
            case 3:
                HoolaiServiceCreater.rxJavaLogin3(new ProgressObservable<User>(this, new ObservableOnNextListener<User>() {
                    @Override
                    public void onNext(User user) {
                        Toast.makeText(MainActivity.this, "type=" + type + " " + new Gson().toJson(user), Toast.LENGTH_SHORT).show();
                    }
                }), "1", 1, "456oooppp");
                break;
            case 4:
                Consumer<HoolaiResponse<User>> nextConsumer = new Consumer<HoolaiResponse<User>>() {
                    @Override
                    public void accept(HoolaiResponse<User> userHoolaiResponse) throws Exception {
                        User user = userHoolaiResponse.getValue();
                        Toast.makeText(MainActivity.this, "type=" + type + " " + new Gson().toJson(user), Toast.LENGTH_SHORT).show();
                    }
                };
                Consumer<Throwable> errorConsumer = new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this, "type=" + type + " Error", Toast.LENGTH_SHORT).show();
                    }
                };
                Action complateAction = new Action() {
                    @Override
                    public void run() throws Exception {
                        Toast.makeText(MainActivity.this, "type=" + type + " Complate", Toast.LENGTH_SHORT).show();
                    }
                };
                observable.subscribe(nextConsumer, errorConsumer, complateAction);
                break;
        }
    }
}
