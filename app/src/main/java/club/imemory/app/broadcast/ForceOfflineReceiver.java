package club.imemory.app.broadcast;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import club.imemory.app.activity.LoginActivity;
import club.imemory.app.util.AppManager;

/**
 * 强制下线广播接收器
 *
 * @Author: 张杭
 * @Date: 2017/3/12 16:39
 */

public class ForceOfflineReceiver extends BroadcastReceiver {

    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("下线通知");
        dialog.setMessage("你的账号在其他地方登录，如非本人操作，密码可能已泄露，建议立即修改密码。");
        dialog.setCancelable(false);//返回键无效
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppManager.finishAll();//销毁所有活动
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);//重新启动登录界面
            }
        });
        dialog.show();
    }
}
