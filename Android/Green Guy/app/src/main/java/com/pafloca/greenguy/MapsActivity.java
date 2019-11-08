package com.pafloca.greenguy;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<Marker> markers = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        setDemoMarkers(googleMap);
        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));


    }

    private void setDemoMarkers(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng tetech = new LatLng(48.713111, 2.199685);
        LatLng poubelle_recyclable = new LatLng(48.714426, 2.202051);
        LatLng poubelle_compost = new LatLng(48.711205, 2.198896);
        LatLng atelier_DIY = new LatLng(48.717236, 2.197094);
        LatLng eco_friendly = new LatLng(48.714199, 2.201171);
        LatLng info = new LatLng(48.712939, 2.201053);
        LatLng pointEau = new LatLng(48.712211, 2.192545);


        mMap.addMarker(new MarkerOptions()
                .position(tetech)
                .icon(bitmapDescriptorFromVector_bckgrd(this, R.drawable.ic_mylocation))
                .title("MOI")
                .visible(true));

        markers.add(mMap.addMarker(new MarkerOptions()
                .position(poubelle_recyclable)

                .icon(bitmapDescriptorFromVector(this, R.drawable.ic_recycle_bin))
                .title("poubelle recyclable")));
        markers.add(mMap.addMarker(new MarkerOptions()
                .position(poubelle_compost)

                .icon(bitmapDescriptorFromVector(this, R.drawable.ic_compost))
                .title("Compost")));
        markers.add(mMap.addMarker(new MarkerOptions()
                .position(atelier_DIY)

                .icon(bitmapDescriptorFromVector(this, R.drawable.ic_map_event))
                .title("Event : Atelier DIY")));
        markers.add(mMap.addMarker(new MarkerOptions()
                .position(eco_friendly)

                .icon(bitmapDescriptorFromVector(this, R.drawable.ic_restaurant_black_24dp))
                .title("Eco-friendly")));
        markers.add(mMap.addMarker(new MarkerOptions()
                .position(info)

                .icon(bitmapDescriptorFromVector(this, R.drawable.ic_info))
                .title("Information")));
        markers.add(mMap.addMarker(new MarkerOptions()
                .position(pointEau)

                .icon(bitmapDescriptorFromVector(this, R.drawable.ic_water))
                .title("point d'eau")));


        mMap.moveCamera(CameraUpdateFactory.newLatLng(tetech));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11.1f));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker m) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(), 17.0f));
                m.showInfoWindow();
                return true;
            }
        });
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                for (Marker m : markers) {
                    m.setVisible(cameraPosition.zoom > 14.6);
                }
            }
        });
    }



    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private BitmapDescriptor bitmapDescriptorFromVector_bckgrd(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_background);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(35, 20, vectorDrawable.getIntrinsicWidth() + 10, vectorDrawable.getIntrinsicHeight() + 10);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
