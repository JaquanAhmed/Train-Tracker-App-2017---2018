package ukk1451070.ac.kingston.httpkunet.transporttracker.Old;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jaquan on 28/03/2018.
 */

public class JSONParser {

    private static final String TAG = JSONParser.class.getSimpleName();

    public JSONParser(){
    }

    public Object[] getStation (String Station) {
        Object[] arr = new Object[4];
        if (Station != null) {
            try {
                JSONObject jsonObj = new JSONObject(Station);

                JSONArray stations = jsonObj.getJSONArray("member");

                for (int i = 0; i < stations.length(); i++) {
                    JSONObject c = stations.getJSONObject(i);

                    String name = c.getString("name");
                    Double longitude = c.getDouble("longitude");
                    Double latitude = c.getDouble("latitude");
                    String SC = c.getString("station_code");

                    arr = new Object[4];
                    arr[0] = new String(c.getString("name"));
                    arr[1] = new Double(c.getDouble("longitude"));
                    arr[2] = new Double(c.getDouble("latitude"));
                    arr[3] = new String(c.getString("station_code"));
                }
            } catch (JSONException e) {
                Log.e(TAG, "Json parsing error for station: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Couldn't get json for station from server.");
        }
        return arr;
    }

    public void getService (String Service){
        if (Service != null) {
            try {
                JSONObject jsonObj = new JSONObject(Service);

                JSONObject departures = jsonObj.getJSONObject("departures");

                JSONArray all = departures.getJSONArray("all");
                JSONObject a = all.getJSONObject(0);

                int service = a.getInt("service");
            } catch (JSONException e){
                Log.e(TAG, "Json parsing error for service: " + e.getMessage());
            }
        }
        else{
            Log.e(TAG, "Couldn't get json for service from server.");
        }
    }

    public void getPosition (String Position){
        if (Position!= null) {
            try {
                JSONObject jsonObj = new JSONObject(Position);
                // Getting JSON Array node
                JSONArray stops = jsonObj.getJSONArray("stops");
                for (int i = 0; i < stops.length(); i++) {

                    JSONObject s = stops.getJSONObject(i);

                    if (s.getString("status").equals(null)) {
                        String SName = s.getString("station_name");
                    }
                }
            }catch(JSONException e){
                Log.e(TAG, "Json parsing error for Position: " + e.getMessage());
            }
        }else{
            Log.e(TAG, "Couldn't get json for Position from server.");
        }
    }

    public void getRoute (String Route){
        ArrayList<LatLng> foot = new ArrayList<>();
        ArrayList<LatLng> foot1 = new ArrayList<>();
        ArrayList<LatLng> train = new ArrayList<>();
        ArrayList<LatLng> tube = new ArrayList<>();
        ArrayList<LatLng> bus = new ArrayList<>();
        ArrayList<LatLng> o = new ArrayList<>();
        if (Route!= null) {
            try {
                JSONObject jsonObj = new JSONObject(Route);

                // Getting JSON Array node
                JSONArray routes = jsonObj.getJSONArray("routes");

                // looping through All Contacts
                JSONObject r = routes.getJSONObject(0);

                JSONArray route_parts = r.getJSONArray("route_parts");
                for (int i = 0; i < route_parts.length(); i++) {

                    JSONObject rp = route_parts.getJSONObject(i);

                    JSONArray coordinates = rp.getJSONArray("coordinates");

                    for (int i2 = 0; i2 < coordinates.length(); i2++) {

                        JSONArray c = coordinates.getJSONArray(i2);
                        LatLng latLng = new LatLng(c.getDouble(1), c.getDouble(0));
                        if (rp.getString("mode").equals("foot")) {
                            if (rp.equals(route_parts.getJSONObject(0))) {
                                foot1.add(latLng);
                            } else {
                                foot.add(latLng);
                            }
                        } else if (rp.getString("mode").equals("train")) {
                            train.add(latLng);
                        } else if (rp.getString("mode").equals("tube")) {
                            tube.add(latLng);
                        } else if (rp.getString("mode").equals("bus")) {
                            bus.add(latLng);
                        } else {
                            o.add(latLng);
                        }

                    }
                }
            }catch(JSONException e){
                Log.e(TAG, "Json parsing error for Position: " + e.getMessage());
            }
        }else{
            Log.e(TAG, "Couldn't get json for Position from server.");
        }
    }

}
