package com.example.takescreenshot;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button button;
    Bitmap bitmap;
    File cachePath = null;
    Intent shareIntent;
    private static final String CHILD_DIR = "images";
    private static final String TEMP_FILE_NAME = "img";
    private static final String FILE_EXTENSION = ".png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);


    }

    public void takeScreenshot(View view) {
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        button.setVisibility(View.INVISIBLE);
        if (linearLayout != null) {
            bitmap = Bitmap.createBitmap(linearLayout.getWidth(), linearLayout.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            linearLayout.draw(canvas);
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);
        }
        button.setVisibility(View.VISIBLE);

        Cache cache = new Cache(this);
        Uri uri = cache.saveToCacheAndGetUri(bitmap);
        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey please check this application " + "https://play.google.com/store/apps/details?id=" + getPackageName());
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent chooser = Intent.createChooser(shareIntent, "Share File");

        List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            this.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        startActivity(chooser);
    }

//    private void shareImage() {
//
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());
//        String fileName = TEMP_FILE_NAME;
//
//        cachePath = new File(getApplicationContext().getCacheDir(), CHILD_DIR);
//        cachePath.mkdirs();
//        Intent shareIntent;
//
//
//        try {
//            FileOutputStream stream = new FileOutputStream(cachePath + "/" + fileName + FILE_EXTENSION);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//
//            stream.flush();
//            stream.close();
//            shareIntent = new Intent(Intent.ACTION_SEND);
//            shareIntent.setType("image/*");
//            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile());
//            shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey please check this application " + "https://play.google.com/store/apps/details?id=" + getPackageName());
//            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        startActivity(Intent.createChooser(shareIntent, "Share Picture"));
//    }


}