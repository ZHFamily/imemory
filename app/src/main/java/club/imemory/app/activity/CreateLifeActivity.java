package club.imemory.app.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import club.imemory.app.R;

/**
 * @Author: 张杭
 * @Date: 2017/4/11 23:04
 */

public class CreateLifeActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_life);
        getSendData();
    }

    private void getSendData(){
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
                for (Uri uri : imageUris) {
                    //todo:// 处理图片路径
                }
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
