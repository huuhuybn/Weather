package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.INotificationSideChannel;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText edtCityName;
    TextView tvCityName, tvCountryName, tvTemperature, tvStatus, tvHumidity, tvCloud, tvWind, tvDayUpdate;
    Button btnSearch, btnSeeNextDay;
    ImageView imgIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        cityDefault();
        initAction();
    }

    private void cityDefault() {
        getCurrentWeatherData("hanoi");
    }

    private void initView() {
        edtCityName = findViewById(R.id.edtCityName);
        tvCityName = findViewById(R.id.tvCityName);
        tvCountryName = findViewById(R.id.tvCountryName);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvStatus = findViewById(R.id.tvStatus);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvWind = findViewById(R.id.tvWind);
        tvCloud = findViewById(R.id.tvCloud);
        tvDayUpdate = findViewById(R.id.tvDayUpdate);
        btnSearch = findViewById(R.id.btnSearch);
        btnSeeNextDay = findViewById(R.id.btnSeeNextDay);
        imgIcon = findViewById(R.id.imgIcon);
    }


    private void initAction() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCityName.getText().toString().equals(""))
                    getCurrentWeatherData("hanoi");
                else
                    getCurrentWeatherData(edtCityName.getText().toString());
            }
        });

        btnSeeNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("City Name", tvCityName.getText().toString());
                intent.putExtra("Country Name", tvCountryName.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void getCurrentWeatherData(String data) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&appid=0b357f6e80e48dad780c56dfc7a33690";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String day = jsonObject.getString("dt");
                    String name = jsonObject.getString("name");
                    tvCityName.setText(name);
                    long l = Long.valueOf(day);
                    Date date = new Date(l * 1000L);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd-SS");
                    tvDayUpdate.setText(simpleDateFormat.format(date));

                    JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                    String status = jsonObjectWeather.getString("main");
                    String icon = jsonObjectWeather.getString("icon");
                    Picasso.with(getApplicationContext()).load("http://openweathermap.org/img/wn/" + icon + ".png").into(imgIcon);
                    tvStatus.setText(status);


                    JSONObject jsonObjectCountry = jsonObject.getJSONObject("sys");
                    tvCountryName.setText(jsonObjectCountry.getString("country"));


                    JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                    tvHumidity.setText(jsonObjectMain.getString("humidity") + "%");
                    tvTemperature.setText(jsonObjectMain.getString("temp") + "°C");

                    JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                    tvWind.setText(jsonObjectWind.getString("speed") + "m/s");

                    JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
                    tvCloud.setText(jsonObjectCloud.getString("all") + "%");

                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                Log.d("result", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Wrong City Name", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }


}
