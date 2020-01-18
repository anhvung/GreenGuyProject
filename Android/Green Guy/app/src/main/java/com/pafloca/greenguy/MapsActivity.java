package com.pafloca.greenguy;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;
import androidx.fragment.app.FragmentActivity;

import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    static FloatingActionButton addEvent;
    static FloatingActionButton addPoi;
    static FloatingActionButton add;
    static long transiton = 500;// button fade time millis
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
        initializeButtons();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void initializeButtons() {
        addEvent = (FloatingActionButton) findViewById(R.id.addEvent);
        addPoi = (FloatingActionButton) findViewById(R.id.addPoi);
        addEvent.setVisibility(View.GONE);
        addPoi.setVisibility(View.GONE);
        // action qd on appuie sur  le plus en bas droite
        add = (FloatingActionButton) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fadeShow(v);

            }
        });
        addPoi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startAddPoiActivity();

            }
        });
        addEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startAddEventActivity();

            }
        });
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        setDemoMarkers(googleMap);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng arg0) {
                fadeHide(add);
            }
        });
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

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                // TODO Auto-generated method stub
                StartActivity(arg0.getTitle());

            }
        });


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
    private void  startAddPoiActivity(){

    }
    private void  startAddEventActivity(){

    }
    private void StartActivity(String msg) {
        Intent intent = new Intent(this, DisplayInfo.class);

        intent.putExtra(EXTRA_MESSAGE, msg);
        startActivity(intent);
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
    private void fadeShow(View v){

        addEvent.setAlpha(0f);
        addEvent.setVisibility(View.VISIBLE);
        addPoi.setAlpha(0f);
        addPoi.setVisibility(View.VISIBLE);
        addPoi.animate()
                .alpha(1f)
                .setDuration(transiton).setListener(null);
        addEvent.animate()
                .alpha(1f)
                .setDuration(transiton).setListener(null);
        v.animate()
                .alpha(0f)
                .setDuration(transiton)
                .setListener(null);
        v.setVisibility(View.GONE);

    }
    private void fadeHide(View v){
        v.setAlpha(0f);
        v.setVisibility(View.VISIBLE);
        v.animate()
                .alpha(1f)
                .setDuration(transiton).setListener(null);
        addEvent.animate()
                .alpha(0f)
                .setDuration(transiton)
                .setListener(null);
        addEvent.setVisibility(View.GONE);
        addPoi.animate()
                .alpha(0f)
                .setDuration(transiton)
                .setListener(null);
        addPoi.setVisibility(View.GONE);
    }

}
