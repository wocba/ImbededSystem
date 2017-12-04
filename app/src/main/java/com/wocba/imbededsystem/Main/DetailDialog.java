package com.wocba.imbededsystem.Main;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wocba.imbededsystem.R;

import java.io.File;
import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * Created by jinwo on 2017-11-30.
 */

public class DetailDialog extends Dialog {

    private View.OnClickListener mDetailChatListener;
    private View.OnClickListener mDetailCancelListenr;
    private Button mChatButton;
    private Button mCancelButton;
    private String mName;
    private String mContent;
    private String mImage;
    private TextView mTextName;
    private TextView mTextContent;
    private ImageView imageView;
    private StorageReference mStorageRef;
    private File localFile;
    private String imgPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaildialog);

        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://imbededproject.appspot.com");
        StorageReference strRef = mStorageRef.child("images/" + mImage);
        imageView = (ImageView)findViewById(R.id.image_detail);

        try{
            localFile = File.createTempFile("images", "jpg");
        }catch (IOException e){
            e.printStackTrace();
        }
        getFirebase(strRef);

        mChatButton = (Button)findViewById(R.id.btn_chat);
        mCancelButton = (Button)findViewById(R.id.btn_back);
        mChatButton.setOnClickListener(mDetailChatListener);
        mCancelButton.setOnClickListener(mDetailCancelListenr);
        mTextName = (TextView)findViewById(R.id.text_detail_name);
        mTextContent = (TextView)findViewById(R.id.text_detail_content);
        mTextName.setText(mName);
        mTextContent.setText(mContent);
    }

    public DetailDialog(Context context, View.OnClickListener detailChatListener, View.OnClickListener detailCancelListener, String name, String content, String image){
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mDetailChatListener = detailChatListener;
        this.mDetailCancelListenr = detailCancelListener;
        this.mName = name;
        this.mContent = content;
        this.mImage = image;
    }

    private void getFirebase(StorageReference imageRef){
        imageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                Log.e(TAG, "success");
                imgPath = localFile.getAbsolutePath();
                Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e(TAG, "fail");
            }
        });
    }
}
