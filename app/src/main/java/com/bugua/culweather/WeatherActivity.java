package com.bugua.culweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        String selectedCity = getIntent().getStringExtra("selectedCounty");
        requestWeather(selectedCity);
        loadBingPic();

    }

    private void requestWeather(String city) {
        String weatherUrl = "https://free-api.heweather.com/v5/weather?city=" + city + "&key=c78e01f8e59949d4ad1b4b47fa939fa1";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取网络天气信息失败", Toast.LENGTH_SHORT);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String responseText = response.body().string();
                final HeWeather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            showWeather(weather);
                        }else if(weather != null && "unknown city".equals(weather.status)){
                            Toast.makeText(WeatherActivity.this,"未知城市，没有天气信息",Toast.LENGTH_SHORT);
                        }
                    }
                });
            }
        });

    }

    private void showWeather(HeWeather weather) {
        titleCity.setText(weather.basic.getCityName());
        titleUpdateTime.setText(weather.basic.update.getUpdateTime().split(" ")[1]);
        temperature.setText(weather.now.temperature + "℃");
        weatherInfoText.setText(weather.now.condition.info);
        for (DailyForecast dailyForecast : weather.dailyForecast) {
            View view= LayoutInflater.from(this).inflate(R.layout.daily_forecast_item,forecastLayout,false);
            TextView dateText=(TextView) view.findViewById(R.id.date_text);
            TextView infoText=(TextView) view.findViewById(R.id.info_text);
            TextView maxText=(TextView) view.findViewById(R.id.max_text);
            TextView minText=(TextView) view.findViewById(R.id.min_text);
            dateText.setText(dailyForecast.date);
            infoText.setText(dailyForecast.condition.getInfo());
            maxText.setText(dailyForecast.temperature.getMax());
            minText.setText(dailyForecast.temperature.getMin());
            forecastLayout.addView(view);
        }
        aqiText.setText(weather.aqi.aqiCity.getAqi());
        pm25Text.setText(weather.aqi.aqiCity.getPm25());
        comfortText.setText("舒适度：" + weather.suggestion.comfortlevel.context);
        carWashText.setText("洗车指数：" + weather.suggestion.carWash.context);
        sportText.setText("运动建议：" + weather.suggestion.sport.context);


    }

    private void loadBingPic(){
        String requestImage="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestImage, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingUrl=response.body().string();

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
