package com.bugua.culweather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bugua.culweather.gson.Aqi;
import com.bugua.culweather.gson.DailyForecast;
import com.bugua.culweather.gson.HeWeather;
import com.bugua.culweather.util.HttpUtil;
import com.bugua.culweather.util.Utility;
import com.bumptech.glide.Glide;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.zip.Inflater;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView temperature;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView backPic;
    public SwipeRefreshLayout swipeRefreshLayout;
    private String selectedCity;
    public DrawerLayout drawerLayout;
    private Button navButton;
    private LinearLayout apiLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_weather);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        temperature = (TextView) findViewById(R.id.temperature_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm2_5_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        backPic = (ImageView) findViewById(R.id.backPic);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);
        apiLayout = (LinearLayout) findViewById(R.id.api_layout);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        SharedPreferences pfers = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherTxt = pfers.getString("weather", null);
        weatherLayout.setVisibility(View.INVISIBLE);
        if (weatherTxt != null) {
            HeWeather weather = Utility.handleWeatherResponse(weatherTxt);
            selectedCity = weather.basic.cityName;
            showWeather(weather);
        } else {
            selectedCity = getIntent().getStringExtra("selectedCounty");
            requestWeather(selectedCity);
        }
        String picAdr = pfers.getString("picAdr", null);
        if (picAdr != null) {
            Glide.with(WeatherActivity.this).load(picAdr).into(backPic);
        } else {
            loadBingPic();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(selectedCity);
            }
        });
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    public void requestWeather(String city) {
        String weatherUrl = "https://free-api.heweather.com/v5/weather?city=" + city + "&key=c78e01f8e59949d4ad1b4b47fa939fa1";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取网络天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseText = response.body().string();
                final HeWeather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences pfers = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                            SharedPreferences.Editor editor = pfers.edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            selectedCity = weather.basic.cityName;
                            showWeather(weather);
                        } else if (weather != null && "unknown city".equals(weather.status)) {
                            Toast.makeText(WeatherActivity.this, "未知城市，没有天气信息", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }

    private void showWeather(HeWeather weather) {
        weatherLayout.setVisibility(View.VISIBLE);
        titleCity.setText(weather.basic.getCityName());
        titleUpdateTime.setText(weather.basic.update.getUpdateTime().split(" ")[1]);
        temperature.setText(weather.now.temperature + "℃");
        weatherInfoText.setText(weather.now.condition.info);
        forecastLayout.removeAllViewsInLayout();
        for (DailyForecast dailyForecast : weather.dailyForecast) {
            View view = LayoutInflater.from(this).inflate(R.layout.daily_forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(dailyForecast.date);
            infoText.setText(dailyForecast.condition.getInfo());
            maxText.setText(dailyForecast.temperature.getMax());
            minText.setText(dailyForecast.temperature.getMin());
            forecastLayout.addView(view);
        }
        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.aqiCity.getAqi());
            pm25Text.setText(weather.aqi.aqiCity.getPm25());
            apiLayout.setVisibility(View.VISIBLE);
        }else{
            apiLayout.setVisibility(View.GONE);
        }
        comfortText.setText("舒适度：" + weather.suggestion.comfortlevel.context);
        carWashText.setText("洗车指数：" + weather.suggestion.carWash.context);
        sportText.setText("运动建议：" + weather.suggestion.sport.context);


    }

    private void loadBingPic() {

        String requestImage = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestImage, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingUrl = response.body().string();
                SharedPreferences pres = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                SharedPreferences.Editor editor = pres.edit();
                editor.putString("picAdr", bingUrl);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingUrl).into(backPic);
                    }
                });
            }
        });

    }
}
