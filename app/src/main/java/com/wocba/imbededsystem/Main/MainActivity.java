package com.wocba.imbededsystem.Main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.wocba.imbededsystem.Camera.CameraActivity;
import com.wocba.imbededsystem.Common.BaseActivity;
import com.wocba.imbededsystem.Data.DbOpenHelper;
import com.wocba.imbededsystem.R;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private DbOpenHelper mDbOpenHelper;
    private GoogleMap mMap;
    private Cursor mCursor;
    private Uri mUri;
    private LocationManager manager;
    private ArrayList<LatLng> arrayPoints;
    private DetailDialog detailDialog;


    // 임의로 정한 권한 상수
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigationView.getMenu().getItem(0).setChecked(true);

        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();

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
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivity(intent);
//            try{
//                File file =  createImageFile();
//
//                mUri = FileProvider.getUriForFile(getApplication().getApplicationContext(),
//                        "com.wocba.imbededsystem.provider", file);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
//                startActivity(intent);
//            }catch (IOException e){
//                e.printStackTrace();
//            }
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

//    private File createImageFile() throws IOException {
//
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
////        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
//        return image;
//    }

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
        double lati;
        double longi;

        LatLng position = new LatLng(lat, lng);
        GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // 맵 위치이동.

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
        arrayPoints = new ArrayList<LatLng>();
        mCursor = mDbOpenHelper.getAllColumns();
        while (mCursor.moveToNext()) {

            lati = Double.parseDouble(mCursor.getString(mCursor.getColumnIndex("lati")).toString());
            longi = Double.parseDouble(mCursor.getString(mCursor.getColumnIndex("longi")).toString());

            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.kbo);
            Bitmap b=bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lati,longi))
                    .title("Marker")
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
            );
        }
        mCursor.close();
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

            if(location != null) {
                if((location.getAccuracy() > 15 && this != null)) {
//                    Toast.makeText(getApplicationContext(), "GPS수신이 약합니다.", Toast.LENGTH_SHORT).show();
                }


                if ((previousLocation != null && location.getAccuracy() < 15)) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                    MarkerOptions marker = new MarkerOptions();
                    marker.position(new LatLng(location.getLatitude(), location.getLongitude()));
//                    mCursor = mDbOpenHelper.getAllColumns();
//                    while(mCursor.moveToNext()) {
//                        if (location.getLatitude() - 0.001 < Double.parseDouble(mCursor.getString(mCursor.getColumnIndex("lati"))) && location.getLatitude() + 0.001 > Double.parseDouble(mCursor.getString(mCursor.getColumnIndex("lati"))) &&
//                                location.getLongitude() - 0.001 < Double.parseDouble(mCursor.getString(mCursor.getColumnIndex("longi"))) && location.getLatitude() + 0.001 < Double.parseDouble(mCursor.getString(mCursor.getColumnIndex("longi")))) {
//                            Intent intent = new Intent(getApplicationContext(), MyService.class);
//                            startService(intent);
//                        }
//                    }
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
    public boolean onMarkerClick(Marker marker) {

        String name = null;
        String content = null;

        Toast.makeText(this, marker.getTitle() + "," + marker.getPosition(), Toast.LENGTH_SHORT).show();
        mCursor = mDbOpenHelper.getAllColumns();

        while (mCursor.moveToNext()) {
            if(marker.getPosition().latitude == Double.parseDouble(mCursor.getString(mCursor.getColumnIndex("lati"))) && marker.getPosition().longitude == Double.parseDouble(mCursor.getString(mCursor.getColumnIndex("longi")))) {
                name = mCursor.getString(mCursor.getColumnIndex("name")).toString();
                content = mCursor.getString(mCursor.getColumnIndex("content")).toString();
            }
        }
        mCursor.close();

        detailDialog = new DetailDialog(this, deleteChatListener, deleteCancelListener,name,content);
        detailDialog.show();
        return true;
    }

    private View.OnClickListener deleteCancelListener = new View.OnClickListener() {
        public void onClick(View v) {
            detailDialog.dismiss();
        }
    };

    private View.OnClickListener deleteChatListener = new View.OnClickListener() {
        public void onClick(View v) {

        }
    };
}
