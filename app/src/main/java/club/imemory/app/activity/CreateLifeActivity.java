package club.imemory.app.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import club.imemory.app.R;
import club.imemory.app.adapter.AddPhotoAdapter;
import club.imemory.app.db.Life;
import club.imemory.app.util.AppManager;
import club.imemory.app.util.SnackbarUtil;

/**
 * @Author: 张杭
 * @Date: 2017/4/11 23:04
 */

public class CreateLifeActivity extends BaseActivity {

    /**
     * 启动CreateLifeActivity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, CreateLifeActivity.class);
        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());
    }

    private static final int CHOOSE_PHOTO = 2;
    private List<String> mList = new ArrayList<>();
    private List<String> temp = new ArrayList<>();
    private AddPhotoAdapter adapter;
    private CoordinatorLayout coordinator;
    private TextInputLayout mTitleText;
    private TextView mContentText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_life);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        coordinator = (CoordinatorLayout) findViewById(R.id.coordinator);
        mTitleText = (TextInputLayout) findViewById(R.id.text_input_layout_title);
        mContentText = (TextView) findViewById(R.id.add_content);

        initData();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AddPhotoAdapter(mList);
        recyclerView.setAdapter(adapter);

        getSendData();
    }

    private void initData() {
        mList.clear();
        mList.add(String.valueOf(R.drawable.ic_add));
        temp.add(String.valueOf(R.drawable.ic_add));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_create:
                mTitleText.setError(null);
                saveData();
                break;
        }
        return true;
    }

    private void saveData() {
        String title = mTitleText.getEditText().getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            SnackbarUtil.ShortSnackbar(coordinator, "记录标题不能为空", SnackbarUtil.Alert).show();
            mTitleText.setError("不能为空");
        } else {
            Life life = new Life();
            life.setTitle(title);
            life.setSubhead(mContentText.getText().toString().trim());
            life.setAvatar(mList.get(0));
            StringBuffer pathBuffer = new StringBuffer();
            for (String path : mList) {
                pathBuffer.append(path);
                pathBuffer.append("#cppy#");
            }
            life.setPhoto(pathBuffer.toString());
            life.setLocation("武汉");
            life.setCreatetime(new Date());
            if (life.save()) {
                MainActivity.actionStart(this);
                AppManager.showToast("保存成功");
            } else {
                SnackbarUtil.ShortSnackbar(coordinator, "保存失败", SnackbarUtil.Alert).show();
            }
        }
    }

    private void getSendData() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {//文字
                final String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContentText.setText(sharedText);
                        }
                    });
                }
            } else if (type.startsWith("image/")) {//单张图片和文字
                final String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContentText.setText(sharedText);
                        }
                    });
                }
                Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                temp.add(UriToFilePath(imageUri));
                addPhoto();
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {//多张图片
            if (type.startsWith("image/")) {
                ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
                for (Uri uri : imageUris) {
                    temp.add(UriToFilePath(uri));
                }
                addPhoto();
            }
        }
    }

    private void addPhoto() {
        mList.clear();
        for (int i = temp.size() - 1; i >= 0; i--) {
            mList.add(temp.get(i));
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void openAlbum() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("image/*");
            startActivityForResult(intent, CHOOSE_PHOTO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openAlbum();
            } else {
                Snackbar snackbar = Snackbar.make(coordinator, "需要授权才能继续操作", Snackbar.LENGTH_LONG);
                snackbar.setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openAlbum();
                    }
                }).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                temp.add(UriToFilePath(uri));
                addPhoto();
            }
        }
    }

    /**
     * url转为FilePath
     *
     * @param imageUri Uri
     * @return 返回最终文件的实际路径
     */
    private String UriToFilePath(Uri imageUri) {
        if (imageUri != null) {
            String imagePath = imageUri.getPath();
            if (ContentResolver.SCHEME_CONTENT.equals(imageUri.getScheme())) {
                imagePath = getStringPathFromURI(this, imageUri);
            }
            return imagePath;
        }
        return null;
    }

    /**
     * 从ContentResolver 中或得到文件的实际地址
     *
     * @param context    上下文
     * @param contentUri Uri
     * @return 返回最终文件的实际路径
     */
    public String getStringPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] project = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, project, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }
}
