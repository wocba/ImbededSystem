package com.wocba.imbededsystem.Main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wocba.imbededsystem.Chat.ChatActivity;
import com.wocba.imbededsystem.Common.BaseActivity;
import com.wocba.imbededsystem.Data.DbOpenHelper;
import com.wocba.imbededsystem.Data.FireClass;
import com.wocba.imbededsystem.R;
import com.wocba.imbededsystem.Service.MyService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{
    private static final String TAG = "MainActivity";
    private static final int IMAGE_CAMERA_REQUEST = 1;
    private static final int IMAGE_GALLERY_REQUEST = 2;

    private DbOpenHelper mDbOpenHelper;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mMarkerReference;
    private ChildEventListener mChildMapEventListener;
    private ChildEventListener mChildMarkerEventListener;
    private ChildEventListener mChildLocationEventListener;
    private Cursor mCursor;
    private Context context;

    private GoogleMap mMap;
    private Uri mUri;
    private LocationManager manager;
    private ArrayList<LatLng> arrayPoints;
    private Marker Bmarker;
    private Location mLocation;

    private DetailDialog detailDialog;
    private ErrorDialog errorDialog;
    private ContentDialog contentDialog;

    private boolean push = false;
    private boolean push_main = false;

    private MediaPlayer media;

    private String markerKey;
    private double lati;
    private double longi;
    private String email = null;
    private String content = null;
    private String image = null;
    private String imgPath = "";
    private String mContent = null;
    private String mErrorMessage = null;
    private SharedPreferences pref;


    // 임의로 정한 권한 상수
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigationView.getMenu().getItem(0).setChecked(true);

        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getInstance().getReference();
        mMarkerReference = mDatabaseReference.child("markers");
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://imbededproject.appspot.com");


        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {

        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        } else {

        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        startLocationService();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        manager.addGpsStatusListener(gpsStatusListener);
        MapsInitializer.initialize(this);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present..
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_camera)
        {
            cameraIntent();
//            media = new MediaPlayer();
//            media.create(getBaseContext(),R.raw.ppap);
//            media.setLooping(false);
//            media.start();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        this.init();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }

    private final GpsStatus.Listener gpsStatusListener = new GpsStatus.Listener() {

        public void onGpsStatusChanged(int event) {

            switch (event) {
                case GpsStatus.GPS_EVENT_STARTED:
                    Toast.makeText(getApplicationContext(), "GPS연결을 시도합니다.", Toast.LENGTH_SHORT).show();
                    // gps 연결 시도를 시작하면 발생하는 이벤트
                    // mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this); 를 호출하면 발생한다.
                    break;
                case GpsStatus.GPS_EVENT_STOPPED:
                    // gps 연결이 끝났을때 발생하는 이벤트
                    // mLocationManager.removeUpdates(ms_Instance); 를 호출하면 발생한다.
                    break;
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    // gps 연결이 되면 발생하는 이벤트
                    Toast.makeText(getApplicationContext(), "GPS연결이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    break;
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    // gps 와 연결이 되어 있는 위성의 상태를 넘겨받는 이벤트
                    // gps 수신상태를 체크할 수 있다.
                    break;
            }
        }
    };

    private void init() {
        String coordinates[] = {"37.566535 ", "126.977969"};        // 좌표 초기값 서울시청

        double lat = Double.parseDouble(coordinates[0]);
        double lng = Double.parseDouble(coordinates[1]);
//        double lati;
//        double longi;

        LatLng position = new LatLng(lat, lng);
        GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // 맵 위치이동.

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
        arrayPoints = new ArrayList<LatLng>();

        mChildMapEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                double lati = Double.parseDouble(dataSnapshot.getValue(FireClass.class).lati);
                double longi = Double.parseDouble(dataSnapshot.getValue(FireClass.class).longi);
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.heart);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lati, longi))
                        .title("Marker")
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                );
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mMarkerReference.addChildEventListener(mChildMapEventListener);
        mMap.setOnMarkerClickListener(this);
    }

    private void startLocationService(){
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        GPSListener gpsListener = new GPSListener();
        long minTime = 1000;
        float minDistance = 0;

        try {
            // GPS 기반 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);

        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }
    private class GPSListener implements LocationListener {
        private Location previousLocation = null;

        public void onLocationChanged(Location location) {

            mLocation = location;
            if(location != null) {
                if((location.getAccuracy() > 15 && this != null)) {
//                    Toast.makeText(getApplicationContext(), "GPS수신이 약합니다.", Toast.LENGTH_SHORT).show();
                }


                if ((previousLocation != null && location.getAccuracy() < 15)) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                    MarkerOptions marker = new MarkerOptions();
                    marker.position(new LatLng(location.getLatitude(), location.getLongitude()));
                    pref = getSharedPreferences("pref", MODE_PRIVATE);
                    push = pref.getBoolean("push", false);
                    if(push == true) {

                        mChildLocationEventListener = new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                if(mLocation.getLatitude() - 0.0003 < Double.parseDouble(dataSnapshot.getValue(FireClass.class).lati) && mLocation.getLatitude() + 0.0003 > Double.parseDouble(dataSnapshot.getValue(FireClass.class).lati) &&
                                        mLocation.getLongitude() - 0.0003 < Double.parseDouble(dataSnapshot.getValue(FireClass.class).longi) && mLocation.getLatitude() + 0.0003 < Double.parseDouble(dataSnapshot.getValue(FireClass.class).longi)){
                                    if(lati == Double.parseDouble(dataSnapshot.getValue(FireClass.class).lati) && longi == Double.parseDouble(dataSnapshot.getValue(FireClass.class).longi)){
                                        savePushMainOffPreferences();
                                    } else{
                                        savePushMainOnPreferences();
                                    }
                                    push_main = pref.getBoolean("push_main", false);
                                    if(push_main == true){
                                        Intent intent = new Intent(getApplicationContext(), MyService.class);
                                        startService(intent);
                                    }
                                    lati = Double.parseDouble(dataSnapshot.getValue(FireClass.class).lati);
                                    longi = Double.parseDouble(dataSnapshot.getValue(FireClass.class).longi);
                                }
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };
                        mMarkerReference.addChildEventListener(mChildLocationEventListener);


//                        mCursor = mDbOpenHelper.getAllColumns();
//                        while(mCursor.moveToNext()) {
//                            if (location.getLatitude() - 0.001 < Double.parseDouble(mCursor.getString(mCursor.getColumnIndex("lati"))) && location.getLatitude() + 0.001 > Double.parseDouble(mCursor.getString(mCursor.getColumnIndex("lati"))) &&
//                                    location.getLongitude() - 0.001 < Double.parseDouble(mCursor.getString(mCursor.getColumnIndex("longi"))) && location.getLatitude() + 0.001 < Double.parseDouble(mCursor.getString(mCursor.getColumnIndex("longi")))) {
//                                if(lati == Double.parseDouble(mCursor.getString(mCursor.getColumnIndex("lati"))) && longi == Double.parseDouble(mCursor.getString(mCursor.getColumnIndex("longi")))){
//                                    savePushMainOffPreferences();
//                                } else{
//                                    savePushMainOnPreferences();
//                                }
//                                push_main = pref.getBoolean("push_main", false);
//                                if(push_main == true){
//                                    Intent intent = new Intent(getApplicationContext(), MyService.class);
//                                    startService(intent);
//                                }
//                                lati = Double.parseDouble(mCursor.getString(mCursor.getColumnIndex("lati")));
//                                longi = Double.parseDouble(mCursor.getString(mCursor.getColumnIndex("longi")));
//                            }
//                        }
                    }


                }

                // Update stored location
                this.previousLocation = location;
            } else {

            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }
    @Override
    public void onResume() {
        super.onResume();
//        try {
//            mMap.setMyLocationEnabled(true);
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void onPause() {
        super.onPause();
//
//        try {
//            mMap.setMyLocationEnabled(false);
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(media != null){
            media.release();
            media = null;
        }
//        mDatabaseReference.removeEventListener(mChildMapEventListener);
//        mDatabaseReference.removeEventListener(mChildMarkerEventListener);
//        mDatabaseReference.removeEventListener(mChildLocationEventListener);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        Bmarker = marker;
        context = this;

        mChildMarkerEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(Bmarker.getPosition().latitude == Double.parseDouble(dataSnapshot.getValue(FireClass.class).lati) &&
                        Bmarker.getPosition().longitude == Double.parseDouble(dataSnapshot.getValue(FireClass.class).longi)){
                    email = dataSnapshot.getValue(FireClass.class).userEmail;
                    content = dataSnapshot.getValue(FireClass.class).comment;
                    image = dataSnapshot.getValue(FireClass.class).PhotoUrl;
                    markerKey = dataSnapshot.getValue(FireClass.class).firebaseKey;
                    detailDialog = new DetailDialog(context, chatListener, cancelListener, email, content, image);
                    detailDialog.show();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mMarkerReference.addChildEventListener(mChildMarkerEventListener);
//
//        mCursor = mDbOpenHelper.getAllColumns();
//
//        while (mCursor.moveToNext()) {
//            if(marker.getPosition().latitude == Double.parseDouble(mCursor.getString(mCursor.getColumnIndex("lati"))) && marker.getPosition().longitude == Double.parseDouble(mCursor.getString(mCursor.getColumnIndex("longi")))) {
//                name = mCursor.getString(mCursor.getColumnIndex("name")).toString();
//                content = mCursor.getString(mCursor.getColumnIndex("content")).toString();
//                image = mCursor.getString(mCursor.getColumnIndex("image")).toString();
//            }
//        }
//        mCursor.close();

        return true;
    }

    private View.OnClickListener cancelListener = new View.OnClickListener() {
        public void onClick(View v) {
            detailDialog.dismiss();
        }
    };

    private View.OnClickListener chatListener = new View.OnClickListener() {
        public void onClick(View v) {
            // key값으로 marker 마다 채팅방 생성
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            intent.putExtra("markerKey", markerKey);
            startActivity(intent);
        }
    };

    private View.OnClickListener errorListener = new View.OnClickListener() {
        public void onClick(View v) {
            errorDialog.dismiss();
        }
    };

    private void savePushMainOnPreferences()
    {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("push_main", true);
        editor.commit();
    }

    private void savePushMainOffPreferences()
    {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("push_main", false);
        editor.commit();
    }

    private View.OnClickListener contentListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(contentDialog.mEditContent.getText().toString() != null){
                mContent = contentDialog.mEditContent.getText().toString();
                Uri capturedImage = Uri.fromFile(new File(imgPath));
                StorageReference capturedImageRef = mStorageRef.child("images/" + capturedImage.getLastPathSegment());
                sendFirebase(capturedImageRef, capturedImage);
                FireClass fireClass = new FireClass();
                if(mLocation != null){
                    fireClass.userEmail = mAuth.getCurrentUser().getEmail();
                    fireClass.lati = Double.toString(mLocation.getLatitude());
                    fireClass.longi = Double.toString(mLocation.getLongitude());
                    fireClass.PhotoUrl = capturedImage.getLastPathSegment().toString();
                    fireClass.comment = mContent;
                    fireClass.firebaseKey = mMarkerReference.push().getKey();
                    mDbOpenHelper.insertColumn(mAuth.getCurrentUser().getEmail(), capturedImage.getLastPathSegment().toString(),
                            Double.toString(mLocation.getLatitude()), Double.toString(mLocation.getLongitude()), mContent);
                }
                else{
                    mErrorMessage = "GPS 수신이 약합니다";
                    errorDialog = new ErrorDialog(getBaseContext(), errorListener, mErrorMessage);
                    errorDialog.show();
                }


                // key값으로 저장
                Map<String, Object> markerValues = fireClass.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/markers/" + fireClass.firebaseKey, markerValues);
                mDatabaseReference.updateChildren(childUpdates);

            }else{
                mErrorMessage = "내용을 입력해주세요";
                errorDialog = new ErrorDialog(getBaseContext(), errorListener, mErrorMessage);
                errorDialog.show();
            }
            contentDialog.dismiss();
        }
    };

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IMAGE_CAMERA_REQUEST:
                    // 찍고 저장하면 imageView에 보여주면서 firebase에 저장

                    contentDialog = new ContentDialog(this, contentListener);
                    contentDialog.show();

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
