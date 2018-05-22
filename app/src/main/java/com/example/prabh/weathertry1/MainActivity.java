package com.example.prabh.weathertry1;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prabh.location.Loc_Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    com.example.prabh.location.Loc_Listener loc=new Loc_Listener();

    String url="";//http://api.openweathermap.org/data/2.5/weather?lat=19.09&lon=72.89&appid=d141c211a6099a6460b108c1c1b86335";
    Weathertry1 weather;
    TextView place,dateTime,temp,sunrise,sunset,humidity,pressure,description;
    ImageView icon;
    ImageButton refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLocation();
        place=(TextView)findViewById(R.id.place);
        dateTime=(TextView)findViewById(R.id.dateTime);
        temp=(TextView)findViewById(R.id.temp);
        sunset=(TextView)findViewById(R.id.sunset);
        sunrise=(TextView)findViewById(R.id.sunrise);
        humidity=(TextView)findViewById(R.id.humidity);
        pressure=(TextView)findViewById(R.id.pressure);
        description=(TextView)findViewById(R.id.description);
        icon=(ImageView)findViewById(R.id.icon);
        refresh=(ImageButton) findViewById(R.id.refresh);
        url=String.format("http://api.openweathermap.org/data/2.5/weather?lat=%.2f&lon=%.2f&appid=d141c211a6099a6460b108c1c1b86335",loc.getLat(),loc.getLon());
        StringRequest req=retrieve_Data();
        final RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(req);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
                url=String.format("http://api.openweathermap.org/data/2.5/weather?lat=%.2f&lon=%.2f&appid=d141c211a6099a6460b108c1c1b86335",loc.getLat(),loc.getLon());
                queue.add(retrieve_Data());
                Toast.makeText(MainActivity.this,"Refresh Done",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public StringRequest retrieve_Data()
    {
        final StringRequest request=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Code",response);
                GsonBuilder gsonBuilder=new GsonBuilder();
                Gson gson=gsonBuilder.create();
                weather=gson.fromJson(response,Weathertry1.class);
                setData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Something went Wrong",Toast.LENGTH_SHORT).show();
            }
        });
        return request;
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationManager myManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //Loc_Listener loc=new com.example.prabh.location.Loc_Listener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
        myManager.removeUpdates(loc);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getLocation();
    }

    void getLocation()
    {
        LocationManager myManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //Loc_Listener loc=new com.example.prabh.location.Loc_Listener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
        myManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 5, loc);

    }
    public void setData()
    {
        place.setText(String.format("%s",weather.getName()));
        dateTime.setText(String.format("Last Updated:\n%s",getDateTime()));
        temp.setText(String.format("%.2f°C",weather.getMain().getTemp()-273.0));
        sunset.setText(String.format("Sunset:%s",unixTimeToDateTime((weather.getSys().getSunset()))));
        sunrise.setText(String.format("Sunrise:%s",unixTimeToDateTime((weather.getSys().getSunrise()))));
        humidity.setText(String.format("Humidity:%d%%",weather.getMain().getHumidity()));
        pressure.setText(String.format("Pressure:%.0f hPa",weather.getMain().getPressure()));
        description.setText(String.format("%s",weather.getWeather().get(0).getDescription()));
        Picasso.get().load(String.format("http://openweathermap.org/img/w/%s.png",weather.getWeather().get(0).getIcon())).fit().centerCrop().into(icon);
    }
    public static String unixTimeToDateTime(int unixTimeStamp)
    {
        DateFormat dateFormat=new SimpleDateFormat("HH:mm");
        Date date=new Date();
        date.setTime((long)unixTimeStamp*1000);
        return dateFormat.format(date);
    }
    public String getDateTime()
    {
        DateFormat dateFormat=new SimpleDateFormat("dd MMMM yyyy HH:mm");
        Date date=new Date();
        return dateFormat.format(date);
    }
}
