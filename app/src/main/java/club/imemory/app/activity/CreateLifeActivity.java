package club.imemory.app.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import club.imemory.app.R;
import club.imemory.app.adapter.PhotoAdapter;
import club.imemory.app.db.Life;
import club.imemory.app.util.SnackbarUtil;

/**
 * @Author: 张杭
 * @Date: 2017/4/11 23:04
 */

public class CreateLifeActivity extends BaseActivity {

    private List<String> mList = new ArrayList<>();
    private PhotoAdapter adapter;
    private CoordinatorLayout coordinator;
    private TextInputLayout mTitleText;

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

        initData();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PhotoAdapter(mList);
        recyclerView.setAdapter(adapter);

        getSendData();
    }

    private void initData() {
        mList.clear();
        for (int i = 1; i < 10; i++) {
            mList.add("http://imemory.club/imemory/image/" + i + ".jpg");
        }
        for (int i = 1; i < 10; i++) {
            mList.add("http://imemory.club/imemory/image/" + i + ".jpg");
        }
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
            life.setAvatar(mList.get(0));
            StringBuffer pathBuffer = new StringBuffer();
            for (String path : mList){
                pathBuffer.append(path);
                pathBuffer.append("|*imemory#cppy|");
            }
            life.setPhoto(pathBuffer.toString());
            life.setLocation("武汉");
            life.setCreatetime(new Date());
            if(life.save()) {
                SnackbarUtil.ShortSnackbar(coordinator, "保存成功", SnackbarUtil.Confirm).show();
            }else{
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
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null) {
                    //todo:// 处理文字
                }
            } else if (type.startsWith("image/")) {//单张图片和文字
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null) {
                    //todo:// 处理文字
                }
                Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                //todo:// 处理图片路径
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {//多张图片
            if (type.startsWith("image/")) {
                ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
                mList.clear();
                for (Uri uri : imageUris) {
                    mList.add(UriToFilePath(uri));
                }
                addPhoto();
            }
        }
    }

    private void addPhoto() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
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
