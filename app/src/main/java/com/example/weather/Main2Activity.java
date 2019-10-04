package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    List<Weather> listDay;
    TextView tvCityName2;
    ListView lvListDay;
    ImageButton btnBack;
    AdapterWeather adapterWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        initAction();
        getDataCity();
    }

    private void getDataCity() {
        Intent intent = getIntent();
        String city = intent.getStringExtra("City Name");

        Toast.makeText(this, city, Toast.LENGTH_SHORT).show();
        getData(city);
    }

    private void getData(String data) {
        Intent intent = getIntent();
        String cityName = intent.getStringExtra("City Name");
        String countryName = intent.getStringExtra("Country Name");
        tvCityName2.setText(cityName + "-" + countryName);
        listDay = new ArrayList<>();
        String url = "http://api.openweathermap.org/data/2.5/forecast?q=" + data + "&units=metric&cnt=7&appid=0b357f6e80e48dad780c56dfc7a33690";
        RequestQueue requestQueue = Volley.newRequestQueue(Main2Activity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArrayList = jsonObject.getJSONArray("list");

                    Log.e("LISE",jsonArrayList.length()+ "");

                    for (int i = 0; i < jsonArrayList.length(); i++) {
                        JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);
                        String day = jsonObjectList.getString("dt");
                        long l = Long.valueOf(day);
                        Date date = new Date(l * 1000L);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd-SS");
                        String Day = simpleDateFormat.format(date);

                        JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("main");
                        String maxTemp = jsonObjectTemp.getString("temp_max");
                        String minTemp = jsonObjectTemp.getString("temp_min");

                        JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                        String status = jsonObjectWeather.getString("description");
                        String icon = jsonObjectWeather.getString("icon");

                        listDay.add(new Weather(Day, status, icon, maxTemp, minTemp));

                    }
                    adapterWeather = new AdapterWeather(Main2Activity.this, listDay);
                    //adapterWeather.notifyDataSetChanged();
                    lvListDay.setAdapter(adapterWeather);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("result2", "AAAA " + e.getMessage());
                }
                Log.d("result2", response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        requestQueue.add(stringRequest);

    }

    private void initView() {
        tvCityName2 = findViewById(R.id.tvCityName2);
        btnBack = findViewById(R.id.btnBack);
        lvListDay = findViewById(R.id.lvListDay);
    }

    private void initAction() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
