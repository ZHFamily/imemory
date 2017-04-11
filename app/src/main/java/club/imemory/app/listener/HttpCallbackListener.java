package club.imemory.app.listener;

/**
 * @Author: 张杭
 * @Date: 2017/3/18 15:59
 */

public interface HttpCallbackListener {

    /**
     * 服务器成功响应
     *
     * @param response
     */
    void onFinish(String response);

    /**
     * 出现错误
     *
     * @param e
     */
    void onError(Exception e);

}
