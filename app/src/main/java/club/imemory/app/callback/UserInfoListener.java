package club.imemory.app.callback;

import android.os.Message;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import club.imemory.app.util.AppManager;

/**
 * @Author: 张杭
 * @Date: 2017/3/25 23:01
 */

public class UserInfoListener implements IUiListener {

    @Override
    public void onComplete(final Object o) {
        JSONObject userInfoJson = (JSONObject) o;
        try {
            String nickname = userInfoJson.getString("nickname");//直接传递一个昵称的内容过去
            AppManager.logI("UserInfoListener",nickname);
            AppManager.logI("UserInfoListener",userInfoJson.toJSONString());
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
