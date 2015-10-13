package io.github.izzyleung.zhihudailypurify.task;

import android.util.Log;

import org.json.JSONObject;

import io.github.izzyleung.zhihudailypurify.bean.SplashPicBean;
import io.github.izzyleung.zhihudailypurify.support.Constants;
import io.github.izzyleung.zhihudailypurify.support.lib.Http;

/**
 * Created by kylin on 15-10-12.
 */
public class GetSplashPicTask extends BaseDownloadTask<Void, Void, SplashPicBean>{

    protected boolean isRefreshSuccess = true;
    protected String resolution;
    private UpdateUIListener mListener;
    private static  final String TAG = GetSplashPicTask.class.getSimpleName();
    public GetSplashPicTask(String resolution ,UpdateUIListener callback){
        this.resolution = resolution;
        this.mListener = callback;
    }

    //before doInBackground
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mListener.beforeTaskStart();
    }

    @Override
    protected SplashPicBean doInBackground(Void... params) {
        SplashPicBean splashPicBean = null;
        try {
            JSONObject contents = new JSONObject(Http.get(Constants.Url.ZHIHU_DAILY_SPLASH_PIC, resolution));
            Log.d(TAG, contents.toString());
            if (contents != null){
                splashPicBean = new SplashPicBean(contents.getString("text"), contents.getString("img"));
            }else{
                // should load last one pic and text
            }

        }catch (Exception e){
            isRefreshSuccess = false;
            e.printStackTrace();
        }
        return splashPicBean;
    }

    //after doInBackground
    @Override
    protected void onPostExecute(SplashPicBean splashPicBean) {
        super.onPostExecute(splashPicBean);
        mListener.afterTaskFinished(splashPicBean);
    }

    public interface UpdateUIListener {
        void beforeTaskStart();

        void afterTaskFinished(SplashPicBean splashPicBean);
    }
}
