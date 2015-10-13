package io.github.izzyleung.zhihudailypurify.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import io.github.izzyleung.zhihudailypurify.R;
import io.github.izzyleung.zhihudailypurify.bean.SplashPicBean;
import io.github.izzyleung.zhihudailypurify.task.GetSplashPicTask;

public class SplashActivity extends BaseActivity implements GetSplashPicTask.UpdateUIListener{

    public static final String[] RESOLUTIONS = {"320*432", "480*728", "720*1184", "1080*1776"};
    public static final int[] RESOLUTIONS_WIDTH = {320, 480, 720, 1080};
    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int START_MAIN_EVENT = 100;
    private static final int START_TIMEOUT = 3000;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.default_splash)
            .showImageOnFail(R.drawable.default_splash)
            .showImageForEmptyUri(R.drawable.default_splash)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();
    private ImageLoadingListener myImageLoadingListener = new MyImageLoadingListener();
    private ImageView ivSplash;
    private TextView tv_author;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == START_MAIN_EVENT){
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this.getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.activity_splash);
        ViewGroup container = (ViewGroup) findViewById(android.R.id.content);
        container.setClickable(true);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContainerView();
            }
        });
        //get splash pic
        new GetSplashPicTask(getResolution(), this).execute();
        ivSplash = (ImageView) findViewById(R.id.iv_splash);
        tv_author = (TextView) findViewById(R.id.tv_author);
        handler.sendEmptyMessageDelayed(START_MAIN_EVENT, START_TIMEOUT);
    }

    private String getResolution() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int index = 0;
        if (width <= RESOLUTIONS_WIDTH[0]) {
            index = 0;
        } else if(width <= RESOLUTIONS_WIDTH[1]){
            index = 1;
        } else if(width <= RESOLUTIONS_WIDTH[2]){
            index = 2;
        } else{
            index = 3;  // all of the others used high resolution
        }
        return RESOLUTIONS[index];
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean isSystemUiHidden() {
        return ((getWindow().getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) != 0);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void onClickContainerView() {
        if (isSystemUiHidden()) {
            showSystemUI();
        } else {
            hideSystemUI();
        }
    }

    @Override
    public void beforeTaskStart() {

    }

    @Override
    public void afterTaskFinished(SplashPicBean splashPicBean) {
        if (splashPicBean != null){
            tv_author.setText(splashPicBean.getTitle());
            imageLoader.displayImage(splashPicBean.getImage(), ivSplash, options, myImageLoadingListener);
        }
    }

    class MyImageLoadingListener extends SimpleImageLoadingListener{

        @Override
        public void onLoadingStarted(String imageUri, View view) {
            super.onLoadingStarted(imageUri, view);
            Log.d(TAG, "onLoadingStarted");
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            super.onLoadingFailed(imageUri, view, failReason);
            Log.d(TAG, "onLoadingFailed");
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            super.onLoadingComplete(imageUri, view, loadedImage);
            Log.d(TAG, "onLoadingComplete");
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {
            super.onLoadingCancelled(imageUri, view);
            Log.d(TAG, "onLoadingCancelled");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
