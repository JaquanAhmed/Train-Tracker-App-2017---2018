package ukk1451070.ac.kingston.httpkunet.transporttracker.Journey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import ukk1451070.ac.kingston.httpkunet.transporttracker.Database.SaveRoute;
import ukk1451070.ac.kingston.httpkunet.transporttracker.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private String TAG = MapsActivity.class.getSimpleName();

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        View PP = findViewById(R.id.buttonPP);
        if (getIntent().getIntExtra("partNo",0)== 1){
            PP.setVisibility(View.GONE);
        }else{
            PP.setVisibility(View.VISIBLE);
        }

        View NP = findViewById(R.id.buttonNP);
            if (getIntent().getIntExtra("partNo",0) == (getIntent().getIntExtra("endNo",0))){
                NP.setVisibility(View.GONE);
            }else{
                NP.setVisibility(View.VISIBLE);
            }

        ArrayList<LatLng> train = (ArrayList<LatLng>) getIntent().getExtras().get("train");
         View F5 = findViewById(R.id.refresh);
            if (train.isEmpty()){
                F5.setVisibility(View.GONE);
            }else{
                F5.setVisibility(View.VISIBLE);
            }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker Live Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            extras.getInt("PartNo");
            extras.get("train");
            extras.get("foot");
            extras.get("tube");
            extras.get("bus");
            extras.get("other");
            extras.get("full");
            extras.getString("SName");
            extras.getDouble("SLon");
            extras.getDouble("SLat");
        }

        SharedPreferences apiData = getSharedPreferences("api_data", MODE_PRIVATE);
        String nameM = apiData.getString("nameM","name");
        String nameM2 = apiData.getString("nameM2","name");
        String fpnN = apiData.getString("fpnN","name");
        String tpnN = apiData.getString("tpnN","name");
        Double longitudeM = Double.longBitsToDouble(apiData.getLong("longitudeM",0));
        Double longitudeM2 = Double.longBitsToDouble(apiData.getLong("longitudeM2",0));
        Double FLon = Double.longBitsToDouble(apiData.getLong("FLon",0));
        Double TLon = Double.longBitsToDouble(apiData.getLong("TLon",0));
        Double latitudeM = Double.longBitsToDouble(apiData.getLong("latitudeM",0));
        Double latitudeM2 = Double.longBitsToDouble(apiData.getLong("latitudeM2",0));
        Double FLat = Double.longBitsToDouble(apiData.getLong("FLat",0));
        Double TLat = Double.longBitsToDouble(apiData.getLong("TLat",0));


            mMap = googleMap;
            LatLng Ostation = new LatLng(latitudeM, longitudeM);
            mMap.addMarker(new MarkerOptions()
                    .position(Ostation)
                    .title(nameM)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            LatLng Dstation = new LatLng(latitudeM2, longitudeM2);
            mMap.addMarker(new MarkerOptions()
                    .position(Dstation)
                    .title(nameM2)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            LatLng TTstation = new LatLng(extras.getDouble("SLat"), extras.getDouble("SLon"));
            mMap.addMarker(new MarkerOptions()
                    .position(TTstation)
                    .title(extras.getString("SName"))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            LatLng FPstation = new LatLng(FLat, FLon);
            mMap.addMarker(new MarkerOptions()
                    .position(FPstation)
                    .title(fpnN)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
            LatLng TPstation = new LatLng(TLat, TLon);
            mMap.addMarker(new MarkerOptions()
                    .position(TPstation)
                    .title(tpnN)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(FPstation, 10));

            Polyline footline = mMap.addPolyline(new PolylineOptions()
                    .addAll((Iterable<LatLng>) extras.get("foot"))
                    .color(Color.BLACK)
                    .width(13));
            footline.setZIndex(1);
            Polyline trainline = mMap.addPolyline(new PolylineOptions()
                    .addAll((Iterable<LatLng>) extras.get("train"))
                    .color(Color.BLUE)
                    .width(13));
            trainline.setZIndex(1);
            Polyline tubeline = mMap.addPolyline(new PolylineOptions()
                    .addAll((Iterable<LatLng>) extras.get("tube"))
                    .color(Color.GREEN)
                    .width(13));
            tubeline.setZIndex(1);
            Polyline busline = mMap.addPolyline(new PolylineOptions()
                    .addAll((Iterable<LatLng>) extras.get("bus"))
                    .color(Color.RED)
                    .width(13));
            busline.setZIndex(1);
            Polyline other = mMap.addPolyline(new PolylineOptions()
                    .addAll((Iterable<LatLng>) extras.get("other"))
                    .color(Color.YELLOW)
                    .width(20));
            other.setZIndex(0);
            Polyline full = mMap.addPolyline(new PolylineOptions()
                    .addAll((Iterable<LatLng>) extras.get("full"))
                    .color(Color.CYAN)
                    .width(20));
            full.setZIndex(0);


    }

    public void Refresh(View view){
        ArrayList<LatLng> full = (ArrayList<LatLng>) getIntent().getExtras().get("full");
        ArrayList<LatLng> train = (ArrayList<LatLng>) getIntent().getExtras().get("train");
        ArrayList<LatLng> foot = (ArrayList<LatLng>) getIntent().getExtras().get("foot");
        ArrayList<LatLng> tube = (ArrayList<LatLng>) getIntent().getExtras().get("tube");
        ArrayList<LatLng> bus = (ArrayList<LatLng>) getIntent().getExtras().get("bus");
        ArrayList<LatLng> other = (ArrayList<LatLng>) getIntent().getExtras().get("other");
        Intent intent = new Intent(MapsActivity.this, Position.class);
        intent.putExtra("partNo", getIntent().getIntExtra("partNo", 0));
        intent.putExtra("endNo", getIntent().getIntExtra("endNo", 0));
        intent.putExtra("SC", getIntent().getStringExtra("SC"));
        intent.putExtra("SC2", getIntent().getStringExtra("SC2"));
        intent.putExtra("full", full);
        intent.putExtra("train", train);
        intent.putExtra("tube", tube);
        intent.putExtra("foot", foot);
        intent.putExtra("bus", bus);
        intent.putExtra("other", other);
        intent.putExtra("service", getIntent().getExtras().getInt("service"));;
        startActivity(intent);
    }

    public void NextPart(View view){
        SharedPreferences apiData = getSharedPreferences("api_data", MODE_PRIVATE);
        Double longitudeM = Double.longBitsToDouble(apiData.getLong("longitudeM",0));
        Double longitudeM2 = Double.longBitsToDouble(apiData.getLong("longitudeM2",0));
        Double latitudeM = Double.longBitsToDouble(apiData.getLong("latitudeM",0));
        Double latitudeM2 = Double.longBitsToDouble(apiData.getLong("latitudeM2",0));
        int part = getIntent().getExtras().getInt("partNo")+1;
        Intent intent = new Intent(MapsActivity.this,Route.class);
        intent.putExtra("longitudeM2", longitudeM2);
        intent.putExtra("latitudeM2", latitudeM2);
        intent.putExtra("longitudeM", longitudeM);
        intent.putExtra("latitudeM", latitudeM);
        intent.putExtra("partNo", part);
        startActivity(intent);
    }

    public void PreviousPart(View view){
        SharedPreferences apiData = getSharedPreferences("api_data", MODE_PRIVATE);
        Double longitudeM = Double.longBitsToDouble(apiData.getLong("longitudeM",0));
        Double longitudeM2 = Double.longBitsToDouble(apiData.getLong("longitudeM2",0));
        Double latitudeM = Double.longBitsToDouble(apiData.getLong("latitudeM",0));
        Double latitudeM2 = Double.longBitsToDouble(apiData.getLong("latitudeM2",0));
        int part = getIntent().getIntExtra("partNo",0)-1;
        Intent intent = new Intent(MapsActivity.this,Route.class);
        intent.putExtra("longitudeM2", longitudeM2);
        intent.putExtra("latitudeM2", latitudeM2);
        intent.putExtra("longitudeM", longitudeM);
        intent.putExtra("latitudeM", latitudeM);
        intent.putExtra("partNo", part);
        startActivity(intent);
    }

    public void SaveRoute(View view){
        Bundle extras = getIntent().getExtras();
        ArrayList<LatLng> full = (ArrayList<LatLng>) extras.get("full");
        ArrayList<LatLng> train = (ArrayList<LatLng>) extras.get("train");
        ArrayList<LatLng> foot = (ArrayList<LatLng>) extras.get("foot");
        ArrayList<LatLng> tube = (ArrayList<LatLng>) extras.get("tube");
        ArrayList<LatLng> bus = (ArrayList<LatLng>) extras.get("bus");
        ArrayList<LatLng> other = (ArrayList<LatLng>) extras.get("other");
        Intent intent = new Intent(MapsActivity.this, SaveRoute.class);
        intent.putExtra("partNo", getIntent().getIntExtra("partNo", 0));
        intent.putExtra("endNo", getIntent().getIntExtra("endNo", 0));
        intent.putExtra("SC", getIntent().getStringExtra("SC"));
        intent.putExtra("SC2", getIntent().getStringExtra("SC2"));
        intent.putExtra("service", getIntent().getExtras().getInt("service"));
        intent.putExtra("full", full);
        intent.putExtra("train", train);
        intent.putExtra("tube", tube);
        intent.putExtra("foot", foot);
        intent.putExtra("bus", bus);
        intent.putExtra("other", other);
        intent.putExtra("SName", getIntent().getStringExtra("SName"));
        intent.putExtra("SLon", getIntent().getDoubleExtra("SLon", 0));
        intent.putExtra("SLat", getIntent().getDoubleExtra("SLat", 0));
        startActivity(intent);
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MapsActivity.this, Search.class));
        finish();
    }
}
