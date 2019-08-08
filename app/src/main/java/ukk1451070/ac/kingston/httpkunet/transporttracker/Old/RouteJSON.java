package ukk1451070.ac.kingston.httpkunet.transporttracker.Old;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

import ukk1451070.ac.kingston.httpkunet.transporttracker.Helpers.HttpHandler;
import ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.MapsActivity;
import ukk1451070.ac.kingston.httpkunet.transporttracker.R;

public class RouteJSON extends AppCompatActivity {

    private String TAG = OriginJSON.class.getSimpleName();

    private ProgressDialog pDialog;

    ArrayList<LatLng> points = new ArrayList<>();
    ArrayList<HashMap<String, String>> StationList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sr);

        StationList = new ArrayList<>();

        new GetRoutes().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetRoutes extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(RouteJSON.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                extras.getString("search");
                extras.getString("search2");
            }

            String urlO = "http://transportapi.com/v3/uk/places.json?query="+extras.getString("search")+"&type=train_station&app_id=30df19a0&app_key=d860f66065dfff6c84237c6836ae2e66";
            urlO = urlO.replaceAll(" ", "%20");;
            String urlD = "http://transportapi.com/v3/uk/places.json?query="+extras.getString("search2")+"&type=train_station&app_id=30df19a0&app_key=d860f66065dfff6c84237c6836ae2e66";
            urlD = urlD.replaceAll(" ", "%20");;

            // Making a request to url and getting response
            String Origin = sh.makeServiceCall(urlO);
            String Destination = sh.makeServiceCall(urlD);

            Log.e(TAG, "Response from url: " + Origin);
            Log.e(TAG, "Response from url: " + Destination);

            JSONParser j = new JSONParser();

            Object[] O = j.getStation(Origin);
            String Oname = (String)O[0];
            Double Olongitude = (Double)O[1];
            Double Olatitude = (Double)O[2];
            String OSC = (String)O[3];


            Object[] D = j.getStation(Destination);
            String Dname = (String)D[0];
            Double Dlongitude = (Double)D[1];
            Double Dlatitude = (Double)D[2];
            String DSC = (String)D[3];

            Intent intent = new Intent(RouteJSON.this, MapsActivity.class);
            intent.putExtra("nameM", Oname);
            intent.putExtra("longitudeM", Olongitude);
            intent.putExtra("latitudeM", Olatitude);
            intent.putExtra("SC", OSC);
            intent.putExtra("nameM2", Dname);
            intent.putExtra("longitudeM2", Dlongitude);
            intent.putExtra("latitudeM2", Dlatitude);
            intent.putExtra("SC2", DSC);

            startActivity(intent);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
        }

    }


}
