package club.imemory.app.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.litepal.crud.DataSupport;

import java.util.Date;

import club.imemory.app.R;
import club.imemory.app.db.User;
import club.imemory.app.util.AppManager;
import club.imemory.app.util.RegexUtils;

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
        context.startActivity(intent);
    }

    private MaterialEditText mPhoneTV;
    private MaterialEditText mPasswordTv;
    private MaterialEditText mNameTv;
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
        mRegisterBtn = (Button) findViewById(R.id.btn_register);
        mPhoneTV = (MaterialEditText) findViewById(R.id.tv_phone);
        mPasswordTv = (MaterialEditText) findViewById(R.id.tv_password);
        mNameTv = (MaterialEditText) findViewById(R.id.tv_name);
        ImageView headImage = (ImageView) findViewById(R.id.image_head);

        Intent intent = getIntent();
        if(intent.getStringExtra("QQ")!=null&&intent.getStringExtra("QQ").equals("QQ")){
            user = DataSupport.findLast(User.class);
            mNameTv.setText(user.getName());
            Glide.with(this).load(user.getHead()).into(headImage);
        }

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
        mPasswordTv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (v.getText().toString().trim().length()>=6) {
                    return false;
                } else {
                    v.setError("密码长度至少6位");
                    return true;
                }
            }
        });
        mNameTv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
        mNameTv.setError(null);
        mPhoneTV.setError(null);
        mPasswordTv.setError(null);

        String name = mNameTv.getText().toString().trim();
        String phone = mPhoneTV.getText().toString().trim();
        String password = mPasswordTv.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name)){
            mNameTv.setError("昵称不能为空");
            focusView = mNameTv;
            cancel = true;
        }

        //验证密码
        if (TextUtils.isEmpty(password) && password.length() < 6) {
            mPasswordTv.setError("密码错误");
            focusView = mPasswordTv;
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
            showProgressDialog(); // 显示进度条
            new UserRegisterTask().execute(name,phone,password);
        }
    }

    public class UserRegisterTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Thread.sleep(2000);
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
            if (result){
                AppManager.showToast("注册成功");
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }else{
                AppManager.showToast("未知错误，请清除数据后重试");
            }
        }

        @Override
        protected void onCancelled() {
            closeProgressDialog();
        }
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
