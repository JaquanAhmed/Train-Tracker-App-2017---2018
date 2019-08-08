package ukk1451070.ac.kingston.httpkunet.transporttracker.Helpers;

/**
 * Created by Jaquan on 30/11/2017.
 */
//this site helped me to create this
//http://coderzpassion.com/android-working-google-places-api/
public class MyGooglePlaces {
    private String name;
    private String vicinity;
    private double latitude;
    private double longitude;
    public MyGooglePlaces()
    {
        this.name="";
        this.vicinity="";
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getVicinity() {
        return vicinity;
    }
    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }
    public double getLatitude(){
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatLng(double lat, double lon)
    {
        this.latitude=lat;
        this.longitude=lon;
    }
}
