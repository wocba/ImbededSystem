package com.wocba.imbededsystem.ContentDetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wocba.imbededsystem.R;

/**
 * Created by jinwo on 2017-12-04.
 */

public class ContentDetailMapFrag extends Fragment{
    private GoogleMap map;
    private Bundle bundle;
    private LatLng point;
    private double lati = 0.0;
    private double longi = 0.0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detail_frag_1, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        bundle = getArguments();
        try{
            lati = Double.parseDouble(bundle.getString("lati"));
            longi = Double.parseDouble(bundle.getString("longi"));
        }catch (Exception e){
            e.printStackTrace();
        }

        map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.detail_map)).getMap();

        point = new LatLng(lati,longi);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 17));
        MarkerOptions marker = new MarkerOptions();
        marker.position(new LatLng(lati,longi));
        marker.title("위치");
        marker.draggable(true);
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.kbo));
        map.addMarker(marker);

        super.onActivityCreated(savedInstanceState);
    }
}
