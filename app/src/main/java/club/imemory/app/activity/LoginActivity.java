package club.imemory.app.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.io.IOException;

import club.imemory.app.R;
import club.imemory.app.callback.LoginListener;
import club.imemory.app.http.HttpManager;
import club.imemory.app.json.JsonAnalyze;
import club.imemory.app.util.AppManager;
import club.imemory.app.util.ApplicationUtil;
import club.imemory.app.util.RegexUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import static club.imemory.app.http.HttpManager.LOGIN;
import static club.imemory.app.util.AppManager.APP_ID;

/**
 * 实现手机号与密码登录
 */
public class LoginActivity extends BaseActivity {

    /**
     * 启动LoginActivity
     */
    public static void actionStart(Context context, String... strings) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    private AutoCompleteTextView mPhoneTV;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mQQloginBtn;
    private Button mForgetBtn;
    //QQ登录
    private Tencent mTencent; //qq主操作对象
    private IUiListener mLoginListener; //授权登录监听器
    private IUiListener mUserInfoListener; //获取用户信息监听器
    private UserInfo mUserInfo; //qq用户信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mQQloginBtn = (Button) findViewById(R.id.btn_QQ);
        mForgetBtn = (Button) findViewById(R.id.btn_forget);
        mPhoneTV = (AutoCompleteTextView) findViewById(R.id.tv_phone);
        mPasswordView = (EditText) findViewById(R.id.tv_password);
        findViewById(R.id.btn_login).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        //响应软件盘上的事件
        mPhoneTV.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (RegexUtils.isMobileExact(v.getText().toString())) {
                    return false;
                } else {
                    AppManager.showToast("手机号码不正确");
                    v.setError("手机号码不正确");
                    return true;
                }
            }
        });
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                attemptLogin();
                return false;
            }
        });

        /**
         * QQ登录
         */
        mQQloginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTencent = Tencent.createInstance(APP_ID, ApplicationUtil.getContext());
                mLoginListener = new LoginListener(mTencent);
                //调用QQ登录，用IUiListener对象作参数
                if (!mTencent.isSessionValid()) {
                    mTencent.login(LoginActivity.this, "all", mLoginListener);
                } else {
                    mTencent.logout(LoginActivity.this);
                    AppManager.showToast("已注销");
                }
            }
        });
    }
    //显示获取到的头像和昵称
/*    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Find msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {//获取昵称
                tvNickName.setText((CharSequence) msg.obj);
            } else if (msg.what == 1) {//获取头像
                headerLogo.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 尝试登录，对数据进行验证
     */
    private void attemptLogin() {
        // 重置错误
        mPhoneTV.setError(null);
        mPasswordView.setError(null);

        String phone = mPhoneTV.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //验证密码
        if (TextUtils.isEmpty(password) && password.length() < 6) {
            mPasswordView.setError("密码错误");
            focusView = mPasswordView;
            cancel = true;
        }

        // 验证手机号
        if (TextUtils.isEmpty(phone)) {
            mPhoneTV.setError("手机号不能为空");
            focusView = mPhoneTV;
            cancel = true;
        } else if (!RegexUtils.isMobileExact(phone)) {
            mPhoneTV.setError("手机号不正确");
            focusView = mPhoneTV;
            cancel = true;
        }

        if (cancel) {
            //有错误，停止登录
            focusView.requestFocus();
        } else {
            // 显示进度条，记录登录状态
            showProgress(true);
            UserLogin(phone, password);
        }
    }

    private void UserLogin(String phone, String password) {
        FormBody formBody = new FormBody.Builder()
                .add("phone", phone)
                .add("password", password)
                .build();
        HttpManager.submitOKHttp(LOGIN, formBody, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String userJSON = response.body().string();
                AppManager.logI("LoginActivity",userJSON);
                if (JsonAnalyze.handleUserResponse(userJSON)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress(false);
                            AppManager.showToast("登录成功");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgress(false);
                        AppManager.showToast("登录失败");
                    }
                });
            }
        });
    }

    /**
     * 显示进度UI和隐藏登录表单
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}