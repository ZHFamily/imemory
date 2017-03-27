package club.imemory.app.callback;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import club.imemory.app.util.AppManager;
import club.imemory.app.util.ApplicationUtil;

/**
 * @Author: 张杭
 * @Date: 2017/3/25 20:13
 */

public class LoginListener implements IUiListener {

    private Tencent mTencent;

    public LoginListener(Tencent tencent) {
        mTencent = tencent;
    }

    @Override
    public void onComplete(Object value) {
        AppManager.showToast("QQ登录授权成功");
        JSONObject json = (JSONObject) value;
        AppManager.logI("LoginActivity", json.toJSONString());
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
}
