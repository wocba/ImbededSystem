package com.wocba.imbededsystem.ContentDetail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wocba.imbededsystem.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by jinwo on 2017-12-04.
 */

public class ContentDetailImageFrag extends Fragment{
    private static final String TAG = "ContentDetailImageFrag";
    private Bundle bundle;
    private StorageReference mStorageRef;
    private ImageView im;
    private StorageReference strRef;
    private File localFile;
    private String imgPath = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detail_frag_2, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        bundle = getArguments();
        String image = bundle.getString("image");
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://imbededproject.appspot.com");
        im = (ImageView) getActivity().findViewById(R.id.im_test);
        strRef = mStorageRef.child("images/" + image);


        try{
            localFile = File.createTempFile("images", "jpg");
        }catch (IOException e){
            e.printStackTrace();
        }

        getFirebase(strRef);

        super.onActivityCreated(savedInstanceState);

    }

    private void getFirebase(StorageReference imageRef){
        imageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                Log.e(TAG, "success");
                imgPath = localFile.getAbsolutePath();
                Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                im.setImageBitmap(bitmap);
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
