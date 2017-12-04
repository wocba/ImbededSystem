package com.wocba.imbededsystem.Camera;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wocba.imbededsystem.Common.BaseActivity;
import com.wocba.imbededsystem.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by jinwo on 2017-11-20.
 */

public class CameraActivity extends BaseActivity {
    private static final String TAG = "CameraActivity";
    private static final int IMAGE_CAMERA_REQUEST = 1;
    private static final int IMAGE_GALLERY_REQUEST = 2;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private String imgPath = "";
    private StorageReference mStorageRef;
    private Button camera_btn, gallery_btn;
    private ImageView iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mNavigationView.getMenu().getItem(3).setChecked(true);

        // Firebase Storage 참조
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://imbededproject.appspot.com");

        iv = (ImageView)findViewById(R.id.iv);

        camera_btn = (Button)findViewById(R.id.camera_btn);
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // permission이 있으면 cameraIntent()실행
                verifyStoragePermissions();
            }
        });

        gallery_btn = (Button)findViewById(R.id.gallery_btn);
        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryIntent();
            }
        });
    }

    // permission 체크
    public void verifyStoragePermissions() {
        int permission = ActivityCompat.checkSelfPermission(CameraActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    CameraActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            // 이미 permission 존재하면 camera 실행
            cameraIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission이 있으면 camera 실행
                    cameraIntent();
                }
                break;
        }
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getFileUri());
        startActivityForResult(intent, IMAGE_CAMERA_REQUEST);
    }

    private Uri getFileUri() {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".png");
        imgPath = file.getAbsolutePath();
        return FileProvider.getUriForFile(this, "com.wocba.imbededsystem.provider", file);
    }

    private void galleryIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "image from gallery"), IMAGE_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IMAGE_CAMERA_REQUEST:
                    // 찍고 저장하면 imageView에 보여주면서 firebase에 저장
                    Uri capturedImage = Uri.fromFile(new File(imgPath));
                    Bitmap camera_bitmap = BitmapFactory.decodeFile(imgPath);
                    iv.setImageBitmap(camera_bitmap);

                    StorageReference capturedImageRef = mStorageRef.child("images/" + capturedImage.getLastPathSegment());
                    sendFirebase(capturedImageRef, capturedImage);
                    break;

                case IMAGE_GALLERY_REQUEST:
                    // 사진 선택하면 imageView에 보여주면서 firebase에 저장
                    Uri selectedImage = data.getData();
                    try {
                        Bitmap gallery_bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImage);
                        iv.setImageBitmap(gallery_bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    StorageReference selectedImageRef = mStorageRef.child("images/" + selectedImage.getLastPathSegment());
                    sendFirebase(selectedImageRef, selectedImage);
                    break;

            }
        }
    }

    private void sendFirebase(StorageReference imageRef, Uri image) {

        imageRef.putFile(image)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.e(TAG, "success");
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "fail");
                    }
                });
    }
}


