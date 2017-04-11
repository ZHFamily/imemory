package club.imemory.app.util;

import android.content.Context;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

/**
 * 管理程序内一些全局的状态信息
 *
 * @Author: 张杭
 * @Date: 2017/3/18 17:47
 */

public class ApplicationUtil extends LitePalApplication {

    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        LitePal.initialize(context);
        //在这里为应用设置异常处理程序，将捕获的异常信息保存到文件
        //CrashHandler crashHandler = CrashHandler.getInstance();
        //crashHandler.init();
    }

    public static Context getContext() {
        return context;
    }
}
