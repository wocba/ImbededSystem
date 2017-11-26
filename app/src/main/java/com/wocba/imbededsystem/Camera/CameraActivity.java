package com.wocba.imbededsystem.Camera;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.wocba.imbededsystem.BuildConfig;
import com.wocba.imbededsystem.Common.BaseActivity;
import com.wocba.imbededsystem.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by jinwo on 2017-11-20.
 */

public class CameraActivity extends BaseActivity {

    private static final String TAG = CameraActivity.class.getSimpleName();
    private Button mBtnCamera, mBtnGallery;
    private ImageView mImg;
    private File mFile;
    private File mPhotoFile;
    private Uri mUri;
    // start activity request code
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    private static final int REQUEST_CROP_DATA = 3;
/** Called when the activity is first created. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mFile = Util.getFilePath();
        Log.d(TAG, "getFilePath = [ " + mFile + " ] ");
        findViews();
    }

    private void findViews(){
        mBtnCamera = (Button) findViewById(R.id.btn_click_camera);
        mBtnGallery = (Button) findViewById(R.id.btn_click_gallery);
        mImg = (ImageView) findViewById(R.id.img_icon);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                switch(v.getId()){
                    case R.id.btn_click_camera:
                        startActivityCamera();
                        break;
                    case R.id.btn_click_gallery:
                        startActivityGallery();
                        break;
                }
            }
        };
        mBtnCamera.setOnClickListener(listener);
        mBtnGallery.setOnClickListener(listener);
    }

    private void startActivityCamera(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try{
            mPhotoFile = Util.createImageFile();
        }catch(IOException e){
            e.printStackTrace();
        }

 //       final Intent i = Util.getCamaraIntent(mPhotoFile);
        try{

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                if(i.resolveActivity(this.getPackageManager())!=null) {
                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    mUri = FileProvider.getUriForFile(CameraActivity.this,
                            BuildConfig.APPLICATION_ID + "provider", mPhotoFile);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                }
            }
            startActivityForResult(i, REQUEST_CAMERA);
        } catch (ActivityNotFoundException anfe){
            Toast.makeText(this,"No activity found to open this attachment.", Toast.LENGTH_LONG).show();
        }

    }
    private void startActivityGallery(){
        final Intent i = Util.getGalleryIntent();
        startActivityForResult(i, REQUEST_GALLERY);
    }
    private void startActivityCrop(File file){
        MediaScannerConnection.scanFile(this,
                new String[] {file.getAbsolutePath()},
                new String[] {null},
                null);
        try{
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i = Util.getCropImageIntent(FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + "com.wocba.imbededsystem.fileprovider",file));
            }
            startActivityForResult(i, REQUEST_CROP_DATA);
        } catch (ActivityNotFoundException anfe){
            Toast.makeText(this,"No activity found to open this attachment.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
// TODO Auto-generated method stub
//	super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case REQUEST_CAMERA:
                Log.v(TAG, "REQUEST_CAMERA........................");
                startActivityCrop(mPhotoFile);
                break;
            case REQUEST_GALLERY:
                Log.v(TAG, "REQUEST_GALLERY.......................");
                break;
            case REQUEST_CROP_DATA:
                Log.v(TAG, "REQUEST_CROP_DATA......................");
                Bitmap bitmap = data.getParcelableExtra("data");
                getImageViewBitmap(bitmap);
                break;
        }
    }
    private void getImageViewBitmap(Bitmap bitmap){
        final int size = bitmap.getHeight() * bitmap.getWidth() * 4;
        Log.d(TAG, "bitmap size ( " + size + " ) ");
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream(size);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        try {
            outStream.flush();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            outStream.close();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        mImg.setImageBitmap(bitmap);
    }

}
