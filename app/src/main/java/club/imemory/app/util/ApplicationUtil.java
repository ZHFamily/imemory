package club.imemory.app.util;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * 管理程序内一些全局的状态信息
 * @Author: 张杭
 * @Date: 2017/3/18 17:47
 */

public class ApplicationUtil extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        LitePal.initialize(context);
        //在这里为应用设置异常处理程序，然后我们的程序才能捕获未处理的异常
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init();
    }

    public static Context getContext(){
        return context;
    }
}
