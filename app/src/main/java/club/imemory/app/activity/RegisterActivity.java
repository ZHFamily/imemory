package club.imemory.app.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;

import org.litepal.crud.DataSupport;

import java.util.Date;

import club.imemory.app.R;
import club.imemory.app.db.User;
import club.imemory.app.util.AppManager;
import club.imemory.app.util.RegexUtils;
import club.imemory.app.util.SnackbarUtil;

/**
 * @Author: 张杭
 * @Date: 2017/4/10 12:08
 */

public class RegisterActivity extends BaseActivity {

    /**
     * 启动RegisterActivity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());
    }

    private CoordinatorLayout coordinator;
    private TextInputLayout mPhoneText;
    private TextInputLayout mPasswordText;
    private TextInputLayout mNameText;
    private Button mRegisterBtn;
    private ProgressDialog progressDialog;
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        coordinator = (CoordinatorLayout) findViewById(R.id.coordinator);
        mRegisterBtn = (Button) findViewById(R.id.btn_register);
        mPhoneText = (TextInputLayout) findViewById(R.id.text_input_layout_phone);
        mPasswordText = (TextInputLayout) findViewById(R.id.text_input_layout_password);
        mNameText = (TextInputLayout) findViewById(R.id.text_input_layout_name);
        ImageView headImage = (ImageView) findViewById(R.id.image_head);
        mPhoneText.setHint("手机号码");
        mPasswordText.setHint("密码");
        mNameText.setHint("昵称");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String userInfo = prefs.getString("userInfoJson", null);
        if (userInfo != null) {
            JSONObject userInfoJson = JSON.parseObject(userInfo);
            user.setName(userInfoJson.getString("nickname"));
            user.setHead(userInfoJson.getString("figureurl_qq_2"));
            user.setSex(userInfoJson.getString("gender"));
            user.setAddress(userInfoJson.getString("province") + userInfoJson.getString("city"));
            mNameText.getEditText().setText(user.getName());
            Glide.with(this).load(user.getHead()).into(headImage);
        }

        // 焦点处理
        mPhoneText.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (RegexUtils.isMobileExact(((TextView) v).getText().toString().trim())) {
                        mPhoneText.setError(null);
                    } else {
                        SnackbarUtil.ShortSnackbar(coordinator, "手机号码不正确", SnackbarUtil.Alert).show();
                        mPhoneText.setError("手机号码不正确");
                    }
                }
            }
        });
        mPasswordText.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (((TextView) v).getText().toString().trim().length() >= 6) {
                        mPasswordText.setError(null);
                    } else {
                        SnackbarUtil.ShortSnackbar(coordinator, "密码长度至少6位", SnackbarUtil.Alert).show();
                        mPasswordText.setError("密码长度至少6位");
                    }
                }
            }
        });

        //响应软件盘上的事件
        mPhoneText.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (RegexUtils.isMobileExact(v.getText().toString().trim())) {
                    mPhoneText.setError(null);
                    return false;
                } else {
                    SnackbarUtil.ShortSnackbar(coordinator, "手机号码不正确", SnackbarUtil.Alert).show();
                    mPhoneText.setError("手机号码不正确");
                    return true;
                }
            }
        });
        mPasswordText.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (v.getText().toString().trim().length() >= 6) {
                    mPasswordText.setError(null);
                    return false;
                } else {
                    SnackbarUtil.ShortSnackbar(coordinator, "密码长度至少6位", SnackbarUtil.Alert).show();
                    mPasswordText.setError("密码长度至少6位");
                    return true;
                }
            }
        });
        mNameText.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                validateRegister();
                return false;
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateRegister();
            }
        });
    }

    /**
     * 对数据进行验证
     */
    private void validateRegister() {
        // 重置错误
        mNameText.setError(null);
        mPhoneText.setError(null);
        mPasswordText.setError(null);

        String name = mNameText.getEditText().getText().toString().trim();
        String phone = mPhoneText.getEditText().getText().toString().trim();
        String password = mPasswordText.getEditText().getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name)) {
            mNameText.setError("昵称不能为空");
            focusView = mNameText;
            cancel = true;
        }

        //验证密码
        if (TextUtils.isEmpty(password) && password.length() < 6) {
            mPasswordText.setError("密码错误");
            focusView = mPasswordText;
            cancel = true;
        }

        // 验证手机号
        if (TextUtils.isEmpty(phone)) {
            mPhoneText.setError("手机号不能为空");
            focusView = mPhoneText;
            cancel = true;
        } else if (!RegexUtils.isMobileExact(phone)) {
            mPhoneText.setError("手机号不正确");
            focusView = mPhoneText;
            cancel = true;
        }

        if (cancel) {
            //有错误，停止登录
            focusView.requestFocus();
        } else {
            showProgressDialog(); // 显示进度条
            new UserRegisterTask().execute(name, phone, password);
        }
    }

    public class UserRegisterTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return false;
            }
            user.setName(params[0]);
            user.setPhone(params[1]);
            user.setPassword(params[2]);
            user.setLogintime(new Date());
            user.setCreatetime(new Date());
            return user.save();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            closeProgressDialog();
            if (result) {
                AppManager.showToast("注册成功");
                MainActivity.actionStart(RegisterActivity.this);
            } else {
                SnackbarUtil.ShortSnackbar(coordinator, "未知错误，请清除数据后重试", SnackbarUtil.Alert).show();
            }
        }

        @Override
        protected void onCancelled() {
            closeProgressDialog();
        }
    }

    /**
     * 弹框确认是否退出注册
     */
    private void isDialog(String result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage(result); //设置内容
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataSupport.deleteAll(User.class);
                LoginActivity.actionStart(RegisterActivity.this);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
