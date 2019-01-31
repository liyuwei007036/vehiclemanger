package core;

import android.app.Application;
import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.model.UpdateInfo;
import com.tc5u.vehiclemanger.utils.CheckUpdateUtil;
import com.tc5u.vehiclemanger.utils.ObjectUtil;
import com.tc5u.vehiclemanger.utils.OkHttpUtils;

import org.litepal.LitePal;

public class BaseApplication extends Application {

    static {//static 代码段可以防止内存泄露
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.eee, android.R.color.black);//全局设置主题颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                ClassicsFooter.REFRESH_FOOTER_ALLLOADED = "我也是有底线的";
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }

    private String session;

    private static BaseApplication instance;

    private static Context context;

    public String getCookies() {
        return session;
    }

    public static BaseApplication getInstance() {
        if (instance == null) {
            synchronized (OkHttpUtils.class) {
                if (instance == null) {
                    instance = new BaseApplication();
                }
            }
        }
        return instance;
    }

    public void setCookies(String session) {
        this.session = session;
    }

    public Context getContext() {
        return context;
    }

    public Long user_id;

    public String user_name;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
        initUpdate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory();
        }
        Glide.get(this).trimMemory(level);
    }

    private void initUpdate() {
        OkHttpUtils.getInstance().PostWithJson("/updateForAPP", "", new OkHttpUtils.ResultCallBackListener() {
            @Override
            public void noNetWork() {

            }

            @Override
            public void onLoadingShow() {

            }

            @Override
            public void onLoadingDismiss() {

            }

            @Override
            public void onSuccess(String url, String result_str) {
                JSONObject object = JSONObject.parseObject(result_str);
                UpdateInfo updateInfo = new UpdateInfo();
                updateInfo.setVersionName(object.getString("update_ver_name"));
                updateInfo.setVersionCode(object.getInteger("update_ver_code"));
                updateInfo.setIgnore(ObjectUtil.getBoolean(object.getBoolean("forced")));
                updateInfo.setForced(ObjectUtil.getBoolean(object.getBoolean("forced")));
                updateInfo.setMd5(ObjectUtil.getString(object.getString("md5")));
                updateInfo.setUpdateContent(object.getString("update_content"));
                updateInfo.setUpdateUrl(OkHttpUtils.IMG_URL + object.getString("update_url"));
                CheckUpdateUtil.getInstance().init(updateInfo);
            }

            @Override
            public void onFailure(String url, Exception e) {

            }

            @Override
            public void onError(String url, String result_str) {

            }
        });
    }

}
