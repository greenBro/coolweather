package com.bugua.culweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017-10-14.
 */

public class Aqi {
    @SerializedName("city")
    public AqiCity aqiCity;
    public class AqiCity {
        public String aqi;
        public String pm10;
        public String pm25;
        @SerializedName("qlty")
        public String quality;

        public String getAqi() {
            return aqi;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public String getPm10() {
            return pm10;
        }

        public void setPm10(String pm10) {
            this.pm10 = pm10;
        }

        public String getPm25() {
            return pm25;
        }

        public void setPm25(String pm25) {
            this.pm25 = pm25;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }
    }
}
