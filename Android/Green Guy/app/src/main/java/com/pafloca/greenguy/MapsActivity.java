package com.pafloca.greenguy;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.PictureInPictureParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Rational;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Document;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import usefulclasses.CameraPreview;
import usefulclasses.ClientConnexion;
import usefulclasses.GMapV2Direction;

import static java.lang.Math.max;


/**
 * Activité : carte principale.
 *
 *  Il y a une maps (maps sdk), une timeline à droite et le menu à gauche.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, RoutingListener {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static final String EXTRA_MESSAGE2 = "com.example.myfirstapp.MESSAGE2wfw";
    static FloatingActionButton addEvent;
    static FloatingActionButton addPoi;
    static FloatingActionButton add;
    static FloatingActionButton position;
    static long transiton = 500;// button fade time millis
    private GoogleMap mMap;
    GeoDataClient mGeoDataClient;
    PlaceDetectionClient mPlaceDetectionClient;
    FusedLocationProviderClient mFusedLocationProviderClient;
    ArrayList<Marker> markers = new ArrayList<Marker>();
    private final static int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private RecyclerView recyclerView;
    private EventMenuAdapter adapter;
    private ArrayList<ModelEvent> eventList;
    private int ajoutPoiRequestCode = 1000;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private static final int DEFAULT_ZOOM = 15;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private Location mLastKnownLocation;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    boolean choosing=false;
    int code =0; //rien POi Event
    int storedId;
    private Camera mCamera;
    private CameraPreview mPreview;
    boolean cameraAccess=false;

    @Override
    protected void onResume() {
        super.onResume();


        new getAllInfo().execute();
        new getAllMarkers().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_maps);
        SharedPreferences sharedPref = getSharedPreferences("SAVE", Context.MODE_PRIVATE);
        storedId = sharedPref.getInt("USER_ID", -1);


        initializeButtons();
        initializeMenu();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        12847);

            }
        } else {
            cameraAccess=true;
        }
        mCamera = getCameraInstance();
        if(mCamera!=null){
            mCamera.setDisplayOrientation(90);
        }

        mPreview = new CameraPreview(this, mCamera);
        final FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        SeekBar seekBar=findViewById(R.id.seekBar);
        seekBar.setMax(100);
        seekBar.setProgress(0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
             double alpha=1-(progress/100.0);
             RelativeLayout rl=findViewById(R.id.RelativeLayoutMaps);
             if (alpha<0.9){
                 rl.setAlpha(max((float)alpha,(float)0.1));
                 if(mCamera!=null && cameraAccess){
                     mCamera.startPreview();
                 }


             }
             else{
                 rl.setAlpha((float)alpha);
                 if(mCamera!=null && cameraAccess){
                     mCamera.stopPreview();
                 }
             }
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        /*
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/
        //position of location button

        View locationButton = ((View) findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rlp.setMargins(0, 180, 180, 0);





        eventList = new ArrayList<>();
        /*
        ModelEvent modelEvent1 = new ModelEvent( "CleanWalk", "Ramassage de déchets collectif dans un quartier","blablabla", "Serguei", "8 mars", "Paris");
        ModelEvent modelEvent2 = new ModelEvent( "Cop 1", "Rassemblement entre étudiants","blablabla", "Vlad", "9 mai", "Moscou");
        ModelEvent modelEvent3 = new ModelEvent( "Cop 29", "Rassemblement pour défendre l'écologie", "blablabla","Marie Thérèse", "1 avril", "Cracovie");
        ModelEvent modelEvent4 = new ModelEvent( "Conférence", "Les économistes enfument ils la planère","blablabla", "Charles", "3 avril", "Berlin");
        ModelEvent modelEvent5 = new ModelEvent( "Débat politico écologique", "Occasion de retrouver les figures principales du mouvement écologique", "blablabla", "Thomas", "25 décembre", "Marseille");
        ModelEvent modelEvent6 = new ModelEvent( "Cinéma", "Au nom de la terre, film projeté en plein air", "blablabla","Marion", "5 septembre", "Bruges");
        ModelEvent modelEvent7 = new ModelEvent( "Salon de l'agriculture", "Rencontre avec les agriculteurs venant de toute la France","blablabla", "Manu", "1 janvier", "Toulouse");
        ModelEvent modelEvent8 = new ModelEvent( "Voyage dans plusieurs villes d'Europe", "But: rencontrer des jeunes avec des pensées diverses et variées sur le sujet","blablabla", "David", "26 mai", "Amsterdam - Rotterdam");

        eventList.add(modelEvent1);
        eventList.add(modelEvent2);
        eventList.add(modelEvent3);
        eventList.add(modelEvent4);
        eventList.add(modelEvent5);
        eventList.add(modelEvent6);
        eventList.add(modelEvent7);
        eventList.add(modelEvent8);*/



        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_event_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventMenuAdapter(this, eventList);
        recyclerView.setAdapter(adapter);

        chargementEvents();
        new getAllMarkers().execute();
        new getAllInfo().execute();
        test();

    }

    public void test() {
           Routing routing = new Routing.Builder()
            .travelMode(AbstractRouting.TravelMode.DRIVING)
            .withListener(MapsActivity.this)
            .waypoints(new LatLng(48.712900, 2.244050), new LatLng(48.714140,2.171400))
            .key("AIzaSyApwtfKqENhCUUoh4H2qqIFcFyDGhgee9Y")
            .build();
        routing.execute();
    }
    @Override
    public void onRoutingFailure(RouteException e) {
        Log.e("check", e.getMessage());
    }

    @Override
    public void onRoutingStart() {
        Log.e("check", "onRoutingStart");
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        Log.e("check", "onRoutingSuccess");
        CameraUpdate center = CameraUpdateFactory.newLatLng(  new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        List<Polyline> polylines = new ArrayList<>();

        mMap.moveCamera(center);


        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(R.color.colorBlue));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);
        }
    }

    @Override
    public void onRoutingCancelled() {
        Log.e("check", "onRoutingCancelled");
    }

    private void chargementEvents() {

        //ajouter à eventList les évènements récupérés de la base de donnée

        adapter.notifyDataSetChanged();


    }

    private void initializeMenu(){
        /**
        *\brief initialise les boutons du menu gauche
         */

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                int id = item.getItemId();
                switch (id){
                    case R.id.nav_add_friend :
                        Intent intentQR = new Intent(MapsActivity.this, AddFriendQr.class);
                        startActivity(intentQR);
                    break;
                    case R.id.nav_profile :
                        Intent intent_profile = new Intent(MapsActivity.this, MyProfileActivity.class);
                        startActivity(intent_profile);
                        break;
                    case R.id.nav_friend :
                        Intent intentMs = new Intent(MapsActivity.this, AllConvActivity.class);
                        startActivity(intentMs);
                        break;
                    case R.id.nav_event :
                        Intent intent_event = new Intent(MapsActivity.this, ListOfEventsActivity.class);
                        startActivity(intent_event);
                        break;
                    /*

                    case R.id.nav_my_event :
                        Intent intent_my_event = new Intent(MapsActivity.this, ListOfMyEventsActivity.class);
                        startActivity(intent_my_event);
                        Toast.makeText(getApplicationContext(),"events",Toast.LENGTH_SHORT).show();
                        break;

                     */
                    case R.id.nav_settings :
                        start(SettingsActivity.class);
                        break;

                }

                return true;
            }
        });

    }


    private void initializeButtons() {
        /**
         *\brief initialise les boutons flottants et leur animation
         */
        LinearLayout overlay=findViewById(R.id.Overlay);
        overlay.setVisibility(View.GONE);

        addEvent = findViewById(R.id.addEvent);
        addPoi =  findViewById(R.id.addPoi);
        addEvent.setVisibility(View.GONE);
        addPoi.setVisibility(View.GONE);
        // action qd on appuie sur  le plus en bas droite
        add =  findViewById(R.id.add);
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
                Log.d("greend", "position button pushed");
                startAddEventActivity();

            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setPadding(100, 20, 0, 0);
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
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();

    }



    private void setDemoMarkers(GoogleMap googleMap) {
        /**
         *\brief place les points sur la carte
         */
        mMap = googleMap;
        LatLng tetech = new LatLng(48.713111, 2.199685);
        LatLng poubelle_recyclable = new LatLng(48.714426, 2.202051);
        LatLng poubelle_compost = new LatLng(48.711205, 2.198896);
        LatLng atelier_DIY = new LatLng(48.717236, 2.197094);
        LatLng eco_friendly = new LatLng(48.714199, 2.201171);
        LatLng info = new LatLng(48.712939, 2.201053);
        LatLng pointEau = new LatLng(48.712211, 2.192545);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {


                if(code ==1){
                    Intent intent = new Intent(MapsActivity.this, AddPoiActivity.class);
                    Bundle args = new Bundle();
                    args.putParcelable("position", latLng);
                    intent.putExtra("bundle", args);
                    startActivityForResult(intent, ajoutPoiRequestCode);
                }
                else if (code==2){
                    Intent intent = new Intent(MapsActivity.this, AddEventActivity.class);
                    Bundle args = new Bundle();
                    args.putParcelable("position", latLng);
                    intent.putExtra("bundle", args);
                    startActivityForResult(intent, ajoutPoiRequestCode);
                }



            }
        });
