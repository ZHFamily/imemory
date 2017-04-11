package club.imemory.app.listener;

import android.os.Handler;
import android.os.Message;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import club.imemory.app.db.User;
import club.imemory.app.util.AppManager;
import club.imemory.app.util.ApplicationUtil;

/**
 * @Author: 张杭
 * @Date: 2017/3/25 20:13
 */

public class LoginListener implements IUiListener {

    private Tencent mTencent;
    private Handler mHandler;

    public LoginListener(Tencent tencent, Handler handler) {
        mTencent = tencent;
        mHandler = handler;
    }

    @Override
    public void onComplete(Object object) {
        JSONObject json = (JSONObject) object;
        AppManager.logI("LoginActivity", json.toString());
        //设置openid和token
        try {
            mTencent.setAccessToken(json.getString("access_token"), json.getString("expires_in"));
            mTencent.setOpenId(json.getString("openid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //sdk给我们提供了一个类UserInfo，这个类中封装了QQ用户的一些信息，我么可以通过这个类拿到这些信息
        QQToken mQQToken = mTencent.getQQToken();
        UserInfo userInfo = new UserInfo(ApplicationUtil.getContext(), mQQToken);
        userInfo.getUserInfo(new UserInfoListener());
    }

    @Override
    public void onError(UiError arg0) {
        AppManager.showToast("QQ登录授权失败");
    }

    @Override
    public void onCancel() {
        AppManager.showToast("QQ登录授权取消");
    }

    private class UserInfoListener implements IUiListener {

        @Override
        public void onComplete(Object object) {
            JSONObject userInfoJson = (JSONObject) object;
            AppManager.logI("LoginActivity", userInfoJson.toString());
            try {
                User user = new User();
                user.setName(userInfoJson.getString("nickname"));
                user.setHead(userInfoJson.getString("figureurl_qq_2"));
                user.setSex(userInfoJson.getString("gender"));
                user.setAddress(userInfoJson.getString("province") + userInfoJson.getString("city"));
                user.setLogintime(new Date());
                user.setCreatetime(new Date());
                user.save();
                Message msg = new Message();
                msg.what = 0;//QQ登录成功
                mHandler.sendMessage(msg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            AppManager.showToast("获取qq用户信息错误");
        }

        @Override
        public void onCancel() {
            AppManager.showToast("获取qq用户信息取消");
        }
    }
}
