package com.wocba.imbededsystem.Main;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wocba.imbededsystem.Data.DbOpenHelper;
import com.wocba.imbededsystem.R;

import java.util.ArrayList;

/**
 * Created by jinwo on 2017-11-10.
 */

public class MainMapFrag extends Fragment {
    private GoogleMap map;
    private Bundle bundle;
    private LocationManager manager;
    private ArrayList<LatLng> arrayPoints;
    private DbOpenHelper mDbOpenHelper;
    private Cursor mCursor;


    private final GpsStatus.Listener gpsStatusListener = new GpsStatus.Listener() {

        public void onGpsStatusChanged(int event) {

            switch (event) {
                case GpsStatus.GPS_EVENT_STARTED:
//                    Toast.makeText(getActivity().getApplicationContext(), "GPS연결을 시도합니다.", Toast.LENGTH_SHORT).show();
                    // gps 연결 시도를 시작하면 발생하는 이벤트
                    // mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this); 를 호출하면 발생한다.
                    break;
                case GpsStatus.GPS_EVENT_STOPPED:
                    // gps 연결이 끝났을때 발생하는 이벤트
                    // mLocationManager.removeUpdates(ms_Instance); 를 호출하면 발생한다.
                    break;
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    // gps 연결이 되면 발생하는 이벤트
//                    Toast.makeText(getActivity().getApplicationContext(), "GPS연결이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    break;
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    // gps 와 연결이 되어 있는 위성의 상태를 넘겨받는 이벤트
                    // gps 수신상태를 체크할 수 있다.
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.main_frag_2, container, false);

//        View v = inflater.inflate(R.layout.main_frag_2, container, false);
//        if (map == null) {
//            SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.main_map);
//            mapFrag.getMapAsync(this);
//        }
//        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bundle = getArguments();
        try{
//            lati = Double.parseDouble(bundle.getString("lati"));
//            longi = Double.parseDouble(bundle.getString("longi"));
        }catch (Exception e){
            e.printStackTrace();
        }

        map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.main_map)).getMap();

//        point = new LatLng(Lat,Long);
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 7));
//        MarkerOptions marker = new MarkerOptions();
//        marker.position(new LatLng(Lat,Long));
//        marker.title("위치");
//        marker.draggable(true);
//        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.bubble_mask));
//        map.addMarker(marker);

        startLocationService();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        MapsInitializer.initialize(getActivity().getApplicationContext());
        this.init();
    }

    private void init() {
        String coordinates[] = {"37.566535 ", "126.977969"};        // 좌표 초기값 서울시청

        double lat = Double.parseDouble(coordinates[0]);
        double lng = Double.parseDouble(coordinates[1]);
        double lati;
        double longi;
        String latiString;
        String longiString;

        LatLng position = new LatLng(lat, lng);
        GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());

        // 맵 위치이동.

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
        arrayPoints = new ArrayList<LatLng>();
        mDbOpenHelper = new DbOpenHelper(getActivity());
        mCursor = mDbOpenHelper.getAllColumns();
        while (mCursor.moveToNext()) {

            lati = Double.parseDouble(mCursor.getString(mCursor.getColumnIndex("lati")).toString());
            longi = Double.parseDouble(mCursor.getString(mCursor.getColumnIndex("longi")).toString());

            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.kbo);
            Bitmap b=bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 20, 20, false);
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(lati,longi))
                    .title("Marker")
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
            );
        }
        mCursor.close();



    }

    private void startLocationService(){
        manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
                if((location.getAccuracy() > 15 && getActivity()!=null)) {
//                    Toast.makeText(getActivity(), "GPS수신이 약합니다.", Toast.LENGTH_SHORT).show();
                }


                if ((previousLocation != null && location.getAccuracy() < 15)) {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
                    map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                    MarkerOptions marker = new MarkerOptions();
                    marker.position(new LatLng(location.getLatitude(), location.getLongitude()));
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
        try {
            map.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            map.setMyLocationEnabled(false);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

}
