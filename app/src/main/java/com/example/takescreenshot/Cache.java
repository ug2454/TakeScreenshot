package com.example.takescreenshot;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.example.takescreenshot.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Save a file in the App cache and get Uri for it
 *
 * @author Ivan V on 12.05.2019.
 * @version 1.0
 */
public class Cache {
    Context context;
    Cache(Context context){
        this.context=context;
    }

    public static final String TAG = Cache.class.getSimpleName();

    private static final String CHILD_DIR = "images";
    private static final String TEMP_FILE_NAME = "img";
    private static final String FILE_EXTENSION = ".png";

    private static final int COMPRESS_QUALITY = 100;

    /**
     * Save image to the App cache
     * @param bitmap to save to the cache
     * @param name file name in the cache.
     * If name is null file will be named by default {@link #TEMP_FILE_NAME}
     * @return file dir when file was saved
     */
    public File saveImgToCache(Bitmap bitmap, @Nullable String name) {
        File cachePath = null;
        String fileName = TEMP_FILE_NAME;
        if (!TextUtils.isEmpty(name)) {
            fileName = name;
        }
        try {
            cachePath = new File(context.getApplicationContext().getCacheDir(), CHILD_DIR);
            cachePath.mkdirs();

            FileOutputStream stream = new FileOutputStream(cachePath + "/" + fileName + FILE_EXTENSION);
            bitmap.compress(Bitmap.CompressFormat.PNG, COMPRESS_QUALITY, stream);
            stream.close();
        } catch (IOException e) {
            Log.e(TAG, "saveImgToCache error: " + bitmap, e);
        }
        return cachePath;
    }

    /**
     * Save an image to the App cache dir and return it {@link Uri}
     * @param bitmap to save to the cache
     */
    public Uri saveToCacheAndGetUri(Bitmap bitmap) {
        return saveToCacheAndGetUri(bitmap, null);
    }

    /**
     * Save an image to the App cache dir and return it {@link Uri}
     * @param bitmap to save to the cache
     * @param name file name in the cache.
     * If name is null file will be named by default {@link #TEMP_FILE_NAME}
     */
    public Uri saveToCacheAndGetUri(Bitmap bitmap, @Nullable String name) {
        File file = saveImgToCache(bitmap, name);
        return getImageUri(file, name);
    }

    /**
     * Get a file {@link Uri}
     * @param name of the file
     * @return file Uri in the App cache or null if file wasn't found
     */
    @Nullable public Uri getUriByFileName(String name) {
        Context context1 = context.getApplicationContext();
        String fileName;
        if (!TextUtils.isEmpty(name)) {
            fileName = name;
        } else {
            return null;
        }

        File imagePath = new File(context1.getCacheDir(), CHILD_DIR);
        File newFile = new File(imagePath, fileName + FILE_EXTENSION);
        return FileProvider.getUriForFile(context1, context1.getPackageName() + ".provider", newFile);
    }

    // Get an image Uri by name without extension from a file dir
    private Uri getImageUri(File fileDir, @Nullable String name) {
        Context context1 = context.getApplicationContext();
        String fileName = TEMP_FILE_NAME;
        if (!TextUtils.isEmpty(name)) {
            fileName = name;
        }
        File newFile = new File(fileDir, fileName + FILE_EXTENSION);
        return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", newFile);
    }

    /**
     * Get Uri type by {@link Uri}
     */
    public String getContentType(Uri uri) {
        return context.getApplicationContext().getContentResolver().getType(uri);
    }

}