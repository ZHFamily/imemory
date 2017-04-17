package club.imemory.app.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.tauth.Tencent;

import org.litepal.crud.DataSupport;

import club.imemory.app.R;
import club.imemory.app.db.User;
import club.imemory.app.listener.LoginListener;
import club.imemory.app.util.AppManager;
import club.imemory.app.util.RegexUtils;

/**
 * 实现手机号与密码登录
 */
public class LoginActivity extends BaseActivity {

    /**
     * 启动LoginActivity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    private EditText mPhoneTV;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Tencent mTencent; //qq主操作对象
    private LoginListener mLoginListener; //授权登录监听器

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
        Button mQQLoginBtn = (Button) findViewById(R.id.btn_QQ);
        Button mWeiBoLoginBtn = (Button) findViewById(R.id.btn_weibo);
        Button mRegisterBtn = (Button) findViewById(R.id.btn_register);
        Button mForgetBtn = (Button) findViewById(R.id.btn_forget);
        mPhoneTV = (EditText) findViewById(R.id.tv_phone);
        mPasswordView = (EditText) findViewById(R.id.tv_password);
        findViewById(R.id.btn_login).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                validateLogin();
            }
        });

        //响应软件盘上的事件
        mPhoneTV.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (RegexUtils.isMobileExact(v.getText().toString().trim())) {
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
                validateLogin();
                return false;
            }
        });

        mRegisterBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.actionStart(LoginActivity.this);
            }
        });

        mForgetBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.actionStart(LoginActivity.this);
                AppManager.showToast("忘记密码就重新注册一个吧");
            }
        });

        mQQLoginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                qqLogin();
            }
        });

        mWeiBoLoginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.showToast("该功能没实现,试试QQ登录");
            }
        });
    }

    private void qqLogin() {
        mTencent = Tencent.createInstance("1106090620", LoginActivity.this);
        mLoginListener = new LoginListener(mTencent, mHandler);
        //调用QQ登录，用IUiListener对象作参数
        if (!mTencent.isSessionValid()) {
            mTencent.login(LoginActivity.this, "all", mLoginListener);
        }
    }

    /**
     * QQ登录成功后跳转到主界面
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                AppManager.showToast("QQ授权成功");
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("QQ", "QQ");
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResultData(requestCode, resultCode, data, mLoginListener);
    }

    /**
     * 尝试登录，对数据进行验证
     */
    private void validateLogin() {
        // 重置错误
        mPhoneTV.setError(null);
        mPasswordView.setError(null);

        String phone = mPhoneTV.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

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
            showProgress(true); // 显示进度条
            new UserLoginTask().execute(phone, password);
        }
    }

    public class UserLoginTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return 0;
            }
            User user = DataSupport.findLast(User.class);
            if (user == null) {
                return 0;
            } else if (params[0].equals(user.getPhone()) && params[1].equals(user.getPassword())) {
                return 1;
            } else {
                return 2;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            showProgress(false);

            switch (result) {
                case 0:
                    AppManager.showToast("账号不存在请先注册");
                    break;
                case 1:
                    AppManager.showToast("登录成功");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 2:
                    AppManager.showToast("账号或密码错误");
                    mPasswordView.setError("");
                    mPhoneTV.setError("");
                    mPhoneTV.requestFocus();
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }

    /**
     * 显示进度UI和隐藏登录表单
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
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
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}