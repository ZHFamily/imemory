package club.imemory.app.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import club.imemory.app.listener.HttpCallbackListener;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @Author: 张杭
 * @Date: 2017/3/23 18:41
 */

public class HttpManager {

    //OkHttpClient为重量级的
    private static OkHttpClient client = new OkHttpClient();
    public static final String WEATHER_INFO_URL = "http://guolin.tech/api/weather?cityid=";
    public static final String WEATHER_INFO_KEY = "&key=bc0418b57b2d4918819d3974ac1285d9";
    public static final String GET_BingPic = "http://guolin.tech/api/bing_pic";
    
    /**
     * 使用HttpURLConnection的GET方式发送HTTP请求
     */
    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * 使用OKHttp发送HTTP请求，获取服务器数据(默认get方式)
     */
    public static void sendOKHttpRequest(String address, Callback callback) {
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 向服务器提交数据
     */
    public static void submitOKHttp(String address, RequestBody requestBody, Callback callback) {
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
