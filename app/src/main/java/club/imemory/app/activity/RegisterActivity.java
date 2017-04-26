package club.imemory.app.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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
    public static void actionStart(Context context, Pair<View, String>... sharedElements) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context, sharedElements).toBundle());
    }

    private CoordinatorLayout coordinator;
    private TextInputLayout mPhoneText;
    private TextInputLayout mPasswordText;
    private TextInputLayout mNameText;
    private ProgressDialog progressDialog;
    private User user = new User();
    private String vCode;
    private String name;
    private String phone;
    private String password;

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
        Button mRegisterBtn = (Button) findViewById(R.id.btn_register);
        mPhoneText = (TextInputLayout) findViewById(R.id.text_input_layout_phone);
        mPasswordText = (TextInputLayout) findViewById(R.id.text_input_layout_password);
        mNameText = (TextInputLayout) findViewById(R.id.text_input_layout_name);
        ImageView headImage = (ImageView) findViewById(R.id.image_head);
        mPhoneText.setHint("手机号码");
        mPasswordText.setHint("密码");
        mNameText.setHint("昵称");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String nickname = prefs.getString("nickname", null);
        String figureUrlQQ = prefs.getString("figureUrlQQ", null);
        String gender = prefs.getString("gender", null);
        String area = prefs.getString("area", null);
        if (nickname != null || figureUrlQQ != null || gender != null || area != null) {
            user.setName(nickname);
            user.setHead(figureUrlQQ);
            user.setSex(gender);
            user.setAddress(area);
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
                        mPhoneText.setErrorEnabled(false);
                    } else {
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
                        mPasswordText.setErrorEnabled(false);
                    } else {
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
                    mPhoneText.setErrorEnabled(false);
                    return false;
                } else {
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
                    mPasswordText.setErrorEnabled(false);
                    return false;
                } else {
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
        mNameText.setErrorEnabled(false);
        mPhoneText.setErrorEnabled(false);
        mPasswordText.setErrorEnabled(false);

        name = mNameText.getEditText().getText().toString().trim();
        phone = mPhoneText.getEditText().getText().toString().trim();
        password = mPasswordText.getEditText().getText().toString().trim();

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
            showVCodeDialog();
        }
    }

    private class UserRegisterTask extends AsyncTask<String, Integer, Boolean> {

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
                finishAfterTransition();
            } else {
                SnackbarUtil.ShortSnackbar(coordinator, "未知错误，请清除数据后重试", SnackbarUtil.Alert).show();
            }
        }

        @Override
        protected void onCancelled() {
            closeProgressDialog();
        }
    }

    private void sendVCode() {
        vCode = String.valueOf((int) (Math.random() * 9999 + 1000));
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, RegisterActivity.class), 0);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.logo_round)
                .setContentTitle("【iMemory】验证码为:"+vCode)
                .setContentIntent(contentIntent)
                .build();
        mNotifyMgr.notify(1, notification);
    }

    /**
     * 弹框验证码
     */
    private void showVCodeDialog() {
        sendVCode();
        LayoutInflater inflater = getLayoutInflater();
        View dialog = inflater.inflate(R.layout.dialog_verify_code, (ViewGroup) findViewById(R.id.vcode_dialog));
        final TextView oneText = (TextView) dialog.findViewById(R.id.tv_one);
        final TextView twoText = (TextView) dialog.findViewById(R.id.tv_two);
        final TextView threeText = (TextView) dialog.findViewById(R.id.tv_three);
        final TextView fourText = (TextView) dialog.findViewById(R.id.tv_four);
        EditText vcodeEdit = (EditText) dialog.findViewById(R.id.edit_vcode);
        vcodeEdit.setCursorVisible(false);//将光标隐藏
        vcodeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String code = s.toString().trim();
                switch (start) {
                    case 0:
                        oneText.setText(count == 1 ? code : "");
                        break;
                    case 1:
                        twoText.setText(count == 1 ? code.substring(1) : "");
                        break;
                    case 2:
                        threeText.setText(count == 1 ? code.substring(2) : "");
                        break;
                    case 3:
                        fourText.setText(count == 1 ? code.substring(3) : "");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("请输入验证码"); //设置标题
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String code = oneText.getText().toString() + twoText.getText().toString() + threeText.getText().toString() + fourText.getText().toString();
                if (!vCode.equals(code)) {
                    AppManager.showToast("验证码输入错误，注册失败");
                } else {
                    showProgressDialog(); // 显示进度条
                    new UserRegisterTask().execute(name, phone, password);
                }
            }
        });
        builder.setCancelable(false);
        builder.setView(dialog);
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