/*
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
                .title("point d'eau"))); */

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                // TODO Auto-generated method stub
                StartActivityDisplay(String.valueOf(arg0.getTag()));

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
        choosing=true;
        POIOverlay();


    }
    private void  startAddEventActivity(){
        choosing=true;
        EventOverlay();
    }
    private void start(Class<SettingsActivity> settingsActivityClass){
        Intent intent = new Intent(this,settingsActivityClass );
        startActivity(intent);
    }
    private void StartActivityDisplay(String msg) {
        Intent intent = new Intent(this, DisplayGeneralEvent.class);

        intent.putExtra(EXTRA_MESSAGE, msg);
        startActivity(intent);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        /**
         *\brief converti les images au format approprié
         */
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private BitmapDescriptor bitmapDescriptorFromVector_bckgrd(Context context, @DrawableRes int vectorDrawableResourceId) {
        /**
         *\brief converti les images au format approprié
         */
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
/**
 *\brief animation
 */
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
        /**
         *\brief animation
         */
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
        Log.d("greend", "fade hide");
        if (choosing){
            noOverlay();
            choosing=false;
        }
    }
    public void noOverlay(){
        LinearLayout linearLayout= findViewById(R.id.searchLayout);
        linearLayout.setVisibility(View.VISIBLE);
        LinearLayout overlay=findViewById(R.id.Overlay);
        overlay.setVisibility(View.GONE);
        findViewById(R.id.textoverlayPOI).setVisibility(View.GONE);
        findViewById(R.id.textoverlayPOI).setVisibility(View.GONE);
        code=0;
    }
    public void POIOverlay(){
        code=1;
        LinearLayout linearLayout= findViewById(R.id.searchLayout);
        linearLayout.setVisibility(View.GONE);
        findViewById(R.id.textoverlayPOI).setVisibility(View.VISIBLE);
        findViewById(R.id.textoverlayEvent).setVisibility(View.GONE);
        LinearLayout overlay=findViewById(R.id.Overlay);
        overlay.setVisibility(View.VISIBLE);

    }
    public void EventOverlay(){
        code=2;
        LinearLayout linearLayout= findViewById(R.id.searchLayout);
        linearLayout.setVisibility(View.GONE);
        findViewById(R.id.textoverlayEvent).setVisibility(View.VISIBLE);
        findViewById(R.id.textoverlayPOI).setVisibility(View.GONE);
        LinearLayout overlay=findViewById(R.id.Overlay);
        overlay.setVisibility(View.VISIBLE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == ajoutPoiRequestCode) {
                //actualiser la carte car on a ajouté un nouveau POI
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d("greend", "Current location is null. Using defaults.");
                            Log.e("greend", "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    public void getDeviceLocation(View v) {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d("greend", "Current location is null. Using defaults.");
                            Log.e("greend", "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
            case 12847 :{



            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onUserLeaveHint () {
        final PictureInPictureParams.Builder pictureInPictureParamsBuilder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            pictureInPictureParamsBuilder = new PictureInPictureParams.Builder();
            Rational aspectRatio = new Rational(2, 3);
            pictureInPictureParamsBuilder.setAspectRatio(aspectRatio);
            enterPictureInPictureMode(pictureInPictureParamsBuilder.build());
        }

    }
    @Override
    public void onPictureInPictureModeChanged (boolean isInPictureInPictureMode, Configuration newConfig) {
        DrawerLayout dl=findViewById(R.id.drawer_layout);
        if (isInPictureInPictureMode) {
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            LinearLayout linearLayout= findViewById(R.id.searchLayout);
            linearLayout.setVisibility(View.GONE);
            dl.closeDrawer(Gravity.LEFT);
            dl.closeDrawer(Gravity.RIGHT);
            fadeHide(add);
            if (choosing){
                addPoi.setVisibility(View.GONE);
                addEvent.setVisibility(View.GONE);
            }
            else{
                add.setVisibility(View.GONE);
            }


        } else {
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            LinearLayout linearLayout= findViewById(R.id.searchLayout);
            linearLayout.setVisibility(View.VISIBLE);
            if (choosing){
                addPoi.setVisibility(View.VISIBLE);
                addEvent.setVisibility(View.VISIBLE);
            }
            else{
                add.setVisibility(View.VISIBLE);
            }


        }
    }

    public void publish(View view) {
        Intent intent=new Intent(this,AddInfo.class);
        startActivity(intent);
    }

    public void RechercherEvent(View view) {
        EditText seqrchtxt=findViewById(R.id.ed_home_searchbar);
        Intent intent = new Intent(this, SearchResultActivity.class);

        Log.d("greend","message search depart : "+seqrchtxt.getText());
        intent.putExtra(EXTRA_MESSAGE2, seqrchtxt.getText().toString());
        seqrchtxt.setText("");
        startActivity(intent);


    }

    private class getAllInfo extends AsyncTask<Void,Void,Void> {
        String[] allId;

        String[] allTitles;
        @Override
        protected Void doInBackground(Void... voids) {
            ClientConnexion connect= new ClientConnexion("192.168.1.17",2345,"0025",String.valueOf(storedId));
            allTitles=connect.magicSauce();
            connect= new ClientConnexion("192.168.1.17",2345,"0024",String.valueOf(storedId));
            allId=connect.magicSauce();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("greend","aweriogjbaeoihjbgraehiogba : "+allId.length+" "+ allTitles.length);
            if(eventList.size()!=allTitles.length){
                for (int i=0;i<allId.length;i++){
                    ModelEvent info = new ModelEvent(allTitles[i],allId[i]);
                    eventList.add(info);
                }
                chargementEvents();
            }


            super.onPostExecute(aVoid);
        }
    }
    private class getAllMarkers extends AsyncTask<Void,Void,Void> {
        String[] allLocations;
        String[] allTypes;
        String[] allTitles;
        String[] allIds;
        LatLng[] posl;
        @Override
        protected Void doInBackground(Void... voids) {
            ClientConnexion connect= new ClientConnexion("192.168.1.17",2345,"0019",String.valueOf(storedId));
            allLocations=connect.magicSauce();
            connect= new ClientConnexion("192.168.1.17",2345,"0020",String.valueOf(storedId));
            allTypes=connect.magicSauce();
            connect= new ClientConnexion("192.168.1.17",2345,"0021",String.valueOf(storedId));
            allTitles=connect.magicSauce();
            connect= new ClientConnexion("192.168.1.17",2345,"0022",String.valueOf(storedId));
            allIds=connect.magicSauce();
            posl= new LatLng[allIds.length];
            for (int i=0;i<allIds.length;i++){
                double latitude=0;
                double longitude=0;
                Geocoder geocoder = new Geocoder(MapsActivity.this);
                List<Address> addresses;

                try {
                    addresses = geocoder.getFromLocationName(allLocations[i], 1);
                    if(addresses.size() > 0) {
                        latitude= addresses.get(0).getLatitude();
                        longitude= addresses.get(0).getLongitude();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                posl[i] = new LatLng(latitude, longitude);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(markers.size()!=allIds.length){
                for (int i=0;i<allIds.length;i++){
                    String mtitre=allTitles[i];
                    LatLng mpos=posl[i] ;
                    Marker marker=null;
                    switch (allTypes[i]) {

                        case "event":
                            marker=mMap.addMarker(new MarkerOptions()
                                    .position(mpos)
                                    .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_event))
                                    .title(mtitre));
                            break;
                        case "Restaurant":
                            marker=mMap.addMarker(new MarkerOptions()
                                    .position(mpos)
                                    .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_restaurant_black_24dp))
                                    .title(mtitre));
                            break;
                        case "lieu touristique":
                            marker=mMap.addMarker(new MarkerOptions()
                                    .position(mpos)
                                    .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_local))
                                    .title(mtitre));
                            break;
                        case "Communauté":
                            marker=mMap.addMarker(new MarkerOptions()
                                    .position(mpos)
                                    .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_group))
                                    .title(mtitre));
                            break;
                        case "Festival":
                            marker=mMap.addMarker(new MarkerOptions()
                                    .position(mpos)
                                    .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_whatshot))
                                    .title(mtitre));
                            break;
                        case "Compost":
                            marker=mMap.addMarker(new MarkerOptions()
                                    .position(mpos)
                                    .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_compost))
                                    .title(mtitre));
                            break;
                        case "Recyclage":
                            marker=mMap.addMarker(new MarkerOptions()
                                    .position(mpos)
                                    .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_recycle_bin))
                                    .title(mtitre));
                            break;
                        case "Lieu Autre":
                            markers.add(mMap.addMarker(new MarkerOptions()
                                    .position(mpos)
                                    .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_place))
                                    .title(mtitre)));
                            break;
                        case "Point d'eau":
                            marker=mMap.addMarker(new MarkerOptions()
                                    .position(mpos)
                                    .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_water))
                                    .title(mtitre));
                            break;
                        default:
                            marker=mMap.addMarker(new MarkerOptions()
                                    .position(mpos)
                                    .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_add_poi))
                                    .title(mtitre));
                            break;

                    }
                    marker.setTag(allIds[i]);
                    markers.add(marker);


                }
            }


        }
    }
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }


}
